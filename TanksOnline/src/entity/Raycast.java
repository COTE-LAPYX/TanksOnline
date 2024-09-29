package entity;

import main.GamePanel;

import java.awt.*;

public class Raycast extends Entity {
    public Entity owner;
    public int maxRange;
    public int range;
    public int reactionTime = 10; // 10
    public Entity newGoal = null;

    public Raycast(GamePanel gp, Entity owner) {
        super(gp);
        worldX = owner.worldX;
        worldY = owner.worldY;
        solidArea = new Rectangle(16, 16, 16, 16);

        this.owner = owner;
        direction = owner.direction;
        maxRange = 24;
        range = maxRange;
        speed = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        reactionTime = switch (gp.difficulty) {
            case EASY -> 20;
            case MEDIUM -> 10;
            case HARD -> 0;
        };
    }

    @Override
    public void getImages() {
    }

    @Override
    public void update() {

        if (reactionTime != 0) {
            reactionTime--;
            return;
        }
        newGoal = null;

        if (owner.direction != direction) {
            owner.canRaycast = true;
            owner.isEntityFound = false;
            gp.raycasts.remove(this);
            return;
        }

        if (owner.team != gp.player.team) {
            owner.isEntityFound = gp.cChecker.checkPlayer(this);
            if (owner.goalEntity != gp.player && owner.isEntityFound) {
                newGoal = gp.player;
            }
        }


        int id = gp.cChecker.checkObject(this, gp.buffs);
        if (id != 999) {
            newGoal = gp.buffs.get(id);
        }


        id = gp.cChecker.checkEntity(this, gp.barricades);

        if (id != 999 && !gp.barricades.get(id).isKilled) {
            owner.isEntityFound = true;
        }

        id = gp.cChecker.checkEntity(this, gp.flags);

        if (id != 999 && !gp.flags.get(id).isKilled && gp.flags.get(id).team != owner.team) {
            owner.isEntityFound = true;
        }

        collisionOn = false;

        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case LEFT -> worldX -= 32;
                case DOWN -> worldY += 32;
                case UP -> worldY -= 32;
                case RIGHT -> worldX += 32;
            }
        } else {
            newGoal = null;
            owner.canRaycast = true;
            owner.isEntityFound = false;
            gp.raycasts.remove(this);
            return;
        }
        range--;
        if (range == 0) {
            newGoal = null;
            owner.canRaycast = true;
            owner.isEntityFound = false;
            gp.raycasts.remove(this);
        }

        if (newGoal != null) {
            owner.goalEntity = newGoal;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
    }
}
