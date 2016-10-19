package com.likorn.game;

public class Chronometer implements Runnable{
	
	private int time = 0;
	private CFrame cf;
	private String stime;
	private boolean running = true;

	public Chronometer(){
		cf = new CFrame();
	}
	
	public Chronometer(int i){
		cf = new CFrame();
		this.time = i;
	}
	
	public String cut(int i){
		int m = i / 60;
		int s = i - (60*m);
		String str = new String();
		str = m+":"+s;
		return str;
	}
	
	@Override
	public void run() {
		do{
			stime = cut(time);
			time += 1;
			System.out.println(stime + "  "+time);
			cf.refresh(stime);
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
