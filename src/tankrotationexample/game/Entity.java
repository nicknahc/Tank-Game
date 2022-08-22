package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected float x, y;
    protected double height;
    protected double width;
    protected BufferedImage img;
    protected Rectangle hitBox;
    public Entity(BufferedImage img, float x, float y) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }

    public abstract Rectangle getHitBox();
    public abstract void draw(Graphics2D g2D);
}
