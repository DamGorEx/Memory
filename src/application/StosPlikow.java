package application;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class StosPlikow {
LinkedList <File> list;
int indicator;
	public StosPlikow() {
		list= new LinkedList<File>();
		for(int a=0;a<Main.iloscElementow;a++) {
			list.add(new File("/Users/DamianGoraj/Desktop/Proj2/Obrazki/"+this.indicator+".png"));
			list.add(new File("/Users/DamianGoraj/Desktop/Proj2/Obrazki/"+this.indicator+".png"));
			indicator++;
		}
		indicator=list.size()-1;
		swap();
	}

	File take() {
		return list.get(indicator--); 
	}
	void swap() {
		for(int a=0;a<list.size()*2;a++) {
		Random rand= new Random();
		Collections.swap(list, rand.nextInt(list.size()), rand.nextInt(list.size()));
		}
	}

}
