package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Item_DisableRotate extends ItemType {
    Item_DisableRotate(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = true;
        this.message = "Just 30 Seconds, Disable Rotate ...";
        this.itemIndex = 2;
        this.fileName = "item_disablerotation.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getTetris().setRotaionTimer(System.currentTimeMillis());
        itemManager.getTetris().setRotationIndex(false);
    }
}
