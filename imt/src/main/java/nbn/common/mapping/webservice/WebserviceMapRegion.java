/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.webservice;

import nbn.common.mapping.ims.MapService;

/**
 *
 * @author Administrator
 */
public enum WebserviceMapRegion {
    GBIreland (MapService.WS_GB),
    GB (MapService.WS_GB),
    Ireland (MapService.WS_IRELAND);

    private final MapService _mapService;

    private WebserviceMapRegion(MapService service) {
        this._mapService = service;
    }

    public MapService getMapService() {
        return this._mapService;
    }
}
