package com.togb.finance;

import java.io.File;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Hello world!
 *
 */
public class App {
	private DownloadCSV downloadCSV = new DownloadCSV();
	private CSVToExcel csvToExcel = new CSVToExcel();
	public static void main(String[] args) {
		new App().doTask();
	}
	
	private void doTask(){
		csvToExcel.getCSV().subscribe(new Action1<File>() {

			public void call(File t) {
				System.out.println(t.getName());
				
			}
		});
	}
}
