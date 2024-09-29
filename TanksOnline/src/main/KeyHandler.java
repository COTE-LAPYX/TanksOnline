package main;

import main.enums.DifficultyEnum;
import main.enums.GameStateEnum;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, eKeyPressed, qKeyPressed, mKeyPressed, cKeyPressed, gKeyPressed;
    public boolean debugMode = false;
    public boolean specialButtonPressed = false;

    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        empty
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (debugMode) {
            System.out.println("------------------------------");
            System.out.println(KeyEvent.getKeyText(code));
            System.out.println("------------------------------");
        }

//        Title State
        if (gp.gameState == GameStateEnum.TITLE_STATE) {
            if (code == KeyEvent.VK_W) {
                gp.playSE(3);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.playSE(3);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(3);
                if (gp.ui.commandNum == 0) {
                    gp.gameState = GameStateEnum.DIFFICULTYCHOOSE_STATE;
                }
                if (gp.ui.commandNum == 1) {
                    gp.beforeOptionState = GameStateEnum.TITLE_STATE;
                    gp.gameState = GameStateEnum.OPTION_STATE;
                    gp.stopMusic();
                }
                if (gp.ui.commandNum == 2) {
                    //#todo free button
                }
                if (gp.ui.commandNum == 3) {
                    System.exit(0);
                }
            }
        } else

//        play state
            if (gp.gameState == GameStateEnum.PLAY_STATE) {
                if (code == KeyEvent.VK_W) {
                    upPressed = true;
                }
                if (code == KeyEvent.VK_S) {
                    downPressed = true;
                }
                if (code == KeyEvent.VK_A) {
                    leftPressed = true;
                }
                if (code == KeyEvent.VK_D) {
                    rightPressed = true;
                }
                if (code == KeyEvent.VK_E) {
                    eKeyPressed = true;
                }
                if (code == KeyEvent.VK_Q) {
                    qKeyPressed = true;
                }
                if (code == KeyEvent.VK_SPACE) {
                    gp.player.attackMethod();
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    gp.gameState = GameStateEnum.PAUSE_STATE;
                    gp.stopMusic();
                }
            }

//        Pause State
            else if (gp.gameState == GameStateEnum.PAUSE_STATE) {

                if (code == KeyEvent.VK_W) {
                    gp.playSE(3);
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.playSE(3);
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(3);
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = GameStateEnum.PLAY_STATE;
                        gp.continueMusic();
                    }
                    if (gp.ui.commandNum == 1) {
                        gp.beforeOptionState = GameStateEnum.PAUSE_STATE;
                        gp.gameState = GameStateEnum.OPTION_STATE;
                        gp.ui.commandNum = 0;
                        gp.stopMusic();
                    }
                    if (gp.ui.commandNum == 2) {
                        gp.gameState = GameStateEnum.TITLE_STATE;
                        gp.ui.commandNum = 1;
                        gp.playMusic(7);
                    }

                }
            } else if (gp.gameState == GameStateEnum.OPTION_STATE) { // Option Menu State
                if (code == KeyEvent.VK_W) {
                    gp.playSE(3);
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 3;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.playSE(3);
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 3) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(3);
                    if (gp.ui.commandNum == 0) {
                        gp.isFullScreenOn = !gp.isFullScreenOn;
                        gp.setFullScreen();
                        Main.setFullScreen(gp.isFullScreenOn);
                    }

                    if (gp.ui.commandNum == 2) {
                        gp.spawnBuffs = !gp.spawnBuffs;
                    }

                    if (gp.ui.commandNum == 3) {
                        gp.gameState = gp.beforeOptionState;
                        if (gp.beforeOptionState == GameStateEnum.TITLE_STATE) gp.playMusic(7);
                        else gp.stopMusic();
                        gp.ui.commandNum = 0;

                        try {
                            gp.data.saveOptionConfig();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                if (gp.ui.commandNum == 1) {
                    if (code == KeyEvent.VK_RIGHT) {
                        if (gp.volumeValue < 5) {
                            gp.volumeValue += 1f;
                        }
                        gp.playSE(3);
                    }
                    if (code == KeyEvent.VK_LEFT) {
                        if (gp.volumeValue > -80) {
                            gp.volumeValue -= 1f;
                        }
                        gp.playSE(3);
                    }
                }

            } else if (gp.gameState == GameStateEnum.GAMEOVER_STATE) {
                if (code == KeyEvent.VK_W) {
                    gp.playSE(3);
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 1;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.playSE(3);
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 1) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(3);
                    switch (gp.ui.commandNum) {
                        case 0 -> {
                            gp.player.setDefaultValues();
                            gp.loadMapAndEntity("/map/maps/" + gp.currentMapName + ".txt", gp.currentMapName);
                            gp.stopMusic();
                        }
                        case 1 -> {
                            gp.gameState = GameStateEnum.TITLE_STATE;
                            gp.stopMusic();
                        }
                    }
                }
            } else if (gp.gameState == GameStateEnum.WIN_STATE) {
                if (code == KeyEvent.VK_W) {
                    gp.playSE(3);
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 1;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.playSE(3);
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 1) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(3);
                    switch (gp.ui.commandNum) {
                        case 0 -> {
                            gp.player.setDefaultValues();
                            int mapId = Integer.parseInt(gp.currentMapName.substring(4));
                            mapId++;
                            gp.loadMapAndEntity("/map/maps/map0" + mapId + ".txt", "map0" + mapId);
                            gp.stopMusic();
                        }
                        case 1 -> {
                            gp.gameState = GameStateEnum.TITLE_STATE;
                            gp.stopMusic();
                        }
                    }
                }
            } else if (gp.gameState == GameStateEnum.DIFFICULTYCHOOSE_STATE) {
                if (code == KeyEvent.VK_W) {
                    gp.playSE(3);
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gp.playSE(3);
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 2) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER) {
                    gp.playSE(3);
                    switch (gp.ui.commandNum) {
                        case 0 -> {
                            gp.difficulty = DifficultyEnum.EASY;
                        }
                        case 1 -> {
                            gp.difficulty = DifficultyEnum.MEDIUM;
                        }
                        case 2 -> {
                            gp.difficulty = DifficultyEnum.HARD;
                        }
                    }
                    gp.player.setDefaultValues();
                    gp.loadMapAndEntity("/map/maps/map01.txt", "map01");
                    gp.stopMusic();
                    gp.ui.commandNum = 0;
                }
                if (code == KeyEvent.VK_ESCAPE) {
                    gp.playSE(3);
                    gp.ui.commandNum = 0;
                }
            }
//         debug
        if (code == KeyEvent.VK_J && !gp.canDebug) {
            debugMode = !debugMode;
        }
        if (debugMode) {

            if (code == KeyEvent.VK_F7) {
                int mapId = Integer.parseInt(gp.currentMapName.substring(4));
                mapId++;
                gp.stopMusic();
                gp.loadMapAndEntity("/map/maps/map0" + mapId + ".txt", "map0" + mapId);
            }
            if (code == KeyEvent.VK_I) {
                gp.player.speed++;
            }
            if (code == KeyEvent.VK_O) {
                gp.player.speed--;
            }

            if (code == KeyEvent.VK_N) {
                gp.player.noColFeature = !gp.player.noColFeature;
            }

            if (code == KeyEvent.VK_P) {
                gp.showHitboxes = !gp.showHitboxes;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            eKeyPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            qKeyPressed = false;
        }
    }
}
