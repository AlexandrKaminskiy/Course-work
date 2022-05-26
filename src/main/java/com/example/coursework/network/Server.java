package com.example.coursework.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private final List<Socket> clients = Collections.synchronizedList(new ArrayList<>());
    private Thread clientCreator;
    private Thread receiver;
    ServerSocket serverSocket;

    public static void main(String[] args) {
        new Server();
    }

    Server() {
        clientCreator = new Thread(this::clientListener);
        receiver = new Thread(this::receive);

        receiver.start();
        clientCreator.start();
    }
    private synchronized void receive() {
        Object obj;
        while (true) {
            clients.size();
            for (var client : clients) {
                System.out.println("зашел в поток прослушки");
                if (!client.isClosed()) {
                    try (ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream())) {
                        System.out.println("поток ввода клиента создан");
                        if ((obj = objectInputStream.readObject()) != null) {
                            send();
                            System.out.println("объект отправлен");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("ошибка получения от клиента сервером");
                    }
                } else {
                    clients.remove(client);
                    break;
                }
            }
        }
    }
    private synchronized void send() {
        for (var client : clients) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream())) {
                objectOutputStream.writeObject("123123132");
            } catch (IOException e){
                System.out.println("ошибка во время отправки клиенту от сервера");
            }
        }
    }

    public void clientListener() {
        try {
            serverSocket = new ServerSocket(10101);
            while (true) {
                var socket = serverSocket.accept();
                clients.add(socket);
            }
        } catch (IOException e){
            System.out.println("Creating server socket error");
        }
    }
}
