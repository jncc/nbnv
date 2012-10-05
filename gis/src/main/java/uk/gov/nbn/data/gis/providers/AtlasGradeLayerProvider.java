package uk.gov.nbn.data.gis.providers;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.nbn.data.gis.processor.AtlasGrade;
import uk.gov.nbn.data.gis.processor.MapServiceMethod;
import uk.gov.nbn.data.gis.processor.Provider;

/**
 * The following provider will return the atlas grade mapping layer which was 
 * requested from the atlas grade map.
 * @author Chris Johnson
 */
@Component
public class AtlasGradeLayerProvider implements Provider {
    @Autowired AtlasGradeProvider atlasGradeProvider;
    
    @Override
    public boolean isProviderFor(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return clazz.equals(AtlasGrade.GridLayer.class);
    }

    @Override
    public AtlasGrade.GridLayer provide(Class<?> clazz, MapServiceMethod method, HttpServletRequest request, List<Annotation> annotations) {
        return getResolution(request.getParameter("resolution"), atlasGradeProvider.provide(clazz, method, request, annotations));
    }
    
    public static AtlasGrade.GridLayer getResolution(String resolution, AtlasGrade atlasGradeProperties) {
        //Work out which layer to use. Either one requested or this AtlasGrade maps default
        String resolutionToUse = (resolution != null) ? resolution : atlasGradeProperties.defaultLayer();
        //Find the layer which corresponds to this resolution
        for(AtlasGrade.GridLayer currLayer : atlasGradeProperties.layers()) {
            if(resolutionToUse.equals(currLayer.name())) {
                return currLayer;
            }
        }
        throw new IllegalArgumentException("This map service does not support the resolution " + resolutionToUse);
    }
}
