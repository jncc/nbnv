package uk.org.nbn.nbnv.api.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@XmlRootElement
public class BoundingBox {
    private BigDecimal minX,minY,maxX,maxY;
    private String epsgCode;
    
    protected BoundingBox() {}
    
    public BoundingBox(String epsgCode, BigDecimal minX, BigDecimal minY, BigDecimal maxX, BigDecimal maxY) {
        this.epsgCode = epsgCode;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public String getEpsgCode() {
        return epsgCode;
    }

    public void setEpsgCode(String epsgCode) {
        this.epsgCode = epsgCode;
    }

    public BigDecimal getMinX() {
        return minX;
    }

    public void setMinX(BigDecimal minX) {
        this.minX = minX;
    }

    public BigDecimal getMinY() {
        return minY;
    }

    public void setMinY(BigDecimal minY) {
        this.minY = minY;
    }

    public BigDecimal getMaxX() {
        return maxX;
    }

    public void setMaxX(BigDecimal maxX) {
        this.maxX = maxX;
    }

    public BigDecimal getMaxY() {
        return maxY;
    }

    public void setMaxY(BigDecimal maxY) {
        this.maxY = maxY;
    }
}
