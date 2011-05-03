package ru.swing.html.tags.ui;

import ru.swing.html.tags.Tag;

/**
 * <p>
 * This tag is used to provide default condition wo &lt;choose> tag, which is used when no
 * &lt;when> tags conditions match.
 *
 * <h2>Example:</h2>
 * <pre>
 *     &lt;otherwise>
 *        &lt;p>child tag&lt;/p>
 *     &lt;/otherwise>
 * </pre>
 *
 * @see Choose
 * @see When

 */
public class Otherwise extends Tag {

    public void handleLayout() {
    }

}
