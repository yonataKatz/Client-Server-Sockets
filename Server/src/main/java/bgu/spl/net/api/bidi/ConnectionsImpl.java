package bgu.spl.net.api.bidi;

import bgu.spl.net.api.User;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class ConnectionsImpl<T> implements Connections<T> {

    //fields
    private ConcurrentHashMap<Integer, BlockingConnectionHandler> clientsHandlers;
    private ConcurrentHashMap<Integer, Boolean> idLoginMap;
    private ConcurrentHashMap<Integer, Boolean> idRegisterMap;
    private ConcurrentHashMap<String,Integer> idUsernameMap;
    private ConcurrentHashMap<Integer,User> idUserMap;
    private LinkedList<String> forbiddenWords ;
    private ConcurrentHashMap<Integer, LinkedBlockingQueue<Integer>> blockingMap;



    //constructor
    public ConnectionsImpl ()
    {
        clientsHandlers = new ConcurrentHashMap<>();
        idLoginMap = new ConcurrentHashMap<>();
        idRegisterMap = new ConcurrentHashMap<>();
        idUsernameMap =  new ConcurrentHashMap<>();
        idUserMap =  new ConcurrentHashMap<>();
        forbiddenWords = new LinkedList<>();
        blockingMap = new ConcurrentHashMap<>();
        forbiddenWords.add("war");
        forbiddenWords.add("trump");
        forbiddenWords.add("bitch");
        forbiddenWords.add("fuck");
//        forbiddenWords.add("");
    }



    public ConcurrentHashMap<Integer, LinkedBlockingQueue<Integer>> getBlockingMap() {
        return blockingMap;
    }

    public LinkedList<String> getForbiddenWords() {
        return forbiddenWords;
    }

    public ConcurrentHashMap<Integer, User> getIdUserMap() {
        return idUserMap;
    }

    public ConcurrentHashMap<String,Integer> getIdUsernameMap()
    {
        return idUsernameMap;
    }

    public ConcurrentHashMap<Integer,BlockingConnectionHandler> getClientsHandlers()
    {
        return clientsHandlers;
    }

    public ConcurrentHashMap<Integer,Boolean> getIdLoginMap()
    {
        return idLoginMap;
    }

    public ConcurrentHashMap<Integer,Boolean> getIdRegisterMap()
    {
        return idRegisterMap;
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        ConnectionHandler curCon = clientsHandlers.get(connectionId);
        curCon.send((Object) msg);
        return true;
    }



    @Override
    public void broadcast(Object msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
