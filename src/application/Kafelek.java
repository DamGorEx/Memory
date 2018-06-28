package application;

import java.io.File;
import java.util.LinkedList;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Kafelek extends Rectangle implements Comparable<Kafelek>, EventHandler<MouseEvent> {
	ImagePattern rewers;
	ImagePattern awers;
	ImagePattern active;
	File sourFile;
	static StosPlikow stos= new StosPlikow();
	static LinkedList <Kafelek> checker= new  <Kafelek>LinkedList();

	public Kafelek(int a, int b) {
		super(a,b);
		this.rewers= new ImagePattern(Main.img);
		this.active=this.rewers;
		this.setFill(active);
		this.sourFile=stos.take();
		this.awers=Kafelek.awersFactory(this.sourFile);
	}
	static ImagePattern awersFactory(File a) {
		Image img = new Image(a.toURI().toString());
		return new ImagePattern(img);
	}
	boolean flip() {
		if(this.active.equals(rewers)) this.active=this.awers;
		else this.active=this.rewers;
		super.setFill(this.active);
		return true;
	}
	@Override
	public int compareTo(Kafelek arg0) {
		if(this.sourFile.hashCode()==arg0.sourFile.hashCode()) {
			return 0;
		}else return 1;
	}
	@Override
	public void  handle(MouseEvent event) {
		if(this.getFill()!=Color.GREEN&&active!=awers) {
		this.flip();
		checker.add(this);
		}
	}
}



