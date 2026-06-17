package apple;

import java.awt.*;

public enum AppleType {
    REGULAR(1, Color.RED, "/images/redApple.png"),
    GOLDEN(3, Color.YELLOW, "/images/goldenApple.png"),
    POISONED(-2, Color.MAGENTA, "/images/rottenApple.png");

    private final int points;
    private final Color color;
    private final String imagePath;

    private AppleType(int points, Color color, String imagePath){
        this.points = points;
        this.color = color;
        this.imagePath = imagePath;
    }

    public int getPoints(){
        return points;
    }

    public Color getColor(){
        return color;
    }

    public String getImagePath(){
        return imagePath;
    }
}