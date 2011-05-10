package ru.swing.html.css;

import ru.swing.html.tags.Tag;

import java.util.*;

/**
 * <p>
 *     Represents the chain of the selector elements and relations among them.
 * </p>
 * <p>
 *     You must fill the chain from the end. This means that for the chain of the tokens in selector
 * </p>
 * <pre>
 * body div ul li&gt;p
 * </pre>
 * <p>
 *     you must fill this way:
 * </p>
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
        //1st elements must match the tag
        SelectorToken currentToken = tokens.get(0);
        boolean res = currentToken.matches(tag);


        if (res) {

            Tag currentTag = tag;

            for (int i=1; i<tokens.size(); i++) {

                currentToken = tokens.get(i);

                //get the relation among previous tag an current
                SelectorTokenRelation relationWithPrevious = relationWithNext.get(tokens.get(i-1));

                if (SelectorTokenRelation.PARENT.equals(relationWithPrevious)) {
                    //parent tag must match current token
                    Tag parentTag = currentTag.getParent();
                    if (!currentToken.matches(parentTag)) {
                        return false;
                    }
                    else {
                        currentTag = parentTag;
                    }
                }

                else if (SelectorTokenRelation.ANY.equals(relationWithPrevious)) {
                    //search for the first matching parent tag, starting with direct parent.
                    //algorithm:
                    //search matching parent tag in the current tag. When the matching is found, take the rest
                    //tokens, including current and recursivelly invoke checking for match them on found parent's tag.
                    //if cheking fails , continue searching next matching tag.
                    //the role of the recursion is to avoid situations where we find matching parent tag, but
                    //rest tokens are not suitable for this tag, e.g. the condition "span+div.foo p" will not match
                    //this document:
                    //<html>
                    //<body>" +
                    //  <span/>"+
                    //  <div id='div1' class='foo'>" +
                    //     <div id='div2' class='foo'>"+
                    //        <p/>"+
                    //     </div>"+
                    //  </div>" +
                    //</body>
                    //</html>
                    //as non-recursive search will find div2, but rest checking will show that tag does not have
                    //span subling
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

                    //get the parent for the current tag
                    Tag parentTag = currentTag.getParent();
                    if (parentTag==null) {
                        return false;
                    }

                    //get subling for the current
                    //<div><p/><li/></div>, for the "li" we must get "p"
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
