package engine;

import engine.cycle.Engine;
import engine.inf.InfMain;
import engine.map.Room;
import engine.net.client.Ping;
import engine.net.client.tcp.TCPControl;
import engine.net.client.tcp.TCPRead;
import engine.net.client.udp.UDPControl;
import engine.net.client.udp.UDPRead;
import game.client.Game;

public class Global {
	
	public static Engine engine; //Главный игровой поток
	public static Game game; //Главный объект игры вне движка
	public static Room room; //Текущая комната

	public static InfMain infMain; //Главный класс интерфейса

	public static TCPControl tcpControl; //Хранит настройки и работает с сетью по TCP протоколу
	public static TCPRead tcpRead; //Цикл считывания данных с сервера по TCP протоколу
	public static UDPControl udpControl; //Хранит настройки и работает с сетью по UDP протоколу
	public static UDPRead udpRead; //Цикл считывания данных с сервера по UDP протоколу
	public static Ping pingCheck;//Объект для проверки пинга

}

