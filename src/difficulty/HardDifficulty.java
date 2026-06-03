package difficulty;

public class HardDifficulty implements DifficultyBehaviour{
    private final static int DELAY = 100; 

    @Override
    public int getDelay() {
        return DELAY;
    }
} 
    
    