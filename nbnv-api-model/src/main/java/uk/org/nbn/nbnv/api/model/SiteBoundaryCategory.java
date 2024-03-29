package uk.org.nbn.nbnv.api.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
public class SiteBoundaryCategory {
    
    @NotEmpty
    private int id;
    @NotEmpty
    private String name;
    private List<SiteBoundaryDataset> siteBoundaryDatasets;
    
    public SiteBoundaryCategory(){}
    
    public SiteBoundaryCategory(int id, String name, List<SiteBoundaryDataset> siteBoundaryDatasets){
        super();
        this.id = id;
        this.name = name;
        this.siteBoundaryDatasets = siteBoundaryDatasets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SiteBoundaryDataset> getSiteBoundaryDatasets() {
        return siteBoundaryDatasets;
    }

    public void setSiteBoundaryDatasets(List<SiteBoundaryDataset> siteBoundaryDatasets) {
        this.siteBoundaryDatasets = siteBoundaryDatasets;
    }
    
}
