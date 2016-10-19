package com.likorn.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TDialog extends JDialog{
	
	private JLabel minutesLabel , secondsLabel;
	private JTextField minutesField , secondsField;
	private int time;
	
	public TDialog(JFrame parent , String title , boolean modal){
		super(parent,title,modal);
		this.setSize(350 , 240);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initComponents();
	}
	
	public int showTimeDialog(){
		this.setVisible(true);
		return this.time;
	}
	
	private void initComponents(){
		
		JPanel minutesPan = new JPanel();
		minutesPan.setBackground(Color.WHITE);
		minutesPan.setPreferredSize(new Dimension(290 , 70));
		minutesPan.setBorder(BorderFactory.createTitledBorder("Minutes"));
		minutesField = new JTextField();
		minutesField.setPreferredSize(new Dimension(100 , 25));
		minutesLabel = new JLabel("Entrez le nombres de minutes : ");
		minutesPan.add(minutesLabel);
		minutesPan.add(minutesField);
		
		JPanel secondsPan = new JPanel();
		secondsPan.setBackground(Color.WHITE);
		secondsPan.setPreferredSize(new Dimension(290 , 70));
		secondsPan.setBorder(BorderFactory.createTitledBorder("Secondes"));
		secondsField = new JTextField();
		secondsField.setPreferredSize(new Dimension(100 , 25));
		secondsLabel = new JLabel("Entrez le nombres de secondes : ");
		secondsPan.add(secondsLabel);
		secondsPan.add(secondsField);
		
		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.add(minutesPan);
		content.add(secondsPan);
		
		JPanel bottomPan = new JPanel();
		JButton okButton = new JButton("OK");
		
		okButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				time = processTime();
				setVisible(false);
			}
			
			public int processTime(){
				int i = 0;
				int sec = 0;
				int min = 0;
				if(!secondsField.getText().equals("")){
					try{
						sec = Integer.parseInt(secondsField.getText());
					} catch(Exception e){
						sec = 5;
					}
				}
				else{
					sec = 0;
				}
				if(!minutesField.getText().equals("")){
					try{
						min = Integer.parseInt(minutesField.getText());
					} catch(Exception e){
						min = 1;
					}
				}
				else{
					min = 0;
				}
				i = (60*min)+sec;
				return i;
			}
		});
		bottomPan.add(okButton);
	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(bottomPan, BorderLayout.SOUTH);
	}
	
}
