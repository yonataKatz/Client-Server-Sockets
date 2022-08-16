package bgu.spl.net.srv;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ServerTPC<T> extends BaseServer<T>{

    //fields
    private Connections connections = new ConnectionsImpl<Message>();
    private AtomicInteger ids=new AtomicInteger(-1);


    //constructor
    public ServerTPC(int port, Supplier<BidiMessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encdecFactory){
        super( port, protocolFactory, encdecFactory);
    }


    //methods
    @Override
    protected void execute(BlockingConnectionHandler<T> handler) {
        int id = ids.incrementAndGet();
        ConnectionsImpl con = (ConnectionsImpl)connections;
        con.getClientsHandlers().put(id,handler);
        handler.getProtocol().start(id,connections);
        Thread t = new Thread(handler);
        t.start();

    }
}

