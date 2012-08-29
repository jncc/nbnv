/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Administrator
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();
}
