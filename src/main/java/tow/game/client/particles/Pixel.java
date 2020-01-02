package tow.game.client.particles;

import tow.engine2.obj.Obj;
import tow.engine2.obj.components.particles.Part;
import tow.engine2.obj.components.particles.ParticlesGeometry;
import tow.engine.image.Color;

public class Pixel extends ParticlesGeometry {

    public Pixel(Obj obj, double x, double y, double dir, double speed){
        this(obj, x, y, dir, speed, Color.BLACK);
    }

    public Pixel(Obj obj, double x, double y, double dir, double speed, Color color) {
        super(obj);

        Part part = new Part();

        part.x = x;
        part.y = y;
        part.direction = dir;
        part.speed = speed;

        part.directionDraw = 0;
        part.width = 1;
        part.height = 1;
        part.color = color;
        part.type = Part.Type.FILL;
        part.life = Integer.MAX_VALUE;

        parts.add(part);

    }
}
