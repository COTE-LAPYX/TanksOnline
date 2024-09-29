package entity;

import main.enums.DirectionEnum;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Barricade extends Entity{
    public Barricade(GamePanel gp, int worldX, int worldY) {
        super(gp);
        maxLife = 3;
        life = maxLife;
        direction = DirectionEnum.DOWN;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    @Override
    public void getImages() {
        try {

            images = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/entity/object/barricade_tilesheet.png"));


            for (int y = 0; y < tileSheet.getHeight() / 16; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 16; x++) {
                    images.add(tileSheet.getSubimage(x * 16, y * 16, 16, 16));
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (life == 3){
            spriteNum = 1;
        }
        if (life == 2){
            spriteNum = 2;
        }
        if (life <= 1){
            spriteNum = 3;
            isKilled = true;
        }

        if (invincibleCounter != 0) {
            if (invincibleCounter < 0) {
                invincibleCounter = 0;
            }
            invincibleCounter--;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            image = null;

            if (spriteNum == 1) {
                image = images.get(0);
            }
            if (spriteNum == 2) {
                image = images.get(1);
            }
            if (spriteNum == 3){
                image = images.get(2);
            }

            if (invincibleCounter > 0) {
                hpBarOn = true;
                hpBarCounter = 0;
            }
            g2.drawImage(image, screenX+8, screenY+8, gp.tileSize-16, gp.tileSize-16, null);
            changeAlpha(g2, 1f);
            if (gp.showHitboxes) {
                g2.setColor(Color.blue);
                g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height);
            }
        }

    }
}
