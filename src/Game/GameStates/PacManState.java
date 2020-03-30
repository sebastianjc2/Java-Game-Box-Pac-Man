package Game.GameStates;

import Display.UI.UIManager;
import Game.Galaga.Entities.BaseEntity;
import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.GhostSpawner;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.Dot;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Game.PacMan.entities.Dynamics.GhostSpawner.*;
import static Game.PacMan.World.MapBuilder.*;
import static Game.PacMan.entities.Dynamics.PacMan.*;

public class PacManState extends State {

    private UIManager uiManager;
    private Animation titleAnimation;
    public String Mode = "Intro";
    public int startCooldown = 60*4;//seven seconds for the music to finish
    public static boolean justStarted = true;
    public static int timer = 0;
    public static BufferedImage theMap = Images.map2;


    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(theMap, handler));
    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
            if(PacMan.getHealth()>0 || dedcounter>0) {
                if (startCooldown <= 0) {
                    justStarted = false;
                    for (BaseDynamic entity : handler.getMap().getEnemiesToAdd()) {
                        handler.getMap().getEnemiesOnMap().add(entity);
                    }
                    handler.getMap().getEnemiesToAdd().clear();

                    ArrayList<BaseDynamic> toRemove = new ArrayList<>();
                    for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                        entity.tick();
                        if (entity instanceof Ghost && entity.ded) {
                            toRemove.add(entity);
                        }
                    }
                    for (BaseDynamic toErase : toRemove) {
                        handler.getMap().getEnemiesOnMap().remove(toErase);
                    }

                    ArrayList<BaseStatic> toREmove = new ArrayList<>();
                    for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
                        if (blocks instanceof Dot) {
                            if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
                                handler.getMusicHandler().playEffect("pacman_chomp.wav");
                                toREmove.add(blocks);
                                handler.getScoreManager().addPacmanCurrentScore(10);
                            }
                        } else if (blocks instanceof BigDot) {
                            if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
                                handler.getMusicHandler().playEffect("pacman_chomp.wav");
                                toREmove.add(blocks);
                                handler.getScoreManager().addPacmanCurrentScore(100);

                            }
                        }
                    }
                    for (BaseStatic removing : toREmove) {
                        handler.getMap().getBlocksOnMap().remove(removing);
                    }


                    if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)) {
                        spawn(false);
                    }

                    if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_X)) {
                        killAll();
                    }

                    timer++;

                } else {
                    startCooldown--;
                }
            }
            else {
            	titleAnimation.tick();
                Mode = "Menu";
                killAll();
            }
        }else if (Mode.equals("Menu")){
            setHealth(3);
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().playEffect("pacman_beginning.wav");
            }
        }else{
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Menu";
            }
        }



    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        if (Mode.equals("Stage")){
            g.setColor(Color.BLACK);
            g.fillRect(centralize - pixelMultiplier/2,0,pixelMultiplier*theMap.getWidth() + pixelMultiplier,handler.getHeight());
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),(handler.getWidth() * 11 / 40 + handler.getHeight() * 39 / 40), 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),(handler.getHeight()) * 39 / 40 + handler.getWidth() * 11 / 40, 75);
            for (int i = 0; i< PacMan.getHealth();i++) {
                g.drawImage(Images.pacmanRight[0], (handler.getWidth() * 11 / 40 + handler.getHeight() * 39 / 40) + (100*i), handler.getHeight()-handler.getHeight()/4, 64, 64, null);
            }
            g.drawRect(arena.x, arena.y, arena.width, arena.height);
            for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                g.drawRect(entity.getBounds().x,entity.getBounds().y,entity.getBounds().width,entity.getBounds().height);
            }
//            for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
//                if(blocks instanceof BoundBlock) {
//                    g.drawRect(blocks.getRightBounds().x, blocks.getRightBounds().y, blocks.getRightBounds().width, blocks.getRightBounds().height);
//                    g.drawRect(blocks.getLeftBounds().x, blocks.getLeftBounds().y, blocks.getLeftBounds().width, blocks.getLeftBounds().height);
////                    g.drawRect(blocks.getTopBounds().x, blocks.getTopBounds().y, blocks.getTopBounds().width, blocks.getTopBounds().height);
////                    g.drawRect(blocks.getBottomBounds().x, blocks.getBottomBounds().y, blocks.getBottomBounds().width, blocks.getBottomBounds().height);
//                }
//            }  //USED TO CHECK ENTITY BOUNDS
            g.drawLine(handler.getWidth()/2, 0, handler.getWidth()/2, handler.getHeight());
        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,handler.getWidth() / 4,0,handler.getWidth()/2,handler.getHeight(),null);
        }else{
            g.drawImage(Images.intro,handler.getWidth() / 4,0,handler.getWidth()/2,handler.getHeight(),null);

        }
    }

    @Override
    public void refresh() {

    }


}
