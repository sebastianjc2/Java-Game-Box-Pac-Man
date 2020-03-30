package Resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class Images {


    public static BufferedImage titleScreenBackground;
    public static BufferedImage pauseBackground;
    public static BufferedImage selectionBackground;
    public static BufferedImage galagaCopyright;
    public static BufferedImage galagaSelect;
    public static BufferedImage muteIcon;
    public static BufferedImage galagaPlayerLaser;
    public static BufferedImage[] startGameButton;
    public static BufferedImage[] galagaLogo;
    public static BufferedImage[] pauseResumeButton;
    public static BufferedImage[] pauseToTitleButton;
    public static BufferedImage[] pauseOptionsButton;
    public static BufferedImage[] galagaPlayer;
    public static BufferedImage[] galagaPlayerDeath;
    public static BufferedImage[] galagaEnemyDeath;
    public static BufferedImage[] galagaEnemyBee;

    public static BufferedImage map1;
    public static BufferedImage[] pacmanLogo;
    public static BufferedImage[] pacmanDots;
    public static BufferedImage[] bound;
    public static BufferedImage[] pacmanRight;
    public static BufferedImage[] pacmanLeft;
    public static BufferedImage[] pacmanUp;
    public static BufferedImage[] pacmanDown;
    public static BufferedImage[] pacmanDeath;
    public static BufferedImage[] bigDotBlink;

    public static BufferedImage[] ghostBlinkyRight;
    public static BufferedImage[] ghostBlinkyLeft;
    public static BufferedImage[] ghostBlinkyUp;
    public static BufferedImage[] ghostBlinkyDown;
    public static BufferedImage[] ghostInkyRight;
    public static BufferedImage[] ghostInkyLeft;
    public static BufferedImage[] ghostInkyUp;
    public static BufferedImage[] ghostInkyDown;
    public static BufferedImage[] ghostPinkyRight;
    public static BufferedImage[] ghostPinkyLeft;
    public static BufferedImage[] ghostPinkyUp;
    public static BufferedImage[] ghostPinkyDown;
    public static BufferedImage[] ghostClydeRight;
    public static BufferedImage[] ghostClydeLeft;
    public static BufferedImage[] ghostClydeUp;
    public static BufferedImage[] ghostClydeDown;
    public static BufferedImage[] ghostCanDie;
    public static BufferedImage intro;
    public static BufferedImage start;



    public static BufferedImage galagaImageSheet;
    public SpriteSheet galagaSpriteSheet;

    public static BufferedImage pacmanImageSheet;
    public SpriteSheet pacmanSpriteSheet;

    public Images() {

        startGameButton = new BufferedImage[3];
        pauseResumeButton = new BufferedImage[2];
        pauseToTitleButton = new BufferedImage[2];
        pauseOptionsButton = new BufferedImage[2];
        galagaLogo = new BufferedImage[3];
        galagaPlayer = new BufferedImage[8];//not full yet, only has second to last image on sprite sheet
        galagaPlayerDeath = new BufferedImage[8];
        galagaEnemyDeath = new BufferedImage[5];
        galagaEnemyBee = new BufferedImage[8];
        
        pacmanLogo = new BufferedImage[1];
        pacmanDots = new BufferedImage[2];
        bound = new BufferedImage[16];
        pacmanRight = new BufferedImage[2];
        pacmanLeft = new BufferedImage[2];
        pacmanUp = new BufferedImage[2];
        pacmanDown = new BufferedImage[2];
        bigDotBlink = new BufferedImage[2];
        pacmanDeath = new BufferedImage[12];
        ghostBlinkyRight = new BufferedImage[2];
        ghostBlinkyLeft = new BufferedImage[2];
        ghostBlinkyUp = new BufferedImage[2];
        ghostBlinkyDown = new BufferedImage[2];
        ghostInkyRight = new BufferedImage[2];
        ghostInkyLeft = new BufferedImage[2];
        ghostInkyUp = new BufferedImage[2];
        ghostInkyDown = new BufferedImage[2];
        ghostPinkyRight = new BufferedImage[2];
        ghostPinkyLeft = new BufferedImage[2];
        ghostPinkyUp = new BufferedImage[2];
        ghostPinkyDown = new BufferedImage[2];
        ghostClydeRight = new BufferedImage[2];
        ghostClydeLeft = new BufferedImage[2];
        ghostClydeUp = new BufferedImage[2];
        ghostClydeDown = new BufferedImage[2];
        ghostCanDie = new BufferedImage[4];



        try {

            startGameButton[0]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/NormalStartButton.png"));
            startGameButton[1]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/HoverStartButton.png"));
            startGameButton[2]= ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Start/ClickedStartButton.png"));

            titleScreenBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Title.png"));

            pauseBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Pause.png"));

            selectionBackground = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/Selection.png"));

            galagaCopyright = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/Copyright.png"));

            galagaSelect = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/galaga_select.png"));

            muteIcon = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/mute.png"));

            galagaLogo[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/galaga_logo.png"));
            galagaLogo[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Selection/Galaga/hover_galaga_logo.png"));
            galagaLogo[2] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Selection/Galaga/pressed_galaga_logo.png"));

            pauseResumeButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/Resume/NormalHoverResume.png"));
            pauseResumeButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/Resume/PressedResume.png"));

            pauseToTitleButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToTitle/NormalHoverToTitleButton.png"));
            pauseToTitleButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToTitle/PressedToTitleButton.png"));

            pauseOptionsButton[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToOptions/NormalHoverToOptionsButton.png"));
            pauseOptionsButton[1] = ImageIO.read(getClass().getResourceAsStream("/UI/Buttons/Pause/ToOptions/PressedToOptionsButton.png"));

            galagaImageSheet = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/Galaga/Galaga.png"));
            galagaSpriteSheet = new SpriteSheet(galagaImageSheet);

            galagaPlayer[0] = galagaSpriteSheet.crop(160,55,15,16);

            galagaPlayerDeath[0] = galagaSpriteSheet.crop(209,48,32,32);
            galagaPlayerDeath[1] = galagaSpriteSheet.crop(209,48,32,32);
            galagaPlayerDeath[2] = galagaSpriteSheet.crop(247,48,32,32);
            galagaPlayerDeath[3] = galagaSpriteSheet.crop(247,48,32,32);
            galagaPlayerDeath[4] = galagaSpriteSheet.crop(288,47,32,32);
            galagaPlayerDeath[5] = galagaSpriteSheet.crop(288,47,32,32);
            galagaPlayerDeath[6] = galagaSpriteSheet.crop(327,47,32,32);
            galagaPlayerDeath[7] = galagaSpriteSheet.crop(327,47,32,32);

            galagaEnemyDeath[0] = galagaSpriteSheet.crop(201,191,32,32);
            galagaEnemyDeath[1] = galagaSpriteSheet.crop(223,191,32,32);
            galagaEnemyDeath[2] = galagaSpriteSheet.crop(247,191,32,32);
            galagaEnemyDeath[3] = galagaSpriteSheet.crop(280,191,32,32);
            galagaEnemyDeath[4] = galagaSpriteSheet.crop(320,191,32,32);

            galagaEnemyBee[0] = galagaSpriteSheet.crop(188,178,9,10);
            galagaEnemyBee[1] = galagaSpriteSheet.crop(162,178,13,10);
            galagaEnemyBee[2] = galagaSpriteSheet.crop(139,177,11,12);
            galagaEnemyBee[3] = galagaSpriteSheet.crop(113,176,14,13);
            galagaEnemyBee[4] = galagaSpriteSheet.crop(90,177,13,13);
            galagaEnemyBee[5] = galagaSpriteSheet.crop(65,176,13,14);
            galagaEnemyBee[6] = galagaSpriteSheet.crop(42,178,12,11);
            galagaEnemyBee[7] = galagaSpriteSheet.crop(19,177,10,13);


            galagaPlayerLaser = galagaSpriteSheet.crop(365 ,219,3,8);
            
            pacmanLogo[0] = ImageIO.read(getClass().getResourceAsStream("/UI/Misc/logo.png"));




            //PacMan game sprites
            pacmanImageSheet = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/PacMan/Background.png"));
            pacmanSpriteSheet = new SpriteSheet(pacmanImageSheet);
            map1 = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/PacManMaps/map1.png"));

            pacmanDots[0] = pacmanSpriteSheet.crop(643,18,16,16);
            pacmanDots[1] = pacmanSpriteSheet.crop(623,18,16,16);

            bound[0] = pacmanSpriteSheet.crop(603,18,16,16);//single
            bound[1] = pacmanSpriteSheet.crop(615,37,16,16);//right open
            bound[2] = pacmanSpriteSheet.crop(635,37,16,16);//down open
            bound[3] = pacmanSpriteSheet.crop(655,37,16,16);//left open
            bound[4] = pacmanSpriteSheet.crop(655,57,16,16);//up open
            bound[5] = pacmanSpriteSheet.crop(655,75,16,16);//up/down
            bound[6] = pacmanSpriteSheet.crop(656,116,16,16);//left/Right
            bound[7] = pacmanSpriteSheet.crop(656,136,16,16);//up/Right
            bound[8] = pacmanSpriteSheet.crop(655,174,16,16);//up/left
            bound[9] = pacmanSpriteSheet.crop(655,155,16,16);//down/Right
            bound[10] = pacmanSpriteSheet.crop(655,192,16,16);//down/left
            bound[11] = pacmanSpriteSheet.crop(664,232,16,16);//all
            bound[12] = pacmanSpriteSheet.crop(479,191,16,16);//left
            bound[13] = pacmanSpriteSheet.crop(494,191,16,16);//right
            bound[14] = pacmanSpriteSheet.crop(479,208,16,16);//top
            bound[15] = pacmanSpriteSheet.crop(479,223,16,16);//bottom


            //PacMan sprites
            pacmanRight[0] = pacmanSpriteSheet.crop(473,1,12,13);
            pacmanRight[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanLeft[0] = pacmanSpriteSheet.crop(474,17,12,13);
            pacmanLeft[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanUp[0] = pacmanSpriteSheet.crop(473,34,13,12);
            pacmanUp[1] = pacmanSpriteSheet.crop(489,1,13,13);

            pacmanDown[0] = pacmanSpriteSheet.crop(473,48,13,12);
            pacmanDown[1] = pacmanSpriteSheet.crop(489,1,13,13);
            
            bigDotBlink[0] = pacmanSpriteSheet.crop(647, 22, 8, 8);
            bigDotBlink[1] = pacmanSpriteSheet.crop(655, 22, 8, 8);


            pacmanDeath[0] = pacmanSpriteSheet.crop(489,1,13,13);
            pacmanDeath[1] = pacmanSpriteSheet.crop(505,1,13,13);
            pacmanDeath[2] = pacmanSpriteSheet.crop(520,1,15,13);
            pacmanDeath[3] = pacmanSpriteSheet.crop(536,1,15,13);
            pacmanDeath[4] = pacmanSpriteSheet.crop(552,1,15,13);
            pacmanDeath[5] = pacmanSpriteSheet.crop(568,1,15,13);
            pacmanDeath[6] = pacmanSpriteSheet.crop(584,1,15,13);
            pacmanDeath[7] = pacmanSpriteSheet.crop(601,1,13,13);
            pacmanDeath[8] = pacmanSpriteSheet.crop(617,1,13,13);
            pacmanDeath[9] = pacmanSpriteSheet.crop(633,1,13,13);
            pacmanDeath[10] = pacmanSpriteSheet.crop(649,1,13,13);
            pacmanDeath[11] = pacmanSpriteSheet.crop(665,1,13,13);


            //Ghost sprites
            //Blinky
            ghostBlinkyRight[0] = pacmanSpriteSheet.crop(457,65,14,14);
            ghostBlinkyRight[1] = pacmanSpriteSheet.crop(473,65,14,14);

            ghostBlinkyLeft[0] = pacmanSpriteSheet.crop(489,65,14,14);
            ghostBlinkyLeft[1] = pacmanSpriteSheet.crop(505,65,14,14);

            ghostBlinkyUp[0] = pacmanSpriteSheet.crop(521,65,14,14);
            ghostBlinkyUp[1] = pacmanSpriteSheet.crop(537,65,14,14);

            ghostBlinkyDown[0] = pacmanSpriteSheet.crop(553,65,14,14);
            ghostBlinkyDown[1] = pacmanSpriteSheet.crop(569,65,14,14);

            //Inky
            ghostInkyRight[0] = pacmanSpriteSheet.crop(457,97,14,14);
            ghostInkyRight[1] = pacmanSpriteSheet.crop(473,97,14,14);

            ghostInkyLeft[0] = pacmanSpriteSheet.crop(489,97,14,14);
            ghostInkyLeft[1] = pacmanSpriteSheet.crop(505,97,14,14);

            ghostInkyUp[0] = pacmanSpriteSheet.crop(521,97,14,14);
            ghostInkyUp[1] = pacmanSpriteSheet.crop(537,97,14,14);

            ghostInkyDown[0] = pacmanSpriteSheet.crop(553,97,14,14);
            ghostInkyDown[1] = pacmanSpriteSheet.crop(569,97,14,14);

            //Pinky
            ghostPinkyRight[0] = pacmanSpriteSheet.crop(457,81,14,14);
            ghostPinkyRight[1] = pacmanSpriteSheet.crop(473,81,14,14);

            ghostPinkyLeft[0] = pacmanSpriteSheet.crop(489,81,14,14);
            ghostPinkyLeft[1] = pacmanSpriteSheet.crop(505,81,14,14);

            ghostPinkyUp[0] = pacmanSpriteSheet.crop(521,81,14,14);
            ghostPinkyUp[1] = pacmanSpriteSheet.crop(537,81,14,14);

            ghostPinkyDown[0] = pacmanSpriteSheet.crop(553,81,14,14);
            ghostPinkyDown[1] = pacmanSpriteSheet.crop(569,81,14,14);

            //Clyde
            ghostClydeRight[0] = pacmanSpriteSheet.crop(457,113,14,14);
            ghostClydeRight[1] = pacmanSpriteSheet.crop(473,113,14,14);

            ghostClydeLeft[0] = pacmanSpriteSheet.crop(489,113,14,14);
            ghostClydeLeft[1] = pacmanSpriteSheet.crop(505,113,14,14);

            ghostClydeUp[0] = pacmanSpriteSheet.crop(521,113,14,14);
            ghostClydeUp[1] = pacmanSpriteSheet.crop(537,113,14,14);

            ghostClydeDown[0] = pacmanSpriteSheet.crop(553,113,14,14);
            ghostClydeDown[1] = pacmanSpriteSheet.crop(569,113,14,14);


            //Ghost can die mode
            ghostCanDie[0] = pacmanSpriteSheet.crop(585,65,14,14);
            ghostCanDie[1] = pacmanSpriteSheet.crop(633,65,14,14);
            ghostCanDie[2] = pacmanSpriteSheet.crop(601,65,14,14);
            ghostCanDie[3] = pacmanSpriteSheet.crop(617,65,14,14);

            intro = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/PacMan/intro.png"));
            start = ImageIO.read(getClass().getResourceAsStream("/UI/Backgrounds/startScreen.png"));



        }catch (IOException e) {
        e.printStackTrace();
    }


    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Images.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
