package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import java.util.HashMap;
import java.util.Random;

/**
 * gowoon-choi
 * TODO comment


public class MultiPlay {
    /**
     * gowoon-choi
     * TODO comment
     */
    private static Client client;
    private static Tetris tetris;
    private static BoardPanel myBoard;

    /**
     * gowoon-choi
     * TODO comment
     */
    private int gamerCount;
    private BoardPanel[] gamersBoard = new BoardPanel[gamerCount];
    private HashMap<String, Integer> userId2boardIndex;

    private String receivedString;

    /**
     * gowoon-choi
     * TODO comment
     */
    MultiPlay(Client c){
        this.client = c;
        this.tetris = new Tetris(client);
        this.myBoard = new BoardPanel(tetris);
        this.tetris.setMode(3);
        this.tetris.setMultiPlay(this);
    }

    /**
     * gowoon-choi
     * TODO comment
     */
    void start(){
        // TODO gamerCount 할당

        for(int i=0; i<gamerCount; i++){
            gamersBoard[i] = new BoardPanel(tetris);
        }

        // TODO 각 유저 아이디와 보드 index 연결하기 > userId2boardIndex

        this.tetris.startGame();
        String delimiter = "\\:";
        String[] datas;
        while(true){
            client.send(board2String(myBoard));
            receivedString = client.receive();
            if(receivedString!=""&&receivedString!=null){
                datas = receivedString.split(delimiter);
                if(datas[0] != client.getUserid()){
                    if(datas[1] == "board"){
                        string2Board(receivedString);
                    }
                    else if(datas[1] == "attack"){
                        addLinebyAttack(receivedString);
                    }
                    else{
                        System.out.println("error : wrong string");
                    }
                }
            }
        }
    }


    /**
     * gowoon-choi
     * TODO comment
     */
    String board2String(BoardPanel board){
        String boardInfo = "";
        String temp = "";
        boardInfo += client.getUserid();
        boardInfo += ":board";
        for(int col=0; col<board.COL_COUNT; col++){
            for(int row=0; row<board.ROW_COUNT; row++){
                if(board.getTile(col,row) != null){
                    temp += col;
                    temp += ",";
                    temp += row;
                    temp += ",";
                    temp += board.getTile(col,row);
                    boardInfo += ":";
                    boardInfo += temp;
                    temp = "";
                }
            }
        }
        return boardInfo;
    }

    /**
     * gowoon-choi
     * TODO ! comment
     */
    void string2Board(String boardInfo){
        BoardPanel board;
        String delimiter = "\\:";
        String[] boardDatas = boardInfo.split(delimiter);
        board = gamersBoard[userId2boardIndex.get(boardDatas[0])];
        board.clear();
        delimiter = "\\,";
        for(int i=2; i<boardDatas.length; i++){
            String[] tileDatas = boardDatas[i].split(delimiter);
            for(int j=0; j<3; j++){
                board.setTile(Integer.parseInt(tileDatas[0]),Integer.parseInt(tileDatas[1]), TileType.valueOf(tileDatas[2]));
            }
        }
    }

    /**
     * gowoon-choi
     * TODO ! comment
     *
     */
    public void attack(int count){
        String line = "";
        line += client.getUserid();
        line += ":attack";
        line += count;
        client.send(line);
    }


    void addLinebyAttack(String attackInfo){
        String delimiter = "\\:";
        String datas[] = attackInfo.split(delimiter);
        int line = Integer.parseInt(datas[2]);
        for(int row = 1; row < myBoard.ROW_COUNT; row++) {
            for(int col = 0; col < myBoard.COL_COUNT; col++) {
                myBoard.setTile(col, row - line, myBoard.getTile(col, row));
            }
        }

        int randomNum = randomNumberGenerator();
        for(int i=0; i<line; i++){
            for(int col=0; col<myBoard.COL_COUNT; col++){
                if(col == randomNum) continue;
                myBoard.getTiles()[myBoard.ROW_COUNT-1][col] = TileType.values()[8];
            }
        }
    }

    int randomNumberGenerator(){
        int randomNum;
        Random random = new Random();
        randomNum = random.nextInt(myBoard.COL_COUNT);
        return randomNum;
    }
}
