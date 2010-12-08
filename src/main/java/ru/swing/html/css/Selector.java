package ru.swing.html.css;

import org.apache.batik.css.parser.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.*;
import ru.swing.html.tags.Tag;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: deady
 * Date: 08.12.2010
 * Time: 23:03:01
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

    private SelectorToken appendSelector(org.w3c.css.sac.Selector s) {

        if (s instanceof DescendantSelector) {

            DescendantSelector ds = (DescendantSelector) s;

            SelectorToken token = appendSelector(ds.getSimpleSelector());
            chain.setRelationType(token, s.getSelectorType()== org.w3c.css.sac.Selector.SAC_DESCENDANT_SELECTOR ?
                    SelectorTokenRelation.ANY : SelectorTokenRelation.PARENT);
            appendSelector(ds.getAncestorSelector());
        }
        else if (s instanceof SiblingSelector) {
            SiblingSelector ds = (SiblingSelector) s;

            SelectorToken token = appendSelector(ds.getSiblingSelector());
            chain.setRelationType(token, SelectorTokenRelation.SUBLING);
            appendSelector(ds.getSelector());
        }
        else if (s instanceof SimpleSelector) {
            SimpleSelector selector = (SimpleSelector) s;
            SelectorToken token = new SelectorToken();

            if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_ELEMENT_NODE_SELECTOR) {
                ElementSelector elementSelector = (ElementSelector) s;
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());
            }
            else if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_CONDITIONAL_SELECTOR) {
                ConditionalSelector cs = (ConditionalSelector) s;
                ElementSelector elementSelector = (ElementSelector) cs.getSimpleSelector();
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());

                Condition cond = cs.getCondition();
                parseCondition(token, cond);
            }
            chain.append(token, SelectorTokenRelation.ANY);
            return token;
        }
        return null;

    }

    private void parseCondition(SelectorToken token, Condition cond) {
        if (cond.getConditionType()== Condition.SAC_ATTRIBUTE_CONDITION ||
                cond.getConditionType()== Condition.SAC_ID_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    ac.getValue()!=null? AttributeConstraint.EQUALS: AttributeConstraint.HAS_ATTRIBUTE);
            token.getAttributeMatchers().add(m);
        }
        else if (cond.getConditionType()== Condition.SAC_CLASS_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        else if (cond.getConditionType()== Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        else if (cond.getConditionType()== Condition.SAC_AND_CONDITION) {
            CombinatorCondition c = (CombinatorCondition) cond;
            parseCondition(token, c.getFirstCondition());
            parseCondition(token, c.getSecondCondition());
        }
    }

    public boolean matches(Tag tag) {
        return chain.matches(tag);
    }

}
