package lesson01.MiniPr_3;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SimulatingHeatTransfer extends Applet{
	int iwidth, iheight;
	File file;
	BufferedImage img;
	ArrayList<Point> heater;
	HashMap<Point, RGB> temperature;
	HashMap<Point, RGB> newTem;
	double k;
	
	public void init(){
		try{
			file = new File("PictureT1.jpg");
			img = ImageIO.read(file);
		}catch(IOException e){
			e.getMessage();
		}
		
		Point p;
		int clr,red,green,blue;
		iwidth = img.getWidth(this);
		iheight = img.getHeight(this);
		setSize(iwidth, iheight);
		temperature = new HashMap<Point, RGB>();
		newTem = new HashMap<Point, RGB>();
		
		for(int i = 0; i < iheight; i++){
			for(int j = 0; j < iwidth; j++){
				p = new Point(j,i);
				clr = img.getRGB(j,i);
				red = (clr & 0x00ff0000) >> 16;
	    		green = (clr & 0x0000ff00) >> 8;
	    		blue = clr & 0x000000ff;
	    		RGB d = new RGB(red, green, blue);
	    		temperature.put(p,d);
			}
		}
	}
	
	void copy(){
		
	}
	
	void blend(){
		
	}
	
	void findBorder() {
		Point p;
		int red, green, blue;
		RGB color;
		heater = new ArrayList<Point>();
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				color = temperature.get(p);
				red = color.r;
				green = color.g;
				blue = color.b;
				if (red <= 50 && green <= 50 && blue <= 50) {
					if (j == 0 || i == 0 || j == iwidth || i == iheight) {
						continue;
					}
					if (isBorder(j, i)) {
						heater.add(new Point(j, i));
					}
				}
			}
		}
	}
	
	public boolean isBorder(int x, int y) {
		int trueCount = 0;
		trueCount += getBorder(x - 1, y - 1);
		trueCount += getBorder(x, y - 1);
		trueCount += getBorder(x + 1, y - 1);
		trueCount += getBorder(x - 1, y);
		trueCount += getBorder(x + 1, y);
		trueCount += getBorder(x - 1, y + 1);
		trueCount += getBorder(x, y + 1);
		trueCount += getBorder(x + 1, y + 1);

		if (trueCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	int getBorder(int x, int y) {
		if (x < 0 || y < 0 || x > iwidth || y > iheight) {
			return 0;
		}
		Point p = new Point(x, y);
		RGB c = temperature.get(p);

		if (c.r > 50 && c.b > 50 && c.g > 50) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
	public void paint(){
		
	}

}
