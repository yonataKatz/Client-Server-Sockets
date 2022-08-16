package bgu.spl.net.Messages;

public class LoginMessage extends Message{
    //fields
    String userName;
    String userPassword;
    String captcha;

    //constructor
    public LoginMessage(String name, String pass, String cap){
        userName=name;
        userPassword=pass;
        captcha=cap;
    }

    //methods

    public String getUserName() {
        return userName;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
