package lesson01.MiniPr_3;

import java.awt.Point;

import javax.swing.JOptionPane;

import gpjava.Canvas;

public class ER_model {
	
	public static void main(String[] args){
	    Canvas.show();
	    String input = JOptionPane.showInputDialog("nを入力");
	    int n = Integer.parseInt(input);
	    Point[] p = new Point[n];
	    for(int i = 0; i < n; i++){
//	    	int x = 0;
//	    	int y = 
//	    	p[i] = 
	    }
	    Canvas.drawLine(0, 0, 15, 10);
	}	

}
