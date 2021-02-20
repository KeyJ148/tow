package tow.game.client.tanks.player;

import tow.engine.image.Mask;
import tow.engine.image.TextureHandler;
import tow.engine.image.TextureManager;
import tow.engine.map.Border;
import tow.engine.gameobject.GameObject;
import tow.engine.gameobject.components.Collision;
import tow.engine.gameobject.components.Follower;
import tow.engine.gameobject.components.Movement;
import tow.engine.gameobject.components.Position;
import tow.engine.gameobject.components.render.Animation;
import tow.engine.gameobject.components.render.Rendering;
import tow.engine.setting.ConfigReader;
import tow.game.client.map.Box;
import tow.game.client.map.Wall;
import tow.game.client.tanks.Effect;
import tow.game.client.tanks.enemy.EnemyArmor;

/*
ПРИ ДОБАВЛЕНИЕ НОВОГО КЛАССА БРОНИ ОБНОВИТЬ BMassSmall.java
 */

public class Armor extends GameObject {

	public static final String PATH_SETTING = "game/armor/";
	public String name, title; //name - техническое название, title - игровое

	public Player player;
	public Effect effect = new Effect();

	public int animSpeed;
	public TextureHandler[] textureHandlers;

	public void init(Player player, double x, double y, double direction, String name){
		this.player = player;
		this.name = name;

		loadData();

		setComponent(new Position(x, y, textureHandlers[0].depth, direction));
		setComponent(new Animation(textureHandlers));
		getComponent(Rendering.class).scale_x = 2;
		getComponent(Rendering.class).scale_y = 2;
		setComponent(new Movement());
		getComponent(Movement.class).setDirection(direction);
		getComponent(Movement.class).update(0);

		setComponent(new Collision(new Mask("..", textureHandlers[0].mask.getWidth()*2, textureHandlers[0].mask.getHeight()*2)));
		getComponent(Collision.class).addCollisionObjects(new Class[] {Wall.class, EnemyArmor.class, Box.class, Border.class});
		getComponent(Collision.class).addListener(player.controller);

		for (int i = 0; i < player.guns.length; i++) {
			if (player.guns[i] != null) player.guns[i].setComponent(new Follower(this, false, player.gunsPosX[i], player.gunsPosY[i]));
		}
	}

	@Override
	public void update(long delta){
		super.update(delta);

		//Если мы мертвы, то ничего не делать
		if (!player.alive) return;
		
		//Для анимации гусениц
		Animation animation = (Animation) getComponent(Rendering.class);
		if (getComponent(Movement.class).speed != 0 && animation.getFrameSpeed() == 0){
			animation.setFrameSpeed(animSpeed);
		}
		if (getComponent(Movement.class).speed == 0 && animation.getFrameSpeed() != 0){
			animation.setFrameSpeed(0);
		}
	}

	@Override
	public void destroy(){
		super.destroy();
		player.effects.remove(effect);
	}

	public String getConfigFileName(){
		return PATH_SETTING + name + ".properties";
	}
	
	public void loadData(){
		ConfigReader cr = new ConfigReader(getConfigFileName());
		
		effect.addition.hpMax = cr.findDouble("HP_MAX")*5;
		effect.addition.hpRegen = cr.findDouble("HP_REGEN")*5;
		effect.addition.speedTankUp = cr.findDouble("SPEED_UP");
		effect.addition.speedTankDown = cr.findDouble("SPEED_DOWN");
		effect.addition.directionGunUp = cr.findDouble("DIRECTION_GUN_UP");
		effect.addition.directionTankUp = cr.findDouble("DIRECTION_TANK_UP");
		effect.addition.stability = cr.findInteger("STABILITY");

		animSpeed = cr.findInteger("ANIMATION_SPEED");
		textureHandlers = TextureManager.getAnimation(cr.findString("IMAGE_NAME"));
		title = cr.findString("TITLE");
	}
}