package com.example.coursework.network;

import com.example.coursework.gameobjects.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnection {
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final int port = 10101;
    private final String ip = "localhost";

    public TCPConnection(){
        if (!connect()) {
            createServer();
        }
    }
    public Object receivingObject() {
        try {
            Object o = inputStream.readObject();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendObject(Player player) {
        try {
            outputStream.writeObject(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
//            outputStream.writeObject("21");
            System.out.println("server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect() {
        try {
            socket = new Socket(ip,port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("client");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
