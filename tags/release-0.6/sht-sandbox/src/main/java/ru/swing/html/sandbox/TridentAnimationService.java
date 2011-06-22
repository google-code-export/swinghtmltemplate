package ru.swing.html.sandbox;

import org.pushingpixels.trident.Timeline;
import ru.swing.html.tags.Tag;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

public class TridentAnimationService {

    public void animate(Tag tag, String attribute, Object startValue, Object endValue, long duration) {
        JComponent component = tag.getComponent();
        final Timeline animation = new Timeline(component);


        animation.addPropertyToInterpolate(attribute, startValue, endValue);
        animation.setDuration(duration);
        animation.play();
    }

}
