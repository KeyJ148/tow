package main.setting;

public class SettingStorage {

	public int TPS; //���-�� ���������� update � �������
	public int SKIP_TICKS;//������� � ������������ ����� ������������
	public boolean DEBUG_CONSOLE;//�������� � ������� ��������� �������?
	public boolean DEBUG_CONSOLE_IMAGE;//�������� � ������� ��������� �������� � ��������?
	public boolean DEBUG_CONSOLE_MASK;//�������� � ������� ��������� �������� �����?
	public boolean DEBUG_CONSOLE_FPS;//�������� � ������� ���?
	public boolean DEBUG_MONITOR_FPS;//�������� � ���� ���?
	
	public int WIDTH;
	public int HEIGHT;//������ ����
	public String WINDOW_NAME;
	
	public String fileName = "main.properties";
	
	public void initFromFile(){
		ConfigReader cr = new ConfigReader(fileName);
		
		TPS = cr.findInteger("TPS");
		WIDTH = cr.findInteger("WIDTH");
		HEIGHT = cr.findInteger("HEIGHT");
		
		WINDOW_NAME = cr.findString("WINDOW_NAME");
		
		DEBUG_CONSOLE = cr.findBoolean("DEBUG_CONSOLE");
		DEBUG_CONSOLE_IMAGE = cr.findBoolean("DEBUG_CONSOLE_IMAGE");
		DEBUG_CONSOLE_MASK = cr.findBoolean("DEBUG_CONSOLE_MASK");
		DEBUG_CONSOLE_FPS = cr.findBoolean("DEBUG_CONSOLE_FPS");
		DEBUG_MONITOR_FPS = cr.findBoolean("DEBUG_MONITOR_FPS");
		
		cr.close();
		
		SKIP_TICKS = 1000/TPS;
	}
}
