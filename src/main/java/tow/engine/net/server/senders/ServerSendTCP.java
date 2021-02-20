package tow.engine.net.server.senders;

import tow.engine.Global;
import tow.engine.logger.Logger;
import tow.engine.net.server.GameServer;

import java.io.IOException;

public class ServerSendTCP {

    public static void send(int id, int type, String str) {
        if (!GameServer.connects[id].disconnect) {
            try {
                GameServer.connects[id].out.writeUTF(type + " " + str);
                GameServer.connects[id].numberSend++; //Кол-во отправленных пакетов
            } catch (IOException e) {
                Global.logger.print("Send message failed (TCP)", Logger.Type.SERVER_ERROR);
            }
        }
    }

    public static void sendAllExceptId(int id, int type, String str){
        for(int i=0; i<GameServer.peopleMax; i++){//Отправляем сообщение всем
            if (i != id){//Кроме игрока, приславшего сообщение
                send(i, type, str);
            }
        }
    }

    public static void sendAll(int type, String str){
        for(int i=0; i<GameServer.peopleMax; i++){//Отправляем сообщение всем
            send(i, type, str);
        }
    }

}
