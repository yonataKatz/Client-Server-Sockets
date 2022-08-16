package bgu.spl.net.Messages;

public class PMMessage extends Message{
    //fields
    private String toUserName;
    private String content;
    private String dateAndTime;

    //constructor
    public PMMessage(String n, String con, String dAt){
        toUserName=n;
        content = con;
        dateAndTime = dAt;
    }

    
    //methods
    public void setContent(String updated) {
        content = updated;
    }

    public String getToUserName() {
        return toUserName;
    }

    public String getContent() {
        return content;
    }
}
