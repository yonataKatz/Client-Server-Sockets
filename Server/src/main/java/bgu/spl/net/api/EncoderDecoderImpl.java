package bgu.spl.net.api;

import bgu.spl.net.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class EncoderDecoderImpl<Message> implements MessageEncoderDecoder<Message> {
    //fields
    private short opcode;
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;

    //TODO: constructor
    public EncoderDecoderImpl() {

    }

    //methods
    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            Message response = null;
            opcode = bytesToShort(bytes);

            //Register
            if (opcode == 1) {
                int i = 2;
                while (bytes[i] != '\0')
                    i++;
                String userName = new String(bytes, 2, i - 2);
                i++;
                int j = i;
                while (bytes[j] != '\0')
                    j++;
                String userPassword = new String(bytes, i, j - i);
                j++;
                i = j;
                while (bytes[i] != '\0')
                    i++;
                String bDay = new String(bytes, j, i - j);
                response = (Message) new RegisterMessage(bDay, userName, userPassword);
            }

            //Login
            if (opcode == 2) {
                int i = 2;
                while (bytes[i] != '\0')
                    i++;
                String userName = new String(bytes, 2, i - 2);
                i++;
                int j = i;
                while (bytes[j] != '\0')
                    j++;
                String userPassword = new String(bytes, i, j - i);
                j++;
                String captcha = new String(bytes, j, 1);
                response = (Message) new LoginMessage(userName, userPassword, captcha);
            }

            //Logout
            if (opcode == 3) {
                response = (Message) new LogoutMessage();
            }

            //Follow
            if (opcode == 4) {
                String isFollow = new String(bytes, 2, 1);
                int i = 3;
                while (bytes[i] != '\0')
                    i++;
                String userName = new String(bytes, 3, i-3);
                response = (Message) new FollowMessage(isFollow, userName);
            }
            //Post
            if (opcode == 5) {
                int i = 2;
                while (bytes[i] != '\0')
                    i++;
                String con = new String(bytes, 2, i - 2);
                response = (Message) new PostMessage(con);
            }
            //PM
            if (opcode == 6) {
                bytes[len++]=';';
                int i = 2;
                while (bytes[i] != '\0')
                    i++;
                String userName = new String(bytes, 2, i - 2);
                i++;
                int j = i;
                while (bytes[j] != ';')
                    j++;
                String con = new String(bytes, i, j - i-1);
                String trueCon = "";
                for (int k=0; k<con.length(); k++)
                {
                    if (con.charAt(k)=='\0')
                        trueCon = trueCon + " ";
                    else
                        trueCon = trueCon + con.charAt(k);
                }
                String dateAndTime = null;
                response = (Message) new PMMessage(userName, trueCon, dateAndTime);
            }
            //Logstat
            if (opcode == 7) {
                response = (Message) new LogstatMessage();
            }
            //Stat
            if (opcode == 8) {
                int i = 2;
                LinkedList<String> userNames = new LinkedList<>();
                while (bytes[i] != '\0') {
                    String curUserName = "";
                    while (bytes[i] != '|' & bytes[i] != '\0') {
                        String curChar = new String(bytes, i, 1);
                        curUserName = curUserName + curChar;
                        i++;
                    }
                    userNames.add(curUserName);
                    if (bytes[i]!='\0')
                        i++;
                }
                response = (Message) new StatMessage(userNames);
            }
            //Notification
            if (opcode == 9) {
                String notiType = new String(bytes, 2, 1);
                int i = 3;
                while (bytes[i] != '\0')
                    i++;
                String postName = new String(bytes, 3, i - 3);
                i++;
                int j = i;
                while (bytes[j] != '\0')
                    j++;
                String content = new String(bytes, i, j - i);
                response = (Message) new NotificationMessage(notiType, postName, content);
            }
            //ACK
            if (opcode == 10) {
                byte[] MOpCBytes = new byte[2];
                MOpCBytes[0] = bytes[2];
                MOpCBytes[1] = bytes[3];
                short MOpC = bytesToShort(MOpCBytes);
                int i = 4;
                while (bytes[i] != '\0')
                    i++;
                String optional = new String(bytes, 4, i - 4);

                response = (Message) new AckMessage(MOpC, optional);
            }
            //Error
            if (opcode == 11) {
                byte[] opErrorForBytes = new byte[2];
                opErrorForBytes[0] = bytes[2];
                opErrorForBytes[1] = bytes[3];
                short opErrorFor = bytesToShort(opErrorForBytes);
                response = (Message) new ErrorMessage(opErrorFor);
            }
            //Block
            if (opcode == 12) {
                int i = 2;
                while (bytes[i] != '\0')
                    i++;
                String userName = new String(bytes, 2, i - 2);
                response = (Message) new BlockMessage(userName);
            }
         //   System.out.println(response.getClass());
            resetBytesAndLen();
            return response;
        } else {
            pushByte(nextByte);
        }
        return null;
    }

    private void resetBytesAndLen() {
        for (int i = 0; i <= len; i++)
            bytes[i] = 0;
        len = 0;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    // we will convert to message to string and the to bytes
    // types of messages that will be encoded are : Ack Error Notification
    public byte[] encode(Message message) {
        String messageToSend = "";

        if (message.getClass().equals(AckMessage.class))
            messageToSend = "ACK " + ((AckMessage) message).getMOpCode() + " " + ((AckMessage) message).getOptional() + "\n";

        if (message.getClass().equals(ErrorMessage.class))
            messageToSend = "Error " + ((ErrorMessage) message).getOpErrorFor() + "\n";

        if (message.getClass().equals(NotificationMessage.class)) {
            NotificationMessage m = (NotificationMessage) message;
            messageToSend = "Notification " + m.isPublic() + " " + m.getPostName() + " " + m.getContent() + "\n";
        }
        return messageToSend.getBytes(StandardCharsets.UTF_8);
    }


    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }
}
