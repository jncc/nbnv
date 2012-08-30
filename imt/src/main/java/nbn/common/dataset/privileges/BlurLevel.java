/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset.privileges;

import java.util.EnumSet;
import nbn.common.feature.Resolution;

/**
 *
 * @author Administrator
 */
public enum BlurLevel {
    NO_ACCESS (0, null),
    BLUR_10KM (1, Resolution._10km),
    BLUR_2KM (2, Resolution._2km),
    BLUR_1KM (3, Resolution._1km),
    BLUR_100M (4, Resolution._100m);

    private final int blurLevelCode;
    private Resolution resolution;

    private BlurLevel(int code, Resolution resolution) {
        this.blurLevelCode = code;
        this.resolution = resolution;
    }

    public int getBlurLevelCode() {
        return this.blurLevelCode;
    }

    /*If returns null, that means that there is no access*/
    public Resolution getResolutionBlurredTo() {
        return resolution;
    }

    public String getName() {
        if(resolution == null)
            return "No Access";
        else
            return resolution.getName();
    }

    static BlurLevel getBlurLevelByCode(int code) {
        for (BlurLevel b : EnumSet.allOf(BlurLevel.class))
            if (b.getBlurLevelCode() == code)
                return b;
        throw new RuntimeException(new IllegalArgumentException("getBlurLevelByCode: Illegal code {code = " + String.valueOf(code) + "}"));
    }
}
