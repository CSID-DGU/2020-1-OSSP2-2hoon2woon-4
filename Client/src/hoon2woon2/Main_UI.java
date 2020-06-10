package hoon2woon2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main_UI extends JFrame{
    public Main_UI()
    {
        super("J프레임 테스트");  //프레임의 타이틀 지정
        setSize(500,500);        //컨테이너 크기 지정
        setVisible(true);        //창을 보이게함
    }

    public static void main(final String[] args) {

        final Main_UI t = new Main_UI();
        t.setDefaultCloseOperation(EXIT_ON_CLOSE);    //종료 이벤트 

    }

}
