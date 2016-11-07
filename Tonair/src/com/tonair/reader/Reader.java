package com.tonair.reader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.tonair.interpreter.Interpreter;

public class Reader {

	private String fileName = "";
	private File file;

	public Reader() {
		//
	}

	public Reader(String fileName) {
		this.fileName = fileName;
	}

	@SuppressWarnings("resource")
	public void queryFileNameInput() {
		String name = "";
		do {
			Scanner input = new Scanner(System.in);
			System.out.println("Please enter name of the file to open :");
			name = input.nextLine();
		} while (!checkCorrect(name));
		this.fileName = name;
		this.file = new File(fileName);
	}

	public boolean checkCorrect(String s) {
		File f = new File(s);
		if (!f.exists()&&f.isFile())
			System.out.println("The file " + s + " doesn't exists !");
		return (f.exists()&&f.isFile());
	}

	public String readAndExec(String[] args) {
		if(file==null)
			file = new File(fileName);
		List<String> instructions = FileReader.getInstructions(file, args);
		Interpreter i = new Interpreter();
		for(String s : instructions){
			if(s.equals("%STOP%"))break;
			i.exec(s);
		}
		return i.getReturnedValue();
	}

}
