package com.likorn.multiclip.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Window extends JFrame {

	private List<ClipPanel> cpans = new LinkedList<ClipPanel>();
	private JPanel content = new JPanel();
	private JScrollPane scroll = new JScrollPane(content);
	private JPanel bottomPan;
	private JButton addb = new JButton("Add");

	public Window() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icon.png")));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setTitle("Multi-clipboard");
		this.setSize(
				new Dimension(300, 69 * cpans.size() + ((cpans.size() > 1) ? 42 : 50) + ((cpans.size() > 0) ? 0 : 35)));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		//this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = new String();
				Transferable ss = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				boolean hasText = (ss != null) && ss.isDataFlavorSupported(DataFlavor.stringFlavor);
				if (hasText) {
					try {
						s = (String) ss.getTransferData(DataFlavor.stringFlavor);
					} catch (UnsupportedFlavorException | IOException ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}
				ClipPanel cp = new ClipPanel(s);
				addPan(cp);
			}
		});
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		bottomPan = new JPanel();
		addb.setPreferredSize(new Dimension(280, 50));
		bottomPan.add(addb);
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(bottomPan, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	public void addPan(ClipPanel cp) {
		cpans.add(cp);
		this.refreshContent();
	}
	public void removePan(ClipPanel cp) {
		cpans.remove(cp);
		this.getContentPane().remove(content);
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		this.refreshContent();
	}
	public void refreshContent() {
		this.setSize(
				new Dimension(300, 69 * cpans.size() + ((cpans.size() > 1) ? 42 : 50) + ((cpans.size() > 0) ? 0 : 35)));
		addb.setPreferredSize(new Dimension(280, (cpans.size() > 0) ? 25 : 50));
		if (cpans.size() == 0)
			addb.setSize(new Dimension(280, 50));
		for (ClipPanel c : cpans) {
			content.add(c);
		}
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.revalidate();
	}

}
