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
import Game.PacMan.entities.Statics.Fruit;
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
	public String Mode = "Intro";
	public int startCooldown = 60*4;//seven seconds for the music to finish
	public static boolean justStarted = true, isVulnerable = false;
	public static BufferedImage theMap = Images.map2;
	public static int isVulnerableCooldown = 0;
	int digits;
	boolean changeMap;
	public static boolean everythingEdible;
	public int[] Konami = new int[9];
	int KonamiCooldown = 0;


	public PacManState(Handler handler){
		super(handler);
		handler.setMap(MapBuilder.createMap(theMap, handler));
	}


    @Override
    public void tick() {
        if (Mode.equals("Stage")){
            if(PacMan.getHealth()>0 || dedcounter>0) {
                if (startCooldown <= 0) {
                    toAdd();
                    toRemove();

					ArrayList<BaseStatic> toREmove = new ArrayList<>();
					for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
						if(!handler.getPacman().getSuperHungry()) {
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
									isVulnerableCooldown = 60 * 5;
									isVulnerable = true;
								}
							} else if (blocks instanceof Fruit) {
								if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
									handler.getMusicHandler().playEffect("pacman_chomp.wav");
									toREmove.add(blocks);
									handler.getScoreManager().addPacmanCurrentScore(120);
								}
							}
						} else {
							if(blocks instanceof BigDot) {
								if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
									handler.getMusicHandler().playEffect("pacman_chomp.wav");
									toREmove.add(blocks);
									handler.getScoreManager().addPacmanCurrentScore(100);
									isVulnerableCooldown = 60 * 5;
									isVulnerable = true;
								}
							} else if (blocks instanceof Fruit) {
								if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
									handler.getMusicHandler().playEffect("pacman_chomp.wav");
									toREmove.add(blocks);
									handler.getScoreManager().addPacmanCurrentScore(120);
								}
							} else if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
								handler.getMusicHandler().playEffect("pacman_chomp.wav");
								toREmove.add(blocks);
								handler.getScoreManager().addPacmanCurrentScore(10);
							}
						}

					}
					for (BaseStatic removing : toREmove) {
						handler.getMap().getBlocksOnMap().remove(removing);
					}


					if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || Konami[0] == 1) {
						Konami[0] = 1;
						KonamiCooldown++;
						if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) || Konami[1] == 1) {
							Konami[1] = 1;
							if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || Konami[2] == 1) {
								Konami[2] = 1;
								if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) || Konami[3] == 1) {
									Konami[3] = 1;
									if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || Konami[4] == 1) {
										Konami[4] = 1;
										if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) || Konami[5] == 1) {
											Konami[5] = 1;
											if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || Konami[6] == 1) {
												Konami[6] = 1;
												if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) || Konami[7] == 1) {
													Konami[7] = 1;
													if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE) || Konami[8] == 1) {
														Konami[8] = 1;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if (Konami[8] == 1){
						Konami = new int[9];
						KonamiCooldown = 0;
						if (handler.getPacman().getSuperHungry()) {
							handler.getPacman().setSuperHungry(false);
						} else {
							handler.getPacman().setSuperHungry(true);
						}
					}


					if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C)) {
						spawn(false);
					}

					if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_X)) {
						killAll(true);
					}

					if (handler.getKeyManager().keyJustPressed((KeyEvent.VK_Z))) {
						killAll(false);
					}


                    if (handler.getScoreManager().getPacmanCurrentScore() > handler.getScoreManager().getPacmanHighScore()) {
                        handler.getScoreManager().setPacmanHighScore(handler.getScoreManager().getPacmanCurrentScore());
                    }

                    for (int i = 0; i < ghosts.length; i++) {
                    	if (ghosts[i]==0){
                    		spawn(true);
						}
					}
                    
                    if(isVulnerableCooldown<= 0) {
                    	isVulnerable=false;
                    }
                    
                    isVulnerableCooldown--;

                    if (addEdibles() == 0 || handler.getKeyManager().keyJustPressed((KeyEvent.VK_B))) {
                    	Mode = "Menu";
                    	changeMap = true;
						killAll(true);
						toRemove();
					}

				} else {
					startCooldown--;
				}
			}
			else {
				Mode = "Menu";
				killAll(true);
				toRemove();
				handler.getScoreManager().setGalagaCurrentScore(0);
			}
		}else if (Mode.equals("Menu")){
			setHealth(3);
			startCooldown = 60*4;
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
				if(changeMap) {
					if(theMap == Images.map1) {
						theMap = Images.map2;
					} else {
						theMap = Images.map1;
					}
				}
				changeMap = false;
				handler.setMap(MapBuilder.createMap(theMap, handler));
				Mode = "Stage";
				handler.getMusicHandler().playEffect("pacman_beginning.wav");
				spawnAll();
                toAdd();
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
            for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) { //see ghosts and pacman bounds
//                g.setColor(Color.WHITE);
//                g.drawRect(entity.getBounds().x,entity.getBounds().y,entity.getBounds().width,entity.getBounds().height);
                if (entity instanceof Ghost) { //see the ghosts' targets (all of them explained in the Ghost class)
                    g.setColor(Color.RED);
                    g.drawRect(((Ghost) entity).chasingX, ((Ghost) entity).chasingY, pixelMultiplier, pixelMultiplier);
                }
            }
//            for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
//                if(blocks instanceof BoundBlock) {
//                    g.drawRect(blocks.getRightBounds().x, blocks.getRightBounds().y, blocks.getRightBounds().width, blocks.getRightBounds().height);
//                    g.drawRect(blocks.getLeftBounds().x, blocks.getLeftBounds().y, blocks.getLeftBounds().width, blocks.getLeftBounds().height);
//                    g.drawRect(blocks.getTopBounds().x, blocks.getTopBounds().y, blocks.getTopBounds().width, blocks.getTopBounds().height);
//                    g.drawRect(blocks.getBottomBounds().x, blocks.getBottomBounds().y, blocks.getBottomBounds().width, blocks.getBottomBounds().height);
//                }
//            }  //USED TO CHECK ENTITY BOUNDS
        }else if (Mode.equals("Menu")){
            g.drawImage(Images.start,handler.getWidth() / 4,0,handler.getWidth()/2,handler.getHeight(),null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.PLAIN, 40));
            digits = String.valueOf(handler.getScoreManager().getPacmanHighScore()).length();
            g.drawString(String.valueOf(handler.getScoreManager().getPacmanHighScore()), handler.getWidth()/2-digits*10, handler.getHeight()/16);
        }else{
            g.drawImage(Images.intro,handler.getWidth() / 4,0,handler.getWidth()/2,handler.getHeight(),null);

		}
	}

	@Override
	public void refresh() {

	}
	 public void toRemove() {
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
	    }

	    public void toAdd() {
	        for (BaseDynamic entity : handler.getMap().getEnemiesToAdd()) {
	            handler.getMap().getEnemiesOnMap().add(entity);
	        }
	        handler.getMap().getEnemiesToAdd().clear();
	    }

	    public int addEdibles() {
			int count = 0;
		 	for (BaseStatic dots : handler.getMap().getBlocksOnMap()) {
		 		if(dots instanceof Dot || dots instanceof BigDot || dots instanceof Fruit) {
		 			count++;
				}
		 	}
		 	return count;
		}

	}

