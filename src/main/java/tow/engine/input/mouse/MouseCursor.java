package tow.engine.input.mouse;

import org.lwjgl.BufferUtils;
import tow.engine.Global;
import tow.engine.Vector2;
import tow.engine.image.TextureHandler;
import tow.engine.obj.Obj;
import tow.engine.obj.components.Position;
import tow.engine.obj.components.render.Sprite;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class MouseCursor {

    private Obj cursor;
    private boolean captureCursor = false;

    public MouseCursor(){
        //Создание объекта курсора (используется компонент Position и Sprite)
        cursor = new Obj();
        cursor.position = new Position(cursor, 0, 0, -1000);
        cursor.position.absolute = false;
    }

    public void update(){
        Vector2<Integer> mousePos = getPositionFromGLFW();
        cursor.position.x = mousePos.x;
        cursor.position.y = mousePos.y;
    }

    public void draw(){
        cursor.draw();
    }

    public Vector2<Integer> getPosition(){
        return new Vector2<>((int) cursor.position.x, (int) cursor.position.y);
    }

    public void setTexture(TextureHandler texture){
        //Отключение стнадартного курсора
        glfwSetInputMode(Global.engine.render.getWindowID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        //Присвоение текстуры объекту курсора
        cursor.rendering = new Sprite(cursor, texture);
    }

    public void setDefaultTexture(){
        //Включение стнадартного курсора
        glfwSetInputMode(Global.engine.render.getWindowID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        //Отключение текстуры у объекта курсора
        cursor.rendering = null;
    }

    public void setCapture(boolean captureCursor){
        this.captureCursor = captureCursor;
    }

    //Обновление позиции объекта cursor напрямую из позиции мыши в OpenGL
    private Vector2<Integer> getPositionFromGLFW(){
        DoubleBuffer bufX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer bufY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(Global.engine.render.getWindowID(), bufX, bufY);
        Vector2<Integer> mousePos = new Vector2<>((int) bufX.get(), (int) bufY.get());

        if (captureCursor) captureInWindow(mousePos);
        glfwSetCursorPos(Global.engine.render.getWindowID(), mousePos.x, mousePos.y);
        return mousePos;
    }

    //Если позиция мыши выходит за пределы окна, то функция нормализует значения в mousePos
    private void captureInWindow(Vector2<Integer> mousePos){
        int width = Global.engine.render.getWidth();
        int height = Global.engine.render.getHeight();

        if (mousePos.x < 0) mousePos.x = 0;
        if (mousePos.x > width) mousePos.x = width;
        if (mousePos.y < 0) mousePos.y = 0;
        if (mousePos.y > height) mousePos.y = height;
    }
}