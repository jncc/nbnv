/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.feature.recorded;

import nbn.common.feature.Feature;

/**
 *
 * @author Administrator
 */
public interface RecordedSite<T extends Feature> {
    public T getUnderlyingFeature();
}
