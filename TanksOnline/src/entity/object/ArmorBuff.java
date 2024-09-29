package entity.object;

import entity.Entity;
import main.GamePanel;
import main.enums.BuffEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ArmorBuff extends Object {
    GamePanel gp;

    public ArmorBuff(GamePanel gp) {
        super(gp);
        getImages();

        this.gp = gp;

        baseLifeCounter = 1200;
        lifeCounter = baseLifeCounter;
    }

    @Override
    public void getImages() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entity/object/buff/armor.png"));
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
            user.hasArmor = true;
            deleteOnUpdate = true;
            gp.playSE(0);
        }
    }