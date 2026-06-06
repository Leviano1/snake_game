package apple;

public class Apple {
    private final int x;
    private final int y;
    private final AppleType appleType;

    public Apple(int x, int y, AppleType appleType){
        this.x = x;
        this.y = y;
        this.appleType = appleType;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public AppleType getAppleType(){
        return appleType;
    }

    public int getPoints(){
        return appleType.getPoints();
    }
}
