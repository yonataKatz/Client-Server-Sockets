package bgu.spl.net.Messages;

public class NotificationMessage extends Message{

    //fields
    private boolean isPublic;
    private String content;
    private String postName;

    //constructor
    public NotificationMessage(String type, String name, String con){
        if (type.equals("0"))
            isPublic = false;
        else
            isPublic = true;
        postName = name;
        content = con;
    }

    //methods
    @Override
    public int getClientId() {
        return super.getClientId();
    }

    public String getContent() {
        return content;
    }

    public String getPostName() {
        return postName;
    }

    public String isPublic() {
        if (isPublic)
            return "PUBLIC";
        return "PM";
    }

}
