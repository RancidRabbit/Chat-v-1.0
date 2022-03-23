package RR;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Client {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final File file = new File("chat_log.txt");
    private Controller controller;


    public Client(Controller controller) {
        this.controller = controller;
    }

    public void openConnection() {

        try {
            socket = new Socket("localhost", 8180);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            final String msgAuth = in.readUTF();
                            if (msgAuth.equals("/timeElapsed")) {
                                out.writeUTF("/exitAuth");
                                Client.this.closeConnection();
                            }
                            if (msgAuth.startsWith("/authOK")) {
                                final String[] split = msgAuth.split(" ");
                                final String nick = split[1];
                                controller.addMsg("Успешная авторизация под ником " + nick);
                                controller.setAuth(true);
                                loadHistory(file);
                                break;
                            }
                        }
                        while (true) {
                            String msg = in.readUTF();
                            if ("/end".equals(msg)) {
                                in.close();
                                controller.setAuth(false);

                            }
                            if (msg.startsWith("/clients")) {
                                final String[] tokens = msg.replace("/clients ", "").split(" ");
                                final List<String> clients = Arrays.asList(tokens);
                                controller.updateClientList(clients);
                                msg = "";
                            }
                            controller.addMsg(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        Client.this.closeConnection();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHistory(File file) {

        int lines = 0;
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file)) {
            String line = "";
            while ((line = reader.readLine()) != null && lines < 5) {
                lines++;
                controller.addMsg(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
