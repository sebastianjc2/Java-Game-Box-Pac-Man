package Game.PacMan.entities.Dynamics;

import Game.GameStates.PacManState;
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

import static Game.GameStates.PacManState.theMap;
import static Game.PacMan.World.MapBuilder.*;

public class Ghost extends BaseDynamic{

    protected double velX,velY,speed = 1.5, chasing, angle;
    public String facing = "Left";
    public boolean moving = true, moveFlag = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim, canDieAnim;
    int turnCooldown = 0;
    BufferedImage image;
    int leaveSpawnTimer, ghost, timer;
    boolean goingRandom = false;
    public int chasingX, chasingY, facingMultiplierX, facingMultiplierY, position;

    public static String[] moves = new String[] {"Right", "Left", "Up", "Down", "None"};



    public Ghost(int x, int y, int width, int height, Handler handler, int ghost) {
        super(x, y, width, height, handler, null);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        canDieAnim = new Animation(128,Images.ghostCanDie);
        this.ghost = ghost;
        switch (ghost){
            case 0:
                //blinky
                leftAnim = new Animation(128,Images.ghostBlinkyLeft);
                rightAnim = new Animation(128,Images.ghostBlinkyRight);
                upAnim = new Animation(128,Images.ghostBlinkyUp);
                downAnim = new Animation(128,Images.ghostBlinkyDown);
                leaveSpawnTimer = 0;
                break;
            case 1:
                //pinky
                leftAnim = new Animation(128,Images.ghostPinkyLeft);
                rightAnim = new Animation(128,Images.ghostPinkyRight);
                upAnim = new Animation(128,Images.ghostPinkyUp);
                downAnim = new Animation(128,Images.ghostPinkyDown);
                leaveSpawnTimer = 60*10;
                break;
            case 2:
                //inky
                leftAnim = new Animation(128,Images.ghostInkyLeft);
                rightAnim = new Animation(128,Images.ghostInkyRight);
                upAnim = new Animation(128,Images.ghostInkyUp);
                downAnim = new Animation(128,Images.ghostInkyDown);
                leaveSpawnTimer = 60*20;
                break;
            case 3:
                //clyde
                leftAnim = new Animation(128,Images.ghostClydeLeft);
                rightAnim = new Animation(128,Images.ghostClydeRight);
                upAnim = new Animation(128,Images.ghostClydeUp);
                downAnim = new Animation(128,Images.ghostClydeDown);
                leaveSpawnTimer = 60*30;
                break;
        }

        super.sprite = image;
    }

    @Override
    public void tick(){
        switch (facing){
            case "Right":
                if (velX != 0){
                    velX += speed/2;
                }
                x+=velX;
                rightAnim.tick();
                keepInMiddleY();
                break;
            case "Left":
                x-=velX;
                leftAnim.tick();
                keepInMiddleY();
                break;
            case "Up":
                y-=velY;
                upAnim.tick();
                keepInMiddleX();
                break;
            case "Down":
                if (velY != 0){
                    velY += speed/2;
                }
                y+=velY;
                downAnim.tick();
                keepInMiddleX();
                break;
        }

        if(this.ghost == 0) { //chase pacman
            chasingX = handler.getPacman().x;
            chasingY = handler.getPacman().y;
        } else if (this.ghost == 1){ //chase 4 tiles in front of pacman
            if (handler.getPacman().facing == "Right") {
                facingMultiplierX = 1;
                facingMultiplierY = 0;
            } else if (handler.getPacman().facing.equals("Left")) {
                facingMultiplierX = -1;
                facingMultiplierY = 0;
            } else if (handler.getPacman().facing.equals("Up")) {
                facingMultiplierX = 0;
                facingMultiplierY = -1;
            } else {
                facingMultiplierX = 0;
                facingMultiplierY = 1;
            }
            chasingX = handler.getPacman().x + 4*pixelMultiplier*facingMultiplierX;
            chasingY = handler.getPacman().y + 4*pixelMultiplier*facingMultiplierY;
        } else if (this.ghost == 2) { //looks 2 tiles ahead of pacman, makes a vector from blinky to that tile, doubles vector, the end of that vector is the target (yes thats how inky was calculated originally, pretty cool)
            if (handler.getPacman().facing == "Right") {
                facingMultiplierX = 1;
                facingMultiplierY = 0;
            } else if (handler.getPacman().facing.equals("Left")) {
                facingMultiplierX = -1;
                facingMultiplierY = 0;
            } else if (handler.getPacman().facing.equals("Up")) {
                facingMultiplierX = 0;
                facingMultiplierY = -1;
            } else {
                facingMultiplierX = 0;
                facingMultiplierY = 1;
            }
            chasingX = handler.getPacman().x + 2*pixelMultiplier*facingMultiplierX;
            chasingY = handler.getPacman().y + 2*pixelMultiplier*facingMultiplierY;

            for (BaseDynamic enemy : handler.getMap().getEnemiesOnMap()) {
                if (enemy instanceof Ghost) {
                    if (((Ghost) enemy).ghost == 0) {
                        chasing = 2 * Point.distance(enemy.x, enemy.y, chasingX, chasingY); //getting distance vector and doubling it
                        angle = Math.atan2(enemy.y - chasingY, enemy.x - chasingX);

                        chasingX = ((int) (chasing * Math.cos(angle)*-1)) + enemy.x; //since the vector starts at blinky, it must take into account his position
                        chasingY = ((int) (chasing * Math.sin(angle)*-1)) + enemy.y;
                    }
                }
            }
        } else { //clyde chases pacman until he gets close, then he hides in a corner, strange behavior but I'll still leave it like that
            if (Point.distance(this.x, this.y, handler.getPacman().x, handler.getPacman().y) < 8*pixelMultiplier) {
                chasingX = centralize + pixelMultiplier;
                chasingY = handler.getHeight() + 4 * pixelMultiplier;
            } else {
                chasingX = handler.getPacman().x;
                chasingY = handler.getPacman().y;
            }
        }


        if(leaveSpawnTimer<=0 && !moveFlag){ //while in spawn, idle, once its time, leave
            facing = leaveSpawn();
            if (!arena.getBounds().intersects(this.getBounds())){
                moveFlag = true;
            }
        }
        else if (!moveFlag){
            facing = spawnIdle();
            leaveSpawnTimer--;
        }


        if (moveFlag && moving && othersAvailable(facing) && turnCooldown<=0) {  //once out of spawn start chasing (unless you're clyde, in that case just scatter)
            facing = ChaseMode(chasingX, chasingY, facing);
            turnCooldown = 10;
            position = 0;
        }
        else if (moveFlag && !moving) { //if ghost stopped moving (wall between target and ghost) move randomly and then continue chasing
            facing = scatterRandomly(facing);
            turnCooldown = 10;
            position = 1;
        }

        if (facing.equals("Right") || facing.equals("Left")) {
            checkHorizontalCollision();
        } else {
            checkVerticalCollisions();
        }

        if (moveFlag) {
            timer++;
        }

        turnCooldown--;
    }


    public void checkVerticalCollisions() {
        Ghost ghost = this;
        moving = true;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();

        boolean ghostDies = false;
        boolean toUp = facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;

        if(ghost.getBounds().intersects(arena.getTopBounds()) && moveFlag && !toUp) {
            velY = 0;
            moving = false;
            ghost.setY(arena.y - ghost.getDimension().height);
        }

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghost.getBounds().intersects(brickBounds)) {
                    velY = 0;
                    moving = false;
                    if (toUp)
                        ghost.setY(brick.getY() + brick.getDimension().height);
                    else
                        ghost.setY(brick.getY() - ghost.getDimension().height);
                }
            }
        }

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            if (ghostBounds.intersects(enemyBounds) && !(enemy instanceof Ghost)) {
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
        boolean toUp = facing.equals("Up");

        Rectangle ghostBounds = toUp ? ghost.getTopBounds() : ghost.getBottomBounds();

        velY = speed;
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghost.getBounds().intersects(brickBounds)) {
                    return false;
                }
            }
        }
        if (ghost.getBounds().intersects(arena.getTopBounds()) && moveFlag && !toUp) {
            return false;
        }
        return true;

    }



    public void checkHorizontalCollision(){
        Ghost ghost = this;
        moving = true;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        velX = speed;
        boolean ghostDies = false;
        boolean toRight = facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for(BaseDynamic enemy : enemies) {
            if (!(enemy instanceof Ghost)) {
                Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
                if (ghost.getBounds().intersects(enemyBounds)) {
                    ghostDies = true;
                    break;
                }
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
                        moving = false;
                        if (toRight)
                            ghost.setX(brick.getX() - ghost.getDimension().width);
                        else
                            ghost.setX(brick.getX() + brick.getDimension().width);
                    }
                }
            }
        }
    }


    public boolean checkPreHorizontalCollisions(String facing){
        Ghost ghost = this;
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();

        velX = speed;
        boolean toRight = facing.equals("Right");

        Rectangle ghostBounds = toRight ? ghost.getRightBounds() : ghost.getLeftBounds();

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (ghost.getBounds().intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void keepInMiddleX() { //makes sure ghosts cant go halfway into a wall
        int min=1000;
        int calc;
        int block = 0;
        for (int i = 0; i < theMap.getWidth(); i++) {
            calc = Math.abs((this.x - centralize) - i*pixelMultiplier);
            if(calc < min) {
                min = calc;
                block = i;
            }
        }
        this.x = block * pixelMultiplier + centralize;
    }

    public void keepInMiddleY() { //makes sure ghosts cant go halfway into a wall
        int min = 1000;
        int calc;
        int block = 0;
        for (int i = 0; i < theMap.getHeight(); i++) {
            calc = Math.abs(this.y - i * pixelMultiplier);
            if (calc < min) {
                min = calc;
                block = i;
            }
        }
        this.y = block * pixelMultiplier;
    }

    public String ChaseMode(int chaseX, int chaseY, String facing) { //chases the coordinate (chaseX, chaseY)
        int xDistance, yDistance, index = 0, notGoingBack;
        double distance, moveProbability = 0;
        int[] possibleMoves = new int[4];
        double[] moveWeight = new double[4];

        xDistance = this.x - chaseX;
        yDistance = this.y - chaseY;
        distance = Math.abs(xDistance) + Math.abs(yDistance);

        notGoingBack = dontTurnBack(facing);

        for (int i = 0; i < possibleMoves.length; i++) {
            if (i < 2 && i!=notGoingBack) {
                possibleMoves[i] = checkPreHorizontalCollisions(moves[i]) ? 1 : 0;
                moveWeight[i] = (i == 0 ? xDistance < 0 : xDistance >= 0) ? possibleMoves[i] * Math.abs(xDistance / distance) : 0;
            } else if (i >= 2 && i!=notGoingBack) {
                possibleMoves[i] = checkPreVerticalCollisions(moves[i]) ? 1 : 0;
                moveWeight[i] = (i == 2 ? yDistance >= 0 : yDistance < 0) ? possibleMoves[i] * Math.abs(yDistance / distance) : 0;
            }

            if (moveProbability < moveWeight[i]) {
                moveProbability = moveWeight[i];
                index = i;
            }
        }
        return moves[index];
    }

    public String scatterRandomly(String facing) { //self explanatory, move randomly
        double moveProbability = Math.random(), total = 0, notGoingBack;
        int[] possibleMoves = new int[4];
        double[] moveWeight = new double[4];

        notGoingBack = dontTurnBack(facing);

        for (int i = 0; i < possibleMoves.length; i++) {
            if (i < 2 && i!=notGoingBack) {
                possibleMoves[i] = checkPreHorizontalCollisions(moves[i]) ? 1 : 0;
                if (possibleMoves[i] == 1) {
                    total++;
                }
            } else if (i >= 2 && i!=notGoingBack) {
                possibleMoves[i] = checkPreVerticalCollisions(moves[i]) ? 1 : 0;
                if (possibleMoves[i] == 1) {
                    total++;
                }
            }
        }
        for (int i = 0; i < possibleMoves.length; i++) {
            moveWeight[i] = possibleMoves[i] * (1 / total);
            if (moveProbability < moveWeight[i]) {
                return moves[i];
            } else {
                moveProbability -= moveWeight[i];
            }
        }
        return "?";
    }

    public String leaveSpawn(){
        return ChaseMode(handler.getWidth()/2- this.width/2, arena.y - 2*pixelMultiplier, facing);
    }

    public String spawnIdle() {
        if(theMap==Images.map2) {
            return "Down";
        }
        if(moving && facing.equals("Down")){
            return "Down";
        }
        else if(!moving && facing.equals("Down")){
            return "Up";
        }
        else if(moving && facing.equals("Up")){
            return "Up";
        }
        else if(!moving && facing.equals("Up")){
            return "Down";
        }
        return "Down";
    }

    public boolean othersAvailable(String facing) { //if going up or down, checks if a move to the left or right is available and vice versa
        if (facing.equals(moves[0]) || facing.equals(moves[1])) {
            if (checkPreVerticalCollisions(moves[2]) || checkPreVerticalCollisions(moves[3])) {
                return true;
            }
            return false;
        }
        else {
            if (checkPreHorizontalCollisions(moves[0]) || checkPreHorizontalCollisions(moves[1])) {
                return true;
            }
            return false;
        }
    }

    public int dontTurnBack(String facing) {  //makes sure ghost never reverses
        if (facing.equals("Right")) { //index of left
            return 1;
        }
        else if (facing.equals("Left")) {
            return 0;
        }
        else if (facing.equals("Up")) {
            return 3;
        }
        else if (facing.equals("Down")) {
            return 2;
        }
        return 4;
    }

    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }

}
