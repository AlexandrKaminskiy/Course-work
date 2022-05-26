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
        outputStream.flush();
        System.out.println("создан поток вывода");
        receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("в потоке");
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    System.out.println("создан поток ввода");
                    Object readObject;
                    while (true) {
                        if (socket.isClosed()) {
                            return;
                        }
                        try {
                            if ((readObject = inputStream.readObject()) != null) {
                                System.out.println("ответ пришел");
                            }
                        }catch (EOFException e){

                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ошибка чтения объекта");
                }
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
