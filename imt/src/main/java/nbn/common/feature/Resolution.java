/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.feature;

/**
 *
 * @author Administrator
 */
public enum Resolution {
    Any (0, "Any"),
    _10km (1,"10km"),
    _2km (2,"2km"),
    _1km (3,"1km"),
    _100m (4,"100m"),
    _site (5,"Site");

    private final int resolutionCode;
    private String name;

    private Resolution(int code, String name) {
        this.resolutionCode = code;
        this.name = name;
    }

    public int getResolutionCode() {
        return this.resolutionCode;
    }

    public int getAccuracyInMetres() {
        switch(this) {
            case _10km:return 10000;
            case _2km: return 2000;
            case _1km: return 1000;
            case _100m: return 100;
            default:
                throw new IllegalStateException("Unable to get an accuracy for resolution of type" + this);
        }
    }

    public String getName() {
        return name;
    }

    public static Resolution getResolutionByGridRef(String gridRef) {
        return getResolutionByGridRef(gridRef, GridSystem.getGridSystemByGridRef(gridRef));
    }

    public static Resolution getResolutionByGridRef(String gridRef, GridSystem system) {
        switch(system) {
            case IRISH_NATIONAL_GRID: switch(gridRef.length()) {
                case 3: return Resolution._10km;
                case 4: return Resolution._2km;
                case 5: return Resolution._1km;
                case 7: return Resolution._100m;
                default: throw new IllegalArgumentException("The specified gridRef (" + gridRef + ")does not have a known resolution in the " + system.getName() + " System");
            }
            case BRITISH_NATIONAL_GRID: switch(gridRef.length()) {
                case 4: return Resolution._10km;
                case 5: return Resolution._2km;
                case 6: return Resolution._1km;
                case 8: return Resolution._100m;
                default: throw new IllegalArgumentException("The specified gridRef (" + gridRef + ")does not have a known resolution in the " + system.getName() + " System");
            }
            default:
                throw new IllegalArgumentException("The specified grid system is not known");
        }
    }

    public static Resolution getResolutionByName(String name) {
        for(Resolution r : values()) {
            if(name.equals(r.name))
                return r;
        }
        throw new IllegalArgumentException("There is no known resolution that matches to the resolution " + name);
    }

    static Resolution getResolutionByCode(int code) {
        for (Resolution r : values())
            if (r.getResolutionCode() == code)
                return r;
        throw new IllegalArgumentException("getResolutionByCode: Illegal code {code = " + String.valueOf(code) + "}");
    }

    @Override
    public String toString() {
        return getName();
    }
}
