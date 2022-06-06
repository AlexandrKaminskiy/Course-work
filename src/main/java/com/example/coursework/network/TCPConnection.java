package com.example.coursework.network;

import com.example.coursework.dto.PlayerDto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPConnection {
    public ServerSocket serverSocket;
    public Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int port;
    private String ip;

    public TCPConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void createServer(){
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }

    }

    public boolean connect() {
        try {
            socket = new Socket(ip,port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            byte[] sendingBuffer;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(new PlayerDto());
            sendingBuffer = baos.toByteArray();
            dos.write(sendingBuffer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public PlayerDto receivingObject(PlayerDto player) throws IOException {
        try {
            byte[] receivingBuffer = new byte[1024];
            int i = dis.read(receivingBuffer);

            if (i == -1) {
                throw new IOException();
            }
            bais = new ByteArrayInputStream(receivingBuffer);
            ois = new ObjectInputStream(bais);
            PlayerDto o = (PlayerDto) ois.readObject();
            byte[] sendingBuffer;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(player);
            sendingBuffer = baos.toByteArray();
            dos.write(sendingBuffer);
            return o;
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            close();
            return null;
        }
    }

    public void close() throws IOException {
        if (oos != null) {
            oos.close();
        }
        ois.close();
        if (baos != null) {
            baos.close();
        }
        bais.close();
        dis.close();
        dos.close();
        socket.close();
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}