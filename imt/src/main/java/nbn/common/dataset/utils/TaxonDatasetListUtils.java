/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.dataset.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nbn.common.dataset.TaxonDataset;
import nbn.common.user.User;

/**
 *
 * @author Administrator
 */
public class TaxonDatasetListUtils {
    public static String getCommaDelimitedDatasetList(List<TaxonDataset> list) {
        if (list == null || list.isEmpty())
            return "";
        
        StringBuilder result = new StringBuilder();

        for (TaxonDataset td : list) {
            result.append(',');
            result.append(td.getDatasetKey());
        }

        return result.substring(1);
    }

    public static List<TaxonDataset> filterListByDownloadable(List<TaxonDataset> list, User user) throws SQLException {
        if (list == null || list.isEmpty())
            return null;

        List<TaxonDataset> rlist = new ArrayList<TaxonDataset>();

        for (TaxonDataset td : list) {
            if (td.getPrivileges(user).hasDownloadRawData()) {
                rlist.add(td);
            }
        }

        return rlist;
    }
}
