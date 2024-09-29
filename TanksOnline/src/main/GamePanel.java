package main;


import entity.Entity;
import entity.Player;
import entity.Raycast;
import entity.object.Object;
import entity.object.*;
import entity.projectiles.SuperProjectile;
import gfx.SuperGraphicalEffect;
import main.ai.PathFinder;
import main.enums.DifficultyEnum;
import main.enums.GameStateEnum;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    public final int scale = 3; // 3
    public final int maxScreenCol = 20; //16
    //    Screen settings
    public final int maxScreenRow = 12; //12
    //    World Setting
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    //    fps
    public final String gameVersion = "0.9";
    final int originalTileSize = 16; // 16x16
    public final int tileSize = originalTileSize * scale;
    public final int screenWidth = tileSize * maxScreenCol;
    //FUll screen
    int screenWidth2 = screenWidth;
    public final int screenHeight = tileSize * maxScreenRow;
    int screenHeight2 = screenHeight;
    public boolean canDebug = false;
    public int FPS = 60;
    public double drawInterval;
    public int frames;

    //    Classes
    public List<SuperProjectile> projectiles = new ArrayList<>();
    public Data data = new Data(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    //    Entity
    public Player player = new Player(this, keyH);
    public List<Entity> enemies = new ArrayList<>();
    public List<Entity> respawningEnemies = new ArrayList<>();
    public List<Entity> barricades = new ArrayList<>();
    public List<Entity> flags = new ArrayList<>();
    public List<SuperGraphicalEffect> effects = new ArrayList<>();
    //    game state
    public GameStateEnum gameState;
    public float volumeValue = 0;
    public String currentMapName = "map01";
    public boolean showHitboxes = false;
    public int loadingTimer = 0;
    public TileManager tileM = new TileManager(this);
    public MouseHandler mouseHandler = new MouseHandler(this);
    public List<Raycast> raycasts = new ArrayList<>();
    public PathFinder pathFinder = new PathFinder(this);
    public DifficultyEnum difficulty = DifficultyEnum.HARD;
    Sound music = new Sound(this);
    Sound soundEffect = new Sound(this);
    Thread gameThread;
    long timer = 0;
    int drawCount = 0;
    double delta = 0;
    //          features
    BufferedImage tempScreen;
    Graphics2D g2;
    boolean isFullScreenOn = false;
    public int points = 0;
    public int pointsPerLevel = 100;
    public int pointsToGet;
    public List<Object> buffs = new ArrayList<>();
    public int buffSpawnCounter = 1800; //3600
    public boolean spawnBuffs = false;
    public boolean spawnBuffsOnMap = false;
    public MapConverter converter = new MapConverter(this);
    public float fullScreenOffsetFactor = 0f;
    public int armorCounter = 0;
    public int nukeCounter = 0;
    public GameStateEnum beforeOptionState = GameStateEnum.TITLE_STATE;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.setFocusable(true);
    }

    public void setUpGame() throws IOException {
        gameState = GameStateEnum.TITLE_STATE;
        playMusic(7);

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if (isFullScreenOn) {
            setFullScreen();
        }

        try {
            data.loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFullScreen() {
        if (isFullScreenOn) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            screenWidth2 = (int) width;
            screenHeight2 = (int) height;
            /*//offset factor to be used by mouse listener or mouse motion listener if you are using cursor in your game. Multiply your e.getX()e.getY() by this.*/
            fullScreenOffsetFactor = (float) screenWidth / (float) screenWidth2;
        } else {
            screenWidth2 = screenWidth;
            screenHeight2 = screenHeight;
            Main.window.setSize(screenWidth2, screenHeight2);
            Main.window.setLocationRelativeTo(null);
        }
    }

    public void startGameThread() {
        try {
            gameThread = new Thread(this);
            gameThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        drawInterval = 1000000000 / FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                //new method
                drawToTempScreen();
                drawToScreen();
                /*repaint(); OLD METHOD*/
                delta--;
                drawCount++;
            }
            if (timer > 1000000000) {
                frames = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        if (gameState == GameStateEnum.PLAY_STATE) {

            if (data.coolDown > 0) {
                data.coolDown--;
            }

            trySpawnRandomBuff();

            for (int j = 0; j < raycasts.size(); j++) {
                Entity entity = raycasts.get(j);
                if (entity != null) {
                    entity.update();
                }
            }

            for (int j = 0; j < buffs.size(); j++) {
                Object entity = buffs.get(j);
                if (entity != null) {
                    entity.update();
                }
            }

            for (int j = 0; j < barricades.size(); j++) {
                Entity entity = barricades.get(j);
                if (entity != null) {
                    entity.update();
                }
            }

            boolean isEnemyFlagBroken = false;
            boolean isPlayerFlagBroken = false;

            if (flags.size() == 1){
                if (flags.get(0).team == player.team){
                    isEnemyFlagBroken = true;
                } else {
                    isPlayerFlagBroken = true;
                }
            }

            for (int j = 0; j < flags.size(); j++) {
                Entity entity = flags.get(j);
                if (entity != null) {
                    entity.update();
                    if (entity.team == player.team && entity.isKilled) {
                        isPlayerFlagBroken = true;
                    } else if (entity.team != player.team && entity.isKilled) {
                        isEnemyFlagBroken = true;
                    }
                }
            }

            player.update();

            for (int j = 0; j < enemies.size(); j++) {
                Entity entity = enemies.get(j);
                if (entity != null) {
                    entity.update();
                }
            }

            for (int j = 0; j < respawningEnemies.size(); j++) {
                Entity entity = respawningEnemies.get(j);
                if (entity != null) {
                    entity.update();
                }
            }

            for (int j = 0; j < projectiles.size(); j++) {
                SuperProjectile projectile = projectiles.get(j);
                if (projectile != null) {
                    projectile.update();
                    projectile.life--;
                    if (projectile.life <= 0) {
                        projectiles.remove(projectile);
                    }
                }
            }

            for (int j = 0; j < effects.size(); j++) {
                SuperGraphicalEffect effect = effects.get(j);
                if (effect != null) {
                    effect.update();
                    effect.life--;
                    if (effect.life <= 0) {
                        effects.remove(effect);
                    }
                }
            }

            if (isPlayerFlagBroken && player.life <= 0) {
                gameState = GameStateEnum.GAMEOVER_STATE;
                givePoints(false);
                stopMusic();
                playSE(10);
            } else if (isEnemyFlagBroken && enemies.size() == 0) {
                gameState = GameStateEnum.WIN_STATE;
                givePoints(true);
                stopMusic();
                playSE(9);
            }
        }

        if (tileM.animationCounter >= 60) {
            tileM.next();
            tileM.animationCounter = 0;
        } else {
            tileM.animationCounter++;
        }
        if (gameState == GameStateEnum.PAUSE_STATE) {
        }
        if (gameState == GameStateEnum.LOADING_STATE) {
            if (loadingTimer > 0) {
                if (loadingTimer == 10){
                    tileM.loadMap("/map/maps/" + currentMapName + ".txt");
                }
                loadingTimer--;
            } else {
                gameState = GameStateEnum.PLAY_STATE;
                playMusic(6);
            }
        }

        if (armorCounter >= 12){
            armorCounter = 0;
        } else armorCounter++;

        if (nukeCounter > 0){
            nukeCounter --;
        }
    }


    public void drawToScreen() {
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(tempScreen, 5 * nukeCounter, 5 * nukeCounter, screenWidth2, screenHeight2, null); // nuke counter for nuke camera shake
            g.dispose();
        }
    }


    public void drawToTempScreen() {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        if (gameState == GameStateEnum.TITLE_STATE || gameState == GameStateEnum.OPTION_STATE || gameState == GameStateEnum.LOADING_STATE || gameState == GameStateEnum.DIFFICULTYCHOOSE_STATE) {
            ui.draw(g2);
        } else {

            tileM.draw(g2);

            for (Entity entity : barricades) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }

            for (Entity entity : flags) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }

            for (Object entity : buffs) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }

            player.draw(g2);

            for (Entity entity : enemies) {
                if (entity != null) {
                    entity.draw(g2);
                }
            }

            for (SuperProjectile projectile : projectiles) {
                if (projectile != null) {
                    projectile.draw(g2);
                }
            }

            for (SuperGraphicalEffect effect : effects) {
                if (effect != null) {
                    effect.draw(g2);
                }
            }
            ui.draw(g2);
        }
    }

    public void loadMapAndEntity(String path, String mapName) {
        spawnBuffsOnMap = spawnBuffs;
        pointsToGet = 0;
        loadingTimer = 60;
        buffSpawnCounter = 1800;
        gameState = GameStateEnum.LOADING_STATE;

        currentMapName = mapName;

        try {
            converter.convert(mapName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buffs.clear();
        projectiles.clear();
        raycasts.clear();
        effects.clear();
        respawningEnemies.clear();

        //aSetter.setObjects(currentMapName);

        //aSetter.setEnemies(currentMapName);

        if (flags != null) {
            player.flag = flags.stream().filter(x -> x.team == player.team && !x.isKilled).findFirst().orElse(null);
        }

        player.worldX = player.flag.worldX;
        player.worldY = player.flag.worldY;
    }


    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void continueMusic() {

        music.play();
        music.loop();
    }

    public void stopMusic() {

        music.stop();
    }


    public void playSE(int i) {
        soundEffect.setFile(i);
        soundEffect.play();
    }

    public void givePoints(boolean win){
        int p;

        if (win) p = pointsToGet + pointsPerLevel;
        else p = pointsToGet / 2;

        if (p < 0) p = 0;

        switch (difficulty){
            case EASY -> {}
            case MEDIUM -> p = p*2;
            case HARD -> p = p*3;
        }
        points += p;
        pointsToGet = p;

        try {
            data.saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void trySpawnRandomBuff(){
        if (!spawnBuffsOnMap) return;

        if (buffSpawnCounter > 0){
            buffSpawnCounter--;
            return;
        }

        buffSpawnCounter = 1800;

        int randomBuff = (int) (Math.random() * 6);

        switch (randomBuff) {
            case 0 -> buffs.add(new SpeedBuff(this));
            case 1 -> buffs.add(new ArmorBuff(this));
            case 2 -> buffs.add(new DamageBuff(this));
            case 3 -> buffs.add(new NukeBuff(this));
            case 4 -> buffs.add(new ReloadSpeedBuff(this));
            case 5 -> buffs.add(new ShootSpeedBuff(this));
        }
    }
}
