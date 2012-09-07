/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.TaxonObservation;

/**
 *
 * @author Paul Gilbertson
 */
public interface TaxonObservationMapper {
    @Select("SELECT * FROM UserTaxonObservationData WHERE observationID = #{id} AND userKey = #{userKey}")
    public TaxonObservation selectById(@Param("id") int id, @Param("userKey") int userKey);
}
