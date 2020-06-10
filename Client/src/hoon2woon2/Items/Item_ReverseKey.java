package hoon2woon2.Items;

public class Item_ReverseKey extends ItemType {
    Item_ReverseKey(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = true;
        this.message = "Just 30 Seconds, Reverse Left and Right Key ...";
        this.itemIndex = 3;
    }

    @Override
    public void action() {
        itemManager.getTetris().setReverseTimer(System.currentTimeMillis());
        itemManager.getTetris().setReverseIndex(true);
    }
}

