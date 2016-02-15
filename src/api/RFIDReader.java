package api;

import java.util.Map;

import exceptions.ReadTagException;

public interface RFIDReader {

	
	public boolean start();
	
	public boolean stop();
	
	public Map<String, Tag> read() throws ReadTagException;
	
}
