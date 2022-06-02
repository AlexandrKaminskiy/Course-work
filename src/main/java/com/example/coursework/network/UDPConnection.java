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




}
