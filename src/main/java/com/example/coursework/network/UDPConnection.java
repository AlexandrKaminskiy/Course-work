package com.example.coursework.network;

import com.example.coursework.gameobjects.Player;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPConnection {
    private DatagramSocket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private DatagramSocket datagramSocket;
    private int oppPort;
    private DatagramPacket receivedPacket;
    private DatagramPacket sendPacket;
    private InetAddress inetAddress;
    private final int port = 10101;
    private final String ip = "localhost";


    public UDPConnection(int port, int oppPort) throws SocketException {
        socket = new DatagramSocket(port);
        this.oppPort = oppPort;
    }

    public Player receivingObject() throws IOException {
        byte[] buf = new byte[0];
        receivedPacket = new DatagramPacket(buf,buf.length);
        socket.receive(receivedPacket);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(receivedPacket.getData());
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Player) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendObject(Player player) {
        byte[] receivingDataBuffer = new byte[1024];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(player);
            receivingDataBuffer = bos.toByteArray();
            sendPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length,oppPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
