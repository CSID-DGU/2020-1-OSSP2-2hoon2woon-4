package hoon2woon2.Items;

public class Item_DoubleScore extends ItemType {
    Item_DoubleScore(int x, int y, ItemManager itemManager) {
        super(x, y, itemManager);
        this.isBad = false;
        this.message = "Just 30 Seconds, Double Score !!!";
        this.itemIndex = 6;
    }

    @Override
    public void action() {
        itemManager.getTetris().setScoreTimer(System.currentTimeMillis());
        itemManager.getTetris().setScoreIndex(true);
    }

}
