package apple;

import java.util.Random;

public class AppleSpawner {
    private final Random random = new Random();
    private AppleType appleType;

    public Apple spawnApple(int screenWidth, int screenHeight, int unitSize){
        if(random.nextInt(5) == 0){
            appleType = AppleType.GOLDEN;
        }else{
            appleType = AppleType.REGULAR;
        }
        return spawnAppleOfType(screenWidth, screenHeight, unitSize, appleType);
    }

    public Apple spawnPoisonApple(int screenWidth, int screenHeight, int unitSize){
        return spawnAppleOfType(screenWidth, screenHeight, unitSize, AppleType.POISONED);
    }

    public Apple spawnAppleOfType(int screenWidth, int screenHeight, int unitSize, AppleType appleType){
        int x = random.nextInt(screenWidth / unitSize) * unitSize;
        int y = random.nextInt(screenHeight / unitSize) * unitSize;
        return new Apple(x, y, appleType);
    }
}
