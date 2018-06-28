package application;

import java.util.Date;

public class LocaleDate extends Thread {
	static Date localeDate;
	public LocaleDate() {
		LocaleDate.localeDate=new Date();
		LocaleDate.localeDate.setTime(0);
	}
	void addSec(int n) {
		Date nextdate = new Date(localeDate.getTime() + n*(1000));
		LocaleDate.localeDate=nextdate;	
	}
	public String toString() {
		long a=localeDate.getTime()/1000;
		return a+" sec";
	}
	public void run() {
		while(!interrupted()) {
		try {
			Main.timer.setText(this.toString());
			sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		this.addSec(1);	
		}
	}
}
