package game.client.lobby;

import engine.Global;
import engine.Loader;
import engine.io.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class LobbyClient implements Runnable{

    private LobbyWindow lobbyWindow;

    private String ip;
    private int port;
    private String name;

    private DataInputStream in;
    private DataOutputStream out;

    public LobbyClient(String ip, int port, String name){
        this.ip = ip;
        this.port = port;
        this.name = name;

        this.lobbyWindow = new LobbyWindow();
        lobbyWindow.addPlayerToLobby(name);

        new Thread(this).start();
    }

    @Override
    public void run() {
        mainClientProccess();
    }

    private void mainClientProccess() {
        try{
            //Подключение к лобби-серверу
            Socket sock = new Socket(InetAddress.getByName(ip), port+1);
            in = new DataInputStream(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());

            //Отправка своего ника на сервер
            out.writeUTF(name);

            //Получение ников других игроков
            String newPlayerName = "";

            //Пока не получили сообщение о старте сервера
            while (!newPlayerName.equals(" ")){
                newPlayerName = in.readUTF();
                lobbyWindow.addPlayerToLobby(newPlayerName);
            }

            //Закрытие лобби и создание основного окна
            sock.close();
            lobbyWindow.dispose();

            //Подключение к серверу
            Global.tcpControl.connect(ip, port);
            Global.tcpRead.start();
        } catch(IOException e){
            Logger.println(e.getMessage(), Logger.Type.ERROR);
            Loader.exit();
        }
    }
}