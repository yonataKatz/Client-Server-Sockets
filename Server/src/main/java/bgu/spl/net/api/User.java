package bgu.spl.net.api;

import bgu.spl.net.Messages.Message;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

public class User {
    //fields
    private String name;
    private String password;
    private String birthday;
    private LinkedBlockingQueue<User> followers;
    private LinkedBlockingQueue<User> following;
    private LinkedBlockingQueue<Message> postMessages;
    private LinkedBlockingQueue<Message> privateMessages;
    private LinkedBlockingQueue<Message> missedMessages;


    //constructor
    public User(String n, String p, String bd) {
        name = n;
        password = p;
        birthday = bd;
        followers = new LinkedBlockingQueue<>();
        following = new LinkedBlockingQueue<>();
        postMessages = new LinkedBlockingQueue<>();
        privateMessages = new LinkedBlockingQueue<>();
        missedMessages = new LinkedBlockingQueue<>();
    }

    //methods
    public String getName() { return name;}

    public LinkedBlockingQueue<Message> getMissedMessages() { return missedMessages;}

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public LinkedBlockingQueue<User> getFollowers() {
        return followers;
    }

    public LinkedBlockingQueue<User> getFollowing() {
        return following;
    }

    public void addFollower(User f) {
        followers.add(f);
    }

    public void addFollowing(User f) {
        following.add(f);
    }

    public void removeFollowing(User f) {
        following.remove(f);
    }

    public void removeFollower(User f) {
        followers.remove(f);
    }

    public int getAge() {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        int userYear = Integer.parseInt(birthday.substring(6));
        return curYear - userYear;
    }

    //update it in the correct place !!!!!!!
    public LinkedBlockingQueue<Message> getPostMessages() {
        return postMessages;
    }

    public LinkedBlockingQueue<Message> getPrivateMessages() {
        return privateMessages;
    }

    public void addPostMessages (Message m){
        postMessages.add(m);
    }

    public void addPrivateMessages(Message m ){
        privateMessages.add(m);
    }
}