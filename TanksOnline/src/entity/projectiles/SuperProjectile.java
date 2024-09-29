package entity.projectiles;

import entity.Entity;
import gfx.ExplosionGraphicalEffect;
import main.GamePanel;
import main.enums.DifficultyEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuperProjectile extends Entity {
    public List<BufferedImage> animation;
    public Entity owner;
    GamePanel gp;

    public SuperProjectile(GamePanel gp, Entity owner) {
        super(gp);
        this.gp = gp;
        this.owner = owner;
        direction = owner.direction;
        maxLife = 240; // 240
        life = maxLife;
        baseSpeed = 6; // 6
        speed = baseSpeed;
        backupSpeed = speed;
        damageVariable = owner.damageVariable;
        getProjectileImages();
        solidArea = new Rectangle(22, 22, 6, 6);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void getProjectileImages() {
        try {
            animation = new ArrayList<>();

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/entity/projectiles/tank_projectile_tilesheet.png"));


            for (int y = 0; y < tileSheet.getHeight() / 16; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 16; x++) {
                    animation.add(tileSheet.getSubimage(x * 16, y * 16, 16, 16));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (owner == gp.player) {
            damageEntity(gp.cChecker.checkEntity(this, gp.enemies), gp.enemies);
        } else if (gp.cChecker.checkPlayer(this)) {
            damagePlayer();
        }
        damageEntity(gp.cChecker.checkEntity(this, gp.barricades), gp.barricades);
        int flagId = gp.cChecker.checkEntity(this, gp.flags);
        if (flagId != 999) {
            if (gp.flags.get(flagId).team != owner.team) {
                damageEntity(flagId, gp.flags);
            } else {
                collisionOn = false;
            }
        }

        if (collisionOn) {
            ExplosionGraphicalEffect gfx = new ExplosionGraphicalEffect(gp);
            gfx.worldX = worldX;
            gfx.worldY = worldY;
            gp.effects.add(gfx);
            life = 0;
        }

        if (!collisionOn) switch (direction) {
            case UP -> worldY -= speed;
            case DOWN -> worldY += speed;
            case LEFT -> worldX -= speed;
            case RIGHT -> worldX += speed;
        }
    }

    public void damageEntity(int i, List<Entity> entities) {
        if (i != 999) {
            if (entities.get(i).invincibleCounter == 0 && !entities.get(i).isKilled) {
                gp.playSE(1);

                if (entities.get(i).hasArmor) {
                    entities.get(i).hasArmor = false;
                    entities.get(i).invincibleCounter = 20;
                    return;
                }

                if (gp.enemies.contains(entities.get(i))){
                    if (owner == gp.player){
                        gp.pointsToGet += 5;
                    }
                    if (entities.get(i).life <= damageVariable) {
                        entities.get(i).dying = true;
                        if (owner == gp.player){
                            gp.pointsToGet += 10;
                        }
                    } else {
                        entities.get(i).life -= damageVariable;
                        entities.get(i).invincibleCounter = 30;
                    }
                    entities.get(i).goalEntity = owner;
                } else {
                    if (gp.barricades.contains(entities.get(i)) && owner == gp.player){
                        gp.pointsToGet += 5;
                    } else if (gp.flags.contains(entities.get(i))){
                        if (owner == gp.player) gp.pointsToGet += 15;
                        if (owner != gp.player) gp.pointsToGet -= 25;
                    }
                    if (entities.get(i).life <= 1) {
                        entities.get(i).dying = true;
                    } else {
                        entities.get(i).life -= 1;
                        entities.get(i).invincibleCounter = 10;
                    }
                }
            }
        }
    }

    public void damagePlayer() {
        if (gp.player.invincibleCounter == 0 && !gp.player.isKilled) {
            gp.playSE(2);

            if (gp.player.hasArmor) {
                gp.player.hasArmor = false;
                gp.player.invincibleCounter = 40;
                return;
            }

            gp.player.life -= damageVariable;
            gp.player.invincibleCounter = 60;
            if (gp.player.life <= 0) {
                gp.player.isKilled = true;
                gp.player.respawnCounter = gp.player.baseRespawnCounter;
                gp.pointsToGet -= 15;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            image = null;

            switch (direction) {
                case UP -> image = animation.get(0);
                case RIGHT -> image = animation.get(1);
                case DOWN -> image = animation.get(2);
                case LEFT -> image = animation.get(3);
            }

            g2.drawImage(image, screenX + 16, screenY + 16, image.getWidth(), image.getHeight(), null);
            changeAlpha(g2, 1f);
            if (gp.showHitboxes) {
                g2.setColor(Color.blue);
                g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
        }
    }
}
