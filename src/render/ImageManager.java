package render;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import apple.AppleType;

public class ImageManager {
    private final String IMAGE_PATH_ERROR = "Could not load image: ";
    private final Image snakeHeadImage;
    private final BufferedImage snakeSkinImage;
    private Map<AppleType, Image> appleImages;

    public ImageManager(){
        snakeHeadImage = loadImage("/images/snakeHead.png");
        snakeSkinImage = loadBufferedImage("/images/snakeSkin.png");
        appleImages = loadAppleImages();
    }

    private Image loadImage(String path){
        URL imageUrl = getClass().getResource(path);
        if(imageUrl == null){
            System.out.println(IMAGE_PATH_ERROR + path);
            return null;
        }
        return new ImageIcon(imageUrl).getImage();
    }
    
    private BufferedImage loadBufferedImage(String path){
        try{
            URL imageUrl = getClass().getResource(path);
            if(imageUrl == null){
                System.out.println(IMAGE_PATH_ERROR + path);
                return null;
            }
            return ImageIO.read(imageUrl);
        }catch(Exception e){
            System.out.println(IMAGE_PATH_ERROR + path);
            return null;
        }
    }

    private Map<AppleType, Image> loadAppleImages(){
        appleImages = new EnumMap<>(AppleType.class);
        for(AppleType appleType : AppleType.values()){
            appleImages.put(appleType, loadImage(appleType.getImagePath()));
        }
        return appleImages;
    }

    public Image getSnakeHeadImage(){
        return snakeHeadImage;
    }

    public BufferedImage getSnakeSkinImage(){
        return snakeSkinImage;
    }

    public Image getAppleImage(AppleType appleType){
        return appleImages.get(appleType);
    }
}
