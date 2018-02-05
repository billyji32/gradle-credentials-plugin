package nu.studer.gradle.credentials;

class Credentials {
    private String username;
    private String password;

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
