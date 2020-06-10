package hoon2woon2;

import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import java.util.HashMap;

/**
 * gowoon-choi
 * TODO comment


// TODO ! 시작하면 게임 인원수 받아서 gamerCount에 할당하기 > gamerBoard에 각 boardPanel객체 할당하기 > 각 유저와 각 보드 연결하기, 즉 userId2boardIndex 내용 할당하기

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
    }

    /**
     * gowoon-choi
     * TODO comment
     */
    void start(){
        this.tetris.startGame();
        while(true){
            client.send(board2String(myBoard));
            receivedString = client.receive();
            if(receivedString!=""&&receivedString!=null){
                string2Board(receivedString);
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
        for(int i=1; i<boardDatas.length; i++){
            String[] tileDatas = boardDatas[i].split(delimiter);
            for(int j=0; j<3; j++){
                board.setTile(Integer.parseInt(tileDatas[0]),Integer.parseInt(tileDatas[1]), TileType.valueOf(tileDatas[2]));
            }
        }
    }

}
