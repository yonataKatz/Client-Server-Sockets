package bgu.spl.net.Messages;

public class AckMessage extends Message{

    //fields
    private short MOpCode;
    private String optional;

    //constructor
    public AckMessage(short m , String o){
        MOpCode = m;
        optional = o;
    }

    //methods

    public short getMOpCode() {
        return MOpCode;
    }

    public String getOptional() {
        return optional;
    }
}
