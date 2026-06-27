package difficulty;

public class MediumDifficulty implements DifficultyBehaviour {
    private final static int DELAY = 250; 

    @Override
    public int getDelay() {
        return DELAY;
    }
}
