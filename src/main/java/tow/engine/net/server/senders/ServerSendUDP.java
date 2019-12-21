package tow.engine.net.server.senders;

import tow.engine2.io.Logger;
import tow.engine.net.server.GameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ServerSendUDP {

    public static void send(int id, int type, String str){
        if (!GameServer.connects[id].disconnect) {
            try {
                byte[] data = (type + " " + str).getBytes();
                InetAddress addr = InetAddress.getByName(GameServer.connects[id].ipRemote);
                int port = GameServer.connects[id].portUDP;

                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                GameServer.socketUDP.send(packet);

                GameServer.connects[id].numberSend++; //Кол-во отправленных пакетов
            } catch (IOException e) {
                Logger.print("Send message failed (UDP)", Logger.Type.SERVER_ERROR);
            }
        }
    }

    public static void sendAllExceptId(int id, int type, String str){
        for(int i = 0; i< GameServer.peopleMax; i++){//Отправляем сообщение всем
            if (i != id){//Кроме игрока, приславшего сообщение
                send(i, type, str);
            }
        }
    }

    public static void sendAll(int type, String str){
        for(int i = 0; i< GameServer.peopleMax; i++){//Отправляем сообщение всем
            send(i, type, str);
        }
    }

}
