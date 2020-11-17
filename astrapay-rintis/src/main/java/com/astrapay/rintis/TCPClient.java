package com.astrapay.rintis;

import java.io.*;
import java.net.*;

class TCPClient {
    //String name="";
    String host = "172.20.3.120";
    int port = 2505;
    Socket socket = null;
    public static void main(String args[]) throws Exception{
        TCPClient client = new TCPClient();
        client.SendToServer("08008220000000010000040000000000000011030346593552240176011001112N003602001?");
        System.out.println("Server Said(1): "+client.RecieveFromServer());
        //client.SendToServer("08008220000000010000040000000000000011021000490004330176011001112N003602001?");
        //System.out.println("Server Said(2): "+client.RecieveFromServer());
        client.close();
    }

    /*public void connect() throws Exception{
        TCPClient client = new TCPClient();
        client.SendToServer("08008220000000010000040000000000000011030346593552240176011001112N003602001?");
        System.out.println("Server Said(1): "+client.RecieveFromServer());
        client.close();

    }*/

    /*TCPClient(String _host, int _port) throws Exception{
        host = _host;
        port = _port;
        socket = new Socket(host, port);
    }*/
    TCPClient() throws Exception{
        socket = new Socket(host, port);
    }
    void SendToServer(String msg) throws Exception{
        //create output stream attached to socket
        PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        //send msg to server
        outToServer.print(msg + '\n');
        outToServer.flush();
    }
    String RecieveFromServer() throws Exception{
        //create input stream attached to socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader (socket.getInputStream()));
        //read line from server
        String res = inFromServer.readLine(); // if connection closes on server end, this throws java.net.SocketException
        return res;
    }
    void close() throws IOException{
        socket.close();
    }
}
