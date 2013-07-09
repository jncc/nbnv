/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Select;

/**
 *
 * @author paulbe
 */
public interface AccessRequestPurposeMapper {
    @Select("SELECT label FROM AccessRequestPurpose WHERE id = #{id}")
    public String getPurposeText(int id);
}
