package tankrotationexample.game;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Bullet extends Entity{

    private final double angle;
    private final double moveXDirection;
    private final double moveYDirection;

    public Bullet(BufferedImage img, double angle, float x, float y) {

        super(img, x, y);
        this.angle = angle;
        double bulletSpeed = 5;
        moveXDirection = (int) Math.round(bulletSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(bulletSpeed * Math.sin(Math.toRadians(angle)));
        this.hitBox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public void update() {
        x += moveXDirection;
        y += moveYDirection;
        this.hitBox.setLocation((int)x, (int)y);
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0, 0);
        g.drawImage(this.img, rotation, null);
    }
}
