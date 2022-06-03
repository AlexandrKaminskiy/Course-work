package com.example.coursework.network;

import com.example.coursework.gameobjects.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class TCPConnection {
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ByteArrayOutputStream baos;
    private ByteArrayInputStream bais;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final int port = 10101;
    private final String ip = "localhost";

    public static void main(String[] args) {
        new TCPConnection(null);
    }
    public TCPConnection(Player player){
        if (!connect(player)) {
            createServer();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        },0, 10);
    }



    private void createServer() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect(Player player) {
        try {
            socket = new Socket(ip,port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            byte[] sendingBuffer;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(player);
            sendingBuffer = baos.toByteArray();
            dos.write(sendingBuffer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Player receivingObject(Player player) throws IOException {

        try {
            byte[] receivingBuffer = new byte[1024];
            dis.read(receivingBuffer);
            bais = new ByteArrayInputStream(receivingBuffer);
            ois = new ObjectInputStream(bais);

            Player o = (Player) ois.readObject();

            byte[] sendingBuffer;
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(player);
            sendingBuffer = baos.toByteArray();
            dos.write(sendingBuffer);

            return o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}