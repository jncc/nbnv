/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.webmapping.json.treewidget;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
public interface ArgumentalDHTMLXtreeWidgetGenerator extends DHTMLXTreeWidgetGenerator{
    public void setArgs(HttpServletRequest request);
}
