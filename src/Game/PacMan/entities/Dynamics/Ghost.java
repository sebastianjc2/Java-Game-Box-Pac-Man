package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends BaseDynamic{

    protected double velX,velY,speed = 1;
    public String facing = "Left";
    public boolean moving = true,turnFlag = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim;
    int turnCooldown = 0;
    BufferedImage image;


    public Ghost(int x, int y, int width, int height, Handler handler, int ghost) {
        super(x, y, width, height, handler, null);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        switch (ghost){
            case 0:
                //inky
                image = Images.ghostInky;
                break;
            case 1:
                //blinky
                image = Images.ghostBlinky;
                break;
            case 2:
                //pinky
                image = Images.ghostPinky;
                break;
            case 3:
                //clyde
                image = Images.ghostClyde;
                break;
        }

        super.sprite = image;
    }

    @Override
    public void tick(){

        switch (facing){
            case "Right":
                x+=velX;
                rightAnim.tick();
                break;
            case "Left":
                x-=velX;
                leftAnim.tick();
                break;
            case "Up":
                y-=velY;
                upAnim.tick();
                break;
            case "Down":
                y+=velY;
                downAnim.tick();
                break;
        }
        if (turnCooldown<=0){
            turnFlag= false;
            turnCooldown = 30;
        }
        if (turnFlag){
            turnCooldown--;
        }

        if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag && checkPreHorizontalCollision("Right")){
            facing = "Right";
            turnFlag = true;

        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag&& checkPreHorizontalCollision("left")){
            facing = "Left";
            turnFlag = true;
        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)  ||handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag&& checkPreVerticalCollisions("Up")){
            facing = "Up";
            turnFlag = true;
        }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag&& checkPreVerticalCollisions("Down")){
            facing = "Down";
            turnFlag = true;
        }

        if (facing.equals("Right") || facing.equals("Left")){
            checkHorizontalCollision();
        }else{
            checkVerticalCollisions();
        }

    }


    public void checkVerticalCollisions() {
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();

        boolean ghostDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp)
                        ghost.setY(brick.getY() + ghost.getDimension().height);
                    else
                        ghost.setY(brick.getY() - brick.getDimension().height);
                }
            }
        }

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            if (ghostBounds.intersects(enemyBounds)) {
                ghostDies = true;
                break;
            }
        }

        if(ghostDies) {
            handler.getMap().reset();
        }
    }


    public boolean checkPreVerticalCollisions(String facing) {
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();

        boolean ghostDies = false;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;

    }



    public void checkHorizontalCollision(){
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        velX = speed;
        boolean ghostDies = false;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
            if (ghostBounds.intersects(enemyBounds)) {
                ghostDies = true;
                break;
            }
        }

        if(ghostDies) {
            handler.getMap().reset();
        }else {

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (ghostBounds.intersects(brickBounds)) {
                        velX = 0;
                        if (toRight)
                            ghost.setX(brick.getX() - ghost.getDimension().width);
                        else
                            ghost.setX(brick.getX() + brick.getDimension().width);
                    }
                }
            }
        }
    }


    public boolean checkPreHorizontalCollision(String facing){
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = speed;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;
    }


    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }

}
