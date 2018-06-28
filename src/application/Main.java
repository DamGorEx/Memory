package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


public class Main extends Application {
	static Text timer;
	Scene paneSize,playGame, scoreCard, Exit; 
	static Kafelek lastMove;
    static File file = new File("/Users/DamianGoraj/Desktop/Proj2/Obrazki/Icon.jpg");
    static Image img = new Image(file.toURI().toString());
	static int iloscElementow;
	static int flipcounter=0;
	static ArrayList<Result> resultConteiner = new ArrayList<Result>();
	static int boardSize;
	static BorderPane root;
	static BorderPane root2;
    static MyVBox enterNickName;
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		try(ObjectInputStream in= new ObjectInputStream(new FileInputStream (new File("/Users/DamianGoraj/Desktop/Result.obj")))){
			resultConteiner=(ArrayList<Result>) in.readObject();
			System.out.println(resultConteiner);

		} catch (FileNotFoundException e) {
			System.out.println("Nie ma pliku");
		}
		
			root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			
			VBox vbox1=new VBox();
			vbox1.setAlignment(Pos.CENTER);
			vbox1.getChildren().addAll(new Text("Witaj w grze Memory"),new Text("s180885"));
			BorderPane.setAlignment(vbox1, Pos.CENTER);
			root.setTop(vbox1);
			
			Scene scene = new Scene(root,200,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			    final KeyCombination kb = new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_DOWN);
			    public void handle(KeyEvent ke) {
			        if (kb.match(ke)) {
			        	primaryStage.close();
			        	try {
							start(primaryStage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			    }
			});
			
			ComboBox<String> combo1= new <String>ComboBox<String>();
			combo1.getItems().addAll("2x2","4x4","6x6");
			
			Button newGame= new Button("New game");
			Button highScore= new Button("High score");
			Button exit= new Button("Exit");
			exit.minWidthProperty().bind(highScore.widthProperty());
			VBox vbox2= new VBox(); // dla gry
			vbox2.setAlignment(Pos.CENTER);
			root.setCenter(vbox2);
			vbox2.getChildren().addAll(newGame,highScore,exit);
			VBox vbox3= new VBox(); // dla wyboru planszy
			vbox3.setAlignment(Pos.CENTER);
			vbox3.getChildren().addAll(new Text("Wybierz wielkosc planszy"),combo1);
			Button ok = new Button("OK");
			ok.setOnAction(n->{
				boardSize=Character.getNumericValue(combo1.getValue().charAt(0));
				Main.iloscElementow=(boardSize*boardSize)/2;
				TilePane tilepane = new TilePane();
				tilepane.setHgap(10);
				tilepane.setVgap(10);
				tilepane.setPadding(new Insets(10,10,10,10));
				tilepane.setMaxWidth(Control.USE_PREF_SIZE);
				for(int a=0;a<iloscElementow*2;a++) {
					Kafelek rect = new Kafelek(50,50); // Tworzenie kafelkow
					rect.setOnMouseClicked(rect); // 
					tilepane.getChildren().add(rect);
				} 
				 Kafelek.stos= new StosPlikow();

				tilepane.setPrefColumns(boardSize);
				tilepane.setPrefRows(boardSize);
				
				try {
					root2 = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
					timer= new Text("Timer");
					BorderPane.setAlignment(timer, Pos.CENTER);
					root2.setTop(timer);
					root2.setCenter(tilepane);
					playGame=new Scene(root2);
					
					primaryStage.setScene(playGame);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				FlipChecker flip=new FlipChecker(); // Uruchamiam warunek sprawdzajacy, czas rusza 

				
				final Task<MyVBox> task = new Task<MyVBox>() {

				    @Override
				    public MyVBox call() throws Exception {
						while(flip.finish==false) {
							Thread.sleep(1000);
						}
							long score= Main.boardSize*flip.date.localeDate.getTime()/1000;
						if(flip.finish==true) {
							System.out.println("done");
							tilepane.getChildren().removeAll();
							flip.finish=false;
							return new MyVBox(score) ;
						}else {
							return null;
						}
				    }
				};
		
				Thread t = new Thread(task);
				t.setDaemon(true); // thread will not prevent application shutdown
				t.start();
				
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				    @Override
				    public void handle(WorkerStateEvent event) {
				    	MyVBox result = task.getValue(); // result of computation
				    	if(result!=null)
				        primaryStage.setScene(new Scene(result));

				}});
				
				final Task<Boolean> task2 = new Task<Boolean>() {
					
				    @Override
				    public Boolean call() throws Exception {
						while(MyVBox.processFinished()==false) {
							Thread.sleep(1);
						}
						MyVBox.setProcess(false);
						return true;
				    }
				};
				Thread checkIfSaved = new Thread(task2);
				checkIfSaved.setDaemon(true); // thread will not prevent application shutdown
				checkIfSaved.start();
				task2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
				    		root.setTop(vbox1);
				    		root.setCenter(vbox2);
				    		primaryStage.setScene(scene);
						try(ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream (new File("/Users/DamianGoraj/Desktop/Result.obj")))){
							out.writeObject(resultConteiner);
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				});
			});
	
			vbox3.getChildren().add(ok);
			newGame.setOnAction(n->{
				root.setCenter(vbox3);
			});
			highScore.setOnAction(n->{
		
				ObservableList<Result> data = FXCollections.observableArrayList();
				for (Result r: resultConteiner) {
					data.add(r);
				}
			     TableColumn nickname = new TableColumn("Nick Name");
			     nickname.setCellValueFactory(
			    		    new PropertyValueFactory<Result,String>("nickname")
			    		);
			     TableColumn score = new TableColumn("Nick Name");
			     score.setCellValueFactory(
			    		    new PropertyValueFactory<Result,Long>("score")
			    		);
	
			     TableView<Result> resultable = new TableView<Result>();
	
				 resultable.setItems(data);
				 resultable.getColumns().addAll(nickname,score);
				root.setCenter(resultable);
				
				root.setTop(new VBox(new Text("Tablica Wynikow:")));
				
			});
			exit.setOnAction(n->{
				primaryStage.close();
			});

			primaryStage.setScene(scene);
			primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
