package tow.game.client.tanks.equipment;

import tow.engine.Global;
import tow.engine.logger.Logger;
import tow.engine.Loader;
import tow.engine.gameobject.components.Position;
import tow.engine.setting.ConfigReader;
import tow.game.client.tanks.player.Armor;
import tow.game.client.tanks.player.Bullet;
import tow.game.client.tanks.player.Gun;
import tow.game.client.tanks.player.Player;

import java.io.File;
import java.util.Random;

/*
При добавление необходимо менять...
Броня:
Storage, tow.player.equipment.armor/, res/settings/armor
Оружие:
Storage, tow.player.equipment.gun/, res/settings/gun, res/settings/armor, res/settings/bullet
Патроны:
Storage, tow.player.equipment.bullet/, res/settings/bullet
*/

public class EquipManager {

    private static Random random = new Random();

    private static final String[] ARMOR_PROPS = {
            "ADefault.properties",
            "ALight.properties",
            "AFortified.properties",
            "AVampire.properties",
            "AFury.properties",
            "AElephant.properties",
            "ARenegat.properties",
            "AMite.properties"
    };

    private static final String[] BULLET_PROPS = {
            "BDefault.properties",
            "BStreamlined.properties",
            "BFury.properties",
            "BMass.properties",
            "BSteel.properties",
            "BVampire.properties",
            "BMassSmall.properties",
            "BSquaremass.properties"
    };

    public static void newArmor(Player player){
        //Получение валидного имени экипировки (Которое не равно текущему и соответствует пушке)
        String newArmorName = "";
        boolean exit;
        do{
            exit = false;

            String config = ARMOR_PROPS[random.nextInt(ARMOR_PROPS.length)];
            if (!config.contains(".properties")) continue;

            ConfigReader cr = new ConfigReader(Armor.PATH_SETTING + config);
            if (!contain(cr.findString("ALLOW_GUN").split(" "), ((Gun) player.guns[0]).name)) continue;

            newArmorName = config.substring(0, config.lastIndexOf("."));
            if (!newArmorName.equals(((Armor) player.armor).name)) exit = true;
        } while (!exit);

        //Создание класса через рефлексию
        String newArmorClass = new ConfigReader(Armor.PATH_SETTING + newArmorName + ".properties").findString("CLASS");
        Armor newArmor = null;
        try {
            String newArmorFullPath = getClassPackage(player.armor) + "." + newArmorClass;
            newArmor = (Armor) Class.forName(newArmorFullPath).newInstance();
            newArmor.init(player, player.armor.getComponent(Position.class).x, player.armor.getComponent(Position.class).y, player.armor.getComponent(Position.class).getDirectionDraw(), newArmorName);
            player.replaceArmor(newArmor);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            Global.logger.println("Armor not found: " + newArmorClass, Logger.Type.ERROR);
            Loader.exit();
        }
    }

    public static void newGun(Player player){
        String nowGunName = ((Gun) player.guns[0]).name;

        //Получение валидного имени экиперовки (Которое не равно текущему и соответствует броне и патрону)
        String newGunName;
        boolean end = false;
        String[] allowGun = new ConfigReader(((Armor) player.armor).getConfigFileName()).findString("ALLOW_GUN").split(" ");
        do{
            do {
                newGunName = allowGun[random.nextInt(allowGun.length)];
            } while (newGunName.equals(nowGunName));

            ConfigReader cr = new ConfigReader(Bullet.PATH_SETTING + player.bullet.name + ".properties");
            String[] allowGunForBullet = cr.findString("ALLOW_GUN").split(" ");

            for (int i=0;i<allowGunForBullet.length;i++){
                if (newGunName.equals(allowGunForBullet[i])){
                    end = true;
                }
            }
        } while(!end);

        //Создание класса через рефлексию
        Gun newGun = null;
        try {
            String newGunClassName = new ConfigReader(Gun.PATH_SETTING + newGunName + ".properties").findString("CLASS");
            String newGunFullPath = getClassPackage(player.guns[0]) + "." + newGunClassName;
            for (int i = 0; i < player.guns.length; i++) {
                newGun = (Gun) Class.forName(newGunFullPath).newInstance();
                newGun.init(player, player.guns[i].getComponent(Position.class).x, player.guns[i].getComponent(Position.class).y,
                        player.guns[i].getComponent(Position.class).getDirectionDraw(), newGunName, player.gunsPosX[i], player.gunsPosY[i]);
                player.replaceGun(i, newGun);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            Global.logger.println("Gun not found: " + newGunName, Logger.Type.ERROR);
            Loader.exit();
        }

        //Установление новой пушке параметров как у старой
    }

    public static void newBullet(Player player){
        //Получение валидного имени экипировки (Которое не равно текущему и соответствует пушке)
        String newBulletName = "";
        boolean exit;
        do{
            exit = false;

            String config = BULLET_PROPS[random.nextInt(BULLET_PROPS.length)];
            if (!config.contains(".properties")) continue;

            ConfigReader cr = new ConfigReader(Bullet.PATH_SETTING + config);
            if (!contain(cr.findString("ALLOW_GUN").split(" "), ((Gun) player.guns[0]).name)) continue;

            newBulletName = config.substring(0, config.lastIndexOf("."));
            if (!newBulletName.equals(player.bullet.name)) exit = true;
        } while (!exit);

        //Назначение нового патрона
        player.replaceBullet(newBulletName);
    }

    private static String getClassName(Object object){
        return object.getClass().getSimpleName();
    }

    private static String getClassPackage(Object object){
        String fullName = object.getClass().getName();
        return fullName.substring(0, fullName.lastIndexOf("."));
    }

    private static boolean contain(String[] array, String value){
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) return true;
        }

        return false;
    }

}

