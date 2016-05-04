package tow.image;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import tow.Global;

public class Sprite implements Rendering {
	
    private Texture texture;
    private int scale_x = 1;
    private int scale_y = 1; 
    
    public String path;//���� � �����, ����� ��� �������� ����� � �������
    public Mask mask;
    
    public Sprite(String path) {
		this.path = path; //��� �������� ����� � ������� 
		
		//�������� �����������
        try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
			if (Global.setting.DEBUG_CONSOLE_IMAGE) Global.p("Load image \"" + path + "\" complited.");
		} catch (IOException e1) {
			Global.error("Image \"" + path + "\" not loading");
		} catch (UnsupportedOperationException e2){
			Global.error("Image \"" + path + "\" not loading");
		}
        
		//�������� �����
		this.mask = new Mask(path, getWidth(), getHeight());
    }
    
    public int getWidth() {
        return (int) texture.getImageWidth();
    }

    public int getHeight() {
        return (int) texture.getImageHeight();
    }
    
    public Mask getMask(){
		return this.mask;
	}
    
    public String getPath(){
		return path;
	}
    
    public void update(long delta){}//����������� �� Rendering, ����� ��� ��������, ���������� ������ ����
    
    public void draw(int x, int y, double direction) {
    	direction -= Math.PI/2; //������� ���������� ���� � ������� �� �����
    	direction = Math.toDegrees(direction);
    	
    	int width=(int)(getWidth()*scale_x); 
        int height=(int)(getHeight()*scale_y); 
    	
        GL11.glLoadIdentity();     
	    GL11.glTranslatef(x, y, 0);
	    GL11.glRotatef(Math.round(-direction), 0f, 0f, 1f);
	    
	    Color.white.bind(); 
	    texture.bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(0,0); 
		    GL11.glVertex2f(-width/2, -height/2); 
		    GL11.glTexCoord2f(1,0); 
		    GL11.glVertex2f(width/2, -height/2); 
		    GL11.glTexCoord2f(1,1); 
		    GL11.glVertex2f(width/2, height/2); 
		    GL11.glTexCoord2f(0,1); 
		    GL11.glVertex2f(-width/2, height/2); 
	    GL11.glEnd(); 
    }
    
    public boolean isAnim(){
    	return false;
    }
}