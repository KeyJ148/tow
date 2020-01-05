package tow.game.client.tanks.player;


import tow.engine.Global;
import tow.engine3.image.TextureHandler;
import tow.engine3.image.TextureManager;
import tow.engine2.map.Border;
import tow.engine2.obj.Obj;
import tow.engine2.obj.components.Collision;
import tow.engine2.obj.components.CollisionDirect;
import tow.engine2.obj.components.Movement;
import tow.engine2.obj.components.Position;
import tow.engine2.obj.components.render.Sprite;
import tow.engine3.setting.ConfigReader;
import tow.game.client.ClientData;
import tow.game.client.GameSetting;
import tow.game.client.map.Wall;
import tow.game.client.particles.Explosion;
import tow.game.client.tanks.enemy.EnemyArmor;

public class Bullet extends Obj implements Collision.CollisionListener{

	public static final String PATH_SETTING = "game/bullet/";
	public String name, title; //name - техническое название, title - игровое

	public double damage; //Дамаг (пушка и в loadData добавляем дамаг пули)
	public int range; //Дальность (пушка и в loadData добавляем дальность пули)
	public int explosionSize;
	
	public double startX;
	public double startY;
	public long idNet;

	public String sound_shot;
	public String sound_hit;

	public Player player;
	public TextureHandler texture;

	public void init(Player player, double x, double y, double dir, double damage, int range, String name){
		this.player = player;
		this.name = name;
		this.damage = damage; //Дамаг исключительно от выстрелевшей пушки
		this.range = range; //Дальность исключительно от выстрелевшей пушки
		this.idNet = ClientData.idNet;
		this.startX = x;
		this.startY = y;

		this.movement = new Movement(this);
		this.movement.setDirection(dir);
		loadData();

		this.position = new Position(this, x, y, texture.depth, dir);
		this.rendering = new Sprite(this, texture);

		this.collision = new CollisionDirect(this, texture.mask, range);
		this.collision.addCollisionObjects(new Class[] {Wall.class, EnemyArmor.class, Border.class});
		this.collision.addListener(this);
		((CollisionDirect) collision).init();

		Global.tcpControl.send(13, getData());
		ClientData.idNet++;

		playSoundShot();
	}

	@Override
	public void collision(Obj obj) {
		if (destroy) return;

		if (obj.getClass().equals(Border.class)){
			destroy(0);
		}

		if (obj.getClass().equals(Wall.class)){
			destroy(explosionSize);

			Global.audioPlayer.playSoundEffect(Global.audioStorage.getAudio(sound_hit), (int) position.x, (int) position.y, GameSetting.SOUND_RANGE);
			Global.tcpControl.send(25, (int) position.x + " " + (int) position.y + " " + sound_hit);
		}

		if (obj.getClass().equals(EnemyArmor.class)){
			EnemyArmor ea = (EnemyArmor) obj;

			Global.tcpControl.send(14, damage + " " + ea.enemy.id);
			destroy(explosionSize);

			Global.audioPlayer.playSoundEffect(Global.audioStorage.getAudio(sound_hit), (int) position.x, (int) position.y, GameSetting.SOUND_RANGE);
			Global.tcpControl.send(25, (int) position.x + " " + (int) position.y + " " + sound_hit);

			//Для вампирского сета
			if (ea.enemy.alive) player.hitting(damage);
		}
	}

	public void destroy(int expSize){
		destroy();
		Global.tcpControl.send(15, idNet + " " + expSize);

		if (explosionSize > 0) {
			Obj explosion = new Obj(position.x, position.y, -100);
			explosion.particles = new Explosion(explosion, expSize);
			explosion.particles.destroyObject = true;
			Global.room.objAdd(explosion);
		}
	}

	@Override
	public void update(long delta) {
		if (!destroy) {
			if (Math.sqrt(Math.pow(startX - position.x, 2) + Math.pow(startY - position.y, 2)) >= range) {
				destroy(0);
			}
		}

		super.update(delta);
	}

	public void playSoundShot(){
		Global.audioPlayer.playSoundEffect(Global.audioStorage.getAudio(sound_shot), (int) position.x, (int) position.y, GameSetting.SOUND_RANGE);
		Global.tcpControl.send(25, (int) position.x + " " + (int) position.y + " " + sound_shot);
	}

	public String getData(){
		return  Math.round(position.x)
		+ " " +	Math.round(position.y)
		+ " " +	movement.getDirection()
		+ " " +	movement.speed
		+ " " +	texture.name
		+ " " +	idNet;
	}

	public String getConfigFileName(){
		return PATH_SETTING + name + ".properties";
	}
	
	public void loadData(){
		ConfigReader cr = new ConfigReader(getConfigFileName());
		
		movement.speed = cr.findDouble("SPEED") + player.stats.speedTankUp/2;
		movement.speed = Math.max(movement.speed, player.stats.speedTankUp*GameSetting.MIN_BULLET_SPEED_KOEF);

		damage += cr.findDouble("DAMAGE");//К дамагу пушки прибавляем дамаг патрона
		range += cr.findInteger("RANGE");//К дальности пушки прибавляем дальность патрона
		texture = TextureManager.getTexture(cr.findString("IMAGE_NAME"));
		title = cr.findString("TITLE");
		sound_shot = cr.findString("SOUND_SHOT");
		sound_hit = cr.findString("SOUND_HIT");
		explosionSize = cr.findInteger("EXPLOSION_SIZE");
	}

}