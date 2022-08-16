package bgu.spl.net.Messages;

public class Message {
    //fields
    private int clientId;

    //constructor
    public Message()
    {}

    //methods
    public void setClientId(int id_)
    {
        clientId=id_;
    }
    public int getClientId()
    {
        return clientId;
    }
}
