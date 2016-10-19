package com.likorn.multiclip.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.likorn.multiclip.misc.Main;

public class MiniWin extends JFrame{
	
	public MiniWin(String s){
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel content = new JPanel();
		content.setBackground(Color.getHSBColor(179, 46, 95));
		content.setLayout(new BoxLayout(content,BoxLayout.PAGE_AXIS));
		
		s = s.replace("\t", "         ");
		
		String[] ss = s.split("\n");
		
		this.setSize(ss[Main.getMaxOf(ss)].length()*8, ss.length*13);
		
		for(String str : ss){
			JLabel jtf = new JLabel(str);
			jtf.setPreferredSize(new Dimension(this.getWidth() , 13));
			content.add(jtf);
		}
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
}
