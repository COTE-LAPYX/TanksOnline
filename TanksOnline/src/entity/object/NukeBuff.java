package entity.object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class NukeBuff extends Object {
    GamePanel gp;

    public NukeBuff(GamePanel gp) {
        super(gp);
        getImages();

        this.gp = gp;

        baseLifeCounter = 1200;
        lifeCounter = baseLifeCounter;
    }

    @Override
    public void getImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entity/object/buff/nuke.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }

    @Override
    public void use(Entity user) {
        gp.playSE(8);
        gp.nukeCounter = 10;
        if (user.team != gp.player.team) {
            gp.player.hasArmor = false;
            gp.player.life = 0;
            gp.player.isKilled = true;
            gp.player.respawnCounter = gp.player.baseRespawnCounter;
            gp.pointsToGet -= 15;

        } else {
            for (int i = 0; i < gp.enemies.size(); i++) {
                gp.enemies.get(i).dying = true;
                gp.pointsToGet += 5;
            }
        }
        deleteOnUpdate = true;
    }
}