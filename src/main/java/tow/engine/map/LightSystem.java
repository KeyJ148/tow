package tow.engine.map;

import org.lwjgl.opengl.GL11;
import tow.engine.Global;
import tow.engine.Vector2;
import tow.engine.gameobject.components.Collision;
import tow.engine.image.Color;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

public class LightSystem {

    public static boolean[][] frameMask; //true, если занято
    public static int[][] light; //от 0 до 255 затемнение (255 - полная темнота)

    public static final int DARK = 150;

    public static void init(int w, int h) {
        frameMask = new boolean[w][h];
        light = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                light[i][j] = DARK;
            }
        }
    }

    public static void render(int x, int y, Collision collision) {
        //Vector2<Integer>[] maskDrawView = new Vector2[collision.maskAbsolute.length];
        Polygon p = new Polygon();
        for (int i = 0; i < collision.maskAbsolute.length; i++) {
            Vector2<Integer> v2 = Global.location.camera.toRelativePosition(collision.maskAbsolute[i].copy());
            p.addPoint(v2.x, v2.y);
        }
        Rectangle2D rec = p.getBounds2D();

        for (int i = (int) rec.getMinX(); i < rec.getMaxX(); i++) {
            for (int j = (int) rec.getMinY(); j < rec.getMaxY(); j++) {
                if (i > 0 && j > 0 && i < frameMask.length && j < frameMask[0].length) {
                    frameMask[i][j] = true;
                }
            }
        }
    }

    public static final int COUNT_DIR = 2500;

    public static void light(int x, int y, int range, int lightLevel) {
        Vector2<Integer> v2 = Global.location.camera.toRelativePosition(new Vector2(x, y));
        x = v2.x;
        y = v2.y;

        for (int i = 0; i < COUNT_DIR; i++) {
            double dir = Math.toRadians(360.0 / COUNT_DIR * i) - Math.PI / 2;
            double cos = Math.cos(dir);
            double sin = Math.sin(dir);

            for (int dis = 0; dis < range; dis++) {
                int checkX = x + (int) (cos * dis);
                int checkY = y - (int) (sin * dis);
                if (checkX > 0 && checkY > 0 && checkX < frameMask.length && checkY < frameMask[0].length) {
                    if (frameMask[checkX][checkY]) break;
                    int delta = DARK - lightLevel;
                    int range_dark = range / 10;
                    light[checkX][checkY] = lightLevel + ((dis > range - range_dark) ? delta / range_dark * (range_dark - (range - dis)) : 0);
                }
            }
        }
    }

    public static void light_dir(int x, int y, int drawDir, int range, int lightLevel) {
        Vector2<Integer> v2 = Global.location.camera.toRelativePosition(new Vector2(x, y));
        x = v2.x;
        y = v2.y;

        for (int i = 0; i < COUNT_DIR; i++) {
            double dir = 80.0 / COUNT_DIR * i;
            dir = Math.toRadians(drawDir - 40 + dir);
            double cos = Math.cos(dir);
            double sin = Math.sin(dir);

            for (int dis = 0; dis < range; dis++) {
                int checkX = x + (int) (cos * dis);
                int checkY = y - (int) (sin * dis);
                if (checkX > 0 && checkY > 0 && checkX < frameMask.length && checkY < frameMask[0].length) {
                    if (frameMask[checkX][checkY]) break;
                    int delta = DARK - lightLevel;
                    int range_dark = range / 10;
                    light[checkX][checkY] = lightLevel + ((dis > range - range_dark) ? delta / range_dark * (range_dark - (range - dis)) : 0);
                }
            }
        }
    }

    public static void render() {
        Set<Integer> lightLevels = new HashSet<>();
        for (int i = 0; i < light.length; i++) {
            for (int j = 0; j < light[i].length; j++) {
                lightLevels.add(light[i][j]);
            }
        }

        GL11.glLoadIdentity();
        for (Integer lightLevel : lightLevels) {
            new Color(0, 0, 0, lightLevel).bind();
            GL11.glBegin(GL11.GL_POINTS);
            for (int i = 0; i < light.length; i++) {
                for (int j = 0; j < light[i].length; j++) {
                    if (light[i][j] == lightLevel) GL11.glVertex2f(i, j);
                }
            }
            GL11.glEnd();
        }
    }
}
