package uk.gov.nbn.data.powerless.view;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 *
 * @author cjohn
 */
public class PowerlessFreeMarkerViewResolver extends FreeMarkerViewResolver {
    @Override
    protected Class requiredViewClass() {
        return PowerlessFreeMarkerView.class;
    }
}
