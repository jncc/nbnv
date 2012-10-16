package uk.org.nbn.nbnv.api.model;

public class Designation {
    private int id;
    private String name;
    private String label;
    private String code;
    private int designationCategoryID;
    private String description;

    public Designation() {}
    
    public Designation(int designationID, String name, String label, String code, int designationCategoryID, String description) {
        super();
        this.id = id;
        this.name = name;
        this.label = label;
        this.code = code;
        this.designationCategoryID = designationCategoryID;
        this.description = description;
    }

    public int getDesignationCategoryID() {
        return designationCategoryID;
    }

    public void setDesignationCategoryID(int designationCategoryID) {
        this.designationCategoryID = designationCategoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
