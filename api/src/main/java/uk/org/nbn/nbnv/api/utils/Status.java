package uk.org.nbn.nbnv.api.utils;

import java.util.HashMap;
import java.util.Map;

public enum Status{
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
