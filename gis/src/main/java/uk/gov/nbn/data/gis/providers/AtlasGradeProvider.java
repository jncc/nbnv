package uk.gov.nbn.data.gis.providers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.Annotations;
import uk.gov.nbn.data.gis.processor.AtlasGrade;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * Simple provider to allow obtain the atlas grade annotation from a provided for method
 * @author Chris Johnson
 */
@Component
public class AtlasGradeProvider implements Provider {

    @Override
    public boolean isProviderFor(Class<?> clazz, Annotations annotations) {
        return clazz.equals(AtlasGrade.class);
    }

    @Override
    public AtlasGrade provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, Annotations annotations) {
        return method.getUnderlyingMapMethod().getAnnotation(AtlasGrade.class);
    }
}
