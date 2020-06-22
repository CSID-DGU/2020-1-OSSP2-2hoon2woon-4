package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Item_AddOneLine extends ItemType {
    Item_AddOneLine(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = false;
        this.message = "Add One Line ...";
        this.itemIndex = 1;
        this.fileName = "item_unremovable.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getBoard().addUnTypeX();
    }

}
