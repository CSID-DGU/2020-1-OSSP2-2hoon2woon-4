package hoon2woon2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.psnbtech.*;

import javafx.event.ActionEvent;

public class Main_UI extends JFrame{
	public static Tetris tetris;
	public static Client client;

    
    public Main_UI()
    {
        super("J프레임 테스트");  //프레임의 타이틀 지정
        setSize(500,500);        //컨테이너 크기 지정
        setVisible(true);        //창을 보이게함
		// client = new Client();
        // tetris = new Tetris(client);
    }

    public static void main(String[] args) {
        JFrame Main_UI = new Main_UI();
        JButton startB = new JButton();
        Main_UI.add(startB);
    }
}
