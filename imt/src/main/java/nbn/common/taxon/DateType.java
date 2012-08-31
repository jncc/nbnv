/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon;

/**
 *
 * @author Administrator
 */
public enum DateType {
    Day,
    DayRange,
    Month,
    MonthRange,
    Year,
    YearRange,
    BeforeYear,
    AfterYear,
    Circa,
    PublicationDate,
    NoDate,
    Unknown;

    public static DateType getTypeByCode(String key) {
        if ("-Y".equals(key)) {
            return BeforeYear;
        } else if ("<Y".equals(key)) {
            return BeforeYear;
        } else if ("Y-".equals(key)) {
            return AfterYear;
        } else if (">Y".equals(key)) {
            return AfterYear;
        } else if ("B".equals(key)) {
            return Unknown;
        } else if ("R".equals(key)) {
            return Unknown;
        } else if ("U".equals(key)) {
            return Unknown;
        } else if ("XX".equals(key)) {
            return Unknown;
        } else if ("C".equals(key)) {
            return Circa;
        } else if ("D".equals(key)) {
            return Day;
        } else if ("DD".equals(key)) {
            return DayRange;
        } else if ("M".equals(key)) {
            return Month;
        } else if ("O".equals(key)) {
            return Month;
        } else if ("MM".equals(key)) {
            return MonthRange;
        } else if ("OO".equals(key)) {
            return MonthRange;
        } else if ("ND".equals(key)) {
            return NoDate;
        } else if ("P".equals(key)) {
            return PublicationDate;
        } else if ("Y".equals(key)) {
            return Year;
        } else if ("YY".equals(key)) {
            return YearRange;
        } else {
            return Unknown;
        }
    }

    public static DateType getTypeByName(String name) {
        if ("Before Year".equals(name)) {
            return BeforeYear;
        } else if ("After Year".equals(name)) {
            return AfterYear;
        } else if ("Unknown".equals(name)) {
            return Unknown;
        } else if ("Circa".equals(name)) {
            return Circa;
        } else if ("Day".equals(name)) {
            return Day;
        } else if ("Day Range".equals(name)) {
            return DayRange;
        } else if ("Month".equals(name)) {
            return Month;
        } else if ("Month Range".equals(name)) {
            return MonthRange;
        } else if ("No date".equals(name)) {
            return NoDate;
        } else if ("Publication Date".equals(name)) {
            return PublicationDate;
        } else if ("Year".equals(name)) {
            return Year;
        } else if ("Year Range".equals(name)) {
            return YearRange;
        } else {
            return Unknown;
        }
    }

}
