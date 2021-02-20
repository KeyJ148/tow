package tow.game.client.tanks.enemy;

import tow.engine.Global;
import tow.engine.image.TextureHandler;
import tow.engine.image.TextureManager;
import tow.engine.gameobject.GameObjectFactory;
import tow.engine.gameobject.components.Follower;
import tow.engine.gameobject.components.Movement;
import tow.engine.gameobject.components.Position;
import tow.engine.gameobject.components.render.Animation;
import tow.engine.gameobject.components.render.Rendering;
import tow.engine.gameobject.components.render.Sprite;
import tow.game.client.ClientData;
import tow.game.client.tanks.Tank;

public class Enemy extends Tank {

    public int id = -1;

    public boolean valid = false; //Этот враг уже инициализирован? (Отправл свои данные: цвет, ник)
    public static final long REQUEST_DATA_EVERY_TIME = (long) (0.5 * Math.pow(10, 9));//Промежуток времени между запросами информации о враге
    public long timeLastRequestDelta = 0;
    private long lastNumberPackage = -1; //Номер последнего пакета UDP

    public Enemy(int id){
        this.id = id;

        setComponent(new Position(0, 0, 0));
        setComponent(new Movement());
    }

    public Enemy(Enemy enemy){
        this(enemy.id);
        this.valid = enemy.valid;
        this.timeLastRequestDelta = enemy.timeLastRequestDelta;
        this.lastNumberPackage = enemy.lastNumberPackage;
        this.color = enemy.color;
        this.name = enemy.name;
        this.kill = enemy.kill;
        this.death = enemy.death;
        this.win = enemy.win;
    }

    @Override
    public void update(long delta){
        super.update(delta);

        if (!valid) {
            timeLastRequestDelta -= delta;
            if (timeLastRequestDelta <= 0) {
                timeLastRequestDelta = REQUEST_DATA_EVERY_TIME;
                Global.tcpControl.send(16, String.valueOf(id));
            }
        }
    }

    public void setData(int x, int y, int direction, int directionGun, int speed, double moveDirection, int animSpeed, long numberPackage){
        if (!ClientData.battle) return;
        if (!alive) return;
        if (numberPackage < lastNumberPackage) return;
        lastNumberPackage = numberPackage;

        //Инициализация брони
        if (armor == null){
            TextureHandler[] armorAnimation = TextureManager.getAnimation("a_default");
            armor = new EnemyArmor(x, y, direction, armorAnimation, this);
            Global.location.objAdd(armor);
            setColorArmor(color);

            setComponent(new Follower(armor));
            camera.setComponent(new Follower(armor));
        }

        //Инициализация пушки
        for (int i = 0; i < guns.length; i++) {
            if (guns[i] == null){
                TextureHandler gunTexture = TextureManager.getTexture("g_default");
                guns[i] = GameObjectFactory.create(x, y, directionGun, gunTexture);
                guns[i].setComponent(new Movement());
                guns[i].setComponent(new Follower(armor, false, gunsPosX[i], gunsPosY[i]));
                guns[i].getComponent(Movement.class).directionDrawEquals = false;
                Global.location.objAdd(guns[i]);
                setColorGun(color);
            }
        }

        //Инициализация камеры
        if (camera == null){
            initCamera();
            camera.setComponent(new Follower(armor));
        }

        armor.getComponent(Position.class).x = x;
        armor.getComponent(Position.class).y = y;
        armor.getComponent(Position.class).setDirectionDraw(direction);

        for (int i = 0; i < guns.length; i++) {
            guns[i].getComponent(Position.class).setDirectionDraw(directionGun);
        }

        //Для интерполяции (предсказания) движения врага
        armor.getComponent(Movement.class).speed = speed;
        armor.getComponent(Movement.class).setDirection(moveDirection);

        //Анимация гусениц
        if (alive) {
            Animation animation = (Animation) armor.getComponent(Rendering.class);
            if (animation.getFrameSpeed() != animSpeed) animation.setFrameSpeed(animSpeed);
        }
    }

    public void newArmor(String nameArmor){
        armor.setComponent(new Animation(TextureManager.getAnimation(nameArmor)));
        armor.getComponent(Rendering.class).scale_x = 2;
        armor.getComponent(Rendering.class).scale_y = 2;
        setColorArmor(color);

        Global.location.mapControl.update(armor);
    }

    public void newGun(String nameGun){
        if (guns[0] != null) {
            for (int i = 0; i < guns.length; i++) {
                guns[i].setComponent(new Sprite(TextureManager.getTexture(nameGun)));
                setColorGun(color);

                Global.location.mapControl.update(guns[i]);
            }
        }
    }
}
