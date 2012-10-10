package uk.gov.nbn.data.gis.maps;

import java.awt.Color;

/**
 * The following class aids in the creation of generation colour gradients
 * @author Christopher Johnson
 */
public class ColourHelper {    
    public static Color getHuedColour(int i, int amount, float saturation, float brightness) {
        return Color.getHSBColor((float)i/(float)amount, saturation, brightness);
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
