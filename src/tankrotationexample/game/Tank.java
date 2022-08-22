package tankrotationexample.game;

import tankrotationexample.Resources;
import tankrotationexample.Sound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tank extends Entity {

    private double moveXDirection, moveYDirection, angle;
    private double tankSpeed;
    private final double rotationSpeed;
    private final ArrayList<Bullet> BulletList;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;
    private double lastShot;
    private float saveX, saveY;
    private int health, lives;
    private boolean bulletPowerUp = false;
    private double bulletCoolDown = 800;
    private final BufferedImage bulletImg = Resources.getImage("bullet");
    private final BufferedImage bulletImg2 = Resources.getImage("bullet2");
    private int type;
    private Rectangle hitBox;

    public Tank(BufferedImage img, float x, float y, double angle, int type) {
        super(img, x, y);
        this.angle = angle;
        this.BulletList = new ArrayList<>();
        this.tankSpeed = 2.0;
        this.rotationSpeed = 1.5;
        this.health = 3;
        this.lives = 2;
        this.lastShot = 0;
        this.type = type;
        this.hitBox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() {
        this.ShootPressed = true;
    }

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public ArrayList<Bullet> getBulletList(){
        return this.BulletList;
    }

    public void update() {
        saveCoordinatesX();
        saveCoordinatesY();
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed) {
            double time = System.currentTimeMillis();
            if (time > lastShot + bulletCoolDown) {
                if (!bulletPowerUp){
                    BulletList.add(new Bullet(bulletImg2, angle, getTankCenterX(), getTankCenterY()));
                    (new Sound(Resources.getSound("bullet"))).playSound();
                }
                if (bulletPowerUp){
                    BulletList.add(new Bullet(bulletImg, angle, getTankCenterX(), getTankCenterY()));
                    (new Sound(Resources.getSound("bullet"))).playSound();
                }
                lastShot = time;
            }
        }
    }

    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    private void moveBackwards() {

        moveXDirection = (tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (tankSpeed * Math.sin(Math.toRadians(angle)));
        x -= moveXDirection;
        y -= moveYDirection;
        this.hitBox.setLocation((int)x, (int)y);
    }

    private void moveForwards() {
        moveXDirection = (tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (tankSpeed * Math.sin(Math.toRadians(angle)));
        x += moveXDirection;
        y += moveYDirection;
        this.hitBox.setLocation((int)x, (int)y);
    }

    public void setBulletPowerUp() {
        bulletCoolDown -= 200;
        bulletPowerUp = true;
    }

    public void setSpeedPowerUp(){
        tankSpeed += 1.0;
    }
    public void resetTankStatus(){
        bulletCoolDown = 800;
        tankSpeed = 2.0;
        setHealth(3);
        if (type == 1){
            angle = 270.0;
            setX(100);
            setY(1000);
        } else {
            angle = 90.0;
            setX(1030);
            setY(100);
        }
        this.hitBox.setLocation((int)x, (int)y);
    }

    public void saveCoordinatesX() {
        saveX = x;
    }

    public void saveCoordinatesY() {
        saveY = y;
    }

    public void stopOverlap() {
            this.x = saveX;
            this.y = saveY;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return this.lives;
    }

    public float getTankCenterX() {
        return (float) (x + (width/2));
    }

    public float getTankCenterY() {
        return (float) (y + (height/2));
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public void draw(Graphics2D g2d) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth()/2.0, this.img.getHeight()/2.0);
        g2d.drawImage(this.img, rotation, null);
    }
}

