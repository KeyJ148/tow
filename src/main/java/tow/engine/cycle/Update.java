package tow.engine.cycle;

import tow.engine.Global;
import tow.engine2.Loader;
import tow.engine3.image.Camera;
import tow.engine2.io.Logger;
import tow.engine3.io.KeyboardHandler;
import tow.engine3.io.MouseHandler;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Update {

	private long startUpdateTime, lastUpdateTime = 0;//Для вычисления delta

	public void loop(){
		//При первом вызове устанавливаем текущее время
		if (lastUpdateTime == 0) lastUpdateTime = System.nanoTime();

		startUpdateTime = System.nanoTime();
		loop(System.nanoTime() - lastUpdateTime);
		lastUpdateTime = startUpdateTime;//Начало предыдущего update, чтобы длительность update тоже учитывалась
	}

	//Обновляем игру в соответствие с временем прошедшим с последнего обновления
	private void loop(long delta){
		glfwPollEvents();//Получение событий ввода и других callbacks

		Global.game.update(delta);//Обновить главный игровой класс при необходимости

		Global.tcpRead.update();//Обработать все полученные сообщения по TCP
		Global.udpRead.update();//Обработать все полученные сообщения по UDP

		if (Global.room != null) {
			Global.room.update(delta);//Обновить все объекты в комнате
		} else {
			Logger.println("No create room! (Global.room)", Logger.Type.ERROR);
			Loader.exit();
		}

		Camera.calc();//Расчёт положения камеры

		MouseHandler.update(); //Очистка истории событий мыши
		KeyboardHandler.update(); //Очистка истории событий клавиатуры
	}

}
