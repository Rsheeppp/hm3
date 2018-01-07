
package server.model;

import common.AccountDTO;


public class ConnectedClient{
    private final long id;
    private final String username;
    private final AccountDTO remoteNode;

    public ConnectedClient(String username, AccountDTO remoteNode, long userId) {
        this.id = userId;
        this.remoteNode = remoteNode;
        this.username = username;
    }
    
    public long getID(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public AccountDTO getRemoteNode(){
        return remoteNode;
    }
}
