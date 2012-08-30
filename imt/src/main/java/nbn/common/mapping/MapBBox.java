/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.mapping;

/**
 *
 * @author Administrator
 */
public class MapBBox {
    public static final MapBBox GBExtents = new MapBBox(0, 0, 700000, 1300000);
    public static final MapBBox IrelandExtents = new MapBBox(0, 0, 400000, 500000);


    private double _minX;
    private double _minY;
    private double _maxX;
    private double _maxY;

    /**
     * @return the _minX
     */
    public double getMinX() {
        return _minX;
    }

    /**
     * @param minX the _minX to set
     */
    public void setMinX(double minX) {
        this._minX = minX;
    }

    /**
     * @return the _minY
     */
    public double getMinY() {
        return _minY;
    }

    /**
     * @param minY the _minY to set
     */
    public void setMinY(double minY) {
        this._minY = minY;
    }

    /**
     * @return the _maxX
     */
    public double getMaxX() {
        return _maxX;
    }

    /**
     * @param maxX the _maxX to set
     */
    public void setMaxX(double maxX) {
        this._maxX = maxX;
    }

    /**
     * @return the _maxY
     */
    public double getMaxY() {
        return _maxY;
    }

    /**
     * @param maxY the _maxY to set
     */
    public void setMaxY(double maxY) {
        this._maxY = maxY;
    }

    public MapBBox(double minx, double miny, double maxx, double maxy) {
        this._maxX = maxx;
        this._maxY = maxy;
        this._minX = minx;
        this._minY = miny;
    }
}
