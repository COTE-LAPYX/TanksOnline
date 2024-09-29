package entity.object;

import entity.Entity;
import main.GamePanel;
import main.enums.DirectionEnum;

import java.awt.*;

public class Object extends Entity {
    int baseLifeCounter;
    int lifeCounter;
    GamePanel gp;
    boolean deleteOnUpdate = false;

    public Object(GamePanel gp) {
        super(gp);
        baseLifeCounter = 120;
        lifeCounter = baseLifeCounter;
        direction = DirectionEnum.DOWN;
        baseSpeed = 0;
        speed = baseSpeed;
        this.gp = gp;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        worldX = gp.tileSize * (int) (Math.random() * gp.maxWorldCol);
        worldY = gp.tileSize * (int) (Math.random() * gp.maxWorldRow);
    }

    @Override
    public void getImages() {
    }

    @Override
    public void update() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.barricades);


        while (collisionOn) {
            collisionOn = false;
            worldX = gp.tileSize * (int) (Math.random() * 13);
            worldY = gp.tileSize * (int) (Math.random() * 13);
            gp.cChecker.checkTile(this);
            gp.cChecker.checkEntity(this, gp.barricades);
        }

        if (lifeCounter <= 0) {
            gp.buffs.remove(this);
        } else {
            lifeCounter--;
        }

        if (deleteOnUpdate){
            gp.buffs.remove(this);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            if (gp.showHitboxes) {
                g2.setColor(Color.blue);
                g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height);
            }
        }
    }

    public void use(Entity user) {
        deleteOnUpdate = true;
    }
}
