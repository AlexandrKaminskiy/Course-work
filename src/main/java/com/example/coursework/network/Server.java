package com.example.coursework.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private final List<TCPConnection> clients = Collections.synchronizedList(new ArrayList<>());
    private Thread clientCreator;
    ServerSocket serverSocket;

    public static void main(String[] args) {
        new Server();
    }

    Server() {
        clientCreator = new Thread(this::clientListener);
        clientCreator.start();
    }

    private synchronized void send(Object obj) throws IOException {
        for (var socket : clients) {
//            socket.getOutputStream().writeObject("123123132");
        }
    }

    public void createThread(Socket socket) {
        if (!socket.isClosed()) {
            Object obj;
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                 ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {
                System.out.println("поток ввода клиента создан");
                while (true) {
                    if (bufferedInputStream.available() > 0) {

                        obj = objectInputStream.readObject();


                        send(obj);
                        System.out.println("объект отправлен");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("ошибка получения от клиента сервером");
            }
        }
    }

    public void clientListener() {
        try {
            serverSocket = new ServerSocket(10101);
            while (true) {
                var socket = serverSocket.accept();
//                TCPConnection tcpConnection = new TCPConnection(socket,new Thread(()->createThread(socket)));
//
//                tcpConnection.getThread().start();
//                clients.add(tcpConnection);
            }
        } catch (IOException e){
            System.out.println("Creating server socket error");
        }
    }
}
