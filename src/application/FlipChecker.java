package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FlipChecker extends Thread {
	int iloscKafelkowWGrze;
	long score;
	LocaleDate date;
	private Scene enterData;
	private VBox vbox1;
	private Text text1;
	boolean finish;
	 public FlipChecker() {
		iloscKafelkowWGrze=Main.iloscElementow;
		date=new LocaleDate();
		vbox1=new VBox();
		vbox1.setAlignment(Pos.CENTER);
		date.start();
		this.start();
		this.finish=false;
	}
	@Override
	public void run() {
		while(!interrupted()) {
			if(iloscKafelkowWGrze>0) {
			if(Kafelek.checker.size()>=2) {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					return;
				}
					if(Kafelek.checker.get(0).compareTo(Kafelek.checker.get(1))==0) {
						Kafelek.checker.get(0).setFill(Color.GREEN);
						Kafelek.checker.get(1).setFill(Color.GREEN);
						Kafelek.checker.remove(0);
						Kafelek.checker.remove(0);
						iloscKafelkowWGrze-=1;
					}else {
						Kafelek.checker.get(0).flip();
						Kafelek.checker.get(1).flip();
						Kafelek.checker.remove(0);
						Kafelek.checker.remove(0);
					}
				}
			}else {
				date.interrupt();
				this.finish=true;
				this.interrupt();
			}
		}
	}	
}
