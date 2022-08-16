package bgu.spl.net;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.api.EncoderDecoderImpl;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.ServerTPC;
import java.util.function.Supplier;

public class main {
    public static void main(String[]args)
    {
        String port = args[1]; //args[0] is hostIP and args[1] is port
        int portnum = Integer.parseInt(port);

        ServerTPC<Message> server = new ServerTPC<>(portnum, new protocolFactory(), new encdecFactory());

        server.serve();
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
