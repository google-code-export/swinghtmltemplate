package ru.swing.html.css;

import org.apache.batik.css.parser.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.*;
import ru.swing.html.tags.Tag;

import java.io.IOException;

/**
 * Селектор. Формат селектора: http://www.w3.org/TR/CSS2/selector.html.
 * Разбор строки селектора происходит с помощью библиотеки batik-css.
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
     * Рекурсивно обрабатывает селектор sac, постепенно выделяя из него дочерние элементы и преобразуя
     * их в токены.
     * Селектор sac имеет вид бинарного дерева. Правый узел - информация по правому подселектору,
     * левый - информация по поддереву для оставшейся левой части селектора.
     * Например, для
     * <pre>
     * div a>p
     * </pre>
     * , дерево выглядит примерно так:
     * <pre>
     *       child
     *      |    |
     *   parent  p
     *   |    |
     *  div   a
     * </pre>
     * @param s селектор
     * @return токен, если для данного типа селектора возможно создать токен (а именно, когда
     * селектор реализует SimpleSelector), иначе null
     */
    private SelectorToken appendSelector(org.w3c.css.sac.Selector s) {
        //обрабатываем случаи "div p" и "div>p". В обоих случаях тип sac селектора будет DescendantSelector,
        //причем в поле simpleSelector будут храниться данные по токену p, а в  getAncestorSelector -
        //дерево токенов для всего, что левее p (div в данном случае)
        if (s instanceof DescendantSelector) {

            DescendantSelector ds = (DescendantSelector) s;
            //обрабатываем правый селектор. Так как это simpleSelector, то в результате
            //вызова метода вернется созданный токен. Добавим его в цепочку токенов
            SelectorToken token = appendSelector(ds.getSimpleSelector());
            //поправим тип связи для токена
            chain.setRelationType(token, s.getSelectorType()== org.w3c.css.sac.Selector.SAC_DESCENDANT_SELECTOR ?
                    SelectorTokenRelation.ANY : SelectorTokenRelation.PARENT);
            //обрабатываем дерево левого селектора
            appendSelector(ds.getAncestorSelector());
        }
        //обрабатываем случаи "div+p". В поле siblingSelector хранится правый селектор, в selector - левый
        else if (s instanceof SiblingSelector) {
            SiblingSelector ds = (SiblingSelector) s;
            //sublingSelector - это simpleSelector, создаем его и добавляем в цепочку токенов
            SelectorToken token = appendSelector(ds.getSiblingSelector());
            //поправим тип связи для токена
            chain.setRelationType(token, SelectorTokenRelation.SUBLING);
            //обрабатываем дерево левого селектора
            appendSelector(ds.getSelector());
        }
        //обрабатываем данные по конерктному единичному кусочку селектора
        else if (s instanceof SimpleSelector) {
            SimpleSelector selector = (SimpleSelector) s;
            SelectorToken token = new SelectorToken();
            //если тип селектора - селектор по названию тега ("p"), выставим имя тега в токене
            if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_ELEMENT_NODE_SELECTOR) {
                ElementSelector elementSelector = (ElementSelector) s;
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());
            }
            //если тип селектора - селектор с условием ("p.foo"), выставим имя тега в токене, и обработаем условие
            else if (selector.getSelectorType()== org.w3c.css.sac.Selector.SAC_CONDITIONAL_SELECTOR) {
                ConditionalSelector cs = (ConditionalSelector) s;
                ElementSelector elementSelector = (ElementSelector) cs.getSimpleSelector();
                token.setName(elementSelector.getLocalName()==null ? "*" : elementSelector.getLocalName());

                Condition cond = cs.getCondition();
                parseCondition(token, cond);
            }
            //добавляем тег в цепочку токенов. При этом в качестве связи установим любую связь, так как
            //в данном вызове нет информации о том, какой тип связи для этого листа селекторов.
            //выставлять правильный тип связи будет метод, вызвавший данный метод.
            chain.append(token, SelectorTokenRelation.ANY);
            return token;
        }
        return null;

    }

    /**
     * Рекурсивно обрабатывает условия для данного токена. Условия берутся из соответствующего узла sac селектора.
     * @param token токен
     * @param cond условия
     */
    private void parseCondition(SelectorToken token, Condition cond) {
        //если это условие на наличие аттрибута или условие на идентификатор, то
        //в обоих случаях тип условия - AttributeCondition.
        //Если задан value, то значит условие - на равенство значений (a[class='foo']),
        //иначе - условие на наличие атрибута (a[href])
        if (cond.getConditionType()== Condition.SAC_ATTRIBUTE_CONDITION ||
                cond.getConditionType()== Condition.SAC_ID_CONDITION) {

            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    ac.getValue()!=null? AttributeConstraint.EQUALS: AttributeConstraint.HAS_ATTRIBUTE);

            token.getAttributeMatchers().add(m);
        }
        //условие на наличие классов будет разбито на группу условий, по условию на каждый класс, объединенных
        //в родительское условие с типом Condition.SAC_AND_CONDITION. Для каждого такого подусловия
        //будет вызываться данный метод. Его тип - AttributeCondition, как в случае
        //условия на наличие аттрибута или условия на идентификатор, но для него нужно
        //проставить constraint=AttributeConstraint.HAS_VALUE, а аттрибутные условия
        //проставят ему AttributeConstraint.EQUALS (так как значение будет указано),
        //поэтому условие на класс обрабатываем отдельно от аттрибутных
        else if (cond.getConditionType()== Condition.SAC_CLASS_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        //условие на наличие среди значений атрибута указанного значения (div[class~="foo"])
        else if (cond.getConditionType()== Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.HAS_VALUE);
            token.getAttributeMatchers().add(m);

        }
        //условие чтобы значение атрибута начиналось с указанной строки
        else if (cond.getConditionType()== Condition.SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION) {
            AttributeCondition ac = (AttributeCondition) cond;
            AttributeMatcher m = new AttributeMatcher(ac.getLocalName(),
                    ac.getValue(),
                    AttributeConstraint.STARTS_WITH);
            token.getAttributeMatchers().add(m);

        }
        //компонентное условие, содержит 2 подусловия, обязательных к исполнению.
        //выбираем оба подусловия и рекурсивно обрабатываем их для нашего токена.
        else if (cond.getConditionType()== Condition.SAC_AND_CONDITION) {
            CombinatorCondition c = (CombinatorCondition) cond;
            parseCondition(token, c.getFirstCondition());
            parseCondition(token, c.getSecondCondition());
        }
    }

    /**
     * Проверяет, удовлетворяет ли тег условию селектора.
     * @param tag тег
     * @return true, если тег удовлетворяет условию селектора, иначе false
     */
    public boolean matches(Tag tag) {
        return chain.matches(tag);
    }

}
