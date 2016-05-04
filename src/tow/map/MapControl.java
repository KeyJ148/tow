package tow.map;

import java.util.ArrayList;

import tow.Global;
import tow.image.DepthVector;
import tow.obj.Obj;
import tow.obj.ObjLight;

public class MapControl {

	private ArrayList<DepthVector> depthVectors = new ArrayList<DepthVector>(); //������ � DepthVector ��� ����� �����
	public int numberWidth;//���-�� ������
	public int numberHeight;
	public final int chunkSize = 100;
	private final int borderSize = 100;
	
	public void init(int width, int height){
		int addCell = (int) Math.ceil((double) borderSize/chunkSize)*2;//� ���� ������ ��� ������ ���
		numberWidth = (int) (Math.ceil((double) width/chunkSize)+addCell);//+2 ����� ������� ������ ��� ���� ������������ ����� �� �����
		numberHeight = (int) (Math.ceil((double) height/chunkSize)+addCell);//���������� �� 100-200 px �� ������ ������� (����� �� ������� �������� ����� ��������� �������)
	}
	
	public void add(ObjLight obj){
		int depth = obj.getDepth();
		
		boolean find = false;
		for (int i=0; i<depthVectors.size(); i++){
			DepthVector dv = depthVectors.get(i);
			if (dv.getDepth() == depth){
				dv.add(obj);
				find = true;
				break;
			}
		}
		
		if (!find){//������ ������������ � ������� �������� �������
			boolean create = false;
			for (int i=0; i<depthVectors.size(); i++){
				if (depthVectors.get(i).getDepth() < depth){
					depthVectors.add(i, new DepthVector(depth, obj));
					create = true;
					break;
				}
			}
			
			if (!create){
				depthVectors.add(new DepthVector(depth, obj));
			}
		}
	}
	
	public void del(int id){
		ObjLight obj = Global.getObj(id);
		int depth = obj.getDepth();
		
		DepthVector dv;
		for (int i=0; i<depthVectors.size(); i++){
			dv = (DepthVector) depthVectors.get(i);
			if (dv.getDepth() == depth){
				dv.del(obj);
			}
		}
	}
	
	public void clear(){
		depthVectors.clear();
		depthVectors.trimToSize();
	}
	
	public void update(Obj obj){
		for (int i=0; i<depthVectors.size(); i++){
			if (depthVectors.get(i).getDepth() == obj.getDepth()){
				depthVectors.get(i).update(obj);
				break;
			}
		}
	}
	
	public void render(int x, int y, int width, int height){
		for (int i=0; i<depthVectors.size(); i++)
			depthVectors.get(i).render(x, y, width, height);
			
	}
}