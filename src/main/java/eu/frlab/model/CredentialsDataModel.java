package eu.frlab.model;

public class CredentialsDataModel {

    private final String username;
    private final String password;

    public CredentialsDataModel(String[] credentials) {
        this.username = credentials[0];
        this.password = credentials[1];
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
