package uk.gov.nbn.data.gis.maps.colour;

import java.awt.Color;

/**
 *
 * @author Administrator
 */
public class Band {
    private static final String BAND_REGEX = "[0-9]{4}-[0-9]{4},[0-9a-fA-F]{6},[0-9a-fA-F]{6}";
    
    private final int startYear, endYear;
    private final Color fillColour, outlineColour;
    
    public Band(String band) {
        if(!band.matches(BAND_REGEX)) {
            throw new IllegalArgumentException("The band is not in the correct form");
        }
        String[] bandSplit = band.split(",");
        String[] yearRange = bandSplit[0].split("-");
        
        this.startYear = Integer.parseInt(yearRange[0]);
        this.endYear = Integer.parseInt(yearRange[1]);
        this.fillColour = new Color(Integer.parseInt(bandSplit[1], 16));
        this.outlineColour = new Color(Integer.parseInt(bandSplit[2], 16));
    }

    public String getExpression() {
        return String.format("[STARTDATE] >= %d AND [ENDDATE] <= %d", startYear, endYear);
    }

    public Color getFillColour() {
        return fillColour;
    }

    public Color getOutlineColour() {
        return outlineColour;
    }

}
