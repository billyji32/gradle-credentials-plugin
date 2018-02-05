package nu.studer.gradle.credentials;

import nu.studer.gradle.credentials.domain.CredentialsEncryptor;
import nu.studer.gradle.credentials.domain.CredentialsPersistenceManager;
import org.gradle.api.DefaultTask;

import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import javax.swing.*;

import java.io.File;

/**
 * Adds/updates the given credentials, specified as project properties.
 */
public class AddCredentialsTask extends DefaultTask {
    private CredentialsEncryptor credentialsEncryptor;
    private CredentialsPersistenceManager credentialsPersistenceManager;


    public void setCredentialsEncryptor(CredentialsEncryptor credentialsEncryptor) {
        this.credentialsEncryptor = credentialsEncryptor;
    }

    public void setCredentialsPersistenceManager(CredentialsPersistenceManager credentialsPersistenceManager) {
        this.credentialsPersistenceManager = credentialsPersistenceManager;
    }

    @OutputFile
    public File getEncryptedPropertiesFile() {
        return credentialsPersistenceManager.getCredentialsFile();
    }

    @TaskAction
    void addCredentials() {
        final Credentials credentials = new Credentials();
        getInputFromUser(credentials);
    }

    public void getInputFromUser (final Credentials credentials)
    {
        GetCredentialsFrame frame = new GetCredentialsFrame(getProjectProperty("toolName"), credentials, credentialsEncryptor, credentialsPersistenceManager);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String getProjectProperty(String key) {
        return (String) getProject().getProperties().get(key);
    }
}

