package uk.org.nbn.nbnv.api.dao.providers;

import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import static org.apache.ibatis.jdbc.SelectBuilder.*;

/**
 * key
 *
 * @author Matt Debont
 */
public class AttributeProvider {

    public String getAttributesByID(Map<String, Object> params) {
        BEGIN();
        SELECT("id AS attributeID, label, description");
        FROM("AttributeData");
        WHERE("id IN " + intListToCommaList((List<Integer>) params.get("attribs")));
        return SQL();
    }

    private String intListToCommaList(List<Integer> list) {
        return "('" + StringUtils.collectionToDelimitedString(list, "','") + "')";
    }
}
