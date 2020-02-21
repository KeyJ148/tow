package tow.engine.obj.components.render;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Component;
import tow.engine.Vector2;
import tow.engine.obj.Obj;

public class GUIElement extends Rendering {

    private Component component;

    public GUIElement(Obj obj, Component component) {
        super(obj);
        this.component = component;
        getObj().position.location.addGUIComponent(component);
    }

    public GUIElement(Obj obj, Component component, int width, int height) {
        this(obj, component);
        setWidth(width);
        setHeight(height);
    }

    public Component getComponent(){
        return component;
    }

    @Override
    public void update(long delta) {
        Vector2<Integer> relativePosition = getObj().position.getRelativePosition();
        float xView = relativePosition.x - getWidth()/2;
        float yView = relativePosition.y - getHeight()/2;

        component.setPosition(xView, yView);

        if (getObj().destroy) getObj().position.location.removeGUIComponent(component);
    }

    @Override
    public int getWidthTexture() {
        return (int) component.getSize().x;
    }

    @Override
    public int getHeightTexture() {
        return (int) component.getSize().y;
    }

    @Override
    public int getWidth() {
        return getWidthTexture();
    }

    @Override
    public int getHeight() {
        return getHeightTexture();
    }

    @Override
    public void setWidth(int width) {
        Vector2f size = component.getSize();
        size.x = width;
        component.setSize(size);
    }

    @Override
    public void setHeight(int height) {
        Vector2f size = component.getSize();
        size.y = height;
        component.setSize(size);
    }

    @Override
    public void setDefaultSize() {
        throw new UnsupportedOperationException("GUIElement not have default size");
    }
}