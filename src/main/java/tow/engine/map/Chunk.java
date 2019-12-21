package tow.engine.map;

import tow.engine2.Global;
import tow.engine.obj.Obj;

import java.util.ArrayList;

public class Chunk {

	private ArrayList<Integer> number = new ArrayList<Integer>();//Id объектов которые надо отрисовать
	private int posWidth;//Порядковый номер чанка
	private int posHeight;

	public Chunk(int posWidth, int posHeight){
		this.posWidth = posWidth;
		this.posHeight = posHeight;
	}

	public void add(int id){
		number.add(id);
	}

	public int get(int index){
		return number.get(index);
	}

	public void del(int id){
		for (int j=0; j<number.size(); j++){
			if (number.get(j) == id){
				number.remove(j);
				break;
			}
		}
	}

	public int size(){
		return number.size();
	}

	public void render(){
		for (int id : number){
			if (Global.room.objects.size() > id && Global.room.objects.get(id) != null){
				Obj obj = Global.room.objects.get(id);
				obj.draw();
			}
		}
	}

	public int getPosHeight(){
		return posHeight;
	}

	public int getPosWidth(){
		return posWidth;
	}
}
