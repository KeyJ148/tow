package tow.game.client.tanks.equipment;

import tow.engine.Global;
import tow.engine.Loader;
import tow.engine.io.Logger;
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

    public static Random random = new Random();

    public static void newArmor(Player player){
        //Получение валидного имени экипировки (Которое не равно текущему и соответствует пушке)
        String newArmorName = "";
        boolean exit;
        do{
            exit = false;

            File[] dir = Global.getFile(ConfigReader.PATH_SETTING_DIR + Armor.PATH_SETTING).listFiles();
            File config = dir[random.nextInt(dir.length)];
            if (!config.getName().contains(".properties")) continue;

            ConfigReader cr = new ConfigReader(Armor.PATH_SETTING + config.getName());
            if (!contain(cr.findString("ALLOW_GUN").split(" "), ((Gun) player.gun).name)) continue;

            newArmorName = config.getName().substring(0, config.getName().lastIndexOf("."));
            if (!newArmorName.equals(((Armor) player.armor).name)) exit = true;
        } while (!exit);

        //Создание класса через рефлексию
        String newArmorClass = new ConfigReader(Armor.PATH_SETTING + newArmorName + ".properties").findString("CLASS");
        Armor newArmor = null;
        try {
            String newArmorFullPath = getClassPackage(player.armor) + "." + newArmorClass;
            newArmor = (Armor) Class.forName(newArmorFullPath).newInstance();
            newArmor.init(player, player.armor.position.x, player.armor.position.y, player.armor.position.getDirectionDraw(), newArmorName);
            player.replaceArmor(newArmor);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            Logger.println("Armor not found: " + newArmorClass, Logger.Type.ERROR);
            Loader.exit();
        }
    }

    public static void newGun(Player player){
        String nowGunName = ((Gun) player.gun).name;

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
            String newGunFullPath = getClassPackage(player.gun) + "." + newGunClassName;
            newGun = (Gun) Class.forName(newGunFullPath).newInstance();
            newGun.init(player, player.gun.position.x, player.gun.position.y, player.gun.position.getDirectionDraw(), newGunName);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            Logger.println("Gun not found: " + newGunName, Logger.Type.ERROR);
            Loader.exit();
        }

        //Установление новой пушке параметров как у старой
        player.replaceGun(newGun);
    }

    public static void newBullet(Player player){
        //Получение валидного имени экипировки (Которое не равно текущему и соответствует пушке)
        String newBulletName = "";
        boolean exit;
        do{
            exit = false;

            File[] dir = Global.getFile(ConfigReader.PATH_SETTING_DIR + Bullet.PATH_SETTING).listFiles();
            File config = dir[random.nextInt(dir.length)];
            if (!config.getName().contains(".properties")) continue;

            ConfigReader cr = new ConfigReader(Bullet.PATH_SETTING + config.getName());
            if (!contain(cr.findString("ALLOW_GUN").split(" "), ((Gun) player.gun).name)) continue;

            newBulletName = config.getName().substring(0, config.getName().lastIndexOf("."));
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