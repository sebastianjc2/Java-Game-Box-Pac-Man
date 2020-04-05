package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static Game.GameStates.PacManState.theMap;
import static Game.PacMan.World.MapBuilder.*;

public class PacMan extends BaseDynamic{

    protected double velX,velY,speed = 1.5;
    public String facing = "Left";
    public boolean moving = true, turnFlag = false, turnRightFlag = false, turnLeftFlag = false, turnUpFlag = false, turnDownFlag = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim, pacmanDeathAnim, bigDotBlink;
    int turnCooldown = 20;
    public static int health = 3, dedcounter = 0;



    public PacMan(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
		bigDotBlink = new Animation(128, Images.bigDotBlink);
        pacmanDeathAnim = new Animation(128,Images.pacmanDeath);
    }

    @Override
    public void tick() {
    	bigDotBlink.tick();
        if (dedcounter<=0) {
            switch (facing) {
                case "Right":
                    if (velX != 0){
                        velX += speed/2;
                    }
                    x += velX;
                    rightAnim.tick();
                    keepInMiddleY();
                    break;
                case "Left":
                    x -= velX;
                    leftAnim.tick();
                    keepInMiddleY();
                    break;
                case "Up":
                    y -= velY;
                    upAnim.tick();
                    keepInMiddleX();
                    break;
                case "Down":
                    if (velY != 0){
                        velY += speed/2;
                    }
                    y += velY;
                    downAnim.tick();
                    keepInMiddleX();
                    break;
            }
            if (turnCooldown <= 0) {
                turnFlag = false;
            }
            if (turnFlag) {
                turnCooldown--;
            }


            if (handler.getKeyManager().keyHeld(KeyEvent.VK_RIGHT) || handler.getKeyManager().keyHeld(KeyEvent.VK_D)){ //allows holding buttons to move to directions
                turnRightFlag = true;
            }
            else if (handler.getKeyManager().keyJustReleased(KeyEvent.VK_RIGHT) || handler.getKeyManager().keyJustReleased(KeyEvent.VK_D)) {
                turnRightFlag = false;
            }
            if (handler.getKeyManager().keyHeld(KeyEvent.VK_LEFT) || handler.getKeyManager().keyHeld(KeyEvent.VK_A)){
                turnLeftFlag = true;
            }
            else if (handler.getKeyManager().keyJustReleased(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustReleased(KeyEvent.VK_A)) {
                turnLeftFlag = false;
            }
            if (handler.getKeyManager().keyHeld(KeyEvent.VK_UP) || handler.getKeyManager().keyHeld(KeyEvent.VK_W)){
                turnUpFlag = true;
            }
            else if (handler.getKeyManager().keyJustReleased(KeyEvent.VK_UP) || handler.getKeyManager().keyJustReleased(KeyEvent.VK_W)) {
                turnUpFlag = false;
            }
            if (handler.getKeyManager().keyHeld(KeyEvent.VK_DOWN) || handler.getKeyManager().keyHeld(KeyEvent.VK_S)){
                turnDownFlag = true;
            }
            else if (handler.getKeyManager().keyJustReleased(KeyEvent.VK_DOWN) || handler.getKeyManager().keyJustReleased(KeyEvent.VK_S)) {
                turnDownFlag = false;
            }



            if (turnRightFlag && !turnFlag && checkPreHorizontalCollisions("Right")) {
                facing = "Right";
                turnFlag = true;
                turnCooldown = 0;
            } else if (turnLeftFlag && !turnFlag && checkPreHorizontalCollisions("Left")) {
                facing = "Left";
                turnFlag = true;
                turnCooldown = 0;
            } else if (turnUpFlag && !turnFlag && checkPreVerticalCollisions("Up")) {
                facing = "Up";
                turnFlag = true;
                turnCooldown = 0;
            } else if (turnDownFlag && !turnFlag && checkPreVerticalCollisions("Down")) {
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
                dedcounter=128;
                handler.getMusicHandler().playEffect("pacman_death.wav");
            }

            if (x <= centralize - pixelMultiplier/2) {
                x = centralize + pixelMultiplier * theMap.getWidth() - pixelMultiplier / 2;
            }
            else if (x >= centralize + pixelMultiplier * theMap.getWidth() - pixelMultiplier / 2) {
                x = centralize - pixelMultiplier/2;
            }
        }
        else {
            pacmanDeathAnim.tick();
            dedcounter--;
            if (pacmanDeathAnim.getIndex()==11) {
                x = pacmanX;
                y = pacmanY;
                ded = false;
            }
        }
    	if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && getHealth()<3){
			health=health+1;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P) && !ded){
			handler.getMap().reset();
			health--;
			ded = true;
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
                if (pacman.getBounds().intersects(brickBounds)) {
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


        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacman.getBounds().intersects(brickBounds)) {
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


    public boolean checkPreHorizontalCollisions(String facing){
        PacMan pacman = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = speed;
        boolean toRight = facing.equals("Right");

        Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacman.getBounds().intersects(brickBounds)) {
                        return false;
                    }
                }
            }
        return true;
    }

    public void keepInMiddleX() { //makes sure pacman cant go halfway into a wall
        int min=1000;
        int calc;
        int block = 0;
        for (int i = 0; i < theMap.getWidth(); i++) {
            calc = Math.abs((this.x- centralize)-i*pixelMultiplier);
            if(calc < min) {
                min = calc;
                block = i;
            }
        }
        this.x = block * pixelMultiplier + centralize;
    }

    public void keepInMiddleY() { //makes sure pacman cant go halfway into a wall
        int min=1000;
        int calc;
        int block = 0;
        for (int i = 0; i < theMap.getHeight(); i++) {
            calc = Math.abs(this.y-i*pixelMultiplier);
            if(calc < min) {
                min = calc;
                block = i;
            }
        }
        this.y = block * pixelMultiplier;
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
