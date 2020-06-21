package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Item_DoubleScore extends ItemType {
    Item_DoubleScore(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = false;
        this.message = "Just 30 Seconds, Double Score !!!";
        this.itemIndex = 6;
        this.fileName = "item_doublescore.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getTetris().setScoreTimer(System.currentTimeMillis());
        itemManager.getTetris().setScoreIndex(true);
    }

}
