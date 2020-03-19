package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacMan extends BaseDynamic{

    protected double velX,velY,speed = 1;
    public String facing = "Left";
    public boolean moving = true,turnFlag = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim, pacmanDeathAnim;
    int turnCooldown = 20;
    public static int health = 3, dedcounter = 0;


    public PacMan(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        pacmanDeathAnim = new Animation(100,Images.pacmanDeath);
    }

    @Override
    public void tick() {
//        System.out.println(x + " " + y);
        if (dedcounter<=0) {
            switch (facing) {
                case "Right":
                    x += velX;
                    rightAnim.tick();
                    break;
                case "Left":
                    x -= velX;
                    leftAnim.tick();
                    break;
                case "Up":
                    y -= velY;
                    upAnim.tick();
                    break;
                case "Down":
                    y += velY;
                    downAnim.tick();
                    break;
            }
            if (turnCooldown <= 0) {
                turnFlag = false;
            }
            if (turnFlag) {
                turnCooldown--;
            }


            if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag && checkPreHorizontalCollision("Right")) {
                facing = "Right";
                turnFlag = true;
                turnCooldown = 0;
            } else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag && checkPreHorizontalCollision("Left")) {
                facing = "Left";
                turnFlag = true;
                turnCooldown = 0;
            } else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag && checkPreVerticalCollisions("Up")) {
                facing = "Up";
                turnFlag = true;
                turnCooldown = 0;
            } else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag && checkPreVerticalCollisions("Down")) {
                facing = "Down";
                turnFlag = true;
                turnCooldown = 0;
            }

            if (facing.equals("Right") || facing.equals("Left")) {
                checkHorizontalCollision();
            } else {
                checkVerticalCollisions();
            }

            if(ded) {
                dedcounter=60*2;
                handler.getMusicHandler().playEffect("pacman_death.wav");
            }
        }
        else {
            pacmanDeathAnim.tick();
            dedcounter--;
            if (pacmanDeathAnim.getIndex()==11) {
                x = 189 + handler.getWidth() / 4;
                y = 972;
                ded = false;
            }
        }

    }

    public void checkVerticalCollisions() {
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();

        boolean pacmanDies = false;
        boolean toUp = facing.equals("Up");

        Rectangle pacmanBounds = toUp ? pacman.getTopBounds() : pacman.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp)
                        pacman.setY(brick.getY() + brick.getDimension().height);
                    else
                        pacman.setY(brick.getY() - pacman.getDimension().height);
                }
            }
        }

        for(BaseDynamic enemy : enemies){
            if (enemy instanceof Ghost) {
                Rectangle enemyBounds = enemy.getBounds();
                if (pacman.getBounds().intersects(enemyBounds)) {
                    pacmanDies = true;
                    break;
                }
            }
        }

        if(pacmanDies) {
            System.out.println("here??");
            handler.getMap().reset();
            health--;
            ded = true;
        }
    }


    public boolean checkPreVerticalCollisions(String facing) {
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();

        boolean pacmanDies = false;
        boolean toUp = facing.equals("Up");

        Rectangle pacmanBounds = toUp ? pacman.getTopBounds() : pacman.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;

    }



    public void checkHorizontalCollision(){
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        velX = speed;
        boolean pacmanDies = false;
        boolean toRight = facing.equals("Right");

        Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();

        for(BaseDynamic enemy : enemies){
            if (enemy instanceof Ghost) {
                Rectangle enemyBounds = enemy.getBounds();
                if (pacman.getBounds().intersects(enemyBounds)) {
                    pacmanDies = true;
                    break;
                }
            }
        }

        if(pacmanDies) {
            handler.getMap().reset();
            health--;
            ded = true;
        }else {

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacmanBounds.intersects(brickBounds)) {
                        velX = 0;
                        if (toRight)
                            pacman.setX(brick.getX() - pacman.getDimension().width);
                        else
                            pacman.setX(brick.getX() + brick.getDimension().width);
                    }
                }
            }
        }
    }


    public boolean checkPreHorizontalCollision(String facing){
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = speed;
        boolean toRight = facing.equals("Right");

        Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacmanBounds.intersects(brickBounds)) {
                        return false;
                    }
                }
            }
        return true;
    }

    public static void setHealth(int x) {
        health = x;
    }

    public static int getHealth() {
        return health;
    }

    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }

    public void ded() {

    }

}
