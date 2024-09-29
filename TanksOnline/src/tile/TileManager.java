package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    public List<Tile> tile;
    public int[][] mapTileNum;
    public int animationCounter = 60;
    GamePanel gp;
    boolean drawPath = false;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new ArrayList<>();
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
    }

    public void getTileImage() {

        try {

            tile.add(0, new Tile());
            tile.get(0).animation.add(ImageIO.read(getClass().getResourceAsStream("/tiles/whitevoid.png")));
            tile.get(0).collision = true;

            tile.add(1, new Tile());
            tile.get(1).animation.add(ImageIO.read(getClass().getResourceAsStream("/tiles/floor.png")));

            tile.add(2, new Tile());
            tile.get(2).animation.add(ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png")));
            tile.get(2).collision = true;

            tile.add(3, new Tile());
            tile.get(3).animation.add(ImageIO.read(getClass().getResourceAsStream("/tiles/void.png")));
            tile.get(3).collision = true;

            tile.add(4, new Tile());
            tile.get(4).animation.add(ImageIO.read(getClass().getResourceAsStream("/tiles/floor2.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;


        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tile.get(tileNum).animation.get(tile.get(tileNum).currentImageIndex), screenX, screenY, gp.tileSize, gp.tileSize, null);
            }


            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath){
            g2.setColor(new Color(255, 0, 0, 70));

            for (int i = 0; i< gp.pathFinder.pathList.size(); i++){
                int worldX = gp.pathFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pathFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }

    public void next() {

        for (int tileNum = 0; tileNum < tile.size(); tileNum++){
            if (tile.get(tileNum).animation.size() == 1) continue;
            tile.get(tileNum).currentImageIndex++;
            if (tile.get(tileNum).currentImageIndex >= tile.get(tileNum).animation.size()) {
                tile.get(tileNum).currentImageIndex = 0;
            }
        }
    }
}
