package main;

import entity.Barricade;
import entity.EnemyBasicTank;
import entity.EnemyHeavyTank;
import entity.Flag;
import main.enums.TeamEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class MapConverter {
    GamePanel gp;
    Color wallColor = new Color(105, 106, 106);
    Color floorColor = new Color(238, 195, 154);
    Color voidColor = new Color(0, 0, 0);
    Color redFlagColor = new Color(255, 0, 0);
    Color blueFlagColor = new Color(0, 0, 255);
    Color barricadeColor = new Color(135, 135, 135);
    Color enemyBasicTankColor = new Color(174, 95, 95);
    Color enemyHeavyTankColor = new Color(183, 42, 42);

    public MapConverter(GamePanel gp) {
        this.gp = gp;
    }

    public void convert(String mapName) throws IOException {

        gp.enemies.clear();
        gp.flags.clear();
        gp.barricades.clear();

        int mapId = Integer.parseInt(gp.currentMapName.substring(4));
        BufferedImage mapImage = ImageIO.read(getClass().getResourceAsStream("/map/mapImgs/" + mapName + ".png"));

/*        StringBuilder map = new StringBuilder();

        for (int i = 0; i < gp.maxWorldCol; i++) {
            for (int ii = 0; ii < gp.maxWorldRow; ii++) {
                Color color = new Color(mapImage.getRGB(ii, i));
                if (color.getRGB() == wallColor.getRGB()) {
                    map.append("2 ");
                }

                if (color.getRGB() == floorColor.getRGB() || color.getRGB() == redFlagColor.getRGB() || color.getRGB() == blueFlagColor.getRGB() || color.getRGB() == barricadeColor.getRGB() || color.getRGB() == enemyBasicTankColor.getRGB() || color.getRGB() == enemyHeavyTankColor.getRGB()) {

                    if (mapId > 10 ){
                        map.append("4 ");
                    } else map.append("1 ");
                }

                if (color.getRGB() == voidColor.getRGB()) {
                    map.append("3 ");
                }
            }
            map.append("\n");
        }

        File outputfile = new File("res/map/maps/" + mapName + ".txt");
        PrintWriter pw = new PrintWriter(outputfile);
        pw.write(map.toString());
        pw.close();*/

        for (int i = 0; i < gp.maxWorldCol; i++) {
            for (int ii = 0; ii < gp.maxWorldRow; ii++) {
                Color color = new Color(mapImage.getRGB(ii, i));

                if (color.getRGB() == redFlagColor.getRGB()) {
                    gp.flags.add(new Flag(gp, TeamEnum.RED, gp.tileSize * ii, gp.tileSize * i));
                }
                if (color.getRGB() == blueFlagColor.getRGB()) {
                    gp.flags.add(new Flag(gp, TeamEnum.BLUE, gp.tileSize * ii, gp.tileSize * i));
                }
                if (color.getRGB() == barricadeColor.getRGB()) {
                    gp.barricades.add(new Barricade(gp, gp.tileSize * ii, gp.tileSize * i));
                }
            }
        }

        for (int i = 0; i < gp.maxWorldCol; i++) {
            for (int ii = 0; ii < gp.maxWorldRow; ii++) {
                Color color = new Color(mapImage.getRGB(ii, i));

                if (color.getRGB() == enemyBasicTankColor.getRGB()) {
                    gp.enemies.add(new EnemyBasicTank(gp, gp.tileSize * ii, gp.tileSize * i));
                }
                if (color.getRGB() == enemyHeavyTankColor.getRGB()) {
                    gp.enemies.add(new EnemyHeavyTank(gp, gp.tileSize * ii, gp.tileSize * i));
                }
            }
        }
    }
}
