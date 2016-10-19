package rangement;

import static java.nio.file.StandardCopyOption.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String select = new String(" ");
		Scanner input = new Scanner(System.in);
		String choix = new String(" ");
		int exit = 0;

		select = input.nextLine();
		
		File folder = new File(select);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
			}

		ecrire("Ranger maintenant ? [oui/non]");
		do{
		choix=input.nextLine();
		
		if(choix.equals("oui")){
			for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			    	  if (listOfFiles[i].getName().lastIndexOf(".jpg")>0||listOfFiles[i].getName().lastIndexOf(".png")>0){   
				        	Path source = Paths.get(select + "\\" +listOfFiles[i].getName());
				        	Path target = Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Pictures\\"+listOfFiles[i].getName());
				        	ecrire(source + " --> " + target);
				        	Files.move(source, target, REPLACE_EXISTING);
				        	exit=1;
			    	  }
			    	  else if(listOfFiles[i].getName().lastIndexOf(".mp3")>0||listOfFiles[i].getName().lastIndexOf(".ogg")>0||listOfFiles[i].getName().lastIndexOf(".wav")>0){
				        	Path source = Paths.get(select + "\\" +listOfFiles[i].getName());
				        	Path target = Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\Music\\"+listOfFiles[i].getName());
				        	ecrire(source+" --> "+target);
				        	Files.move(source, target, REPLACE_EXISTING);
				        	exit=1;
				        }
			      }
			}
		}
		
		else if (choix.equals("non")){
			exit = 1;
		}
		else{
			ecrire("Reponse non reconnue");
		}
		}while(exit==0);
	}
	
	public static void ecrire(String txt){
		System.out.println(txt);
	}

}
