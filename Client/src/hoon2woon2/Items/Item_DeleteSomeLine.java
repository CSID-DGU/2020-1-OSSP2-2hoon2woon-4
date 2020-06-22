package hoon2woon2.Items;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Item_DeleteSomeLine extends ItemType {
    Item_DeleteSomeLine(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = false;
        this.message = "Delete Some Line !!!";
        this.itemIndex = 5;
        this.fileName = "item_removeline.PNG";
        try {
            this.image = ImageIO.read(new File(path + fileName));
        }catch (IOException e){

        }
    }

    @Override
    public void action() {
        itemManager.getBoard().removeUnTypeX();
        itemManager.getBoard().removeLine();
    }

}
