package ru.swing.html.css;

import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * Представляет последовательность элементов селектора и связи между элементами.
 *
 * Заполнять последовательность надо с конца. То есть, для цепочки токенов в селекторе
 * <pre>
 * body div ul li&gt;p
 * </pre>
 * заполнять надо так:
 * <pre>
 * chain.append(new SelectorToken("p"), SelectorTokenRelation.PARENT);
 * chain.append(new SelectorToken("li"), SelectorTokenRelation.ANY);
 * chain.append(new SelectorToken("ul"), SelectorTokenRelation.ANY);
 * chain.append(new SelectorToken("div"), SelectorTokenRelation.ANY);
 * chain.append(new SelectorToken("body"), SelectorTokenRelation.ANY);
 * </pre>
 */
public class SelectorTokensChain {

    private List<SelectorToken> tokens = new ArrayList<SelectorToken>();
    private Map<SelectorToken, SelectorTokenRelation> relationWithNext = new HashMap<SelectorToken, SelectorTokenRelation>();


    public void append(SelectorToken token, SelectorTokenRelation relation) {
        tokens.add(token);
        relationWithNext.put(token, relation);
    }

    public void setRelationType(SelectorToken token, SelectorTokenRelation relation) {
        relationWithNext.put(token, relation);
    }

    public boolean matches(Tag tag) {
        //1ый элемент должен совпадать с тегом
        SelectorToken current = tokens.get(0);
        boolean res = current.matches(tag);
        if (res) {

            Tag currentTag = tag;

            for (int i=1; i<tokens.size(); i++) {

                current = tokens.get(i);

                //получаем связь между предыдущим тегом и текущим
                SelectorTokenRelation relationWithPrevious = relationWithNext.get(tokens.get(i-1));

                if (SelectorTokenRelation.PARENT.equals(relationWithPrevious)) {
                    //родительский тег должен удовлетворять текущему токену
                    Tag parentTag = currentTag.getParent();
                    if (!current.matches(parentTag)) {
                        return false;
                    }
                    else {
                        currentTag = parentTag;
                    }
                }

                else if (SelectorTokenRelation.ANY.equals(relationWithPrevious)) {
                    //ищем первый совпадающий родительский тег, начиная с родительского
                    currentTag = currentTag.getParent();
                    boolean found = false;
                    while (!found && currentTag!=null) {
                        found = current.matches(currentTag);
                        if (!found) {
                            currentTag = currentTag.getParent();
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }

                else if (SelectorTokenRelation.SUBLING.equals(relationWithPrevious)) {

                    //получаем родителя текущего тега
                    Tag parentTag = currentTag.getParent();
                    if (parentTag==null) {
                        return false;
                    }

                    //получаем соседний для текущего тег
                    //<div><p/><li/></div>, для тега li мы должны получить тег p
                    int index = parentTag.getChildren().indexOf(currentTag);
                    if (index<=0) {
                        return false;
                    }

                    Tag sublingTag = parentTag.getChildren().get(index-1);


                    if (!current.matches(sublingTag)) {
                        return false;
                    }
                    else {
                        currentTag = sublingTag;
                    }
                }



            }


        }
        return res;

    }

}
