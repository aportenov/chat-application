package org.chatapp.models;

public class UserViewModel {

    private String username;
    private String image;

    public UserViewModel() {
    }

    public UserViewModel(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
