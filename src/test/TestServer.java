package test;

import java.util.Map.Entry;

import server.MainServer;
import server.RFIDReaderThread;
import api.RFID;
import api.Tag;
import exceptions.RFIDConnectionProblem;

public class TestServer {

	public static void main(String[] args) throws RFIDConnectionProblem {
		MainServer server = new MainServer();
		server.getConnections().put("192.168.102.42", new RFIDReaderThread(RFID.getRFIDReader(0, "tcp://192.168.103.42"), 3));
		server.getConnections().put("192.168.102.45", new RFIDReaderThread(RFID.getRFIDReader(0, "tcp://192.168.103.42"), 3));
		server.getConnections().put("192.168.102.46", new RFIDReaderThread(RFID.getRFIDReader(0, "tcp://192.168.103.42"), 3));
		new Thread(server).start();
		
		for(; ; ){
			for (Entry<String, Tag> e : server.getAllTagsPoll().entrySet()){
				System.out.println(e.getValue().getID());
			}
		}
		
		
	}

}
