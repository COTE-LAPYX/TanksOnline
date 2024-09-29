package main;

import entity.Entity;
import entity.object.Object;

import java.util.List;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;
        try {


            switch (entity.direction) {
                case UP -> {
                    entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
                case DOWN -> {
                    entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
                case LEFT -> {
                    entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
                case RIGHT -> {
                    entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public int checkEntity(Entity entity, List<Entity> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case UP -> entity.solidArea.y -= entity.speed;
                    case DOWN -> entity.solidArea.y += entity.speed;
                    case LEFT -> entity.solidArea.x -= entity.speed;
                    case RIGHT -> entity.solidArea.x += entity.speed;
                }
                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    if (target.get(i) != entity) {
                        if (!target.get(i).isKilled) {
                            entity.collisionOn = true;
                        }
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {
        if (gp.player.isKilled) return false;

        boolean collision = false;
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case UP -> {
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    collision = true;
                }
            }
            case DOWN -> {
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    collision = true;
                }
            }
            case LEFT -> {
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    collision = true;
                }
            }
            case RIGHT -> {
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    collision = true;
                }
            }
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return collision;
    }

    public int checkObject(Entity entity, List<Object> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case UP -> entity.solidArea.y -= entity.speed;
                    case DOWN -> entity.solidArea.y += entity.speed;
                    case LEFT -> entity.solidArea.x -= entity.speed;
                    case RIGHT -> entity.solidArea.x += entity.speed;
                }
                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    if (target.get(i) != entity) {
                        if (!gp.raycasts.contains(entity)){
                            target.get(i).isKilled = true;
                            target.get(i).use(entity);
                        }
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }

        return index;
    }
}
