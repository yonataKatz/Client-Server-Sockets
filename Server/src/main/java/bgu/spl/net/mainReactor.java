package bgu.spl.net;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.api.EncoderDecoderImpl;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Reactor;

import java.util.function.Supplier;

public class mainReactor {
    public static void main(String[]args)
    {
        //args[0] is hostIP and args[1] is port args[2]= threadNumber
        String port = args[1];
        int portnum = Integer.parseInt(port);
        String thredNumber = args[2];
        int thredNum = Integer.parseInt(thredNumber);

        Reactor<Message> reactor = new Reactor<Message>(
                thredNum,
                portnum,
                new protocolFactory(),
                new encdecFactory());

        reactor.serve();
    }






    public static class protocolFactory implements Supplier<BidiMessagingProtocol<Message>>
    {
        public BidiMessagingProtocolImpl get() {
            return new BidiMessagingProtocolImpl();
        }
    }

    public static class encdecFactory implements Supplier<MessageEncoderDecoder<Message>>
    {
        public MessageEncoderDecoder<Message> get() {
            return new EncoderDecoderImpl<>() ;
        }
    }
}
