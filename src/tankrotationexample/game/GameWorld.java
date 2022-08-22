/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;

import tankrotationexample.Resources;
import tankrotationexample.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

public class GameWorld extends JPanel implements Runnable{
    protected static final int WORLD_WIDTH = 1530, WORLD_HEIGHT = 1260;
    protected static final int SCREEN_WIDTH = 1275;
    protected static final int SCREEN_HEIGHT = 690;
    private int horizontalScreenBounds, verticalScreenBounds;
    private Graphics2D buffer;
    private Graphics2D g2;
    protected static Tank t1;
    protected static Tank t2;
    private BufferedImage wall, breakableWall, healthPowerUp, reloadPowerUp, speedPowerUp;
    private BufferedReader br;
    private String input;
    private final ArrayList<MapEntity> mapEntities = new ArrayList();
    private BufferedImage world = new BufferedImage(WORLD_WIDTH, WORLD_HEIGHT, 1);
    private boolean endGame = false;
    private boolean suddenDeath = false;
    static double timer;
    private final Launcher lf;
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    public void resetMap(){
        suddenDeath = false;
        timer = System.currentTimeMillis()/1000;
        t1.resetTankStatus();
        t2.resetTankStatus();
        mapMaker();
    }
    public void suddenDeath(){
        timer = System.currentTimeMillis()/1000;
        suddenDeath = true;
        t1.resetTankStatus();
        t2.resetTankStatus();
        t1.setBulletPowerUp();
        t2.setBulletPowerUp();
        t1.setSpeedPowerUp();
        t2.setSpeedPowerUp();
        t1.setHealth(1);
        t2.setHealth(1);

        mapEntities.clear();

        try {
            this.br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GameWorld.class.getClassLoader().getResourceAsStream("map.txt"))));
            this.input = this.br.readLine();
        } catch (IOException var2) {
            System.out.println(var2.getMessage());
        }

        int row = 0;
        try {
            while(this.input != null) {
                for(int i = 0; i < this.input.length(); i++) {
                   if(this.input.charAt(i) == '1') {
                        this.mapEntities.add(new MapEntity(this.wall, (float) (i * 30), (float) (row * 30), 1));
                    }
                }
                row++;
                this.input = this.br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-2);
        }
    }
    public void init() {
        timer = System.currentTimeMillis()/1000;
        Resources.initImages();
        Resources.initSounds();
        this.world = new BufferedImage(WORLD_WIDTH, WORLD_HEIGHT, 1);
        wall = Resources.getImage("wall");
        breakableWall = Resources.getImage("breakableWall");
        healthPowerUp = Resources.getImage("healthPowerUp");
        reloadPowerUp = Resources.getImage("reloadPowerUp");
        speedPowerUp = Resources.getImage("speedPowerUp");
        mapMaker();
        t1 = new Tank(Resources.getImage("tank1"), (float)(100), (float)(WORLD_HEIGHT - 300), 270.0D, 1);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        t2 = new Tank(Resources.getImage("tank2"), (float)(WORLD_WIDTH - 500), (float)(100), 90.0D, 2);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
        this.lf.getJf().setLayout(new BorderLayout());
        this.lf.getJf().setResizable(false);
        this.lf.getJf().add(this);
        this.lf.getJf().setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.lf.getJf().setLocationRelativeTo(null);
        this.lf.getJf().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.lf.getJf().setVisible(true);
    }
    @Override
    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;
        buffer = world.createGraphics();

        BufferedImage battleField = Resources.getImage("background"); //draw background
        int tileWidth = battleField.getWidth();
        int tileHeight = battleField.getHeight();
        int x = WORLD_WIDTH / tileWidth;
        int y = WORLD_HEIGHT / tileHeight;
        for(int i = -1; i <= y; ++i) {
            for(int j = 0; j <= x; ++j) {
                buffer.drawImage(battleField, j * tileWidth, i * tileHeight + tileHeight, tileWidth, tileHeight, this);
            }
        }

        this.mapEntities.forEach(p -> p.draw(buffer));
        t1.getBulletList().forEach(b -> b.draw(buffer));
        t2.getBulletList().forEach(b -> b.draw(buffer));
        t1.draw(buffer);
        t2.draw(buffer);
        final int X = 630;
        BufferedImage t1View = world.getSubimage(this.getHorizontalScreenBounds(t1), this.getVerticalScreenBounds(t1), X, 655);
        g2.drawImage(t1View, 0, 0, this);
        BufferedImage t2View = world.getSubimage(this.getHorizontalScreenBounds(t2), this.getVerticalScreenBounds(t2), X, 655);
        g2.drawImage(t2View, X, 0, this);
        g2.fillRect(X, 0, 2, X);
        g2.fillRect(X - WORLD_WIDTH / 12, X - WORLD_HEIGHT / 6, WORLD_WIDTH / 6, WORLD_HEIGHT / 6);
        Image scaledMap = world.getScaledInstance(150, 150, 0);
        g2.drawImage(scaledMap, X - WORLD_WIDTH / 12, X - WORLD_HEIGHT / 6, WORLD_WIDTH / 6, WORLD_HEIGHT / 6, this);
        livesDisplay();
        healthDisplay();
    }

    public void healthDisplay() {
        g2.setFont(new Font("Courier", Font.PLAIN, 30));
        this.g2.setColor(Color.white);
        this.g2.drawString("Health: " + t1.getHealth(), 69, 600);
        this.g2.drawString("Health: " + t2.getHealth(), 800, 600);
    }

    public void livesDisplay() {
        g2.setFont(new Font("Courier", Font.PLAIN, 30));
        this.g2.setColor(Color.white);
        this.g2.drawString("Lives: " + t1.getLives(), 269, 600);
        this.g2.drawString("Lives: " + t2.getLives(), 1000, 600);

        if (t1.getLives() == -1) {
            this.endGame = true;
        }

        if (t2.getLives() == -1) {
            this.endGame = true;
        }

    }
    public boolean isEndGame() {
        return this.endGame;
    }
    public boolean isSuddenDeath(){return this.suddenDeath;}


    public void updateBullets() {
        t1.getBulletList().forEach(Bullet::update);
        t2.getBulletList().forEach(Bullet::update);
    }

    public void mapMaker() {
        mapEntities.clear();
        try {
            this.br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GameWorld.class.getClassLoader().getResourceAsStream("map.txt"))));
            this.input = this.br.readLine();
        } catch (IOException var2) {
            System.out.println(var2.getMessage());
        }
       int row = 0;
        try {
            while(this.input != null) {
                for(int i = 0; i < this.input.length(); i++) {
                    switch (this.input.charAt(i)) {
                        case '1' -> this.mapEntities.add(new MapEntity(this.wall, (float) (i * 30), (float) (row * 30), 1));
                        case '2' -> this.mapEntities.add(new MapEntity(this.breakableWall, (float) (i * 30), (float) (row * 30), 2));
                        case '3' -> this.mapEntities.add(new MapEntity(this.healthPowerUp, (float) (i * 30), (float) (row * 30), 3));
                        case '4' -> this.mapEntities.add(new MapEntity(this.reloadPowerUp, (float) (i * 30), (float) (row * 30), 4));
                        case '5' -> this.mapEntities.add(new MapEntity(this.speedPowerUp, (float) (i * 30), (float) (row * 30), 5));
                    }
                }
                row++;
                this.input = this.br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-2);
        }

    }


    public void checkCollision() {
        Rectangle t1HitBox = t1.getHitBox();
        Rectangle t2HitBox = t2.getHitBox();
        if (t1HitBox.intersects(t2HitBox)) {
            t1.stopOverlap();
        }

        if (t2HitBox.intersects(t1HitBox)) {
            t2.stopOverlap();
        }

        int i;
        Rectangle bulletHitBox;
        for(i = 0; i <= this.mapEntities.size() - 1; ++i) {
            Rectangle wallHitBox = this.mapEntities.get(i).getHitBox();
            if (t1HitBox.intersects(wallHitBox)) {
                if (this.mapEntities.get(i).getType() == 4) { //bullet power up
                    t1.setBulletPowerUp();
                    (new Sound(Resources.getSound("reload"))).playSound();
                    this.mapEntities.remove(i);
                }

                if (this.mapEntities.get(i).getType() == 3) { //health power up
                    if (t1.getHealth() != 5) {
                        this.mapEntities.remove(i);
                        t1.setHealth(5);
                        (new Sound(Resources.getSound("heal"))).playSound();
                    }
                } else if (this.mapEntities.get(i).getType() == 5) { //speed power up
                        t1.setSpeedPowerUp();
                        (new Sound(Resources.getSound("speed"))).playSound();
                        this.mapEntities.remove(i);
                }else if (this.mapEntities.get(i).getType() != 4) {
                    t1.stopOverlap();
                }
            }

            if (t2HitBox.intersects(wallHitBox)) {
                if (this.mapEntities.get(i).getType() == 4) {
                    t2.setBulletPowerUp();
                    this.mapEntities.remove(i);
                    (new Sound(Resources.getSound("reload"))).playSound();
                }

                if (this.mapEntities.get(i).getType() == 3) {
                    if (t2.getHealth() != 5) {
                        this.mapEntities.remove(i);
                        t2.setHealth(5);
                        (new Sound(Resources.getSound("heal"))).playSound();
                    }
                } else if (this.mapEntities.get(i).getType() == 5) { //speed power up
                    t2.setSpeedPowerUp();
                    (new Sound(Resources.getSound("speed"))).playSound();
                    this.mapEntities.remove(i);
                } else if (this.mapEntities.get(i).getType() != 4) {
                    t2.stopOverlap();
                }
            }

            int j;
            for(j = 0; j < t1.getBulletList().size(); ++j) {
                bulletHitBox = t1.getBulletList().get(j).getHitBox();
                if (wallHitBox.intersects(bulletHitBox) && (this.mapEntities.get(i).getType() == 1 || this.mapEntities.get(i).getType() == 2)) { //if bullet hits wall
                    t1.getBulletList().remove(j);
                    if (this.mapEntities.get(i).getType() == 2) { //wallBreak
                        this.mapEntities.remove(i);
                        (new Sound(Resources.getSound("wallBreak"))).playSound();
                    }
                } else {
                    if (wallHitBox.intersects(bulletHitBox)) {
                        if (this.mapEntities.get(i).getType() == 4) { //bullet power up when shot
                            t1.setBulletPowerUp();
                            (new Sound(Resources.getSound("reload"))).playSound();
                            this.mapEntities.remove(i);
                        }
                        if (this.mapEntities.get(i).getType() == 5) { //speed power up when shot
                            t1.setSpeedPowerUp();
                            (new Sound(Resources.getSound("speed"))).playSound();
                            this.mapEntities.remove(i);
                        }

                        if (this.mapEntities.get(i).getType() == 3) { //health power up when shot
                            if (t1.getHealth() != 3) {
                                this.mapEntities.remove(i);
                                t1.setHealth(3);
                                (new Sound(Resources.getSound("heal"))).playSound();
                            }
                        }
                    }
                }
            }

            for(j = 0; j < t2.getBulletList().size(); ++j) {
                bulletHitBox = t2.getBulletList().get(j).getHitBox();
                if (wallHitBox.intersects(bulletHitBox) && ((this.mapEntities.get(i).getType() == 1) || (this.mapEntities.get(i).getType() == 2))) {
                    t2.getBulletList().remove(j);
                    if (this.mapEntities.get(i).getType() == 2) {
                        this.mapEntities.remove(i);
                        (new Sound(Resources.getSound("wallBreak"))).playSound();
                    }
                } else {
                    if (wallHitBox.intersects(bulletHitBox)) {
                        if (this.mapEntities.get(i).getType() == 4) { //bullet power up when shot
                            t2.setBulletPowerUp();
                            (new Sound(Resources.getSound("reload"))).playSound();
                            this.mapEntities.remove(i);
                        }

                        if (this.mapEntities.get(i).getType() == 5) { //speed power up when shot
                            t2.setSpeedPowerUp();
                            (new Sound(Resources.getSound("speed"))).playSound();
                            this.mapEntities.remove(i);
                        }

                        if (this.mapEntities.get(i).getType() == 3) { //health power up when shot
                            if (t2.getHealth() != 3) {
                                this.mapEntities.remove(i);
                                t2.setHealth(3);
                                (new Sound(Resources.getSound("heal"))).playSound();
                            }
                        }
                    }
                }
            }
        }

        for(i = 0; i < t1.getBulletList().size(); ++i) { //if bullet hits tank
            bulletHitBox = t1.getBulletList().get(i).getHitBox();
            if (t2HitBox.intersects(bulletHitBox)) {
                t1.getBulletList().remove(i);
                (new Sound(Resources.getSound("hurt"))).playSound();
                t2.setHealth(t2.getHealth() - 1);
                if (t2.getHealth() == 0) {
                    (new Sound(Resources.getSound("death"))).playSound();
                    t2.setLives(t2.getLives() - 1);
                    if (t2.getLives() != -1) {
                        resetMap();
                    }
                }
            }
        }

        for(i = 0; i < t2.getBulletList().size(); ++i) {
            bulletHitBox = t2.getBulletList().get(i).getHitBox();
            if (t1HitBox.intersects(bulletHitBox)) {
                (new Sound(Resources.getSound("hurt"))).playSound();
                t2.getBulletList().remove(i);
                t1.setHealth(t1.getHealth() - 1);
                if (t1.getHealth() == 0) {
                    (new Sound(Resources.getSound("death"))).playSound();
                    t1.setLives(t1.getLives() - 1);
                    if (t1.getLives() != -1) {
                        resetMap();
                    }
                }
            }
        }
    }

    private int getHorizontalScreenBounds(Tank tank) {
        if (tank.getTankCenterX() <= (double) WORLD_WIDTH - 315.0D) {
            this.horizontalScreenBounds = (int)tank.getTankCenterX() - 315;
        }

        if (tank.getTankCenterX() >= (double) WORLD_WIDTH - 315.0D) {
            this.horizontalScreenBounds = WORLD_WIDTH - 630;
        }

        if (tank.getTankCenterX() <= 315.0D) {
            this.horizontalScreenBounds = 0;
        }

        return this.horizontalScreenBounds;
    }

    private int getVerticalScreenBounds(Tank tank) {
        if (tank.getTankCenterY() <= (double) WORLD_HEIGHT - 330.0D) {
            this.verticalScreenBounds = (int)tank.getTankCenterY() - 330;
        }

        if (tank.getTankCenterY() >= (double) WORLD_HEIGHT - 330.0D) {
            this.verticalScreenBounds = WORLD_HEIGHT - 660;
        }

        if (tank.getTankCenterY() <= 330.0D) {
            this.verticalScreenBounds = 0;
        }

        return this.verticalScreenBounds;
    }


    @Override
    public void run() {
        try {
            (new Thread(new Sound(Resources.getSound("music")))).start();
            while(!isEndGame()) {
                t1.update();
                t2.update();
                updateBullets();
                checkCollision();
                repaint();
                Thread.sleep(1000/144);
                double time = System.currentTimeMillis()/1000;
                if(time - GameWorld.timer > 60){
                    if(!isSuddenDeath()){
                        suddenDeath();
                    }
                }
            }
            lf.setFrame("end");
        } catch (InterruptedException ignored) {
        }
    }
}
