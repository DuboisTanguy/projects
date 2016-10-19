package com.likorn.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

public class Main {
	
	private static int coups;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		Thread t;
		String everything = new String();
		Set<String> set = new HashSet<String>();
		try(BufferedReader br = new BufferedReader(new FileReader("dico.txt"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "ERREUR !");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "ERREUR !");
		}
		String[] dico = everything.split(" ");
		int index = (int) (Math.random() * dico.length );
		String rands = dico[index];
		System.out.println(rands);
		boolean good = false;
		char[] c = rands.toCharArray();
		HashMap<Integer , Boolean> hm = new HashMap<Integer , Boolean>();
		hm = resetMap(hm , c);
		coups = 0;
		JOptionPane jop = new JOptionPane();
		jop.showMessageDialog(null , "Bienvenue ! ");
		String[] choix = {"Chronomètre" , "Compte-à-rebour" , "Rien"};
		int choice = jop.showOptionDialog(null, "Veuillez choisir une option : " , "Réglages" , JOptionPane.YES_NO_CANCEL_OPTION ,
					 JOptionPane.QUESTION_MESSAGE , null , choix , choix[2]);
		if(choice == 1){
			TDialog td = new TDialog(null , "Enter time" , true);
			TimeUpper tu = new TimeUpper(td.showTimeDialog());
			t=new Thread(tu);
			t.start();
		}
		else if(choice==0){
			Chronometer tu = new Chronometer();
			t=new Thread(tu);
			t.start();
		}
		do{
			int bon = 0 , comp = 0;
			String hint = new String();
			for(int i = 0 ; i< c.length ; i++){
				if( !(hm.get(i)) ){
					hint = hint +". ";
				}
				else{
					hint = hint + c[i] +" ";
				}
			}
			String comprises = new String();
			for(int i = 0 ; i<set.size() ; i++){
				String[] setarray = new String[set.size()];
				set.toArray(setarray);
				comprises += setarray[i]+" ";
			}
			boolean test = false;
			String entered = new String();
			do{
				entered = jop.showInputDialog(null, "Entrez un mot :\n(But : deviner le mot choisi par la machine)\n"+rands.length() +" lettres :  "+hint + "  |  " +"lettres mal placée(s) : "+ comprises);
				hm = resetMap(hm , c);
				set = new HashSet<String>();
				if(entered!=null){
					test = true;
				}
				else{
					System.exit(0);
				}
			}while(!test);
			if(entered.length() == rands.length()){
				coups++;
				for(int i = 0 ; i< c.length ; i++){
					char[] c2 = entered.toCharArray();
					if(c[i] == c2[i]){
						hm.put(i, true);
						bon++;
					}
					else if(rands.contains(c2[i]+"")){
						if(rands.toCharArray()[i] != c2[i])
							comp++;
							set.add(c2[i]+"");
					}
				}
				if(entered.equals(rands)){
					good = true;
					jop.showMessageDialog(null, "BRAVO ! Vous avez trouvé en "+coups+" coups !");
					System.exit(0);
				}
				else{
					jop.showMessageDialog(null, "BILAN\n"+bon+" lettres bonnes\n"+comp+" lettres mal placée(s)\n"+coups+" coups");
				}
			}
			else{
				jop.showMessageDialog(null, "Le mot entré ne fait pas la même longueur ! Veuillez rentrer un mot de la même taille !");
			}
		}while(!good);
	}
	
	public static HashMap<Integer , Boolean> resetMap(HashMap<Integer , Boolean> hm , char[] c){
		hm = new HashMap<Integer , Boolean>();
		for(int i = 0 ; i< c.length ; i++){
			hm.put(i, false);
		}
		return hm;
	}
	
	public static void incrCoups(){
		coups++;
	}
}
