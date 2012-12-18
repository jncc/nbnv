package uk.gov.nbn.data.gis.maps.colour;

import java.awt.Color;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

/**
 * The following class aids in the creation of generation colour gradients
 * @author Christopher Johnson
 */
@Component
public class ColourHelper {    
    private final MessageDigest md5;
    
    public ColourHelper() throws NoSuchAlgorithmException {
        md5 = MessageDigest.getInstance("md5");
    }
    
    /**
     * The following method will generate a Colour based upon some given id 
     * @param id
     * @return A Colour which corresponds to the given id
     */
    public Color getColour(String id) {
        byte[] digest = md5.digest(id.getBytes());
        return new Color(digest[0] & 0xFF, digest[1] & 0xFF, digest[2] & 0xFF);
    }
    
    public static Color getMidColour(int i, int amount, Color start, Color end) {
        return new Color(
                    getMidValue(i, amount, start.getRed(),      end.getRed()),
                    getMidValue(i, amount, start.getGreen(),    end.getGreen()),
                    getMidValue(i, amount, start.getBlue(),     end.getBlue())
                );
    }
    
    private static int getMidValue(int position, int amount, int start, int end) {
        return start - ((start-end) * position)/amount;
    }
    
    public static class ColourRampGenerator {
        private final Color start, end;
        public ColourRampGenerator(Color start, Color end) {
            this.start = start;
            this.end = end;
        }
        
        public Color getColour(int i, int amount) {
            return getMidColour(i, amount, start, end);
        }
    }
}
