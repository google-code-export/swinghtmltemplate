package ru.swing.html.css;

import org.apache.batik.css.parser.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.*;
import ru.swing.html.tags.Tag;

import java.io.IOException;

/**
 * CSS Selector. Selector format: http://www.w3.org/TR/CSS2/selector.html.
 * Selector is parsed with batik-css.
 */
public class Selector {

    private Log logger = LogFactory.getLog(getClass());
    private SelectorTokensChain chain = new SelectorTokensChain();

    public Selector(String selector) {
        Parser p = new Parser();
        SelectorList l = null;
        try {
            l = p.parseSelectors(selector);
        } catch (IOException e) {
            logger.error("Can't parse selector: "+e.getMessage(), e);
            return;
        }
        if (l.getLength()!=1) {
            logger.warn("Parsed more then 1 selectors, must be 1.");
        }

        org.w3c.css.sac.Selector s = l.item(0);
        appendSelector(s);

    }

    /**
     * Recursievly parses selector sac (batik-css class) by extracting child elements and converting them into tokens.
     * Selector sac is binary tree. Right node is the information on right sub-selector. Left is
     * the information rest subtree of the left part of the selector.
     * For example, for
     * <pre>
     * div a>p
     * </pre>
     * , the tree looks like this:
     * <pre>
     *       child
     *      |    |
     *   parent  p
     *   |    |
     *  div   a
     * </pre>
     * The main idea of this method is to convert css-batik tree into plain chain of tokens.
     * @param s selector
     * @return token, if it can be created for this type of selector (it can be created for
     * SimpleSelector) or null
     */
    private SelectorToken appendSelector(org.w3c.css.sac.Selector s) {
        //process cases "div p" and "div>p". In both cases sac selector type is DescendantSelector,
        //the field simpleSelector will contain data for token "p", and getAncestorSelector -
        //the tokens tree for the left side of "p" ("div" in this example)
        if (s instanceof DescendantSelector) {

            DescendantSelector ds = (DescendantSelector) s;
            //process right selector. This is simpleSelector, so the returned object is the created token.
            //We add it to the token chain
            SelectorToken token = appendSelector(ds.getSimpleSelector());
            //Update relation type for token
            chain.setRelationType(token, s.getSelectorType()== org.w3c.css.sac.Selector.SAC_DESCENDANT_SELECTOR ?
                    SelectorTokenRelation.ANY : SelectorTokenRelation.PARENT);
            //process the tree for left selector
            appendSelector(ds.getAncestorSelector());
        }
        //process cases "div+p". The "siblingSelector" field hold right selector ("p"), and "selector" field - left selector ("div")
        else if (s instanceof SiblingSelector) {
            SiblingSelector ds = (SiblingSelector) s;
            //sublingSelector is simpleSelector, create it and append to the tokens chain
            SelectorToken token = appendSelector(ds.getSiblingSelector());
            //Update relation type for token
            chain.setRelationType(token, SelectorTokenRelation.SUBLING);
            //process the tree for left selector
            appendSelector(ds.getSelector());
        }
        //process data for concrete single selector token
        else if (s instanceof SimpleSelector) {
            SimpleSelector selector = (SimpleSelector) s;
            SelectorToken token = new SelectorToken();
            //selector type is selctor by tag name ("p"), set tag name in token
            if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_ELEMENT_NODE_SELECTOR) {
                ElementSelector elementSelector = (ElementSelector) s;
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());
            }
            //selector type is selector with condition ("p.foo"), set tag name in the token and parse the condition
            else if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_CONDITIONAL_SELECTOR) {
                ConditionalSelector cs = (ConditionalSelector) s;
                ElementSelector elementSelector = (ElementSelector) cs.getSimpleSelector();
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());

                Condition cond = cs.getCondition();
                parseCondition(token, cond);
            }
            //add tag to the tokens chain. The relation type is set to SelectorTokenRelation.ANY, because
            //we have no information here for the type of the relation for this node.
            //The correct relation type will be set in the caller method
            chain.append(token, SelectorTokenRelation.ANY);
            return token;
        }
        return null;

    }

    /**
     * Recursivelly process conditions for the token. Conditions are taken from corresponding node of the sac selector.
     * @param token token
     * @param cond conditions
     */
    private void parseCondition(SelectorToken token, Condition cond) {
        //if this is condition for the "existence of the attribute" or the condition for the id, then
        //the type of the condition is AttributeCondition.
        //if value is set, then the condition - for the equality of the values (a[class='foo']),
        //otherwise it is the condition for the existence of the attribute (a[href])
        if (cond.getConditionType()== Condition.SAC_ATTRIBUTE_CONDITION ||
                cond.getConditionType()== Condition.SAC_ID_CONDITION) {

            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    ac.getValue()!=null? AttributeConstraint.EQUALS: AttributeConstraint.HAS_ATTRIBUTE);

            token.getAttributeMatchers().add(m);
        }
        //the condition for the existence of classes will be splitted into the group of conditions (one
        //condition per class name) groupped into parent condition with condition type Condition.SAC_AND_CONDITION.
        //For every such subcondition this method will be called. Subcondition type is AttributeCondition, as in
        //case of condition for "existence of attribute" or the condition on id, but we must set
        //constraint=AttributeConstraint.HAS_VALUE, and attribute conditions will set it  AttributeConstraint.EQUALS
        //(because the value will be also set),
        //that is why we process this separatelly from conditions on attributes
        else if (cond.getConditionType()== Condition.SAC_CLASS_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        //condition for the existence of the supplied attribute among the attribute values (div[class~="foo"])
        else if (cond.getConditionType()== Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        //condition that the attribute value starts with supplied string
        else if (cond.getConditionType()== Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.STARTS_WITH);
            token.getAttributeMatchers().add(m);

        }
        //compound condition. holds 2 subcondition, both required.
        //recursivelly process both for our token
        else if (cond.getConditionType()== Condition.SAC_AND_CONDITION) {
            CombinatorCondition c = (CombinatorCondition) cond;
            parseCondition(token, c.getFirstCondition());
            parseCondition(token, c.getSecondCondition());
        }
    }

    /**
     * Check does tag matches the selector. This delegate matching to token chain
     * @param tag tag
     * @return true, if tag matches the condition, false otherwise
     */
    public boolean matches(Tag tag) {
        return chain.matches(tag);
    }

}
