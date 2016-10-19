package com.bluesky.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics gs){
		int xl = this.getWidth()/4;
		int yl = this.getHeight()/4;
		Graphics2D g = (Graphics2D) gs;
		GradientPaint gp = new GradientPaint(0 , 0 , Color.YELLOW , 0 , 30 ,Color.BLACK , true);
		g.setPaint(gp);
		g.fillOval(xl, yl, this.getWidth()/2 , this.getHeight()/2);
		g.drawOval(0, 0 ,this.getWidth() , this.getHeight());
		g.drawRect(xl, yl, this.getWidth()/2, this.getHeight()/2);
		g.drawLine(0, 0, this.getWidth(), this.getHeight());
		g.drawLine(0, this.getHeight(), this.getWidth(), 0);
		
		Font font = new Font("Courier", Font.BOLD , 20);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString("Fenêtre par le meilleur DEV du monde", 10, 20);
		
		try {
			Image img  = ImageIO.read(new File("banana.gif"));
			g.drawImage(img, 0, 0, this.getWidth() , this.getHeight() ,this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
