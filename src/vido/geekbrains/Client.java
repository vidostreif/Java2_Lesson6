package vido.geekbrains;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket sock = null;
    static PrintWriter out = null;
    static Scanner in = null;

    public static void main(String[] args) {
        final String SERVER_ADDR = "localhost";
        final int SERVER_PORT = 8189;

        try {
            sock = new Socket(SERVER_ADDR, SERVER_PORT);
            System.out.println("Мы подключились к серверу");
            out = new PrintWriter(sock.getOutputStream());
            in = new Scanner(sock.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (in.hasNext()) {
                            String w = in.nextLine();
                            if (w.equalsIgnoreCase("end session")) break;
                            System.out.println("Сообщение от сервера: " + w);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        sock.close();
                        out.close();
                        in.close();
                    } catch (IOException exc) {
                    }
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
                            out.println(input.nextLine());
                            out.flush();
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
}
