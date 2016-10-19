package com.likorn.multiclip.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.likorn.multiclip.misc.Main;

public class ClipPanel extends JPanel{
	
	private String text = new String();
	
	public ClipPanel(String t){
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		this.text = t;
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		JLabel jl = new JLabel(cutText(this.text));		
		JPanel b1 = new JPanel();
		b1.setLayout(new BoxLayout(b1 , BoxLayout.X_AXIS));	
		b1.add(Box.createHorizontalGlue());
		b1.add(jl);
		b1.add(Box.createHorizontalGlue());
		this.addMouseListener(new MListnr());
		
		JButton copyb = new JButton("Copy");
		copyb.setPreferredSize(new Dimension(75,45));
		copyb.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
			}
		});
		JButton removeb = new JButton("Remove");
		removeb.setPreferredSize(new Dimension(85,45));
		removeb.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				remove();
			}
		});
		JPanel east = new JPanel();
		east.add(copyb);
		east.add(removeb);
		
		this.add(b1,BorderLayout.CENTER);
		this.add(east, BorderLayout.EAST);
	}

	public String cutText(String s){
		if(s.length()>=17)
			return s.substring(0, 15)+" ...";
		return s;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void remove(){
		Main.removePan(this);
	}
	
	class MListnr implements MouseListener{

		MiniWin mw;
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mw = new MiniWin(text);
			Point p = MouseInfo.getPointerInfo().getLocation();
			p.x+=10;
			mw.setLocation(p);
			//Main.setOnTop(false);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mw.dispose();
			//Main.setOnTop(true);
		}

		@Override
		public void mousePressed(MouseEvent e) {	
		}
		@Override
		public void mouseReleased(MouseEvent e) {	
		}
		
	}
	
}
