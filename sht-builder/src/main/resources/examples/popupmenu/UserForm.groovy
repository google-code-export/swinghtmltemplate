package examples.popupmenu;


import java.awt.event.ActionEvent
import javax.swing.text.JTextComponent
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

public class UserForm  {

    private Log logger = LogFactory.getLog(getClass());

    public void copy(ActionEvent e) {
        logger.debug("Copy action invoked");
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            c.copy();
        }
    }

    public void paste(ActionEvent e) {
        logger.debug("Paste action invoked");
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) e.getSource();
            c.paste();
        }
    }

}
