package nbn.webmapping.maintenance;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class MaintenanceHelper {
    private static final String IMT_DOWN_KEY = "nbn.imt.down";
    private static final String IMT_DOWN_REDIRECT_ADDRESS = "unavailable/down.jsp";

    public static void redirectIfNessersary(HttpServletResponse response) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("nbnServerSpecificProperties");
        if(bundle.containsKey(IMT_DOWN_KEY) && Boolean.parseBoolean(bundle.getString(IMT_DOWN_KEY)))
            response.sendRedirect(IMT_DOWN_REDIRECT_ADDRESS);
    }
}
