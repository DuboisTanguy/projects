package com.bluesky.gui;

import javax.swing.JFrame;

public class Window extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Animation a = new Animation();

	public Window(){
		this.setTitle("TEST GUI");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setContentPane(a);
		this.setVisible(true);
		startAnimation();
	}
	
	private void startAnimation(){
		
		boolean backX = false;
		boolean backY = false;
		
		int x = a.getPosX() , y = a.getPosY();
		
		while(true){
			
			if(x<1){
				backX = false;
			}
			if(x > a.getWidth()-50){
				backX = true;
			}
			if(y<1){
				backY = false;
			}
			if(y > a.getHeight()-50){
				backY = true;
			}
			if(!backX){
				a.setPosX(++x);
			}
			else{
				a.setPosX(--x);
			}
			if(!backY){
				a.setPosY(++y);
			}
			else{
				a.setPosY(--y);
			}
			
			a.repaint();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
