package difficulty;

public class MediumDifficulty implements DifficultyBehaviour {
    private final static int DELAY = 200; 

    @Override
    public int getDelay() {
        return DELAY;
    }
}
