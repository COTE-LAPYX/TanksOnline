package main;

import main.enums.GameStateEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

public class UI {
    private static final DecimalFormat df = new DecimalFormat("0.0");
    public int currentServerId = 0;
    public boolean messageOn = false;
    public String message = "";
    //    BufferedImage keyImage;
    public int commandNum = 0;
    GamePanel gp;
    Font font = new Font("Areal", Font.BOLD, 40);
    Font notificationsFont = new Font("Times New Roman", Font.BOLD, 16);
    BufferedImage heart_full, heart_half, heart_blank, ammo_full, ammo_blank, armor;
    int messageScreenTime = 0;
    Graphics2D g2;
    BufferedImage coteLogo, backgroundScreen, loadingScreen;

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            armor = ImageIO.read(getClass().getResourceAsStream("/ui/armor.png"));
            heart_full = ImageIO.read(getClass().getResourceAsStream("/ui/health_full.png"));
            heart_half = ImageIO.read(getClass().getResourceAsStream("/ui/health_half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/ui/health_blank.png"));
            ammo_full = ImageIO.read(getClass().getResourceAsStream("/ui/ammo_full.png"));
            ammo_blank = ImageIO.read(getClass().getResourceAsStream("/ui/ammo_blank.png"));
            backgroundScreen = ImageIO.read(getClass().getResourceAsStream("/imgs/startingScreen.png"));
            loadingScreen = ImageIO.read(getClass().getResourceAsStream("/imgs/loadingScreen.png"));
            coteLogo = ImageIO.read(getClass().getResourceAsStream("/imgs/cat.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(font);
        g2.setColor(Color.white);
        if (gp.gameState == GameStateEnum.TITLE_STATE) {
            drawTitleScreen();
        }
        if (gp.gameState == GameStateEnum.PLAY_STATE) {
            drawPlayerLife();
            drawPlayerAmmo();
            drawScore();
        }
        if (gp.gameState == GameStateEnum.PAUSE_STATE) {
            drawPauseScreen();
            drawPlayerLife();
            drawPlayerAmmo();
        }
        if (gp.gameState == GameStateEnum.OPTION_STATE) {
            drawOptionScreen();
        }
        if (gp.gameState == GameStateEnum.LOADING_STATE) {
            drawLoadingScreen();
        }
        if (gp.gameState == GameStateEnum.GAMEOVER_STATE){
            drawGameOverScreen();
        }
        if (gp.gameState == GameStateEnum.WIN_STATE){
            drawWinScreen();
        }
        if (gp.gameState == GameStateEnum.DIFFICULTYCHOOSE_STATE){
            drawDifficultyChooseScreen();
        }

        if (messageOn) {
            if (messageScreenTime == 0) {
                messageOn = false;
            }
            g2.setColor(Color.black);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.fillRect(gp.tileSize * 15 + gp.tileSize / 2, gp.tileSize * 10, gp.tileSize * 4, gp.tileSize * 3 / 2);
            g2.setColor(Color.WHITE);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.drawRect(gp.tileSize * 15 + gp.tileSize / 2, gp.tileSize * 10, gp.tileSize * 4, gp.tileSize * 3 / 2);
            g2.setFont(notificationsFont);

            int yPos = gp.tileSize * 3;
            String[] msg = message.split("\n");
            if (msg.length > 1) {

                g2.setFont(g2.getFont().deriveFont(12f));
                for (int i = 0; i < msg.length; i++) {
                    yPos -= 12;
                    g2.drawString(msg[i], gp.tileSize * 16, gp.tileSize * 8 + yPos);
                    yPos += 24;
                }
            } else {
                g2.drawString(message, gp.tileSize * 16, gp.tileSize * 8 + yPos);
            }

            messageScreenTime--;
        }

//        Debug

        if (gp.keyH.debugMode) {
            g2.setColor(Color.WHITE);
            String text = "Debug mode";
            g2.setFont(notificationsFont);
            g2.drawString(text, gp.tileSize * 15, gp.tileSize * 6);

            text = "FPS: ";
            g2.drawString(text + gp.frames, gp.tileSize * 15, gp.tileSize * 7);

            text = "X: " + gp.player.worldX / gp.tileSize;
            g2.drawString(text, gp.tileSize * 15, gp.tileSize * 8);

            text = "Y: " + gp.player.worldY / gp.tileSize;
            g2.drawString(text, gp.tileSize * 16, gp.tileSize * 8);

            text = "speed: " + gp.player.speed;
            g2.drawString(text, gp.tileSize * 15, gp.tileSize * 9);

            text = "damage: " + gp.player.damageVariable;
            g2.drawString(text, gp.tileSize * 15, gp.tileSize * 10);

            text = "life: " + gp.player.life +"/"+ gp.player.maxLife;
            g2.drawString(text, gp.tileSize * 15, gp.tileSize * 11);
        }
    }

    private void drawScore() {
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(16f));
        g2.drawString("Total Score: " + gp.points, gp.tileSize * 16 + gp.tileSize/2, gp.tileSize * 1);
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;
//      Draw blank hearts
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, gp.tileSize, gp.tileSize, null);
            i++;
            x += gp.tileSize;
        }

        if (gp.player.hasArmor){
            g2.drawImage(armor, x, y, gp.tileSize, gp.tileSize, null);
        }

//        Reset
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

//         Draw Current hearts
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, gp.tileSize, gp.tileSize, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, gp.tileSize, gp.tileSize, null);
            }
            i++;
            x += gp.tileSize;
        }

        if (gp.player.life <= 0){
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(62f));
            g2.drawString(""+gp.player.respawnCounter/60, getXForCenter(""+gp.player.respawnCounter/60), gp.tileSize*3);
        }
    }

    public void drawPlayerAmmo() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize + 32;
        int i = 0;
//      Draw blank ammo
        while (i < gp.player.maxAmmo) {
            g2.drawImage(ammo_blank, x, y, 32, 32, null);
            i++;
            x += gp.tileSize / 2;
            if (i%40 == 0){
                y+= gp.tileSize/2;
                x = gp.tileSize/2;
            }
        }
//        Reset
        x = gp.tileSize / 2;
        y = gp.tileSize + 32;
        i = 0;

//         Draw Current ammo
        while (i < gp.player.ammo) {
            g2.drawImage(ammo_full, x, y, 32, 32, null);
            i++;
            x += gp.tileSize / 2;
            if (i%40 == 0){
                y+= gp.tileSize/2;
                x = gp.tileSize/2;
            }
        }
    }

    public void drawTitleScreen() {
        g2.drawImage(backgroundScreen, 0, 0, gp.tileSize * gp.maxScreenCol, gp.tileSize * gp.maxScreenRow, null);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 68));
        String text = "Tanks";
        int x = getXForCenter(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "InDev " + gp.gameVersion;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12F));
        g2.drawString(text, gp.tileSize * 18, (gp.tileSize * 23) / 2);

//        image

        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize * 2;
        //g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2.drawImage(coteLogo, 0, gp.tileSize * 11, gp.tileSize, gp.tileSize, null);
        //g2.drawImage(image2, 0, gp.tileSize * 11, gp.tileSize, gp.tileSize, null);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
        g2.drawString("creator", gp.tileSize + 6, gp.tileSize * 11 + gp.tileSize / 2);
        g2.drawString("COTE_LAPYX", gp.tileSize + 6, gp.tileSize * 11 + gp.tileSize / 2 + 15);


//        Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "New Game";
        x = getXForCenter(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x + gp.tileSize * 5, y);
        }

        text = "Settings";
        x = getXForCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        g2.setColor(Color.white);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x * 3 / 1.85f - gp.tileSize, y);
        }
        g2.setColor(Color.lightGray);
        text = "---------";
        x = getXForCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x + gp.tileSize * 3, y);
        }
        g2.setColor(Color.white);
        text = "Quit";
        x = getXForCenter(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 3) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x + gp.tileSize * 2, y);
        }
    }

    public void drawOptionScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.drawRect(gp.tileSize * 2, gp.tileSize * 2, gp.tileSize * 16, gp.tileSize * 8);

        if (commandNum == 0) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.setFont(notificationsFont);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        g2.drawString("Full Screen", gp.tileSize * 2.3f, gp.tileSize * 3);
        if (gp.isFullScreenOn) {
            g2.drawString("On", gp.tileSize * 7, gp.tileSize * 3);
        } else {
            g2.drawString("Off", gp.tileSize * 7, gp.tileSize * 3);
        }

        if (commandNum == 1) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Volume", gp.tileSize * 2.3f, gp.tileSize * 4);
        g2.drawString("" + gp.volumeValue, gp.tileSize * 7, gp.tileSize * 4);

        if (commandNum == 2) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Spawn Buffs", gp.tileSize * 2.3f, gp.tileSize * 5);

        if (gp.spawnBuffs) {
            g2.drawString("On", gp.tileSize * 7, gp.tileSize * 5);
        } else {
            g2.drawString("Off", gp.tileSize * 7, gp.tileSize * 5);
        }


        if (commandNum == 3) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Confirm", gp.tileSize * 2.3f, gp.tileSize * 9);
    }

    public void drawPauseScreen() {

        String text = "PAUSE MENU";
        int x;
        x = getXForCenter(text);
        int y = gp.tileSize * 2;
        g2.drawString(text, x, y);

        g2.setColor(Color.black);
        g2.fillRect((int) (gp.tileSize * 6.5f), gp.tileSize * 4, gp.tileSize * 7, gp.tileSize * 6);
        g2.setColor(Color.white);
        g2.drawRect((int) (gp.tileSize * 6.5f), gp.tileSize * 4, gp.tileSize * 7, gp.tileSize * 6);


        text = "Resume";
        x = getXForCenter(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x * 3 / 1.9f - gp.tileSize, y);
        }

        text = "Settings";
        x = getXForCenter(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x * 3 / 1.9f - gp.tileSize, y);
        }

        text = "Main Menu";
        x = getXForCenter(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
            g2.drawString("<", x * 3 / 1.75f - gp.tileSize, y);
        }

        g2.setFont(g2.getFont().deriveFont(10f));
        g2.drawString("vol: " + df.format(gp.volumeValue), gp.tileSize * 14, gp.tileSize);
    }


    public int getXForCenter(String text) {
        int x;
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private void drawLoadingScreen() {
        g2.drawImage(loadingScreen, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.setColor(Color.BLACK);
        g2.drawString("Loading...", getXForCenter("Loading..."), gp.tileSize * 5);


        if (gp.loadingTimer != 0) {
            int i = 60 / gp.loadingTimer;
            double oneScale = (double) gp.tileSize / 60;
            double barValue = oneScale * i;
            g2.setColor(Color.WHITE);
            g2.fillRect(gp.tileSize, gp.tileSize * 10, (int) barValue * 12, 12);
        }
    }

    public void drawGameOverScreen() {
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 68F));
        g2.drawString("Game Over", getXForCenter("Game Over"), gp.tileSize*2);

        g2.setColor(Color.white);
        g2.setFont(notificationsFont);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        g2.drawString("Score: " + gp.pointsToGet, getXForCenter("Score: " + gp.pointsToGet), gp.tileSize * 4);

        if (commandNum == 0) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        g2.drawString("Retry", getXForCenter("Retry"), gp.tileSize * 7);

        if (commandNum == 1) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Main Menu", getXForCenter("Main Menu"), gp.tileSize * 9);
    }

    public void drawWinScreen() {
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 68F));
        g2.drawString("Level finished!", getXForCenter("Level finished!"), gp.tileSize*2);

        g2.setColor(Color.white);
        g2.setFont(notificationsFont);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        g2.drawString("Score: " + gp.pointsToGet, getXForCenter("Score: " + gp.pointsToGet), gp.tileSize * 4);

        if (commandNum == 0) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        g2.drawString("Next", getXForCenter("Next"), gp.tileSize * 7);

        if (commandNum == 1) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Main Menu", getXForCenter("Main Menu"), gp.tileSize * 9);
    }

    public void drawDifficultyChooseScreen() {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 68F));
        g2.drawString("Choose Difficulty", getXForCenter("Choose Difficulty"), gp.tileSize*2);

        if (commandNum == 0) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.setFont(notificationsFont);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        g2.drawString("Easy", getXForCenter("Easy"), gp.tileSize * 7);

        if (commandNum == 1) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Medium", getXForCenter("Medium"), gp.tileSize * 9);

        if (commandNum == 2) {
            g2.setColor(Color.orange);
        } else g2.setColor(Color.white);
        g2.drawString("Hard", getXForCenter("Hard"), gp.tileSize * 11);
    }
}