package tow.game.client.menu.locations;

import org.liquidengine.legui.component.Panel;
import tow.game.client.menu.MenuLocation;

import static tow.game.client.menu.InterfaceStyles.*;

public class PlayerSettingsMenuLocation extends MenuLocation {

    private final static int MAIN_PANEL_WIDTH = 4*MENU_ELEMENT_WIDTH/3;
    private final static int MAIN_PANEL_HEIGHT = 5*MENU_ELEMENT_HEIGHT/3;

    public PlayerSettingsMenuLocation(){
        Panel mainPanel = createPanel(width/2, height/2, MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT);
    }
}