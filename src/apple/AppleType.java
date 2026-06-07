package apple;

import java.awt.*;

public enum AppleType {
    REGULAR(1, Color.RED),
    GOLDEN(3, Color.YELLOW),
    POISONED(-2, Color.MAGENTA);

    private final int points;
    private final Color color;

    private AppleType(int points, Color color){
        this.points = points;
        this.color = color;
    }

    public int getPoints(){
        return points;
    }

    public Color getColor(){
        return color;
    }
}