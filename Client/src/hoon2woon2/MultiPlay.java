package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import java.util.HashMap;

public class MultiPlay {
    private static Client client;
    private static Tetris tetris;
    private static BoardPanel myBoard;

    private int gamerCount;
    private BoardPanel[] gamersBoard = new BoardPanel[gamerCount];
    private HashMap<String, Integer> userId2boardIndex;

    MultiPlay(Client c){
        this.client = c;
        this.tetris = new Tetris(client);
        this.myBoard = new BoardPanel(tetris);
    }

    void start(){
        while(true){
            
        }
    }

    String Board2String(BoardPanel board){
        String boardInfo = "";
        String temp = "";
        boardInfo += client.getUserid();
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

    void String2Board(String boardInfo){
        BoardPanel board;
        String delimiter = "\\:";
        String[] boardDatas = boardInfo.split(delimiter);
        board = gamersBoard[userId2boardIndex.get(boardDatas[0])];
        board.clear();
        delimiter = "\\,";
        for(int i=1; i<boardDatas.length; i++){
            String[] tileDatas = boardDatas[i].split(delimiter);
            for(int j=0; j<3; j++){
                board.setTile(Integer.parseInt(tileDatas[0]),Integer.parseInt(tileDatas[1]), TileType.valueOf(tileDatas[2]));
            }
        }
    }

}
