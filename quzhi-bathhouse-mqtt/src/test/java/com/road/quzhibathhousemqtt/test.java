package com.road.quzhibathhousemqtt;

public class test {

	static Object lock = new Object();

	public static void main(String[] args) throws InterruptedException {
		
		synchronized(lock){
			System.out.println("start");
		    lock.wait(2000);
		    System.out.println("end");
		}
	}
	 

	
}

