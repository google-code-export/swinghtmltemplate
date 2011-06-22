package ru.swing.html.tags.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.beansbinding.ELProperty;
import ru.swing.html.css.SelectorGroup;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Allows to choose child content depending on condition. This tag allows only &lt;when> and &lt;otherwise children.
 * <p>
 * To provide condition use 'when' tags. 'otherwise' child tag will be used if non of 'when' tags matches.
 * <p>
 * Conditions are evaluated during 'before-components-convertion' phase. Only one child tag can match,
 * if several tags match their conditions, the first one will be used.
 * 
 * <p>
 * If some tag matches condition ('when' or 'otherwise') it's content will be merged to 'choose' parent tag,
 * otherise 'choose' tag will be simply removed from it's parent.
 * 
 * <h2>Example</h2>
 * <pre>
 *              &lt;html xmlns="http://www.w3.org/1999/xhtml"
 *                           xmlns:ui='http://swinghtmltemplate.googlecode.com/ui'>
 *              &lt;head>&lt;/head>
 *              &lt;body id='body'>
 *                 &lt;ui:choose>
 *                      &lt;ui:when test="${type eq 'full'}">
 *                          &lt;p>Full&lt;/p>
 *                      &lt;/ui:when>
 *                      &lt;ui:when test="${type eq 'brief'}">
 *                          &lt;p>Brief&lt;/p>
 *                      &lt;/ui:when>
 *                      &lt;ui:otherwise>
 *                          &lt;p>Unknown&lt;/p>
 *                      &lt;/ui:otherwise>
 *                 &lt;/ui:choose>
 *              &lt;/body>
 *              &lt;/html>
 *
 * </pre>
 *
 * @see When
 * @see Otherwise
 */
public class Choose extends Tag {

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public void handleLayout() {
    }


    @Override
    public void beforeComponentsConvertion() {

        Map<String, When> childsByTest = new HashMap<String, When>();
        Otherwise otherwise = null;
        
        for (Tag child : getChildren()) {
            if (child instanceof When) {
                When when = (When) child;
                childsByTest.put(when.getTest(), when);
            }
            else if (child instanceof Otherwise) {
                if (otherwise==null) {
                    otherwise = (Otherwise) child;
                }
                else {
                    logger.warn(toString()+": choose can contain only 1 otherwise");
                }
            }
            else {
                logger.warn(toString()+": choose can contain only <when> and <otherwise> children");
            }
        }

        //this target tag will be merged to the parent tag
        Tag target = null;
        //find matching 'when' tag
        Iterator<String> it = childsByTest.keySet().iterator();
        while (target==null && it.hasNext()) {
            //take next condition from 'when'
            String conditionExpr = it.next();
            ELProperty conditionProp = ELProperty.create(conditionExpr);
            //eval condition value
            if (conditionProp.isReadable(getModelElements())) {
                Object conditionRaw = conditionProp.getValue(getModelElements());
                if (conditionRaw instanceof Boolean) {
                    Boolean conditionValue = (Boolean) conditionRaw;
                    if (conditionValue) {
                        target = childsByTest.get(conditionExpr);
                    }
                }
                else {
                    logger.warn(toString()+": condition resolved to non boolean value: "+conditionExpr);
                }

            }
            else {
                logger.warn(toString()+": condition value is unreadable: "+conditionExpr);
            }

        }

        //if non of 'when' tags matched, use 'otherwise' tag
        if (target == null) {
            target = otherwise;
        }


        if (target!=null) {
            getModel().mergeTag(this, target);
        }
        else {
            getParent().removeChild(this);
        }
    }
}
