package test.rmi.sss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddServerImpl extends UnicastRemoteObject implements AddServer {   
    public AddServerImpl() throws RemoteException {   
        super();   
    }   
    public int AddNumbers(int firstnumber,int secondnumber) throws RemoteException {   
        return firstnumber + secondnumber;   
    }   
} 