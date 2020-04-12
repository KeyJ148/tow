package tow.game.client;

import tow.engine.implementation.StorageInterface;

import java.awt.*;

public class Storage implements StorageInterface {

    //Фон - Дорога - Танк - Дом - Оружие - Дерево
    //0 - 500 - 1000 - 1500 - 2000 - 2500

    public String[][] getRoadSize(){
        return new String[][]{
                {"backwall/road_g.png","64"},
                {"backwall/road_g_fork.png","64"},
                {"backwall/road_g_turn.png","64"},
                {"backwall/road_g_inter.png","64"},
                {"backwall/road_g_end.png","64"},
                {"backwall/road_a.png","64"},
                {"backwall/road_a_big.png","128"},
                {"backwall/road_a_g.png","64"},
                {"backwall/road_a_fork.png","64"},
                {"backwall/road_a_fork_big.png","128"},
                {"backwall/road_a_inter.png","128"},
                {"backwall/road_a_d_big.png","128"},
                {"backwall/road_d.png","64"},
                {"backwall/road_d_fork.png","64"},
                {"backwall/road_d_turn.png","64"},
                {"backwall/road_d_inter.png","64"},
                {"backwall/road_d_end.png","64"},
                {"backwall/road_s.png","64"},
                {"backwall/road_s_fork.png","64"},
                {"backwall/road_s_turn.png","64"},
                {"backwall/road_s_inter.png","64"},
                {"backwall/road_s_end.png","64"}
        };
    }

    @Override
    public String[][] getImages() {
        return new String[][] {
            {"background/grass.png", "background", "0"},
            {"background/sand.png", "background", "0"},
            {"background/snow.png", "background", "0"},

            {"backwall/road_g.png", "road", "500"},
            {"backwall/road_g_fork.png", "road", "500"},
            {"backwall/road_g_turn.png", "road", "500"},
            {"backwall/road_g_inter.png", "road", "500"},
            {"backwall/road_g_end.png", "road", "500"},
            {"backwall/road_a.png", "road", "500"},
            {"backwall/road_a_big.png", "road", "500"},
            {"backwall/road_a_g.png", "road", "500"},
            {"backwall/road_a_fork.png", "road", "500"},
            {"backwall/road_a_fork_big.png", "road", "500"},
            {"backwall/road_a_inter.png", "road", "500"},
            {"backwall/road_a_d_big.png", "road", "500"},
            {"backwall/road_a_g.png", "road", "500"},
            {"backwall/road_d.png", "road", "500"},
            {"backwall/road_d_fork.png", "road", "500"},
            {"backwall/road_d_turn.png", "road", "500"},
            {"backwall/road_d_inter.png", "road", "500"},
            {"backwall/road_d_end.png", "road", "500"},
            {"backwall/road_s.png", "road", "500"},
            {"backwall/road_s_fork.png", "road", "500"},
            {"backwall/road_s_turn.png", "road", "500"},
            {"backwall/road_s_inter.png", "road", "500"},
            {"backwall/road_s_end.png", "road", "500"},

            {"bullet/b_default.png", "bullet", "40"},
            {"bullet/b_steel.png", "bullet", "40"},
            {"bullet/b_squaremass.png", "bullet", "40"},
            {"bullet/b_fury.png", "bullet", "40"},
            {"bullet/b_mass.png", "bullet", "40"},
            {"bullet/b_mass_small.png", "bullet", "40"},
            {"bullet/b_vampire.png", "bullet", "40"},
            {"bullet/b_streamlined.png", "bullet", "40"},

            {"gun/g_default.png", "gun", "400"},
            {"gun/g_double.png", "gun", "400"},
            {"gun/g_fury.png", "gun", "400"},
            {"gun/g_mortar.png", "gun", "400"},
            {"gun/g_quick.png", "gun", "400"},
            {"gun/g_rocketd.png", "gun", "400"},
            {"gun/g_mass.png", "gun", "400"},
            {"gun/g_power.png", "gun", "400"},
            {"gun/g_sniper.png", "gun", "400"},
            {"gun/g_vampire.png", "gun", "400"},
            {"gun/g_osmos.png", "gun", "40"},

            {"wall/home/home1.png", "home", "1500"},
            {"wall/home/home2.png", "home", "1500"},
            {"wall/home/home3.png", "home", "1500"},
            {"wall/home/home4.png", "home", "1500"},
            {"wall/home/home5.png", "home", "1500"},
            {"wall/home/home6.png", "home", "1500"},
            {"wall/home/home7.png", "home", "1500"},
            {"wall/home/home8.png", "home", "1500"},
            {"wall/home/home9.png", "home", "1500"},
            {"wall/home/home10.png", "home", "1500"},
            {"wall/home/home11.png", "home", "1500"},
            {"wall/home/home12.png", "home", "1500"},
            {"wall/home/home13.png", "home", "1500"},
            {"wall/home/home14.png", "home", "1500"},
            {"wall/home/factory1.png", "home", "1500"},
            {"wall/home/factory2.png", "home", "1500"},

            {"wall/tree/tree_1.png", "tree", "2500"},
            {"wall/tree/tree_snowpart_1.png", "tree", "2500"},
            {"wall/tree/tree_snowfull_1.png", "tree", "2500"},
            {"wall/tree/conifer_1.png", "tree", "2500"},
            {"wall/tree/conifer_snowfull_1.png", "tree", "2500"},
            {"wall/tree/tree_wither_1.png", "tree", "2500"},
            {"wall/tree/tree_wither_snow_1.png", "tree", "2500"},

            {"gaming/box_armor.png", "box", "60"},
            {"gaming/box_gun.png", "box", "60"},
            {"gaming/box_bullet.png", "box", "60"},
            {"gaming/box_health.png", "box", "60"},
            {"gaming/box_healthfull.png", "box", "60"},

            {"particles/part_ball.png", "particles", "0"},
            {"particles/explosion/part_metall.png", "particles", "0"},

            {"inf/cursor.png", "interface", "-1000"},
            {"inf/cursor_aim_1.png", "interface", "-1000"},
            {"inf/cursor_aim_2.png", "interface", "-1000"},
            {"inf/icon_save.png", "interface", "-1000"},
            {"inf/inf_deaths.png", "interface", "-1000"},
            {"inf/inf_kills.png", "interface", "-1000"},
            {"inf/inf_ping.png", "interface", "-1000"},
            {"inf/inf_wins.png", "interface", "-1000"},

            {"sys/sys_null.png", "system", "-1000"},
            {"sys/sys_tank.png", "system", "-1000"},
            {"sys/error.png", "system", "-1000"},
        };
    }


    @Override
    public String[][] getAnimations(){
        return new String[][] {
            {"corps/a_default",                 "armor",        "150"},
            {"corps/a_fortified",               "armor",        "150"},
            {"corps/a_elephant",                "armor",        "150"},
            {"corps/a_fury",                    "armor",        "150"},
            {"corps/a_mite",                    "armor",        "150"},
            {"corps/a_vampire",                 "armor",        "150"},
            {"corps/a_light",                   "armor",        "150"},
            {"corps/a_renegat",                 "armor",        "150"}
        };
    }

    @Override
    public int[][] getFonts(){
        return new int[][] {
            {12, Font.PLAIN},
            {14, Font.PLAIN},
            {15, Font.PLAIN},
            {16, Font.PLAIN},
            {12, Font.BOLD},
            {20, Font.BOLD}
        };
    }

    @Override
    public String[] getAudios(){
        return new String[] {};
    }
}
