package gfx;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuperGraphicalEffect {
    public int worldX, worldY;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public List<BufferedImage> images = new ArrayList<>();
    public BufferedImage image;
    public int maxLife;
    public int life;
    GamePanel gp;

    public SuperGraphicalEffect(GamePanel gp) {
        this.gp = gp;
        getImages();
        maxLife = 60;
        life = maxLife;
        gp.playSE(4);
    }

    public void getImages() {
        try {
            images = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/gfx/explosion_tilesheet.png"));

            for (int y = 0; y < tileSheet.getHeight() / 16; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 16; x++) {
                    images.add(tileSheet.getSubimage(x * 16, y * 16, 16, 16));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            image = null;



            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
