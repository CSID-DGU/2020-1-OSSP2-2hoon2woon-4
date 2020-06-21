package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Item_DeleteAllBlocks extends ItemType {
    Item_DeleteAllBlocks(int x, int y, ItemManager itemManager) {
        super(x,y,itemManager);
        this.isBad = false;
        this.message = "Delete All Blocks !!!";
        this.itemIndex = 4;
        this.fileName = "item_clear.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getBoard().clear();
        itemManager.clear();
    }
}
