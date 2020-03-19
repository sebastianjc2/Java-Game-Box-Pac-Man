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
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static Game.PacMan.entities.Dynamics.GhostSpawner.killAll;
import static Game.PacMan.entities.Dynamics.GhostSpawner.spawn;
import static Game.PacMan.entities.Dynamics.PacMan.dedcounter;
import static Game.PacMan.entities.Dynamics.PacMan.setHealth;

public class PacManState extends State {

    private UIManager uiManager;
    public String Mode = "Intro";
    public int startCooldown = 60*4;//seven seconds for the music to finish
    public static boolean justStarted = true;


    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(Images.map1, handler));

        for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
            if (entity instanceof GhostSpawner) {
                entity.tick();
            }
        }
        for (BaseDynamic entity : handler.getMap().getEnemiesToAdd()) {
            handler.getMap().getEnemiesOnMap().add(entity);
        }
        handler.getMap().getEnemiesToAdd().clear();
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
                        spawn();
                    }

                    if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_X)) {
                        killAll();
                    }

                } else {
                    startCooldown--;
                }
            }
            else {
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

        if (Mode.equals("Stage")){
            Graphics2D g2 = (Graphics2D) g.create();
            handler.getMap().drawMap(g2);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
            g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(),(handler.getWidth() * 11 / 40 + handler.getHeight() * 39 / 40), 25);
            g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(),(handler.getHeight()) * 39 / 40 + handler.getWidth() * 11 / 40, 75);
            for (int i = 0; i< PacMan.getHealth();i++) {
                g.drawImage(Images.pacmanRight[0], (handler.getWidth() * 11 / 40 + handler.getHeight() * 39 / 40) + (100*i), handler.getHeight()-handler.getHeight()/4, 64, 64, null);
            }
//            for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
//                g.drawRect(entity.getBounds().x,entity.getBounds().y,entity.getBounds().width,entity.getBounds().height);
//            }
//            for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
//                if(blocks instanceof BoundBlock) {
//                    g.drawRect(blocks.getRightBounds().x, blocks.getRightBounds().y, blocks.getRightBounds().width, blocks.getRightBounds().height);
//                }
//            }  USED TO CHECK ENTITY BOUNDS
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
