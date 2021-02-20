package tow.engine.net.server.readers;

import tow.engine.Global;
import tow.engine.logger.Logger;
import tow.engine.net.server.GameServer;
import tow.engine.net.server.MessagePack;

import java.io.IOException;

public class ServerReadTCP extends Thread{

	private int id; //номер соединения в массиве в gameServer

	public ServerReadTCP(int id){
		this.id = id;
	}

	@Override
	public void run(){
		//Постоянный обмен данными (на TCP)
		//Только после подключения всех игроков
		String str;
		try{
			while (true){
				str = GameServer.connects[id].in.readUTF();
				GameServer.connects[id].messagePack.add(str, MessagePack.Message.InetType.TCP);
			}
		} catch (IOException e){
			Global.logger.println("Player disconnect (id: " + id + ")", Logger.Type.SERVER_INFO);
			GameServer.disconnect++;
			GameServer.connects[id].disconnect = true;
		}
	}
	
}