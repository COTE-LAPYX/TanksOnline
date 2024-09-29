package entity;

import entity.projectiles.SuperProjectile;
import main.GamePanel;
import main.enums.BuffEnum;
import main.enums.DifficultyEnum;
import main.enums.DirectionEnum;
import main.enums.TeamEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.enums.DirectionEnum.DOWN;

public class Entity {

    public int worldX, worldY;
    public int backupSpeed;
    public List<BufferedImage> images = new ArrayList<>();
    public BufferedImage image, immortalAura1, immortalAura2;
    public DirectionEnum direction = DOWN;
    public int spriteCounter = 0;
    public int activityCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean isKilled = false;
    public int invincibleCounter = 0;
    public boolean alive = true;
    public boolean dying = false;
    public boolean poisoned = false;
    public boolean hpBarOn = false;
    public int counter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    //    Character Status
    public int baseSpeed;
    public int speed;
    public int baseDamageVariable;
    public int damageVariable;
    public int maxLife;
    public int life;
    public boolean isEnemy = true;
    public int ammo;
    public int maxAmmo;
    public int shootCounter = 0;
    public int shootCooldown;
    public int baseShootCooldown;
    public int ammoRegenCounter;
    public int ammoRegenCooldown;
    public int ammoRegenBasicCooldown;
    public TeamEnum team = TeamEnum.RED;
    public boolean isEntityFound = false;
    public boolean canRaycast = true;
    public boolean onPath = true;
    public Entity goalEntity;
    public int respawnCounter;
    public int baseRespawnCounter;
    public Entity flag;
    public BuffEnum currentBuff = BuffEnum.NONE;
    public int buffCounter = 0;
    public boolean hasArmor = false;
    public int moveCooldown = 1;
    GamePanel gp;


    public Entity(GamePanel gp) {
        this.gp = gp;
        getImages();
        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 10;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 27;
        solidArea.height = 27;
        maxAmmo = 6;
        ammo = maxAmmo;
        ammoRegenBasicCooldown = 240;
        ammoRegenCooldown = ammoRegenBasicCooldown;
        ammoRegenCounter = ammoRegenCooldown;
        baseDamageVariable = 1;
        damageVariable = baseDamageVariable;
        baseSpeed = 1;
        speed = baseSpeed;
        baseRespawnCounter = 900;

        if ((int) (Math.random() * 2) == 0) {
            goalEntity = gp.player;
        } else if (gp.flags != null) {
            goalEntity = gp.flags.stream().filter(x -> x.team != this.team && !x.isKilled).findFirst().orElse(gp.player);
        }
        if (gp.flags != null) {
            flag = gp.flags.stream().filter(x -> x.team == this.team && !x.isKilled).findFirst().orElse(null);
        }
    }

    public void getImages() {
        try {
            images = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/entity/enemies/black_tank_tilesheet.png"));
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

    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
        gp.cChecker.checkEntity(this, gp.barricades);
        gp.cChecker.checkEntity(this, gp.enemies);
        gp.cChecker.checkObject(this, gp.buffs);
    }

    public void update() {
        if (isKilled) {
            return;
        }

        if (!alive && !gp.respawningEnemies.contains(this)) {
            respawnCounter = baseRespawnCounter;
            gp.enemies.remove(this);
            gp.respawningEnemies.add(this);
        }

        if (!alive) {
            if (respawnCounter <= 0 && flag != null && !flag.isKilled) {
                life = maxLife;
                ammo = maxAmmo;
                alive = true;
                invincibleCounter = 20;
                gp.respawningEnemies.remove(this);
                gp.enemies.add(this);
                worldX = flag.worldX;
                worldY = flag.worldY;
                direction = DOWN;
                respawnCounter = 0;
            } else if (respawnCounter > 0 && flag != null && !flag.isKilled){
                respawnCounter--;
            }
            return;
        }

        if (canRaycast) {
            gp.raycasts.add(new Raycast(gp, this));
            canRaycast = false;
        }

        if (onPath) {
            if (goalEntity.isKilled) {
                if (goalEntity == gp.player) {
                    goalEntity = gp.flags.stream().filter(x -> x.team != this.team && !x.isKilled).findFirst().orElse(null);
                } else {
                    goalEntity = gp.player;
                }
            }

            if (goalEntity == null) {
                onPath = false;
            } else {
                int goalCol = (goalEntity.worldX + goalEntity.solidArea.x) / gp.tileSize;
                int goalRow = (goalEntity.worldY + goalEntity.solidArea.y) / gp.tileSize;

                searchPath(goalCol, goalRow);
            }
        }

        checkCollision();

        if (counter == 1) {
            if (!collisionOn) switch (direction) {
                case UP -> worldY -= speed;
                case DOWN -> worldY += speed;
                case LEFT -> worldX -= speed;
                case RIGHT -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincibleCounter != 0) {
            if (invincibleCounter < 0) {
                invincibleCounter = 0;
            }
            invincibleCounter--;
        }

        if (shootCounter > 0) {
            shootCounter--;
        } else {
            if (isEntityFound) {
                attackMethod();
                isEntityFound = false;
            }
        }

        if (ammo < maxAmmo) {
            if (ammoRegenCounter == 0) {
                ammo++;
                ammoRegenCounter = ammoRegenCooldown;
            } else {
                ammoRegenCounter--;
            }
        }

        if (counter > moveCooldown) {
            counter = 0;
        }

        counter++;

        if (buffCounter == 0 && currentBuff != BuffEnum.NONE) {
            currentBuff = BuffEnum.NONE;
            setBaseStats();
        } else buffCounter--;

    }

    public void attackMethod() {
        if (shootCounter > 0 || ammo <= 0 || dying) return;

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

        if (!alive) {
            return;
        }

        changeAlpha(g2, 1f);

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            image = null;
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

            if ((isEnemy && hpBarOn)) {
                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;
                g2.setColor(new Color(0, 0, 0));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 0));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }


            if (invincibleCounter > 0) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.6f);
            }
            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            changeAlpha(g2, 1f);
            if (gp.showHitboxes) {
                g2.setColor(Color.blue);
                g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height);
            }

            if (hasArmor){
                if (gp.armorCounter <= 6){
                    g2.drawImage(immortalAura1, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else {
                    g2.drawImage(immortalAura2, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
        }

    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 5;

        if (dyingCounter <= i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i && dyingCounter <= i * 2) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > i * 8) {
            dying = false;
            alive = false;
            dyingCounter = 0;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.pathFinder.search()) {
            int nextX = gp.pathFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pathFinder.pathList.get(0).row * gp.tileSize;

            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = DirectionEnum.UP;
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = DirectionEnum.DOWN;
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                if (enLeftX > nextX) {
                    direction = DirectionEnum.LEFT;
                }
                if (enLeftX < nextX) {
                    direction = DirectionEnum.RIGHT;
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                direction = DirectionEnum.UP;
                checkCollision();
                if (collisionOn) {
                    direction = DirectionEnum.LEFT;
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                direction = DirectionEnum.UP;
                checkCollision();
                if (collisionOn) {
                    direction = DirectionEnum.RIGHT;
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                direction = DirectionEnum.DOWN;
                checkCollision();
                if (collisionOn) {
                    direction = DirectionEnum.LEFT;
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                direction = DirectionEnum.DOWN;
                checkCollision();
                if (collisionOn) {
                    direction = DirectionEnum.RIGHT;
                }
            }
/*            int nextCol = gp.pathFinder.pathList.get(0).col;
            int nextRow = gp.pathFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow){
                onPath = false;
                ОТКЛЮЧАЕТ ПОИСК ПУТИ ПРИ ДОСТИЖЕНИИ КОНЦА
            }*/
        }
    }

    public void setBaseStats() {
        speed = baseSpeed;
        damageVariable = baseDamageVariable;
        shootCooldown = baseShootCooldown;
        ammoRegenCooldown = ammoRegenBasicCooldown;
    }
}