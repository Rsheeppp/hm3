
package client.view;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import client.model.ClientManager;
import common.FileCatalog;
import common.AccountDTO;


public class StreamHandler implements Runnable{
    private boolean receivingCmds = false;
    private final Scanner console = new Scanner(System.in);
    private final AccountDTO myRemoteObj;
    private FileCatalog server;
    private long myIdAtServer;
    Command cmd;
    boolean loggedIn = false;
    
    public StreamHandler() throws RemoteException{
        myRemoteObj = new ConsoleOutput();
    }
    
    
    public void start(FileCatalog server) {
        this.server = server;
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (receivingCmds) {
            try {
                String clientInput = readNextLine();
                int i = clientInput.indexOf(' ');
                int j;
                int k;
                String string = "";
                String string2 = "";
                String username;
                String password;
                String fileName;
                String filePath;
                cmd = Command.UNKNOWN;
                if(i < 0){
                    if(clientInput.equals("help")){
                        cmd = Command.HELP;

                    }
                    else if(clientInput.equals("logout")){
                        cmd = Command.LOGOUT;

                    }
                    else if(clientInput.equals("list")){
                        cmd = Command.LIST_FILE;

                    }
                }
                else{
                    string = clientInput.substring(0,i);
                   if(string.equals("modify")){
                        getUpdateCommand(clientInput.substring(i + 1));
                    }
                   else{
                    cmd = checkCmd(string);
                   }
                    string2 = clientInput.substring(i + 1);
                }
                ClientManager cm = new ClientManager(server,string2);
                switch (cmd) {
                    case HELP:
                        cm.printInstructions();
                        break;
                    case LOGIN:
                       
                        loggedIn = cm.login();
                        break; 
                    case LOGOUT:
                        
                    	receivingCmds = cm.logout(loggedIn);
                        break;
                    case REGISTER:
                        
                        cm.register();
                        break;
                    case UNREGISTER:
                        
                        cm.unregister();
                        break;
                    case UPLOAD_FILE:
                        
                        cm.upload(loggedIn);
                        break;
                    case MODIFY_FILE_NAME:
                       
                        cm.modifyName(loggedIn);
                        break;
                    case MODIFY_FILE_PATH:
                        
                        cm.modifyPath(loggedIn);
                        break;
                    case MODIFY_FILE_PRIVACY:
                        
                        cm.modifyPrivacy(loggedIn);
                        break;
                    case MODIFY_FILE_READABILITY:
                       
                        cm.modifyReadability(loggedIn);
                        break;
                    case MODIFY_FILE_WRITEABILITY:
                     
                        cm.modifyWriteablity(loggedIn);
                        break;
                    case MODIFY_FILE_NOTIFICATION:
                     
                        cm.modifyNotification(loggedIn);
                        break;
                    case LIST_FILE:
                       
                        cm.list(loggedIn);
                        break;
                    case READ_FILE:
                     
                        cm.readFile(loggedIn);
                        break;
                    case WRITE_FILE:

                        cm.writeFile(loggedIn);
                        break;
                    case DELETE_FILE:
                       
                        cm.deleteFile(loggedIn);
                        break;
                    case UNKNOWN:
                        break;
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
                e.printStackTrace();
            }
        }
    }
    
    private Command checkCmd(String input){
        switch(input)
        {
            case "login":
                cmd = Command.LOGIN;
            break;
            case "register":
            	cmd = Command.REGISTER;
            break;
            case "unregister":
            	cmd = Command.UNREGISTER;
            break;
            case "upload":
            	cmd = Command.UPLOAD_FILE;
            break;
            case "read":
            	cmd = Command.READ_FILE;
            break;
            case "write":
            	cmd = Command.WRITE_FILE;
            break;
            case "delete":
            	cmd = Command.DELETE_FILE;
            break;
                    
        }
		return cmd;
    }
    
    private String readNextLine() {
        System.out.print(">");
        return console.nextLine();
    }
    
    private void getUpdateCommand(String input){
        int j = input.indexOf(' ');
        String updateCommand = input.substring(0,j);
        System.out.println(updateCommand);
        if(updateCommand.equals("name")){
            cmd = Command.MODIFY_FILE_NAME;
        }
        else if(updateCommand.equals("path")){
            cmd = Command.MODIFY_FILE_PATH;
        }
        else if(updateCommand.equals("readability")){
            cmd = Command.MODIFY_FILE_READABILITY;
        }
        else if(updateCommand.equals("writeability")){
            cmd = Command.MODIFY_FILE_WRITEABILITY;
        }
        else if(updateCommand.equals("privacy")){
            cmd = Command.MODIFY_FILE_PRIVACY;
        }
        else if(updateCommand.equals("notification")){
            cmd = Command.MODIFY_FILE_NOTIFICATION;
        }
    }
    
     private class ConsoleOutput extends UnicastRemoteObject implements AccountDTO {

        public ConsoleOutput() throws RemoteException {
        }

        @Override
        public void recvMsg(String msg) {
            System.out.println((String) msg);
        }
    }  


}