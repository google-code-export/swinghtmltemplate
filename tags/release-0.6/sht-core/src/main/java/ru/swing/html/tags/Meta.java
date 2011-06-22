package ru.swing.html.tags;

import ru.swing.html.css.SelectorGroup;

import javax.swing.*;
import java.util.Map;

/**
 * This tag can be only located inside `&lt;head>` tag to be parsed.
 * This tag is used to pass meta info to the dom model. You can later get them with `getMetaItems()`:
<pre>
&lt;html>
    &lt;head>
        &lt;meta name="foo" value="fooValue"/>
    &lt;/head>
    &lt;body>
    ...
    &lt;/body>
&lt;/html>
</pre>

<pre>
DomModel domModel = ...;
assertEquals("fooValue", domModel.getMetaItems().get("foo"));
</pre>

There are special metas that are used in convetion process:
 <ul>
   <li>
    <p><strong>display-as</strong> - tells `DomConverter` to create window for the form.</p>
    <p>The window can be later accessed with `domModel.getWindow()` method. You can pass you own window instance with
   `domModel.setWindow(window)` method, in this case no window is created, your instance is used instead.</p>

    <p>Possible values are:</p>
    <ul>
        <li><strong>frame</strong> for javax.swing.JFrame</li>
        <li><strong>dialog</strong> for javax.swing.JDialog</li>
    </ul>
   </li>

   <li>
     <p><strong>modal</strong> sets the modality of the `JDialog`. It is used only when `display-as` meta is used and is equal to `dialog`.
        Possible values: `true` or `false`</p>
   </li>

   <li>
     <p><strong>onclose</strong> - this is used to set default close operation for the window (JFrame.setDefaultCloseOperation()).
     It is only used if `display-as` meta is used. Possible values:</p>
     <ul>
       <li>exit - JFrame.EXIT_ON_CLOSE (only for JFrame)</li>
       <li>hide - JFrame.HIDE_ON_CLOSE</li>
       <li>dispose - JFrame.DISPOSE_ON_CLOSE</li>
       <li>nothing - JFrame.DO_NOTHING_ON_CLOSE</li>
     </ul>
   </li>

   <li>
    <strong>size</strong> - sets the size of the window. It is used only if `display-as` meta is used. Format of the size: `width height`.
    <h2>Example:</h2>
    <p>
      <pre>&lt;meta name='size' content='1024 768'/></pre>
    </p>
   </li>

 */
public class Meta extends Tag {

    private String metaName;
    private String metaContent;

    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void handleLayout() {
    }

    @Override
    public void handleChildren(Map<SelectorGroup, JComponent> substitutions) {
    }

    @Override
    public void setAttribute(String name, String value) {
        if ("name".equals(name)) {
            setMetaName(value);
        }
        else if ("content".equals(name)) {
            setMetaContent(value);
        }
    }

    @Override
    public void applyAttribute(JComponent component, String name) {
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public String getMetaContent() {
        return metaContent;
    }

    public void setMetaContent(String metaContent) {
        this.metaContent = metaContent;
    }
}
