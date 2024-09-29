package entity;


import entity.projectiles.SuperProjectile;
import main.enums.BuffEnum;
import main.enums.DifficultyEnum;
import main.enums.DirectionEnum;
import main.enums.TeamEnum;
import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static main.enums.DirectionEnum.*;

public class Player extends Entity {
    public final int screenX;
    public final int screenY;
    public boolean noColFeature = false;
    KeyHandler keyH;


    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();
        getImages();

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 10;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 27;
        solidArea.height = 27;
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 4 + gp.tileSize/2;
        worldY = gp.tileSize * 8;
        direction = UP;

        maxLife = 6;
        life = maxLife;

        spriteCounter = 0;
        invincibleCounter = 0;

        maxAmmo = 6;
        ammo = maxAmmo;

        team = TeamEnum.BLUE;

        isKilled = false;
        baseRespawnCounter = 300;

        damageVariable = baseDamageVariable;
        ammoRegenBasicCooldown = 240;
        ammoRegenCooldown = ammoRegenBasicCooldown;
        ammoRegenCounter = ammoRegenCooldown;
        baseShootCooldown = 60;
        shootCooldown = baseShootCooldown;
        baseSpeed = 1;
        speed = baseSpeed;
        hasArmor = false;
    }

    @Override
    public void getImages() {
        try {
            images = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/entity/player/blue_tank_tilesheet.png"));
            immortalAura1 = ImageIO.read(getClass().getResourceAsStream("/imgs/immortal1.png"));
            immortalAura2 = ImageIO.read(getClass().getResourceAsStream("/imgs/immortal2.png"));

            for (int y = 0; y < tileSheet.getHeight() / gp.tileSize; y++) {
                for (int x = 0; x < tileSheet.getWidth() / gp.tileSize; x++) {
                    images.add(tileSheet.getSubimage(x * gp.tileSize, y * gp.tileSize, gp.tileSize, gp.tileSize));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (isKilled) {
            if (respawnCounter <= 0 && flag != null && !flag.isKilled){
                life = maxLife;
                ammo = maxAmmo;
                isKilled = false;
                invincibleCounter = 60;
                worldX = flag.worldX;
                worldY = flag.worldY;
                direction = DOWN;
            } else {
                respawnCounter--;
                return;
            }
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.eKeyPressed || keyH.qKeyPressed || gp.mouseHandler.isLMBHeld) {

            if (keyH.upPressed) {
                direction = DirectionEnum.UP;
            } else if (keyH.downPressed) {
                direction = DOWN;
            } else if (keyH.leftPressed) {
                direction = LEFT;
            } else if (keyH.rightPressed) {
                direction = RIGHT;
            }

            collisionOn = false;
            if (!noColFeature) {
                gp.cChecker.checkTile(this);
                gp.cChecker.checkEntity(this, gp.enemies);
                gp.cChecker.checkEntity(this, gp.barricades);
                gp.cChecker.checkObject(this, gp.buffs);
            }

            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
                if (!collisionOn) {
                    switch (direction) {
                        case UP -> worldY -= speed;
                        case DOWN -> worldY += speed;
                        case LEFT -> worldX -= speed;
                        case RIGHT -> worldX += speed;
                    }
                    gp.playSE(11);
                }
                spriteCounter++;

                if (spriteCounter > 12) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                        spriteCounter = 0;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                        spriteCounter = 0;
                    }
                }
            }

        } else {
            if (activityCounter > 6) {
                spriteNum = 1;
                spriteCounter = 0;
            }
            activityCounter++;
        }

        if (counter >= 61) {
            counter = 1;
        }
        counter++;

        if (invincibleCounter > 0) {
            invincibleCounter--;
        }

        if (shootCounter > 0) {
            shootCounter--;
        }

        if (ammo < maxAmmo) {
            if (ammoRegenCounter == 0) {
                ammo++;
                ammoRegenCounter = ammoRegenCooldown;
            } else {
                ammoRegenCounter--;
            }
        }

        if (buffCounter == 0 && currentBuff != BuffEnum.NONE) {
            currentBuff = BuffEnum.NONE;
            setBaseStats();
        } else buffCounter--;
    }

    public void attackMethod() {
        if (isKilled) return;

        if (shootCounter != 0 || ammo == 0) return;

        int currentWorldX = worldX;
        int currentWorldY = worldY;

        SuperProjectile projectile = new SuperProjectile(gp, this);
        projectile.direction = direction;
        projectile.worldX = currentWorldX;
        projectile.worldY = currentWorldY;

        gp.projectiles.add(projectile);
        shootCounter = shootCooldown;
        ammo--;
    }

    public void draw(Graphics2D g2) {

//        g2.setColor(Color.WHITE);
//        g2.fillRect(x,y,gp.tileSize,gp.tileSize);
        BufferedImage image = null;
        switch (direction) {
            case UP -> {
                if (spriteNum == 1) {
                    image = images.get(0);
                }
                if (spriteNum == 2) {
                    image = images.get(1);
                }
            }
            case DOWN -> {

                if (spriteNum == 1) {
                    image = images.get(2);
                }
                if (spriteNum == 2) {
                    image = images.get(3);
                }
            }
            case LEFT -> {

                if (spriteNum == 1) {
                    image = images.get(4);
                }
                if (spriteNum == 2) {
                    image = images.get(5);
                }
            }
            case RIGHT -> {

                if (spriteNum == 1) {
                    image = images.get(6);
                }
                if (spriteNum == 2) {
                    image = images.get(7);
                }
            }
        }

        if (invincibleCounter > 0) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        }
        if (image == null) image = images.get(0);
        g2.drawImage(image, screenX, screenY, image.getWidth(), image.getHeight(), null);
        if (gp.showHitboxes) {
            g2.setColor(Color.red);
            g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
            g2.setColor(Color.blue);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if (hasArmor){
            if (gp.armorCounter <= 6){
                g2.drawImage(immortalAura1, screenX, screenY, gp.tileSize, gp.tileSize, null);
            } else {
                g2.drawImage(immortalAura2, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}