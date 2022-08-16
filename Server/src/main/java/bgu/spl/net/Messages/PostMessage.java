package bgu.spl.net.Messages;

import java.util.concurrent.LinkedBlockingQueue;

public class PostMessage extends Message{
    //fields
    private String content;
    private LinkedBlockingQueue<String> tags;

    //constructor
    public PostMessage(String con){

        content = con;
        tags = new LinkedBlockingQueue<>();
        String c = con;
        int index=0;
        index = c.indexOf('@', index);
        int rightIndex = c.indexOf(" ",index);
        while (index>=0 & rightIndex>=0) {
            String name = c.substring(index+1,rightIndex);
            tags.add(name);
            index++;
            index = c.indexOf('@', index);
            rightIndex=c.indexOf(" ",index);
        }
        if (index >=0 & rightIndex<0)
        {
            String name = c.substring(index+1);
            tags.add(name);
        }
    }


    //methods
    public LinkedBlockingQueue<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }
}
