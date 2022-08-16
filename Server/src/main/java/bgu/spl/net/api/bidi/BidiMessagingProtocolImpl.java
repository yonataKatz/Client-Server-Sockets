package bgu.spl.net.api.bidi;
import bgu.spl.net.Messages.*;
import bgu.spl.net.api.User;
import bgu.spl.net.srv.BlockingConnectionHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {


    //Fields
    private ConnectionsImpl<Message> connections =null;
    private int Id =-1;
    private boolean shouldTerminate=false;


    //Methods
    public void start(int connectionId, Connections<Message> connections)
    {
        Id = connectionId;
        this.connections=(ConnectionsImpl<Message>) connections;
        ((ConnectionsImpl)connections).getIdRegisterMap().put(Id,false);
        ((ConnectionsImpl)connections).getIdLoginMap().put(Id,false);
    }

    public void process(Message message) {

        message.setClientId(Id);
        Message response=null;
        //pre - not register,
        if (message.getClass().equals(RegisterMessage.class)) {
            //checks if client who wants to Registered is already registered
            if (connections.getIdRegisterMap().get(Id)==true) {
                response = new ErrorMessage((short) 1);
            }
            else
            {
                String n = ((RegisterMessage) message).getUserName();
                String bd = ((RegisterMessage) message).getBday();
                String pass = ((RegisterMessage) message).getUserPassword();

                //checks if this client was registered before ! under different ID
                if (connections.getIdUsernameMap().get(n)!=null)
                {
                    Integer newId = Id;
                    Id = connections.getIdUsernameMap().get(n);
                    connections.getIdLoginMap().remove(newId);
                    connections.getIdRegisterMap().remove(newId);
                    response = new ErrorMessage((short)1);
                }
                else
                {
                    response = new AckMessage((short) 1, "Registration Successful !");
                    connections.getIdRegisterMap().put(Id, true);
                    connections.getIdLoginMap().put(Id, false);
                    connections.getIdUsernameMap().put(n, Id);
                    connections.getIdUserMap().put(Id, new User(n, pass, bd));
                    connections.getBlockingMap().put(Id, new LinkedBlockingQueue<>());
                }
            }
        }

        //pre- register , match username and password , not log in already ,captcha is 1
        if (message.getClass().equals(LoginMessage.class)) {
            String name = ((LoginMessage) message).getUserName();
            String cap = ((LoginMessage) message).getCaptcha();
            if (cap.equals( "1")) {
                //client login after being logged out
                if (connections.getIdUsernameMap().get(name) != null && connections.getIdUsernameMap().get(name) != Id) {
                    Integer newId = Id;
                    Id = connections.getIdUsernameMap().get(name);
                    connections.getIdLoginMap().remove(newId);
                    connections.getIdRegisterMap().remove(newId);
                    connections.getIdLoginMap().put(Id, true);
                    connections.getClientsHandlers().put(Id,connections.getClientsHandlers().get(newId));

                    //now we want to send all missed Messages
                    User u = connections.getIdUserMap().get(Id);
                    LinkedBlockingQueue<Message> missed = u.getMissedMessages();
                    for (Message element : missed)
                        connections.send(Id, element);
                    response = new AckMessage((short) 2, "Login Successful");

                } else {
                    //checks if register , didn't login before , captcha is 1
                    if ((!connections.getIdRegisterMap().get(Id)) ||
                            connections.getIdLoginMap().get(Id) ||
                            ((LoginMessage) message).getCaptcha().equals("0") ||
                            !((LoginMessage) message).getUserPassword().equals(connections.getIdUserMap().get(Id).getPassword())) {
                        response = new ErrorMessage((short) 2);

                    }
                    else {
                        User u = connections.getIdUserMap().get(Id);
                        LinkedBlockingQueue<Message> missed = u.getMissedMessages();
                        for (Message element : missed)
                            connections.send(Id, element);

                        response = new AckMessage((short) 2, "Login Successful !");
                        connections.getIdLoginMap().put(Id, true);
                    }
                }
            }
            else {
                response = new ErrorMessage((short) 2);
            }
        }


        //register, log in
        if (message.getClass().equals(LogoutMessage.class)) {
            //user wasn't even logged in
            if (connections.getIdLoginMap().get(Id)==false)
                response=new ErrorMessage((short)3);
            else
            {
                response= new AckMessage((short)3, "Logout Successful !" );
                connections.getIdLoginMap().put(Id,false);
            }
        }

        //the user need to be login,if follow userName is not already in follow list / if unFollow need to be in follow list
        if (message.getClass().equals(FollowMessage.class)) {
            if (!connections.getIdRegisterMap().get(Id) || !connections.getIdLoginMap().get(Id)) {
                response = new ErrorMessage((short) 4);
            }
            else {
                String userName = ((FollowMessage) message).getUserName();
                Integer userId = connections.getIdUsernameMap().get(userName);
                //im trying to follow someone who blocked me
                if (userId==null || connections.getBlockingMap().get(userId).contains(Id)) {
                    response = new ErrorMessage((short)4);
                } else {
                    User userToF = connections.getIdUserMap().get(userId);
                    User userWF = connections.getIdUserMap().get(Id);
                    //check if the client want to follow or unFollow
                    if (((FollowMessage) message).isFollow()) {
                        //follow-check if already following
                        if (userToF.getFollowers().contains(userWF))
                        {
                            response = new ErrorMessage((short) 4);
                        }
                        else {
                            response = new AckMessage((short) 4, "Follow successful !");
                            userToF.addFollower(userWF);
                            userWF.addFollowing(userToF);
                        }
                    } else {
                        //unFollow
                        if (!userToF.getFollowers().contains(userWF)) {
                            response = new ErrorMessage((short) 4);
                        } else {
                            response = new AckMessage((short) 4, "unFollow successful !");
                            userToF.removeFollower(userWF);
                            userWF.removeFollowing(userToF);
                        }
                    }
                }
            }
        }

        //user need to be logged in
        if (message.getClass().equals(PostMessage.class)) {
            //the poster is not logged in
            if (!connections.getIdLoginMap().get(Id))
                response = new ErrorMessage((short) 5);
            else {
                LinkedBlockingQueue<String> tags = ((PostMessage)message).getTags();
                LinkedBlockingQueue<User> followers = connections.getIdUserMap().get(Id).getFollowers();
                //saving the post
                User user = connections.getIdUserMap().get(Id);
                user.addPostMessages(message);
                Iterator<String> it=tags.iterator();
                while(it.hasNext())
                {
                    String name = it.next();
                    Integer u_id = connections.getIdUsernameMap().get(name);
                    if (u_id!=null) {
                        User u =connections.getIdUserMap().get(u_id);
                        if (u!=null)
                            //checks if the user is not following me and didn't block me
                            if (!followers.contains(u) & !connections.getBlockingMap().get(u_id).contains(Id)) {
                                NotificationMessage nm = new NotificationMessage("1", user.getName(),((PostMessage) message).getContent());
                                //send notification
                                if (connections.getIdLoginMap().get(u_id))
                                     connections.send(u_id, nm);
                                else
                                    u.getMissedMessages().add(nm);
                            }
                    }
                }
                Iterator<User> iter = followers.iterator();
                while(iter.hasNext())
                {
                    User u = iter.next();
                    Integer u_id = connections.getIdUsernameMap().get(u.getName());
                    if (u_id!=null) {
                        NotificationMessage nm = new NotificationMessage("1", user.getName(),((PostMessage) message).getContent());
                        //send notification
                        if (connections.getIdLoginMap().get(u_id))
                            connections.send(u_id, nm);
                        else
                            u.getMissedMessages().add(nm);
                    }
                }
                response = new AckMessage((short)5, "Posting successful");
            }
        }

        //user login , user that should get the message must be register
        if (message.getClass().equals(PMMessage.class)) {
            //user is not logged in or blocked me
            String toUserName = ((PMMessage) message).getToUserName();
            Integer u_id = connections.getIdUsernameMap().get(toUserName);
            if (!connections.getIdLoginMap().get(Id) || u_id==null || connections.getBlockingMap().get(u_id).contains(Id))
                response = new ErrorMessage((short)6);
            else
            {
                LinkedList<String> forbid = connections.getForbiddenWords();
                String updated ="";
                String current = "";
                String content = ((PMMessage)message).getContent();
                int index=0;
                int rightindex = content.indexOf(' ',0);
                while (rightindex!=-1 & index<content.length())
                {
                    current = content.substring(index,rightindex);
                    if (forbid.contains(current))
                        updated=updated + " <forbidden>";
                    else
                        updated=updated+" "+current;
                    index=rightindex+1;
                    rightindex = content.indexOf(' ',index);
                }
                if (forbid.contains(content.substring(index)))
                    updated = updated + "<forbidden>";
                else
                    updated = updated +" "+ content.substring(index);
                updated = updated.substring(1);

                ((PMMessage)message).setContent(updated);
                //send notification for PM
                String sendUserName = connections.getIdUserMap().get(Id).getName();
                NotificationMessage nm = new NotificationMessage("0", sendUserName , ((PMMessage) message).getContent());
                //update PM queue
                User toUser = connections.getIdUserMap().get(u_id);
                if (connections.getIdLoginMap().get(u_id)) {
                    connections.send(u_id, nm);
                    toUser.addPrivateMessages(message);
                }
                else
                    toUser.getMissedMessages().add(nm);
                response = new AckMessage((short) 6, "Personal Message sent !");
            }
        }

        //register and login
        // make sure dont get blocked users STATS
        if (message.getClass().equals(LogstatMessage.class)) {
            if(!connections.getIdRegisterMap().get(Id) || !connections.getIdLoginMap().get(Id))
                response = new ErrorMessage((short)7);
            else
            {
                String logstatInfo = "";
                Iterator<Integer> loginUsersIdIterator = connections.getIdLoginMap().keySet().iterator();
                LinkedBlockingQueue<Integer> blocks = connections.getBlockingMap().get(Id);
                while (loginUsersIdIterator.hasNext())
                {
                    int curId = loginUsersIdIterator.next();

                    if ( !blocks.contains(curId) && connections.getIdLoginMap().get(curId))
                    {
                        User curUser = connections.getIdUserMap().get(curId);
                        logstatInfo = logstatInfo + curUser.getAge() +" " + curUser.getPostMessages().size() + " " + curUser.getFollowers().size() + " " + curUser.getFollowing().size() + '\n';
                    }
                }
                response = new AckMessage((short)7,logstatInfo);
            }
        }

        //register and login are requaired
        if (message.getClass().equals(StatMessage.class)) {
            if (!connections.getIdRegisterMap().get(Id) || !connections.getIdLoginMap().get(Id))
                response = new ErrorMessage((short)8);
            else
            {
                List<String> userNames = ((StatMessage) message).getUserNames();
                Iterator<String> namesIter = userNames.iterator();
                String stateInfo ="";
                while (namesIter.hasNext()) {
                    String curUserName = namesIter.next();
                    Integer curUserId = connections.getIdUsernameMap().get(curUserName);
                    if (curUserId != null) {
                        Boolean isRegister = connections.getIdRegisterMap().get(curUserId);
                        LinkedBlockingQueue<Integer> my_block_list = connections.getBlockingMap().get(Id);
                        if (isRegister != null & !my_block_list.contains(curUserId)) {
                            User curUser = connections.getIdUserMap().get(curUserId);
                            stateInfo = stateInfo + curUser.getAge() + " " + curUser.getPostMessages().size() + " " + curUser.getFollowers().size() + " " + curUser.getFollowing().size() + "\n";
                        } else {
                            response = new ErrorMessage((short) 8);
                            break;
                        }
                    }
                    else
                    {
                        response = new ErrorMessage((short) 8);
                        break;
                    }
                }
                //check if the went through without insert Error to response
                if(response==null)
                    response = new AckMessage((short)8,stateInfo);
            }
        }

        //user wants to Block someone
        if (message.getClass().equals(BlockMessage.class)) {
            String name = ((BlockMessage) message).getUserNameToBlock();
            //name doesnt exist
            if (connections.getIdUsernameMap().get(name)==null)
                response = new ErrorMessage((short)12);
            else
            {
                response = new AckMessage((short)12, "Blocked as requested");
                Integer id_toBlock = (Integer)connections.getIdUsernameMap().get(name);
                User toBlock = connections.getIdUserMap().get(id_toBlock);
                blockProcedure(id_toBlock, toBlock);
            }
        }

        connections.send(Id,response );
        if (response.getClass().equals(AckMessage.class) && ((AckMessage)response).getMOpCode()==(short)3)
            shouldTerminate=true;
    }


    //removes from each other blockingLists , followersLists and followingLists
    private void blockProcedure (int id_toBlock, User toBlock)
    {
        connections.getBlockingMap().get(Id).add(id_toBlock);
        connections.getBlockingMap().get(id_toBlock).add(Id);

        connections.getIdUserMap().get(Id).getFollowers().remove(toBlock);
        connections.getIdUserMap().get(Id).getFollowing().remove(toBlock);

        connections.getIdUserMap().get(id_toBlock).getFollowers().remove(connections.getIdUserMap().get(Id));
        connections.getIdUserMap().get(id_toBlock).getFollowing().remove(connections.getIdUserMap().get(Id));
    }

    /**
     * @return true if the connection should be terminated
     **/
    public boolean shouldTerminate()
    {
        return shouldTerminate;
    }
}