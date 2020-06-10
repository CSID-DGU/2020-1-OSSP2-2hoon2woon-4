package hoon2woon2.Items;

public class Item_DeleteSomeLine extends ItemType {
    Item_DeleteSomeLine(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = false;
        this.message = "Delete Some Line !!!";
        this.itemIndex = 5;
    }

    @Override
    public void action() {
        itemManager.getBoard().removeUnremovableLine();
        itemManager.getBoard().removeLine();
    }

}
