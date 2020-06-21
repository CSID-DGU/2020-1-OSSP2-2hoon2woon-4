package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Item_ReverseKey extends ItemType {
    Item_ReverseKey(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = true;
        this.message = "Just 30 Seconds, Reverse Left and Right Key ...";
        this.itemIndex = 3;
        this.fileName = "item_reverse.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getTetris().setReverseTimer(System.currentTimeMillis());
        itemManager.getTetris().setReverseIndex(true);
    }
}

