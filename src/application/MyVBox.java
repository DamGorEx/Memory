package application;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class MyVBox extends VBox {
	long score;
	static private boolean accepted;
	public MyVBox(long score) {
		TextArea area= new TextArea();
		Label lb= new Label("Twoj Wynik to:"+score
				+ "Wpisz swoj nickname: ");
		this.score=score;
		Button  save= new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new Result(area.getText(), score);
				 accepted=true;
				 //System.out.println("Wcisniete");
			}
		});
		save.prefWidthProperty().bind(area.widthProperty());

		getChildren().addAll(lb,area,save);
		
	}
	static boolean processFinished() {
		return accepted;
	}
	static void setProcess(boolean a) {
		accepted=a;
	}
	
}
