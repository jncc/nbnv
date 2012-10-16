package uk.org.nbn.nbnv.api.model;

public class DesignationCategory {
    private int id;
    private String label;
    private String description;
    private int sortOrder;

    public DesignationCategory(int id, String label, String description, int sortOrder) {
        super();
        this.id = id;
        this.label = label;
        this.description = description;
        this.sortOrder = sortOrder;
    }
    
    public DesignationCategory() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
