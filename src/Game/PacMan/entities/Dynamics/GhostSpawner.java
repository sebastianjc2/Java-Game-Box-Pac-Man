package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Main.Handler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static Game.GameStates.PacManState.justStarted;
import static Game.PacMan.World.MapBuilder.mapInCreation;
import static Game.PacMan.World.MapBuilder.pixelMultiplier;

public class GhostSpawner extends BaseDynamic{

    public static int[] ghosts = new int[4];
    private static Handler handler;
    Random random = new Random();
    private static int x, y;

    public GhostSpawner(int x, int y, int width, int height, Handler handler) {
        super(x ,y , width, height, handler, null);
        this.x = x;
        this.y = y;
        this.handler = handler;
    }


    public void spawnAll(){
        for (int i = 0; i<ghosts.length; i++) {
            BaseDynamic newGhost = new Ghost(this.x+2*pixelMultiplier-i*pixelMultiplier, this.y, pixelMultiplier, pixelMultiplier, this.handler, i);
            mapInCreation.toAdd(newGhost);
            ghosts[i]=1;
        }
    }

    public static void killAll() {
        for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
            if (entity instanceof Ghost) {
                entity.ded = true;
            }
        }
        ghosts = new int[4];
    }

    public static void spawn() {
        for(int i = 0; i < ghosts.length; i++) {
            System.out.println(i);
            if (ghosts[i]==0) {
                BaseDynamic newGhost = new Ghost(x, y, pixelMultiplier, pixelMultiplier, handler, i);
                mapInCreation.toAdd(newGhost);
                ghosts[i]=1;
                break;
            }
        }
    }

    @Override
    public void tick() {
//        if (justStarted) {
//            spawnAll();
//        }
//        spawn();
    }
}
