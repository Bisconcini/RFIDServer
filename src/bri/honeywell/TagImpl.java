package bri.honeywell;

import api.Tag;

public class TagImpl implements Tag{
	
	private final String ID;
	
	public TagImpl(String ID){
		this.ID = ID;
	}

	@Override
	public String getID() {
		return ID;
	}
	
	
	
	

}
