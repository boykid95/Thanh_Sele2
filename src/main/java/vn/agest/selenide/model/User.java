package vn.agest.selenide.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.agest.selenide.common.ConfigFileReader;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;

    public static User defaultUser() {
        String username = ConfigFileReader.getCredentialProperty("username");
        String password = ConfigFileReader.getCredentialProperty("password");
        return new User(username, password);
    }
}
