package vido.geekbrains;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static ServerSocket serv = null;
    static Socket sock = null;
    static PrintWriter pw = null;

    public static void main(String[] args) {

        try {
            serv = new ServerSocket(8189);
            System.out.println("Сервер запущен, ожидаем подключения...");
            sock = serv.accept();
            System.out.println("Клиент подключился");
            pw = new PrintWriter(sock.getOutputStream());
        } catch (IOException e) {
            System.out.println("Ошибка инициализации сервера");
            serverClose();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner sc = new Scanner(sock.getInputStream());
                    while (true) {
                        String str = sc.nextLine();
                        if (str.equals("end")) {
                            sendEndSession();
                            break;
                        }
                        System.out.println("Сообщение от клиента: " + str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    serverClose();
                }
            }
        }).start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner input = new Scanner(System.in);
                    while (true) {
                        if (input.hasNext()) {
                            pw.println(input.nextLine());
                            pw.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.setDaemon(true);
        t2.start();
    }

    private static void serverClose(){
        try {
            serv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendEndSession(){
        pw.println("end session");
        pw.flush();
    }
}

