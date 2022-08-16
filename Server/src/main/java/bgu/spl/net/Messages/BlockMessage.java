package bgu.spl.net.Messages;

public class BlockMessage extends Message{

    //fields
    private String userNameToBlock;

    //constructor
    public BlockMessage(String toBlock){
        userNameToBlock = toBlock;
    }
    //methods


    public String getUserNameToBlock() {
        return userNameToBlock;
    }
}
