package com.example.coursework.network;

import com.example.coursework.gameobjects.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TCPConnection {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private final int port = 10101;
    private final String ip = "localhost";

    public static void main(String[] args) {
        new TCPConnection();
    }
    public TCPConnection(){
        if (!connect()) {
            createServer();
        }

    }

    private void createServer() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect() {
        try {
            socket = new Socket(ip,port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public byte[] receivingObject() throws IOException {
        int av = inputStream.available();
        if (av == 0) {
            av = 1024;
        }
        byte buff[] = new byte[56];
        var a = inputStream.read(buff);
        return buff;
    }

    public void sendObject(Player o) {

        byte[] sendingDataBuffer = o.objectToByteArray();

        try {
            outputStream.write(sendingDataBuffer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}