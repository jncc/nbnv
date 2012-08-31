/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping.ims;

/**
 *
 * @author Administrator
 */
public enum MapService {
    GRID_GB ("gridGBv4"),
    GRID_IRELAND ("gridIrelandv4"),
    WS_GB ("webServiceGB"),
    WS_IRELAND ("webServiceIreland");

    private final String mapServiceName;

    private MapService(String service) {
        this.mapServiceName = service;
    }

    public String getServiceName() {
        return this.mapServiceName;
    }
}
