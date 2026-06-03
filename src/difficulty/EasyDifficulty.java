package difficulty;

public class EasyDifficulty implements DifficultyBehaviour {
    private final static int DELAY = 300; 

    @Override
    public int getDelay() {
       return DELAY;
    }
}
