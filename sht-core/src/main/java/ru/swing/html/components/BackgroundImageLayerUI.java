package ru.swing.html.components;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * <p>
 *     This class is used as JXLayer UI, when 'background-image' attribute is set for the tag.
 * </p>
 * <p>
 *     When 'background-image' attribute is set, default Tag implementation creates JXLayer as componentWrapper
 *     and uses this class as jxLauer UI.
 * </p>
 * <p>
 *     This UI supports these component states:
 *     <ul>
 *         <li>normal - usual state of the component</li>
 *         <li>hover - mouse is over the component</li>
 *         <li>clicked - mouse is over the component and left mouse button is clicked</li>
 *         <li>disabled - component is disabled</li>
 *     </ul>
 * </p>
 */
public class BackgroundImageLayerUI extends AbstractLayerUI<JComponent> {

    private ImageIcon staticBkg;
    private ImageIcon hoverBkg;
    private ImageIcon clickedBkg;
    private ImageIcon disabledBkg;
    private int lastMouseState = MouseEvent.MOUSE_FIRST;


    public BackgroundImageLayerUI(ImageIcon staticBkg, ImageIcon hoverBkg, ImageIcon clickedBkg, ImageIcon disabledBkg) {
        this.staticBkg = staticBkg;
        this.hoverBkg = hoverBkg;
        this.clickedBkg = clickedBkg;
        this.disabledBkg = disabledBkg;
    }

    @Override
    protected void paintLayer(Graphics2D graphics2D, JXLayer<? extends JComponent> jxLayer) {
        int state;
        //mouse over
        if (jxLayer.getMousePosition()!=null) {
            state = lastMouseState;
        }
        else {
            state = 0;
        }

        if (!jxLayer.getView().isEnabled()) {
            state = -1;
        }


        ImageIcon img;
        switch (state) {
            case -1 : img = disabledBkg; break;
            case 0 : img = staticBkg; break;
            case MouseEvent.MOUSE_PRESSED : img = clickedBkg; break;
            case MouseEvent.MOUSE_RELEASED : img = hoverBkg; break;
            default: img = jxLayer.getMousePosition() != null ? hoverBkg : staticBkg; break;
        }
        graphics2D.drawImage(img.getImage(), 0, 0, null);


        super.paintLayer(graphics2D, jxLayer);
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent, JXLayer<? extends JComponent> jxLayer) {
        super.processMouseEvent(mouseEvent, jxLayer);
        lastMouseState = mouseEvent.getID();
    }
}
