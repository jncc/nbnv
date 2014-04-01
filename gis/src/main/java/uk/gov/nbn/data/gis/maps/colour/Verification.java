package uk.gov.nbn.data.gis.maps.colour;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Posts can contain verification arguments that are either like this 'verification=1'
 * or like this 'verification=1,0000ff,000000'.  The former is used for filtering only,
 * the latter is for drawing layers coloured by their verification status.  This class
 * models that information.
 */
public class Verification {
    private static final String VERIFICATION_REGEX = "[1-4](,[0-9a-fA-F]{6},[0-9a-fA-F]{6})?";
    
    private final Status status;
    private final boolean coloured;
    private final Color fillColour, outlineColour;
    
    public Verification(String verification) {
        if(!verification.matches(VERIFICATION_REGEX)) {
            throw new IllegalArgumentException("The verification is not in the correct form");
        }
	if(verification.length() > 1){
            String[] data = verification.split(",");
	    this.coloured = true;
	    this.status = Status.get(Integer.parseInt(data[0]));
            this.fillColour = new Color(Integer.parseInt(data[1], 16));
            this.outlineColour = new Color(Integer.parseInt(data[2], 16));
	}else{
	    this.coloured = false;
	    this.status = Status.get(Integer.parseInt(verification));
	    this.fillColour = null;
	    this.outlineColour = null;
	}
    }

    public String getExpression() {
        return String.format("[STATUS] = %s ", status.getStatus());
    }

    public Status getStatus(){
	return status;
    }
    
    public boolean isColoured(){
	return coloured;
    }
    
    public Color getFillColour() {
        return fillColour;
    }

    public Color getOutlineColour() {
        return outlineColour;
    }
    
    public static enum Status{
	VERIFIED("VERIFIED",1),
	INCORRECT("INCORRECT",2),
	UNCERTAIN("UNCERTAIN",3),
	UNVERIFIED("UNVERIFIED",4);
	
	private final String status;
	private final Integer key;
	private static final Map<Integer,Status> lookup = new HashMap<Integer,Status>();
	
	static{
	    for(Status status : Status.values()){
		lookup.put(status.key, status);
	    }
	}
	
	Status(String status, Integer key){
	    this.status = status;
	    this.key = key;
	}
	
	public String getStatus(){
	    return status;
	}
	
	public Integer getKey(){
	    return key;
	}
	
	public static Status get(Integer verificationKey){
	    return lookup.get(verificationKey);
	}
	
    }
    
}
