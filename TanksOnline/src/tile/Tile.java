package tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    public List<BufferedImage> animation = new ArrayList<>();
    public boolean collision = false;
    public int currentImageIndex = 0;
}
