
package client.model;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import common.FileCatalog;
import common.AccountDTO;

public class ClientManager {
	String str = null;
	 int j;
     int k;
     String username;
     String password;
     String fileName;
     String filePath;
     private final Scanner console = new Scanner(System.in);
     private long myIdAtServer;
     private final AccountDTO myRemoteObj;
     private FileCatalog server;
	public ClientManager(FileCatalog server,String str) throws RemoteException
	{
		this.server=server;
		this.str=str;
		myRemoteObj = new ConsoleOutput();
	}
	
    public void printInstructions(){
        System.out.println("register <username> <password>");
        System.out.println("unregister <username> <password>");
        System.out.println("login <username> <password>");
        System.out.println("logout");
        System.out.println("upload <file name> <file path>");
        System.out.println("read <file name>");
        System.out.println("write <file name>");
        System.out.println("list");
        System.out.println("Commands for file owners:");
        System.out.println("modify name <file name> <new file name>");
        System.out.println("modify path <file name> <new file path>");
        System.out.println("modify privacy <file name> <public or private>");
        System.out.println("modify readability <file name> <readable or unreadable>");
        System.out.println("modify writeability <file name> <writeable or unwriteable>");
        System.out.println("modify notification <file name> <notify or unnotify>");
    }
    public boolean login() throws RemoteException {
        boolean loggedIn = false;
    	System.out.println("Logging in");
        j = str.indexOf(' ');
        username = str.substring(0,j);
        password = str.substring(j);
        System.out.println(username + " " + password);
        myIdAtServer = server.login(username, password, myRemoteObj);
        if(myIdAtServer != 0){
            loggedIn = true;
        }
        return loggedIn;
    }
    public boolean logout(boolean loggedIn) throws RemoteException {
    	boolean recev = false;
    	if(loggedIn){
    		System.out.println("Logging out");
            server.logout(myIdAtServer);
            boolean forceUnexport = false;
            UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);
            recev = false;
        }
        else{
        	System.out.println("Not logged in");
        }
		return recev;
    }
    public void register() throws RemoteException{
        System.out.println("Registering user");
        j = str.indexOf(' ');
        username = str.substring(0,j);
        password = str.substring(j);
        server.register(username, password);
    }
    public void unregister() throws RemoteException{
        System.out.println("Unregistering user");
        j = str.indexOf(' ');
        username = str.substring(0,j);
        password = str.substring(j);
        server.unregister(username, password);
    }
    public void upload(boolean loggedIn) throws RemoteException{
        if(loggedIn){
                String privacyAnswer;
                String notificationAnswer;
                boolean privacy = false;
                boolean readable = false;
                boolean writeable = false;
                boolean notification = false;
                j = str.indexOf(' ',1);
                fileName = str.substring(0,j);
                filePath = str.substring(j);
                System.out.println("Is the file public?");
                privacyAnswer = readNextLine();
                if(privacyAnswer.equals("yes")){
                privacy = true; //public
                String readableAnswer;
                String writeableAnswer;
                System.out.println("Is the file readable?");
                readableAnswer = readNextLine();
                if(readableAnswer.equals("yes")){
                    readable = true;
                }
                System.out.println("Is the file writeable?");
                writeableAnswer = readNextLine();
                if(writeableAnswer.equals("yes")){
                writeable = true;
                }
                }
                System.out.println("Do you want to be notified if someone reads or writes to the file?");
                notificationAnswer = readNextLine();
                if(notificationAnswer.equals("yes")){
                      notification = true;
                }
                System.out.println("uploading file");
                server.uploadFile(myIdAtServer, fileName, filePath, privacy, readable, writeable, notification);}
                 else{
                      System.out.println("Not logged in");
                 }
    }
    public void modifyName(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            System.out.println("updating file name");
            j = str.indexOf(' ');
            String string3 = str.substring(j + 1);
            k = string3.indexOf(' ');
            fileName = string3.substring(0,k);
            String newName = string3.substring(k + 1);
            server.updateFileName(myIdAtServer, fileName, newName);
            }
            else{
            System.out.println("Not logged in");
            }
    }
    public void modifyPath(boolean loggedIn) throws RemoteException{          
        if(loggedIn){        
            System.out.println("updating file path");
            j = str.indexOf(' ');
            String string3 = str.substring(j + 1);
            k = string3.indexOf(' ');
            fileName = string3.substring(0,k);
            String newPath = string3.substring(k + 1);
            server.updateFilePath(myIdAtServer, fileName, newPath);
            }
            else{
            System.out.println("Not logged in");
            }
    }
    public void modifyPrivacy(boolean loggedIn) throws RemoteException{
           if(loggedIn){
                System.out.println("updating file privacy");
                boolean privacy = false;
                j = str.indexOf(' ');
                String string3 = str.substring(j + 1);
                k = string3.indexOf(' ');
                fileName = string3.substring(0,k);
                String newPrivacy = string3.substring(k + 1);
                if(newPrivacy.equals("public")){
                privacy = true;
                server.updateFilePrivacy(myIdAtServer, fileName, privacy);
                }
                else if(!newPrivacy.equals("private")){
                System.out.println("Invalid command");
                }
                }
                else{
                System.out.println("Not logged in");
                }
    }
    public void modifyReadability(boolean loggedIn) throws RemoteException{
          if(loggedIn){
            System.out.println("updating file readability");
            boolean readable = false;
            j = str.indexOf(' ');
            String string3 = str.substring(j + 1);
            k = string3.indexOf(' ');
            fileName = string3.substring(0,k);
            String newReadable = string3.substring(k + 1);
            if(newReadable.equals("readable")){
                 readable = true;
                server.updateFileReadability(myIdAtServer, fileName, readable);
            }
            else if(!newReadable.equals("unreadable")){
                 System.out.println("Invalid command");
            }
            }
            else{
                System.out.println("Not logged in");
            }
    }
    public void modifyWriteablity(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            System.out.println("updating file writeability");
            boolean writeable = false;
            j = str.indexOf(' ');
            String string3 = str.substring(j + 1);
            k = string3.indexOf(' ');
            fileName = string3.substring(0,k);
            String newWriteable = string3.substring(k + 1);
            if(newWriteable.equals("writeable")){
                 writeable = true;
            }
            else if(!newWriteable.equals("unwriteable")){
               writeable = false;
            }
            server.updateFilePrivacy(myIdAtServer, fileName, writeable);
            }
            else{
            System.out.println("Not logged in");
           }
    }

   
    public void modifyNotification(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            System.out.println("updating file notification");
            boolean notification = false;
            j = str.indexOf(' ');
            String string3 = str.substring(j + 1);
            k = string3.indexOf(' ');
            fileName = string3.substring(0,k);
            String newNotification = string3.substring(k + 1);
            if(newNotification.equals("notify")){
             notification = true;
            }
            else if(!newNotification.equals("unnotify")){
             notification =false;
            }
            server.updateFilePrivacy(myIdAtServer, fileName, notification);
            }
            else{
            System.out.println("Not logged in");
            }
    }
    public void list(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            ArrayList<String> list = server.listFiles(myIdAtServer);
             for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
        }
        else{
            System.out.println("Not logged in");
                        }
    }
    public void readFile(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            fileName = str;
            System.out.println("File path to read: " + server.readFile(myIdAtServer,fileName));
        }
        else{
            System.out.println("Not logged in");
         }
    }
    public void writeFile(boolean loggedIn) throws RemoteException{
        if(loggedIn){
            fileName = str;
            System.out.println("File path to write: " + server.writeFile(myIdAtServer,fileName));
        }
        else{
            System.out.println("Not logged in");
        }
    }
    public void deleteFile(boolean loggedIn) throws RemoteException{
        if(loggedIn){
              System.out.println("deleting file");
              fileName = str;
              server.deleteFile(myIdAtServer,fileName);
        }
        else{
               System.out.println("Not logged in");
        }
    }
    private String readNextLine() {
        System.out.println(">");
        return console.nextLine();
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

