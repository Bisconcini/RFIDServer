package api;

import exceptions.RFIDConnectionProblem;
import bri.honeywell.IF1Honeywell;

public abstract class RFID {

	
	public static RFIDReader getRFIDReader(int classe, String uri) throws RFIDConnectionProblem{
		
		switch(classe){
		
		case (0):
				return new IF1Honeywell(uri);
		}
		
		return null;
	}
	
}
