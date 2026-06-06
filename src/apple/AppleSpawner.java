package apple;

import java.util.Random;

public class AppleSpawner {
    private final Random random = new Random();
    private AppleType appleType;

    public Apple spawnApple(int screenWidth, int screenHeight, int unitSize){
        int x = random.nextInt(screenWidth / unitSize) * unitSize;
        int y = random.nextInt(screenHeight / unitSize) * unitSize;

        if(random.nextInt(5) == 0){
            appleType = AppleType.GOLDEN;
        }else{
            appleType = AppleType.REGULAR;
        }
        
        return new Apple(x, y, appleType);
    }
}
