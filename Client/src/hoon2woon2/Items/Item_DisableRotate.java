package hoon2woon2.Items;

public class Item_DisableRotate extends ItemType {
    Item_DisableRotate(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = true;
        this.message = "Just 30 Seconds, Disable Rotate ...";
        this.itemIndex = 2;
    }

    @Override
    public void action() {
        itemManager.getTetris().setRotaionTimer(System.currentTimeMillis());
        itemManager.getTetris().setRotationIndex(false);
    }
}
