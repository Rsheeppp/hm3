
package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import server.model.ConnectedClient;
import server.integration.FileCatalogDAO;
import common.FileCatalog;
import common.AccountDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.integration.FileCatalogDBException;


public class Controller extends UnicastRemoteObject implements FileCatalog{
    private FileCatalogDAO fileCatalogDb;
    private Map<Long, ConnectedClient> connectedClients = Collections.synchronizedMap(new HashMap<>());
    private long idIncrementer;

    public Controller(String databaseName) throws RemoteException, ClassNotFoundException, SQLException{
        super();
        try {
            fileCatalogDb = new FileCatalogDAO(databaseName, this);
            idIncrementer = 0;
        } catch (FileCatalogDBException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public long login(String username, String password, AccountDTO remoteNode) throws RemoteException {
        try {
            if(fileCatalogDb.checkUserLogin(username, password)){
                Iterator it = connectedClients.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry checkClient = (Map.Entry) it.next();
                    ConnectedClient c = (ConnectedClient) checkClient.getValue();
                    if(c.getUsername().equals(username)){
                        connectedClients.remove(c.getID());
                        break;
                    }
                }
                idIncrementer = ++idIncrementer;
                long userId = idIncrementer;
                ConnectedClient newClient = new ConnectedClient(username, remoteNode, userId);
                connectedClients.put(userId, newClient);
                return userId;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public void logout(long userID) throws RemoteException {
        connectedClients.remove(userID);
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        try {
            fileCatalogDb.registerUser(username, password);
            System.out.println("registered user");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregister(String username, String password) throws RemoteException {
        try {
            return fileCatalogDb.unregisterUser(username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean uploadFile(long userID, String fileName, String filePath, boolean privacy, boolean readable, boolean writeable, boolean notification) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.uploadFile(fileName, username, filePath, privacy, readable, writeable, notification);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't upload file");
        }
        return false;
    }

    @Override
    public boolean updateFileName(long userID, String fileName, String newName) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFileName(fileName, username, newName); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFilePath(long userID, String fileName, String filePath) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFilePath(fileName, username, filePath); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFilePrivacy(long userID, String fileName, boolean privacy) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFilePrivacy(fileName, username, privacy);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileReadability(long userID, String fileName, boolean readable) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFileReadability(fileName, username, readable);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileWriteability(long userID, String fileName, boolean writeable) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFileWriteability(fileName, username, writeable); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public boolean updateFileNotifications(long userID, String fileName, boolean notification) throws RemoteException {
        String username = connectedClients.get(userID).getUsername();
        try {
            fileCatalogDb.updateFileNotification(fileName, username, notification); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't update file");
        }
        return false;
    }

    @Override
    public ArrayList listFiles(long userID) throws RemoteException {
        try {
            return fileCatalogDb.listFiles(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't list files");
        }
        return null;
    }

    @Override
    public String readFile(long userID, String fileName) throws RemoteException {
        String username = connectedClients.get(userID).getUsername(); 
        try {
            return fileCatalogDb.readFile(fileName, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't read file");
        }
        return null;
    }

    @Override
    public String writeFile(long userID, String fileName) throws RemoteException {
         String username = connectedClients.get(userID).getUsername(); 
        try {
            return fileCatalogDb.writeFile(fileName, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
            sendToClient(userID, "Couldn't read file");
        }
        return null;
    }
    
    @Override
    public void deleteFile(long userID, String filename) throws RemoteException{
        String username = connectedClients.get(userID).getUsername(); 
        try {
            fileCatalogDb.deleteFile(filename, username);
        } catch (SQLException ex) {
            sendToClient(userID, "Couldn't delete file");;
        }
    }
    
    public void notifyClient(String username, String filename) throws RemoteException{
           Iterator it = connectedClients.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry checkClient = (Map.Entry) it.next();
                    ConnectedClient c = (ConnectedClient) checkClient.getValue();
                    if(c.getUsername().equals(username)){
                        String msg = "A user has accessed file " + filename;
                        sendToClient(c.getID(),msg);
                        break;
                    }
                }
    }
    
    private void sendToClient(long userID, String msg) throws RemoteException{
        ConnectedClient client = connectedClients.get(userID);
        client.getRemoteNode().recvMsg(msg);
    }
}
