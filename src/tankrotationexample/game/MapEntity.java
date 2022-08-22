package tankrotationexample.game;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MapEntity extends Entity {
    private final int wallType;

    public MapEntity(BufferedImage img, float x, float y, int type) {
        super(img, x, y);
        this.wallType = type;
        this.hitBox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }

    public int getType() {
        return this.wallType;
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public void draw(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
        g.drawImage(this.img, rotation, null);
    }
}
