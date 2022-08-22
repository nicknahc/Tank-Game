package tankrotationexample;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Resources {
    private static final Map<String, BufferedImage> images = new HashMap<>();
    private static final Map<String, Clip> sounds = new HashMap<>();
    private static final HashMap animations = new HashMap();

    public static BufferedImage getImage(String key){
        return Resources.images.get(key);
    }

    public static Clip getSound(String key){
        return Resources.sounds.get(key);
    }

    public static Object getAnimation(String key){
        return Resources.animations.get(key);
    }

    public static void initSounds(){
        try {
            AudioInputStream as;
            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("laserShoot.wav"))));
            Clip clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("bullet", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("hitHurt.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("hurt", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("reloadPowerUp.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("reload", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("healthPowerUp.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("heal", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("speedPowerUp.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("speed", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("wallBreak.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("wallBreak", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("musicLoop.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("music", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("explosion.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("death", clip);

            as = AudioSystem.getAudioInputStream((Objects.requireNonNull(Resources.class.getClassLoader().getResource("end.wav"))));
            clip = AudioSystem.getClip();
            clip.open(as);
            Resources.sounds.put("end", clip);



        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            System.err.println(e);
            e.printStackTrace();
            System.exit(-2);
        }
       // Resources.sounds.put("bullet", loadClip("Resources/Explosion_small.wav"));
    }
    public static void initImages(){
        try {
            Resources.images.put("background", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("bg2.png"))));
            //Resources.images.put("background", ImageIO.read(new File("Resources/bg2.png")));
            Resources.images.put("tank1", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("tank.gif"))));
            Resources.images.put("tank2", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("tank3.gif"))));
            Resources.images.put("wall", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("wall.png"))));
            Resources.images.put("breakableWall", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("breakableWall.png"))));
            Resources.images.put("healthPowerUp", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("health.png"))));
            Resources.images.put("reloadPowerUp", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("reload.png"))));
            Resources.images.put("speedPowerUp", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("speed.png"))));
            Resources.images.put("bullet", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Shell.gif"))));
            Resources.images.put("bullet2", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Shell2.gif"))));
            Resources.images.put("title", ImageIO.read(Objects.requireNonNull(Resources.class.getClassLoader().getResource("title.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void initAnimations(){

    }

    public static void main(String[] args){
        Resources.initImages();
        //Resources.initAnimations();
    }
}
