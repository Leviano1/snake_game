package render;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    private final Map<String, Clip> sounds = new HashMap<>();

    public SoundManager(){
        loadSound("/sounds/music_move.wav");
        loadSound("/sounds/music_food.wav");
        loadSound("/sounds/music_gameover.wav");
    }

    private void loadSound(String path){
        try{
            URL soundUrl = getClass().getResource(path);

            if(soundUrl == null){
                System.out.println("Could not load sound: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl);
            Clip clip = AudioSystem.getClip();

            clip.open(audioStream);
            audioStream.close();
            sounds.put(path, clip);
        }catch(Exception e){
            System.out.println("Could not load sound: " + path);
        }
    }

    public void playSound(String path){
        Clip clip = sounds.get(path);
        if(clip == null){
            return;
        }

        if(clip.isRunning()){
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void playMoveSound(){
        playSound("/sounds/music_move.wav");
    }

    public void playFoodSound(){
        playSound("/sounds/music_food.wav");
    }

    public void playGameOverSound(){
        playSound("/sounds/music_gameover.wav");
    }
    
}
