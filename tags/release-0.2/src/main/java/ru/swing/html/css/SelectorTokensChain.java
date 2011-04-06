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
        return matches(tag, tokens);
    }


    public boolean matches(Tag tag, List<SelectorToken> tokens) {
        if (tokens.isEmpty()) {
            return false;
        }
        //1ый элемент должен совпадать с тегом
        SelectorToken currentToken = tokens.get(0);
        boolean res = currentToken.matches(tag);


        if (res) {

            Tag currentTag = tag;

            for (int i=1; i<tokens.size(); i++) {

                currentToken = tokens.get(i);

                //получаем связь между предыдущим тегом и текущим
                SelectorTokenRelation relationWithPrevious = relationWithNext.get(tokens.get(i-1));

                if (SelectorTokenRelation.PARENT.equals(relationWithPrevious)) {
                    //родительский тег должен удовлетворять текущему токену
                    Tag parentTag = currentTag.getParent();
                    if (!currentToken.matches(parentTag)) {
                        return false;
                    }
                    else {
                        currentTag = parentTag;
                    }
                }

                else if (SelectorTokenRelation.ANY.equals(relationWithPrevious)) {
                    //ищем первый совпадающий родительский тег, начиная с родительского
                    //алгоритм поиска:
                    //ищем совпадающий родительский тег у текущего тега. как только нашли совпадение,
                    //берем оставшуюся часть токенов, включая текущий и рекурсивно вызываем проверку на совпадение
                    //для части токенов и найденного родительского тега.
                    //если проверка не успешна - значит продолжаем искать следующий совпадающий родительский тег.
                    //рекурсия сделана для того, чтобы избежать ситуации когда мы нашли совпадающий родительский тег,
                    //но для этого тега не будут совпадать дальнейшие токены (например, может не совпасть условие
                    //span+div.foo p, для такого документа:
                    // <html>
                    //<body>" +
                    //  <span/>"+
                    //  <div id='div1' class='foo'>" +
                    //     <div id='div2' class='foo'>"+
                    //        <p/>"+
                    //     </div>"+
                    //  </div>" +
                    //</body>
                    //</html>
                    //так как нерекурсивный поиск найдет div2, но дальнейшая проверка покажет что данный тег
                    //не имеет братского тега span.
                    currentTag = currentTag.getParent();
                    boolean found = false;
                    List<SelectorToken> leftTokens = tokens.subList(i, tokens.size());
                    while (!found && currentTag!=null) {
                        found = matches(currentTag, leftTokens);
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


                    if (!currentToken.matches(sublingTag)) {
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
