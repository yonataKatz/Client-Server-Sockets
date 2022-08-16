package bgu.spl.net.Messages;

public class ErrorMessage extends Message{

    //fields
    private short opErrorFor;

    //constructor
    public ErrorMessage(short er){

        opErrorFor = er;
    }


    //methods
    public short getOpErrorFor() {
        return opErrorFor;
    }
}
