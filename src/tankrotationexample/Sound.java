package tankrotationexample;

import javax.sound.sampled.Clip;
public class Sound implements Runnable{
    Clip c;
    @Override
    public void run() {
        c.loop(Clip.LOOP_CONTINUOUSLY);
        this.playSound();
    }

    public Sound(Clip c){
        this.c = c;
    }

    public void stopSound() {
        if(c.isRunning()){
            c.stop();
        }
    }

    public void playSound(){
        if(c == null){
            return;
        }
        if(c.isRunning()){
           c.stop();
        }
        c.setFramePosition(0); //set sound clip progress to beginning
        c.start();
    }
}
