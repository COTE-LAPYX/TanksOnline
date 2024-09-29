package main;

import entity.Barricade;
import entity.EnemyBasicTank;
import entity.EnemyHeavyTank;
import entity.Flag;
import main.enums.DifficultyEnum;
import main.enums.DirectionEnum;
import main.enums.TeamEnum;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setEnemies(String mapName) {
        gp.enemies.clear();
        if (mapName.equals("map01")) {
            gp.enemies.add(new EnemyHeavyTank(gp, gp.tileSize * 4, gp.tileSize));
            //gp.enemies.get(0).maxLife = 4;
            if (gp.difficulty == DifficultyEnum.HARD) {
                gp.enemies.get(0).maxLife++;
            } else if (gp.difficulty == DifficultyEnum.EASY) {
                gp.enemies.get(0).maxLife--;
            }
            gp.enemies.get(0).life = gp.enemies.get(0).maxLife;
            gp.enemies.get(0).direction = DirectionEnum.DOWN;

            if (gp.difficulty != DifficultyEnum.EASY) {
                gp.enemies.add(new EnemyBasicTank(gp, gp.tileSize * 6, gp.tileSize));
                gp.enemies.get(1).maxLife = 4;
                gp.enemies.get(1).life = gp.enemies.get(1).maxLife;
                gp.enemies.get(1).direction = DirectionEnum.DOWN;
            }
        }
        if (mapName.equals("map02")) {
            gp.enemies.add(new EnemyBasicTank(gp, gp.tileSize * 5, gp.tileSize));
            gp.enemies.get(0).maxLife = 4;
            if (gp.difficulty == DifficultyEnum.HARD) {
                gp.enemies.get(0).maxLife++;
            } else if (gp.difficulty == DifficultyEnum.EASY) {
                gp.enemies.get(0).maxLife--;
            }
            gp.enemies.get(0).life = gp.enemies.get(0).maxLife;
            gp.enemies.get(0).direction = DirectionEnum.DOWN;

            gp.enemies.add(new EnemyBasicTank(gp, gp.tileSize * 12, gp.tileSize));
            gp.enemies.get(1).maxLife = 4;
            if (gp.difficulty == DifficultyEnum.HARD) {
                gp.enemies.get(1).maxLife++;
            } else if (gp.difficulty == DifficultyEnum.EASY) {
                gp.enemies.get(1).maxLife--;
            }
            gp.enemies.get(1).life = gp.enemies.get(0).maxLife;
            gp.enemies.get(1).direction = DirectionEnum.DOWN;
        }
    }

    public void setObjects(String mapName) {
        gp.barricades.clear();
        gp.flags.clear();

        if (mapName.equals("map01")) {
            gp.barricades.add(new Barricade(gp, gp.tileSize * 2, gp.tileSize));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 8, gp.tileSize * 2));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 8, gp.tileSize * 4));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 3, gp.tileSize * 6));
            gp.barricades.add(new Barricade(gp, gp.tileSize, gp.tileSize * 7));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 7, gp.tileSize * 8));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 4, gp.tileSize * 5));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 5, gp.tileSize * 4));

            gp.flags.add(new Flag(gp, TeamEnum.RED, gp.tileSize * 4 + gp.tileSize / 2, gp.tileSize));
            gp.flags.add(new Flag(gp, TeamEnum.BLUE, gp.tileSize * 5 - gp.tileSize / 2, gp.tileSize * 8));
        } else if (mapName.equals("map02")) {
            gp.barricades.add(new Barricade(gp, gp.tileSize, gp.tileSize));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 4, gp.tileSize * 2));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 10, gp.tileSize * 2));
            gp.barricades.add(new Barricade(gp, gp.tileSize, gp.tileSize * 4));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 6, gp.tileSize * 4));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 7, gp.tileSize * 6));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 12, gp.tileSize * 6));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 3, gp.tileSize * 8));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 9, gp.tileSize * 9));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 10, gp.tileSize * 9));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 4, gp.tileSize * 10));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 2, gp.tileSize * 11));
            gp.barricades.add(new Barricade(gp, gp.tileSize * 10, gp.tileSize * 11));

            gp.flags.add(new Flag(gp, TeamEnum.RED, gp.tileSize * 7, gp.tileSize));
            gp.flags.add(new Flag(gp, TeamEnum.BLUE, gp.tileSize * 7, gp.tileSize * 12));
        }
    }
}
