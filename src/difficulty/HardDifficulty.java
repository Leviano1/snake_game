package difficulty;

public class HardDifficulty implements DifficultyBehaviour{
    private final static int DELAY = 200; 

    @Override
    public int getDelay() {
        return DELAY;
    }
} 
    
    