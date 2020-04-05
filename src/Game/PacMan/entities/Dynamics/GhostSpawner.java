package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Main.Handler;
import Resources.Images;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static Game.GameStates.PacManState.justStarted;
import static Game.GameStates.PacManState.theMap;
import static Game.PacMan.World.MapBuilder.mapInCreation;
import static Game.PacMan.World.MapBuilder.pixelMultiplier;

public class GhostSpawner extends BaseStatic{

    public static int[] ghosts = new int[4];
    private static Handler handler;
    private static int x, y;
    private static Random random = new Random();

    public GhostSpawner(int x, int y, int width, int height, Handler handler) {
        super(x ,y , width, height, handler, null);
        this.x = x;
        this.y = y;
        this.handler = handler;
    }


    public static void spawnAll(){
        for (int i = 0; i<ghosts.length; i++) {
            BaseDynamic newGhost = new Ghost((theMap == Images.map2) ? (i==0 ? x : x+(i-2)*pixelMultiplier) : x+(i-2)*pixelMultiplier, (theMap == Images.map2) ? (i==0 ? y - pixelMultiplier : y) : y, pixelMultiplier, pixelMultiplier, handler, i);
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

    public static void spawn(boolean addToArray) {
        if (addToArray) {
            for (int i = 0; i < ghosts.length; i++) {
                if (ghosts[i] == 0) {
                    BaseDynamic newGhost = new Ghost(x, y, pixelMultiplier, pixelMultiplier, handler, i);
                    mapInCreation.toAdd(newGhost);
                    ghosts[i] = 1;
                    break;
                }
            }
        }
        else {
            BaseDynamic newGhost = new Ghost(x, y, pixelMultiplier, pixelMultiplier, handler,random.nextInt(4));
            mapInCreation.toAdd(newGhost);
        }
    }
}
