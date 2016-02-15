package test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import api.RFID;
import api.RFIDReader;
import api.Tag;
import exceptions.RFIDConnectionProblem;
import exceptions.ReadTagException;

public class Test {

	public static void main(String[] args) throws RFIDConnectionProblem, ReadTagException, InterruptedException {

		RFIDReader reader = RFID.getRFIDReader(0, "tcp://192.168.103.42");
		reader.start();
		Thread.currentThread().sleep(1000*2);

		for (int x = 0 ; x <= 10 ; x++){
			Map <String, Tag> map = reader.read();
			

			for (Entry<String, Tag> e :  map.entrySet()){
				System.out.println(e.getKey());
			}
			Thread.sleep(1000*5);
		}
		reader.stop();

	}

}
