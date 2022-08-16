package bgu.spl.net.Messages;

import java.util.List;

public class StatMessage extends Message{
    //fields
    private List<String> userNames;

    //constructor
    public StatMessage(List names){
        userNames = names;
    }

    //methods

    public List<String> getUserNames() {
        return userNames;
    }
}
