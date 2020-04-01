package Game.PacMan.entities.Statics;

import Main.Handler;
import Resources.Images;

public class Fruit extends BaseStatic{
    public Fruit(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.fruits[1]);
    }
}