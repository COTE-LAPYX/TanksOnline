package entity;

import main.GamePanel;
import main.enums.DifficultyEnum;

public class EnemyBasicTank extends Entity{
    public EnemyBasicTank(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        maxLife = 4;
        life = maxLife;

        baseShootCooldown = 60;
        shootCooldown = baseShootCooldown;

        if (gp.difficulty == DifficultyEnum.EASY) {
            baseRespawnCounter += 300;
            maxLife--;
            life = maxLife;
        }

        if (gp.difficulty == DifficultyEnum.HARD) {
            moveCooldown += 1;
            baseDamageVariable = 2;
            damageVariable = baseDamageVariable;
            baseSpeed = 2;
            speed = baseSpeed;
            baseShootCooldown = 40;
            shootCooldown = baseShootCooldown;
        }

    }
}
