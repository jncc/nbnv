package uk.gov.nbn.data.gis.maps.colour;

import java.util.ArrayList;
import java.util.List;

/**
 * The following class represents a bucket which can be used in MapFiles to aid
 * the creation of MapServer CLASS's which represent buckets. By using the static
 * methods, it is possible to create a list of buckets from an array of ints
 * which represents the breaks in the buckets
 * @author Christopher Johnson
 */
public class Bucket {
    private final String name;
    private final String expression;
    public Bucket(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }
    
    public static List<Bucket> getBucketsFromValues(String item, int... values) {
        List<Bucket> toReturn = new ArrayList<Bucket>();
        
        for(int i=0; i<values.length-1; i++) {
            int currValue = values[i], nextValue = values[i+1]-1;
            if(currValue > nextValue) {
                throw new IllegalArgumentException("The values in the array are not in consecutive order");
            }
            else if(currValue == nextValue) { //create a single value bucket
                toReturn.add(new Bucket(
                        Integer.toString(currValue), 
                        String.format("[%s] = %d", item, currValue)));
            }
            else { //create a range bucket
                toReturn.add(new Bucket(
                        String.format("%d - %d", currValue, nextValue),
                        String.format("[%s] >= %d AND [%s] <= %d", item, currValue, item, nextValue)));
            }
        }
        
        return toReturn;
    }
    
    public static List<Bucket> getOpenEndedBucketFromValues(String item, int... values) {
        List<Bucket> toReturn = getBucketsFromValues(item, values);
        int lastValue = values[values.length-1];
        toReturn.add(new Bucket( //add an open ended bucket
                String.format("%d -", lastValue), String.format("[%s] > %d",item, lastValue)));
        return toReturn;
    }
}
