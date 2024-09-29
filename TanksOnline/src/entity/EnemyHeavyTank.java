package entity;

import main.GamePanel;
import main.enums.DifficultyEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class EnemyHeavyTank extends Entity {
    public EnemyHeavyTank(GamePanel gp, int worldX, int worldY) {
        super(gp);

        moveCooldown = 3;
        baseDamageVariable = 3;
        damageVariable = baseDamageVariable;
        baseSpeed = 1;
        speed = baseSpeed;

        baseShootCooldown = 100;
        shootCooldown = baseShootCooldown;
        baseRespawnCounter = 1200;
        maxLife = 10;
        life = maxLife;
        maxAmmo = 10;
        ammo = maxAmmo;

        solidArea = new Rectangle();
        solidArea.x = 7;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        this.worldX = worldX;
        this.worldY = worldY;


        if (gp.difficulty == DifficultyEnum.EASY) {
            baseRespawnCounter += 300;
            maxLife-=4;
            life = maxLife;
        }

        if (gp.difficulty == DifficultyEnum.HARD) {
            moveCooldown += 2;
            baseDamageVariable += 1;
            damageVariable = baseDamageVariable;
            baseSpeed = 2;
            speed = baseSpeed;
            baseShootCooldown = 60;
            shootCooldown = baseShootCooldown;
        }
    }

    @Override
    public void getImages() {
        try {
            images = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/entity/enemies/maus_tank_tilesheet.png"));


            for (int y = 0; y < tileSheet.getHeight() / gp.tileSize; y++) {
                for (int x = 0; x < tileSheet.getWidth() / gp.tileSize; x++) {
                    images.add(tileSheet.getSubimage(x * gp.tileSize, y * gp.tileSize, gp.tileSize, gp.tileSize));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
