package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;

public class Result implements Comparable<Result>,Serializable  {
transient private SimpleStringProperty nickname;
transient private SimpleLongProperty score;
	
	public Result(String nickname, long score) {
		this.nickname= new SimpleStringProperty(nickname);
		this.score=new SimpleLongProperty(score);
		Main.resultConteiner.add(this);
		Main.resultConteiner.sort(null);
		
	}
	public String getNickname() {
		return nickname.get();
	}
	public Long getScore() {
		return score.get();
	}
    private void writeObject(ObjectOutputStream s) throws IOException {
    		s.defaultWriteObject();
    		s.writeUTF(getNickname());
    		s.writeLong(getScore());
    }
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    		this.nickname=new SimpleStringProperty(s.readUTF());
    		this.score=new SimpleLongProperty(s.readLong());
    				}
	
	@Override
	public int compareTo(Result arg0) {
		return (int) (arg0.score.get()-score.get());
	}
}
