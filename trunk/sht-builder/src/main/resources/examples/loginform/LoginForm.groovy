package examples.loginform

import java.awt.event.ActionEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JLabel
import ru.swing.html.Bind
import ru.swing.html.DomModel
import ru.swing.html.ModelElement

public class LoginForm {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Bind("result")
    private JLabel result;

    private DomModel model;

    @ModelElement("account")
    private Account account = new Account();

    def onCancelClick(ActionEvent e) {
        result.setText("Cancel clicked");
    }

    def onOkClick() {
        model.select(".button").each {it.enabled = false};
        result.setText("Logging in user " + account.name);
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        Object old = this.account;
        this.account = account;
        pcs.firePropertyChange("account", old, account);
    }

    public void setModel(DomModel model) {
        this.model = model;
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public class Account {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
        private String name;
        private String password;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            String old = this.name;
            this.name = name;
            propertyChangeSupport.firePropertyChange("name", old, name);
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            String old = this.password;
            this.password = password;
            propertyChangeSupport.firePropertyChange("password", old, password);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }

}
