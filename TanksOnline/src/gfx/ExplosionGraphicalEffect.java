package gfx;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ExplosionGraphicalEffect extends SuperGraphicalEffect{
    public ExplosionGraphicalEffect(GamePanel gp) {
        super(gp);
        maxLife = 60;
        life = maxLife;
    }

    @Override
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

    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter == 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 5;
            }
            else if (spriteNum == 5) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            image = images.get(spriteNum-1);

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
