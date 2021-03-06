package tow.engine;

import tow.engine.audio.AudioPlayer;
import tow.engine.cycle.Engine;
import tow.engine.logger.AggregateLogger;
import tow.engine.map.Location;
import tow.engine.resources.audios.AudioStorage;
import tow.engine.implementation.*;
import tow.engine.input.mouse.MouseHandler;
import tow.engine.input.keyboard.KeyboardHandler;
import tow.engine.net.client.Ping;
import tow.engine.net.client.tcp.TCPControl;
import tow.engine.net.client.tcp.TCPRead;
import tow.engine.net.client.udp.UDPControl;
import tow.engine.net.client.udp.UDPRead;

public class Global {

	public static Engine engine; //Главный игровой поток
	public static Location location; //Текущая комната

	public static AggregateLogger logger; //Объект для вывода лога в консоль и файл

	public static AudioPlayer audioPlayer; //Объект, воспроизводящий музыку и хранящий источники музыки
	public static AudioStorage audioStorage; //Объект хранящий звуки (буфферы OpenAL)

	//TODO: убрать в главный класс Network при рефакторинге сети
	public static TCPControl tcpControl; //Хранит настройки и работает с сетью по TCP протоколу
	public static TCPRead tcpRead; //Цикл считывания данных с сервера по TCP протоколу
	public static UDPControl udpControl; //Хранит настройки и работает с сетью по UDP протоколу
	public static UDPRead udpRead; //Цикл считывания данных с сервера по UDP протоколу
	public static Ping pingCheck;//Объект для проверки пинга

	/* Объекты реализуемые вне движка и передаваемые при старте */
	public static GameInterface game; //Главный объект игры
	public static ServerInterface server; //Главный объект сервера
	public static NetGameReadInterface netGameRead; //Объект для обработки сетевых сообщений на клиенте
	public static NetServerReadInterface netServerRead; //Объект для обработки сетевых сообщений на сервере
	public static StorageInterface storage; //Объект для хранения описания картинок, анимаций и звуков
}

