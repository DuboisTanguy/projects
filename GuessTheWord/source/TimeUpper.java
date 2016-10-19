package com.likorn.game;

import javax.swing.JOptionPane;

public class TimeUpper implements Runnable{
	
	private int time = 65;
	private int timeBackup = time;
	private CFrame cf;
	private String stime;
	private boolean running = true;
	
	public TimeUpper(){
		cf = new CFrame();
	}
	
	public TimeUpper(int i){
		time = i;
		timeBackup = time;
		cf = new CFrame();
	}
	
	public String cut(int i){
		int m = i / 60;
		int s = i - (60*m);
		String str = new String();
		str = m+":"+s;
		return str;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run(){
		do{
			stime = cut(time);
			time -= 1;
			System.out.println(stime + "  "+time);
			cf.refresh(stime);
			if(time <= 0){
				time = timeBackup;
				JOptionPane jop = new JOptionPane();
				jop.showMessageDialog(null, "Temps dépasse, pénalité : +1 coup", "Limite atteinte !", JOptionPane.WARNING_MESSAGE);
				Main.incrCoups();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(running);
	}
	
	public void stop(){
		running = false;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getStime() {
		return stime;
	}
	
}
