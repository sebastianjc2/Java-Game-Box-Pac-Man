package Game.PacMan.World;

import Game.GameStates.PacManState;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Main.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {

	ArrayList<BaseStatic> blocksOnMap;
	ArrayList<BaseDynamic> enemiesOnMap;
	Handler handler;
	private double bottomBorder;
	private Random rand;
	private int mapBackground;
	ArrayList<BaseDynamic> toAdd = new ArrayList<>();

	public Map(Handler handler) {
		this.handler=handler;
		this.rand = new Random();
		this.blocksOnMap = new ArrayList<>();
		this.enemiesOnMap = new ArrayList<>();
		bottomBorder=handler.getHeight();
		this.mapBackground = this.rand.nextInt(6);
	}

	public void addBlock(BaseStatic block){
		blocksOnMap.add(block);
	}

	public void addEnemy(BaseDynamic entity){

		enemiesOnMap.add(entity);

	}

	public void toAdd(BaseDynamic x){
		toAdd.add(x);
	}

	public void drawMap(Graphics2D g2) {
		for (BaseStatic block:blocksOnMap) {
			if (block instanceof BigDot) {
				g2.drawImage(handler.getPacman().bigDotBlink.getCurrentFrame(), block.x, block.y, block.width, block.height, null);
			}
			else {
				g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
			}
		}
		for (BaseDynamic entity:enemiesOnMap) {
			if (entity instanceof PacMan) {
				if(!entity.ded) {
					switch (((PacMan) entity).facing) {
					case "Right":
						g2.drawImage(((PacMan) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Left":
						g2.drawImage(((PacMan) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Up":
						g2.drawImage(((PacMan) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Down":
						g2.drawImage(((PacMan) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					}
				}
				else{
					g2.drawImage(((PacMan) entity).pacmanDeathAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
				}
			}
			else if (entity instanceof Ghost) {
				handler.getPacManState();
				if(PacManState.isVulnerable) {
					g2.drawImage(((Ghost) entity).edibleGhostsAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
				}
				else {
					switch (((Ghost) entity).facing) {
					case "Right":
						g2.drawImage(((Ghost) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Left":
						g2.drawImage(((Ghost) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Up":
						g2.drawImage(((Ghost) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					case "Down":
						g2.drawImage(((Ghost) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
						break;
					}
				}
			}

			else {
				g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
			}
		}
	}


	public ArrayList<BaseStatic> getBlocksOnMap() {
		return blocksOnMap;
	}

	public ArrayList<BaseDynamic> getEnemiesOnMap() {
		return enemiesOnMap;
	}

	public ArrayList<BaseDynamic> getEnemiesToAdd() {
		return toAdd;
	}


	public double getBottomBorder() {
		return bottomBorder;
	}

	public void reset() {
	}
}
