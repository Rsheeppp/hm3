
package client.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import client.view.StreamHandler;
import common.FileCatalog;


public class Main {
     public static void main(String[] args) {
        try {
            FileCatalog server = (FileCatalog) Naming.lookup("filesystem");
            new StreamHandler().start(server);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Could not start client.");
        }
    }
}
