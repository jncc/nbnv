package uk.gov.nbn.data.gis.maps.cache;

import uk.ac.ceh.dynamo.bread.Climate;

/**
 *
 * @author Christopher Johnson
 */
public class FixedClimate implements Climate {

    @Override
    public double getCurrentClimate() {
        return 1;
    }

}
