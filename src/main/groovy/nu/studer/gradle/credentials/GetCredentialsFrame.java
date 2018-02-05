package nu.studer.gradle.credentials;

import nu.studer.gradle.credentials.domain.CredentialsEncryptor;
import nu.studer.gradle.credentials.domain.CredentialsPersistenceManager;
import nu.studer.java.util.OrderedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

class GetCredentialsFrame extends JDialog {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCredentialsTask.class);

    private CredentialsEncryptor credentialsEncryptor;
    private CredentialsPersistenceManager credentialsPersistenceManager;

    private JTextField username;
    private JTextField password;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public void setCredentials(Credentials credentials) {
        String value = credentials.getUsername();
        char[] placeholderValue = new char[value.length()];
        Arrays.fill(placeholderValue, '*');

        LOGGER.debug(String.format("Add credentials with key: '%s', value: '%s'", USERNAME, new String(placeholderValue)));

        // read the current persisted credentials
        OrderedProperties orderedCredentials = credentialsPersistenceManager.readCredentials();

        // encrypt value and update credentials
        String encryptedValue = credentialsEncryptor.encrypt(value);
        orderedCredentials.setProperty(USERNAME, encryptedValue);

        // persist the updated credentials
        credentialsPersistenceManager.storeCredentials(orderedCredentials);

        value = credentials.getPassword();

        placeholderValue = new char[value.length()];
        Arrays.fill(placeholderValue, '*');
        LOGGER.debug(String.format("Add credentials with key: '%s', value: '%s'", PASSWORD, new String(placeholderValue)));

        // read the current persisted credentials
        orderedCredentials = credentialsPersistenceManager.readCredentials();

        // encrypt value and update credentials
        encryptedValue = credentialsEncryptor.encrypt(value);
        orderedCredentials.setProperty(PASSWORD, encryptedValue);

        // persist the updated credentials
        credentialsPersistenceManager.storeCredentials(orderedCredentials);
    }


    @SuppressWarnings("LeakingThisInConstructor")
    GetCredentialsFrame(String toolName, final Credentials credentials, CredentialsEncryptor credentialsEncryptor, CredentialsPersistenceManager credentialsPersistenceManager) {
        this.credentialsEncryptor = credentialsEncryptor;
        this.credentialsPersistenceManager = credentialsPersistenceManager;

        setTitle(toolName + "Credential Manager");
        setSize(300, 160);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        GetCredentialsPanel panel = new GetCredentialsPanel();
        panel.add(new JLabel("Username:"), BorderLayout.NORTH);
        username = new JTextField();
        panel.add(username, BorderLayout.NORTH);
        panel.add(new JLabel("Password:"), BorderLayout.NORTH);
        password = new JPasswordField();
        password.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                credentials.setUsername(getUsername());
                credentials.setPassword(getPassword());
                setCredentials(credentials);
                dispose();
            }
        });
        panel.add(password, BorderLayout.NORTH);
        final JButton b2 = new JButton("Done");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                credentials.setUsername(getUsername());
                credentials.setPassword(getPassword());
                setCredentials(credentials);
                dispose();
            }
        });
        b2.setBorderPainted(false);
        b2.setFocusPainted(false);
        b2.setContentAreaFilled(false);
        panel.add(b2, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
    }

    class GetCredentialsPanel extends JPanel implements ActionListener {

        GetCredentialsPanel() {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(0, 1));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private String getUsername() {
        return username.getText();
    }

    private String getPassword() {
        return password.getText();
    }
}