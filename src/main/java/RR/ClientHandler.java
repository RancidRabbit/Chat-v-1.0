package RR;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

public class ClientHandler {
    private final Socket socket;
    private final Server server;
    private final DataOutputStream out;
    private final DataInputStream in;
    private String nick;


    public ClientHandler(Socket socket, Server server, ExecutorService threadPool) {
        try {
            this.nick = "";
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.socket = socket;
            this.server = server;


            threadPool.execute(() -> {
                try {
                    auth();
                    readMessages();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                    threadPool.shutdown();
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null) {
                server.unsubscribe(this);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void auth() throws SQLException {
        while (true) {
            try {
                final String str = in.readUTF();
                if (str.equals("/exitAuth")) {
                    System.out.println("Клиент покинул чат");
                    break;
                }
                if (str.startsWith("/auth")) {
                    final String[] split = str.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = server.getAuthService().authUser(login, password);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage("Пользователь уже занят");
                            continue;
                        }
                        sendMessage("/authOK " + nick);
                        server.broadcast("Пользователь " + nick + " зашел в чат");
                        this.nick = nick;
                        server.subscribe(this);
                        break;
                    } else {
                        sendMessage("Неверные логин и пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String s) {
        try {
            System.out.println("SERVER: Send message to " + nick);
            out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
        try {
            while (true) {
                if (nick.equals("")) {
                    break;
                }
                final String msg = in.readUTF();
                if ("/end".equals(msg)) {
                    break;
                }
                if (msg.startsWith("/w")) {
                    final String[] split = msg.split(" ", 3);
                    server.privMsg("[ " + nick + " пишет: ] " + split[2], split[1]);
                } else
                    server.broadcast(nick + ": " + msg);
                server.logSaver(msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getNick() {
        return nick;
    }


}





