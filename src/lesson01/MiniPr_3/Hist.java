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

public class Hist extends Applet {

	int iwidth, iheight; // 画像の幅、高さ
	File file;
	BufferedImage drimg; // 画像
	HashMap<Point, Boolean> allBrank;
	HashMap<Point, RGB> allRGB;
	ArrayList<Point> borders; // 境界の座標を保持
	ArrayList<RGB> rgb; // 境界のRgbを保持

	ArrayList<RGB> colors;

	public void init() {
		try {
			file = new File("9RED.bmp");
			drimg = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.getMessage();
		}
		int clr, red, green, blue;
		Point p;
		iwidth = drimg.getWidth(this);
		iheight = drimg.getHeight(this);
		borders = new ArrayList<Point>();
		allBrank = new HashMap<Point, Boolean>();
		allRGB = new HashMap<Point, RGB>();
		setSize(iwidth,iheight);
		
		System.out.println(iwidth);
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				clr = drimg.getRGB(j, i);
				red = (clr & 0x00ff0000) >> 16;
				green = (clr & 0x0000ff00) >> 8;
				blue = clr & 0x000000ff;
				allRGB.put(p, (new RGB(red, green, blue)));
			}
		}
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				clr = drimg.getRGB(j, i);
				red = (clr & 0x00ff0000) >> 16;
				green = (clr & 0x0000ff00) >> 8;
				blue = clr & 0x000000ff;
				allRGB.put(p, (new RGB(red, green, blue)));
				allBrank.put(p, true);
				if(red == 50){
//				if (red <= 50 && green <= 50 && blue <= 50) {
					if ((j == 0 || i == 0 || j == iwidth || i == iheight)) {
						continue;
					}
					allBrank.put(p, false);

					if (border(j, i)) {
						borders.add(new Point(j, i)); // 境界を保持
						allRGB.put(p, hist(j, i));
					}
				}
			}
		}
		while(research());

	}

	public boolean research() {
		int red, green, blue;
		Point p;
		boolean end = false;
		borders = new ArrayList<Point>();

		for (int i = 0; i < drimg.getHeight(); i++) {
			for (int j = 0; j < drimg.getWidth(); j++) {
				p = new Point(j, i);
				if (allBrank.get(p)) {
					continue;
				}
				end = true;

				red = allRGB.get(p).r;
				green = allRGB.get(p).g;
				blue = allRGB.get(p).b;
				if(red == 50){
//				if (red <= 50 && green <= 50 && blue <= 50) {					
					if ((j == 0 || i == 0 || j == iwidth || i == iheight)) {
						continue;
					}
					allBrank.put(p, false);
					if (border(j, i)) {
							borders.add(p); // 境界を保持
							allRGB.put(p, hist(j, i));
						}
					}
			}
		}
		return end;
		

	}

	public boolean border(int x, int y) {
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
		Point p = new Point(x,y);
		RGB c = allRGB.get(p);
		if (c == null) {
			return 0;
		}

		if (c.r > 50 && c.b > 50 && c.g > 50) {
			return 1;
		} else {
			return 0;
		}
	}

	RGB hist(int x, int y) {
		Point p = new Point(x, y);
		if (!allBrank.get(p)) {
			allBrank.put(p, true);
		}

		RGB[] c = new RGB[8];
		int aveR = 0;
		int aveG = 0;
		int aveB = 0;
		int dr = 0;
		int dg = 0;
		int db = 0;

		c[0] = allRGB.get(new Point(x - 1, y - 1));
		c[1] = allRGB.get(new Point(x, y - 1));
		c[2] = allRGB.get(new Point(x + 1, y - 1));
		c[3] = allRGB.get(new Point(x - 1, y));
		c[4] = allRGB.get(new Point(x + 1, y));
		c[5] = allRGB.get(new Point(x - 1, y + 1));
		c[6] = allRGB.get(new Point(x, y + 1));
		c[7] = allRGB.get(new Point(x + 1, y + 1));
		for (int i = 0; i < 8; i++) {
			if (c[i].r != 50) {
				aveR += c[i].r;
				dr += 1;
			}
			if (c[i].g != 50) {
				aveG += c[i].g;
				dg += 1;
			}
			if (c[i].b != 50) {
				aveB += c[i].b;
				db += 1;
			}
		}
		if (dr == 0) {
			dr = 1;
		}
		if (dg == 0) {
			dg = 1;
		}
		if (db == 0) {
			db = 1;
		}

		aveR /= dr;
		aveG /= dg;
		aveB /= db;
		return new RGB(aveR, aveG, aveB);

	}

	public void paint(Graphics g) {
		Point p;
		g.drawImage(drimg,0,0,iwidth,iheight,this);
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				g.setColor(new Color(allRGB.get(p).r, allRGB.get(p).g, allRGB
						.get(p).b));
				g.fillRect(j, i, 1, 1);
			}
		}
	}


}