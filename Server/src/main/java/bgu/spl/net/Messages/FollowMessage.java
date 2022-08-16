package bgu.spl.net.Messages;

public class FollowMessage extends Message{
    //fields
    private boolean isFollow;
    private String userName;

    //constructor
    public FollowMessage(String f , String name){
        if(f.equals("0"))
            isFollow = true;
        else
            isFollow = false;
        userName = name;
    }

    //methods
    public boolean isFollow() {
        return isFollow;
    }

    public String getUserName() {
        return userName;
    }
}
