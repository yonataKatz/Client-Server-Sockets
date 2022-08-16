package bgu.spl.net.Messages;

public class RegisterMessage extends Message{

    //fields
    String bday;
    String userName;
    String userPassword;

    //constructor
    public RegisterMessage(String BD, String name,String password)
    {
        bday=BD;
        userName=name;
        userPassword=password;
    }

    public String getUserName() {
        return userName;
    }

    public String getBday() {
        return bday;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public int getClientId() {
        return super.getClientId();
    }
}
