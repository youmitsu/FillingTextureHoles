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
import javax.swing.JOptionPane;

public class ShepardsInterpolation extends Applet {
	int iwidth, iheight;
	File file;
	BufferedImage img;
	ArrayList<Point> input;
	boolean[] idsList;
	HashMap<Point, RGB> allData;

	public void init() {
		try {
			file = new File("PictureT1.jpg");
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.getMessage();
		}

		Point p;
		int clr, red, green, blue;
		iwidth = img.getWidth();
		iheight = img.getHeight();
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
		doShepards();
		while (input.size() != 0) {
			findBorder();
			doShepards();
		}

	}

	void findBorder() {
		Point p;
		int red, green, blue;
		RGB color;
		input = new ArrayList<Point>();
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
					if (border(j, i)) {
						input.add(new Point(j, i));
					}
				}
			}
		}
//		System.out.println("ボーダーの数："+input.size());
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
		if (x < 0 || y < 0 || x > iwidth || y > iheight) {
			return 0;
		}
		Point p = new Point(x, y);
		RGB d;
		d = allData.get(p);

//		if(d.r != 255){
		if (d.r > 50 && d.b > 50 && d.g > 50) {
			return 1;
		} else {
			return 0;
		}
	}

	void doShepards() {
		RGB color;
		for (int i = 0; i < input.size(); i++) {
			// System.out.println("r:" + allData.get(input.get(i)).r + "g:"
			// + allData.get(input.get(i)).g + "b:"
			// + allData.get(input.get(i)).b);
			color = getShepards(input.get(i));
			// System.out.println("r:" + color.r + "g:" + color.g + "b:" +
			// color.b);
			allData.put(input.get(i), color);
		}
	}

	RGB getShepards(Point p) {
		double[] wi;
		RGB[] fi;
		idsList = new boolean[8];
		fi = fi(p);
		wi = wi(p.x, p.y);
		int red = 0, green = 0, blue = 0;
		for (int i = 0; i < fi.length; i++) {
			// System.out.println(wi[i]);
			// System.out.println("r"+fi[i].r+"g"+fi[i].g+"b"+fi[i].b);
			if (idsList[i]) {
				red += wi[i] * fi[i].r;
				green += wi[i] * fi[i].g;
				blue += wi[i] * fi[i].b;
			}

		}
		// System.out.println(red+","+green+","+blue);
		return new RGB(red, green, blue);

	}

	RGB[] fi(Point p) {
		RGB[] fi = new RGB[8];
		Point p1 = new Point(p.x - 1, p.y - 1);
		Point p2 = new Point(p.x, p.y - 1);
		Point p3 = new Point(p.x + 1, p.y - 1);
		Point p4 = new Point(p.x - 1, p.y);
		Point p5 = new Point(p.x + 1, p.y);
		Point p6 = new Point(p.x - 1, p.y + 1);
		Point p7 = new Point(p.x, p.y + 1);
		Point p8 = new Point(p.x + 1, p.y + 1);
		fi[0] = new RGB(allData.get(p1).r, allData.get(p1).g, allData.get(p1).b);
		fi[1] = new RGB(allData.get(p2).r, allData.get(p2).g, allData.get(p2).b);
		fi[2] = new RGB(allData.get(p3).r, allData.get(p3).g, allData.get(p3).b);
		fi[3] = new RGB(allData.get(p4).r, allData.get(p4).g, allData.get(p4).b);
		fi[4] = new RGB(allData.get(p5).r, allData.get(p5).g, allData.get(p5).b);
		fi[5] = new RGB(allData.get(p6).r, allData.get(p6).g, allData.get(p6).b);
		fi[6] = new RGB(allData.get(p7).r, allData.get(p7).g, allData.get(p7).b);
		fi[7] = new RGB(allData.get(p8).r, allData.get(p8).g, allData.get(p8).b);
		for (int i = 0; i < fi.length; i++) {
//			if(fi[i].r == 255){
			if (fi[i].r <= 50 && fi[i].g <= 50 && fi[i].b <= 50) {
				idsList[i] = false;
			} else {
				idsList[i] = true;
			}
		}

		return fi;
	}

	double[] wi(int x, int y) {
		double[] wi = new double[8];
		double[] hi = hi(x, y);
		wi[0] = w(hi[0], hi);
		wi[1] = w(hi[1], hi);
		wi[2] = w(hi[2], hi);
		wi[3] = w(hi[3], hi);
		wi[4] = w(hi[4], hi);
		wi[5] = w(hi[5], hi);
		wi[6] = w(hi[6], hi);
		wi[7] = w(hi[7], hi);
		return wi;
	}

	double w(double hi, double[] hj) {
		double sum = 0;
		for (int i = 0; i < hj.length; i++) {
			if (idsList[i]) {
				sum += Math.pow(hj[i], -2);
			}
		}
		return (Math.pow(hi, -2) / sum);
	}

	double[] hi(int x, int y) {
		double[] hi = new double[8];
		hi[0] = h(x, y, x - 1, y - 1);
		hi[1] = h(x, y, x, y - 1);
		hi[2] = h(x, y, x + 1, y - 1);
		hi[3] = h(x, y, x - 1, y);
		hi[4] = h(x, y, x + 1, y);
		hi[5] = h(x, y, x - 1, y + 1);
		hi[6] = h(x, y, x, y + 1);
		hi[7] = h(x, y, x + 1, y + 1);
		return hi;
	}

	double h(int x, int y, int xi, int yi) {
		double h = Math.sqrt(((x - xi) * (x - xi)) + ((y - yi) * (y - yi)));
		return h;
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