package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import javax.swing.*;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * gowoon-choi
 * TODO comment
*/

public class MultiPlay {
    /**
     * gowoon-choi
     * TODO comment
     */
    private static String line = "";
    private static Client client;
    private static Tetris tetris;
    private static BoardPanel myBoard;

    /**
     * gowoon-choi
     * TODO comment
     */
    private static int gamerCount;
    private BoardPanel[] gamersBoard;
    private HashMap <String,Boolean> status=new HashMap();  
    private HashMap<String, Integer> userId2boardIndex=new HashMap();
    private String receivedString;

    /**
     * gowoon-choi
     * TODO comment
     */
  	
    
    MultiPlay(Client c,Tetris tetris){
        this.client = c;
        System.out.println("111111111111111111111111111111111");
        this.tetris = tetris;
        
        gamerCount = c.getGamerCount();
        
        this.tetris.setGamerCount(gamerCount); 
        // JOptionPane.showMessageDialog(null, gamerCount);
        // System.out.println(gamerCount);
        gamersBoard = new BoardPanel[gamerCount];

        int j = 1;

        for(int i=0; i<gamerCount; i++){
            status.put(client.userList.elementAt(i),true);
            if(client.userList.elementAt(i).equals(client.getUserid())){
                continue;
            }
            else{
                gamersBoard[j] = new BoardPanel(tetris);
                userId2boardIndex.put(client.userList.elementAt(i), j); //각 유저 아이디와 보드 index 연결하기
                gamersBoard[j].setMultiplay(this);
                gamersBoard[j++].setUserId(client.userList.elementAt(i)); // 나빼고 매핑
            }
        }
        myBoard = this.tetris.board;
        gamersBoard[0] = myBoard;

        int bWidth = myBoard.getWidth();
        int bHeight = myBoard.getHeight();
       
        
        
        this.tetris.setSize(200+bWidth*gamerCount+10*gamerCount,bHeight);

        this.tetris.board.setLocation(0,0); 
        this.tetris.add(this.tetris.board);

        int x = bWidth;		  
       
        this.tetris.boards = gamersBoard;
        this.tetris.rankvisible(false);
        
        for(int i = 1; i <	 gamerCount; i++) {
        		this.tetris.boards[i].setLocation(x+10,0);
        		this.tetris.add(this.tetris.boards[i]);
        		x =x+bWidth+10;
        }
        
        this.tetris.side.setLocation(x+10,0);
		this.tetris.add(this.tetris.side);
        
      this.tetris.repaint();
      this.tetris.setMultiPlay(this);
        // this.myBoard = new BoardPanel(tetris);
      this.tetris.setMode(3);
        start();
        //TODO : 게임 시작
    }

    /**
     * gowoon-choi
     * TODO comment
     */
    void start(){
        this.tetris.resetGame();
        String delimiter = "\\:";
        String[] datas;
        int index = 0;
        while(true){
            index ++;
            if(index == 50){
                if(tetris.isGameOver()){
                    client.send(line);
                }
                else{
                    if(line.equals(""))
                        client.send(board2String(myBoard));
                    else
                    {
                        client.send(line);
                        line = "";
                    }
                }
                receivedString = client.receive();
                receivedString = receivedString.replaceAll("\0", "");
                System.out.println(receivedString);
                if(receivedString!=""&&receivedString!=null){
                    datas = receivedString.split(delimiter);
                    if(datas[0].equals("gameend")){
                        // TODO RANKING
                        String message = "";
                        for (int i=0; i<gamerCount; i++){
                            message += "RANK";
                            message += i;
                            message += datas[1+i];
                            message +="\n";
                        }
                        JOptionPane.showMessageDialog(null, message );
                    }
                    if(datas[0] != client.getUserid() && datas.length > 1){
                        if(datas[1].equals("board")){
                            string2Board(receivedString);
                        }
                        else if(datas[1].equals("attack")){
                            System.out.println("test attack");
                            addLinebyAttack(receivedString);
                        }
                        else if (datas[2].equals("dead")){
                            // TODO 이 유저의 보드 status false로
                            status.put(datas[0],false);
                            gamersBoard[userId2boardIndex.get(datas[0])].isOver = false;
                            gamersBoard[userId2boardIndex.get(datas[0])].repaint();
                        }
                        else{
                            System.out.println("error : wrong string");
                        }
                    }
                }
                index = 0;
            }
        }
    }


    /**
     * gowoon-choi
     * TODO comment
     */
    String board2String(BoardPanel board){
        String boardInfo = "";
        boardInfo += client.getUserid();
        boardInfo += ":board:";
        for(int col=0; col<board.COL_COUNT; col++){
            for(int row=2; row<board.ROW_COUNT; row++){
                if(board.getTile(col,row) == null){
                    boardInfo += "0";
                }
                else{
                    boardInfo += board.getTile(col,row).toString().substring(4);
                }
            }
        }
        boardInfo += ">";
        boardInfo += tetris.getPieceType().toString() + ":" + tetris.getPieceCol() + ":"+ tetris.getPieceRow() + ":"+ tetris.getPieceRotation();
        return boardInfo;
    }

    /**
     * gowoon-choi
     * TODO ! comment
     */
    void string2Board(String boardInfo){
        BoardPanel board;
        String delimiter = "\\>";
        String[] Datas = boardInfo.split(delimiter);
        delimiter = "\\:";
        String[] boardDatas = Datas[0].split(delimiter);
        board = gamersBoard[userId2boardIndex.get(boardDatas[0])];
        board.clear();
        for(int i=0; i<board.COL_COUNT*board.VISIBLE_ROW_COUNT; i++){
            if(boardDatas[2].charAt(i) != '0'){
                board.setTile(i/20, i%20 + 2,TileType.valueOf("Type"+boardDatas[2].charAt(i)));
            }
        }
        String[] current = Datas[1].split(delimiter);
        board.setType(TileType.valueOf(current[0]));
        board.setPieceCol(Integer.parseInt(current[1]));
        board.setPieceRow(Integer.parseInt(current[2]));
        board.setRotation(Integer.parseInt(current[3]));
        board.repaint();
    }

    /**
     * gowoon-choi
     * TODO ! comment
     *
     */
    public void attack(int count){
        line = "";
        line += client.getUserid();
        line += ":attack:";
        line += count;
    }


    void addLinebyAttack(String attackInfo){
        String delimiter = "\\:";
        String datas[] = attackInfo.split(delimiter);
        int line = Integer.parseInt(datas[2]);
        for(int row = line; row < myBoard.ROW_COUNT; row++) {
            for(int col = 0; col < myBoard.COL_COUNT; col++) {
                myBoard.setTile(col, row - line, myBoard.getTile(col, row));
            }
        }

        int randomNum = randomNumberGenerator();
        System.out.println(randomNum);
        for(int i=0; i<line; i++){
            for(int col=0; col<myBoard.COL_COUNT; col++){
                if(col == randomNum){
                    System.out.println();
                    myBoard.setTile(col, myBoard.ROW_COUNT-1-i,null);
                    continue;
                }
                myBoard.setTile(col, myBoard.ROW_COUNT-1-i,TileType.values()[8]);
            }
        }
    }

    int randomNumberGenerator(){
        int randomNum;
        Random random = new Random();
        randomNum = random.nextInt(myBoard.COL_COUNT);
        return randomNum;
    }

    
    public int getGamerCount() {
    	return this.gamerCount;
    }
    
    public BoardPanel getBoard(int ind) {
	return this.gamersBoard[ind];
    }


    public void finishGame(){
        line = "die";
    }

    public void afterFinishGame(){
        line = "dead";
    }
    
    public HashMap getStatus() {
    	return status;
    }

    public void setStatus(String userId) {
        status.put(userId,false);
        gamersBoard[userId2boardIndex.get(userId)].isOver = false;
    }
}
