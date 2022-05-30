package com.example.coursework.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Thread receiver;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {

        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 10101));
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("создан поток вывода");
        receiver = new Thread(() -> {
            Object obj;
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
                System.out.println("поток ввода клиента создан");
                while (true) {
                    if (objectInputStream.available() > 0) {
                        obj = objectInputStream.readObject();
                        System.out.println("объект получен");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("ошибка получения от клиента сервером");
            }
        });
        receiver.start();
        System.out.println("поток запущен");

        send();

    }

    private synchronized void send() {
        try {
            outputStream.writeObject("запрос");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
