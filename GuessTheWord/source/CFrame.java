package com.likorn.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class CFrame extends JFrame{
	
	public CFrame(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth() - 250;
		this.setLocation((int)width, 1);
		this.setTitle("Chronometer");
		this.setResizable(false);
		this.setSize(250, -5);
		this.setVisible(true);
	}
	
	public void refresh(String s){
		this.setTitle(s);
	}
	
}
