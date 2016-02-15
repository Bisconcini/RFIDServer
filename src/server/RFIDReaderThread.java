package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import api.RFIDReader;
import api.Tag;
import exceptions.ReadTagException;

public class RFIDReaderThread implements Runnable{
	
	private final RFIDReader rfidReader;
	private final int repeatTime;
	Map<String, Tag> mapPoll = new ConcurrentHashMap<>();
	
	
	public RFIDReaderThread(RFIDReader r, int repeatTime){
		if (r == null){
			throw new RuntimeException("RFIDReader cannot be null");
		}
		this.rfidReader = r;
		this.repeatTime = repeatTime;
	}

	@Override
	public void run() {
		if (rfidReader.start()){
			for ( ; ; ){
			try {
				synchronized(this){ // ninguem mais le enquanto tiver aqui gravando
					Map<String, Tag> map = rfidReader.read();
					mapPoll.putAll(map); // nao vai permitir adicionar repetidos
				}
				Thread.currentThread().sleep(1000*repeatTime);
			} catch (ReadTagException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} // end for
			
		
		} // end if
	}
	
	public synchronized Map<String, Tag> pollTags(){
		Map<String, Tag> temp = new ConcurrentHashMap();
		temp.putAll(mapPoll); // salvo temp
		mapPoll.clear(); // limpo a outra
		return temp;
	}

}
