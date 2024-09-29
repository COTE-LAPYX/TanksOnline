package entity.object;

import entity.Entity;
import main.GamePanel;
import main.enums.BuffEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ShootSpeedBuff extends Object {
    GamePanel gp;

    public ShootSpeedBuff(GamePanel gp) {
        super(gp);
        getImages();

        this.gp = gp;

        baseLifeCounter = 1200;
        lifeCounter = baseLifeCounter;
    }

    @Override
    public void getImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entity/object/buff/shootspeed_buff.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
        public void update () {
            super.update();
        }

        @Override
        public void draw (Graphics2D g2){
            super.draw(g2);
        }

        @Override
        public void use (Entity user){
            user.setBaseStats();
            user.currentBuff = BuffEnum.SHOOTSPEED;
            user.buffCounter = 1200;
            user.shootCooldown = user.shootCooldown/2;
            deleteOnUpdate = true;
            gp.playSE(0);
        }
    }