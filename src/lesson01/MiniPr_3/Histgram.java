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

public class Histgram extends Applet {
	int iwidth, iheight; // 画像の幅、高さ
	File file;
	BufferedImage img; // 画像
	HashMap<Point, RGB> allData;
	ArrayList<Point> borders; // 境界の座標を保持
	boolean[] idsList;
	ArrayList<ArrayList<Integer>> bin = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> c0;
	ArrayList<Integer> c1;
	ArrayList<Integer> c2;
	ArrayList<Integer> c3;
	ArrayList<Integer> c4;
	ArrayList<Integer> c5;
	ArrayList<Integer> c6;
	ArrayList<Integer> c7;

	public void init() {
		try {
			file = new File("PictureT1.jpg");
			img = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.getMessage();
		}
		int clr, red, green, blue;
		Point p;
		iwidth = img.getWidth(this);
		iheight = img.getHeight(this);
		setSize(iwidth, iheight);
		allData = new HashMap<Point, RGB>();

		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				clr = img.getRGB(j, i);
				red = (clr & 0x00ff0000) >> 16;
				green = (clr & 0x0000ff00) >> 8;
				blue = clr & 0x000000ff;
				RGB d = new RGB(red, green, blue);
				allData.put(p, d);
			}
		}
		findBorder();
		histgram();
		while (borders.size() != 0) {
			findBorder();
			histgram();
		}
	}

	void findBorder() {
		Point p;
		int red, green, blue;
		RGB color;
		borders = new ArrayList<Point>();
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				color = allData.get(p);
				red = color.r;
				green = color.g;
				blue = color.b;
	//			if(red == 255){
				if (red <= 50 && green <= 50 && blue <= 50) {
					if (j == 0 || i == 0 || j == iwidth || i == iheight) {
						continue;
					}
					if (isBorder(j, i)) {
						borders.add(new Point(j, i));
					}
				}
			}
		}
		System.out.println(borders.size());
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
		RGB c = allData.get(p);
		
//        if(c.r != 255){
		if (c.r > 50 && c.b > 50 && c.g > 50) {
			return 1;
		} else {
			return 0;
		}
	}

	void isCanSelected(Point p) {
		int x = p.x;
		int y = p.y;
		RGB[] c = new RGB[8];
		c[0] = allData.get(new Point(x - 1, y - 1));
		c[1] = allData.get(new Point(x, y - 1));
		c[2] = allData.get(new Point(x + 1, y - 1));
		c[3] = allData.get(new Point(x - 1, y));
		c[4] = allData.get(new Point(x + 1, y));
		c[5] = allData.get(new Point(x - 1, y + 1));
		c[6] = allData.get(new Point(x, y + 1));
		c[7] = allData.get(new Point(x + 1, y + 1));
		for (int i = 0; i < c.length; i++) {
//			if(c[i].r == 255){
			if (c[i].r <= 50 && c[i].g <= 50 && c[i].b <= 50) {
				idsList[i] = false;
			} else {
				idsList[i] = true;
			}
		}
	}

	void histgram() {
		for (int i = 0; i < borders.size(); i++) {
			doHistgram(borders.get(i));
		}
	}

	void doHistgram(Point p) {
		idsList = new boolean[8];
		isCanSelected(p);
		int r, g, b;
		int[] nr = new int[8];
		int[] ng = new int[8];
		int[] nb = new int[8];

		nr = rNeighbor(p);
		assign(nr);
		decideColor();
		r = decideColor();

		ng = gNeighbor(p);
		assign(ng);
		decideColor();
		g = decideColor();

		nb = bNeighbor(p);
		assign(nb);
		decideColor();
		b = decideColor();

		allData.put(p, new RGB(r, g, b));
	}

	int decideColor() {
		int max = 0;
		ArrayList<Integer> color = new ArrayList<Integer>();
		int averageValue = 0;
		if (c0.size() > max) {
			max = c0.size();
			color = c0;
		}if (c1.size() > max) {
			max = c1.size();
			color = c1;
		}if (c2.size() > max) {
			max = c2.size();
			color = c2;
		}if (c3.size() > max) {
			max = c3.size();
			color = c3;
		}if (c4.size() > max) {
			max = c4.size();
			color = c4;
		}if (c5.size() > max) {
			max = c5.size();
			color = c5;
		}if (c6.size() > max) {
			max = c6.size();
			color = c6;
		}if (c7.size() > max) {
			max = c7.size();
			color = c7;
		}

		for (int i = 0; i < color.size(); i++) {
			averageValue += color.get(i);
		}
		averageValue /= color.size();

		return averageValue;

	}
	
	void assign(int[] c) {
		int color;
		c0 = new ArrayList<Integer>();
		c1 = new ArrayList<Integer>();
		c2 = new ArrayList<Integer>();
		c3 = new ArrayList<Integer>();
		c4 = new ArrayList<Integer>();
		c5 = new ArrayList<Integer>();
		c6 = new ArrayList<Integer>();
		c7 = new ArrayList<Integer>();
		for (int i = 0; i < c.length; i++) {
			if (idsList[i]) {
				if (c[i] >= 0 && c[i] <= 31) {
					c0.add(c[i]);
				} else if (c[i] >= 32 && c[i] <= 63) {
					c1.add(c[i]);
				} else if (c[i] >= 64 && c[i] <= 95) {
					c2.add(c[i]);
				} else if (c[i] >= 96 && c[i] <= 127) {
					c3.add(c[i]);
				} else if (c[i] >= 128 && c[i] <= 159) {
					c4.add(c[i]);
				} else if (c[i] >= 160 && c[i] <= 191) {
					c5.add(c[i]);
				} else if (c[i] >= 192 && c[i] <= 223) {
					c6.add(c[i]);
				} else if (c[i] >= 224 && c[i] <= 255) {
					c7.add(c[i]);
				}
			}
		}

	}

	int[] rNeighbor(Point p) {
		int[] c = new int[8];
		c[0] = allData.get(new Point(p.x - 1, p.y - 1)).r;
		c[1] = allData.get(new Point(p.x, p.y - 1)).r;
		c[2] = allData.get(new Point(p.x + 1, p.y - 1)).r;
		c[3] = allData.get(new Point(p.x - 1, p.y)).r;
		c[4] = allData.get(new Point(p.x + 1, p.y)).r;
		c[5] = allData.get(new Point(p.x - 1, p.y + 1)).r;
		c[6] = allData.get(new Point(p.x, p.y + 1)).r;
		c[7] = allData.get(new Point(p.x + 1, p.y + 1)).r;
		return c;
	}

	int[] gNeighbor(Point p) {
		int[] c = new int[8];
		c[0] = allData.get(new Point(p.x - 1, p.y - 1)).g;
		c[1] = allData.get(new Point(p.x, p.y - 1)).g;
		c[2] = allData.get(new Point(p.x + 1, p.y - 1)).g;
		c[3] = allData.get(new Point(p.x - 1, p.y)).g;
		c[4] = allData.get(new Point(p.x + 1, p.y)).g;
		c[5] = allData.get(new Point(p.x - 1, p.y + 1)).g;
		c[6] = allData.get(new Point(p.x, p.y + 1)).g;
		c[7] = allData.get(new Point(p.x + 1, p.y + 1)).g;
		return c;
	}

	int[] bNeighbor(Point p) {
		int[] c = new int[8];
		c[0] = allData.get(new Point(p.x - 1, p.y - 1)).b;
		c[1] = allData.get(new Point(p.x, p.y - 1)).b;
		c[2] = allData.get(new Point(p.x + 1, p.y - 1)).b;
		c[3] = allData.get(new Point(p.x - 1, p.y)).b;
		c[4] = allData.get(new Point(p.x + 1, p.y)).b;
		c[5] = allData.get(new Point(p.x - 1, p.y + 1)).b;
		c[6] = allData.get(new Point(p.x, p.y + 1)).b;
		c[7] = allData.get(new Point(p.x + 1, p.y + 1)).b;
		return c;
	}

	public void paint(Graphics g) {
		Point p;
		g.drawImage(img, 0, 0, iwidth, iheight, this);
		for (int i = 0; i < iheight; i++) {
			for (int j = 0; j < iwidth; j++) {
				p = new Point(j, i);
				g.setColor(new Color(allData.get(p).r, allData.get(p).g,
						allData.get(p).b));
				g.fillRect(j, i, 1, 1);
			}
		}
	}

}