package model;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class User {
    private String username;
    private String password;
    private ImageView avatar;
    private int bestScore;
    private int timeInBestScore;

    private static ArrayList<User> users = new ArrayList<>();

    public User(String username, String password, ImageView avatar) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeInBestScore() {
        return timeInBestScore;
    }

    public void setTimeInBestScore(int timeInBestScore) {
        this.timeInBestScore = timeInBestScore;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public boolean isPasswordCorrect(String inputPassword) {
        return inputPassword.equals(password);
    }

    public static void createUser(String username, String password, ImageView avatar) {
        User user = new User(username, password, avatar);
        users.add(user);
    }

    public static void scoreBord() {
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                if ((users.get(i).bestScore < users.get(j).bestScore) || (users.get(i).getBestScore() == users.get(j).getBestScore() &&
                        users.get(i).timeInBestScore > users.get(j).timeInBestScore)) {
                    Collections.swap(users, i, j);
                }
            }
        }
    }
}
