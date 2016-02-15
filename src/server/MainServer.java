package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import api.Tag;


public class MainServer implements Runnable {
	
	public static final Map<String, RFIDReaderThread> connections = new HashMap<>();
	private Map<String, Tag> allTagsPoll = new ConcurrentHashMap();
	
	
	

	public synchronized Map<String, Tag> getAllTagsPoll() {
		Map<String, Tag> temp = new ConcurrentHashMap<>();
		temp.putAll(allTagsPoll);
		allTagsPoll.clear();
		return temp;
	}



	public static Map<String, RFIDReaderThread> getConnections() {
		return connections;
	}



	@Override
	public void run() {
		for (Entry<String, RFIDReaderThread> e : connections.entrySet()){
			new Thread(e.getValue()).start(); // novo Thread e starta
			System.out.println(e.getKey()+ " STARTED "+Thread.currentThread().getName());
		}
		for (; ;){
			
			for (Entry<String, RFIDReaderThread> e : connections.entrySet()){
				allTagsPoll.putAll(e.getValue().pollTags()); // add todas as tags
			}
			
		}
		
	}
	
	

}
