package nbn.common.taxoninputcategory;

import java.util.TreeSet;

public class TaxonInputCategory {
    private String _key;
    private String _name;
    private int _speciesCount;
    private boolean _isScientificSpeciesNameCount;
    private boolean _isLevel1;
    private boolean _hasLevel2;
    private boolean _isLevel2;
    private TreeSet<Character> _speciesLetters;

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public int getSpeciesCount(){
        return _speciesCount;
    }

    public boolean isLevel1() {
        return _isLevel1;
    }

    public boolean isScientificSpeciesNameCount(){
        return _isScientificSpeciesNameCount;
    }

    public boolean isLevel2() {
        return _isLevel2;
    }

    public boolean hasLevel2() {
        return _hasLevel2;
    }

    public TreeSet<Character> getSpeciesLetters(){
        return _speciesLetters;
    }

    public void setKey(String key) {
        this._key = key;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setSpeciesCount(int speciesCount) {
        this._speciesCount = speciesCount;
    }

    public void setIsScientificSpeciesNameCount(boolean isScientificSpeciesNameCount) {
        this._isScientificSpeciesNameCount = isScientificSpeciesNameCount;
    }

    public void setIsLevel1(boolean isLevel1) {
        this._isLevel1 = isLevel1;
    }

    public void setHasLevel2(boolean hasLevel2) {
        this._hasLevel2 = hasLevel2;
    }

    public void setIsLevel2(boolean isLevel2) {
        this._isLevel2 = isLevel2;
    }

    public void setSpeciesLetters(TreeSet<Character> speciesLetters){
        this._speciesLetters = speciesLetters;
    }
    
    TaxonInputCategory(String key, String name, int speciesCount, boolean isScientificSpeciesNameCount, boolean isLevel1, boolean hasLevel2, boolean isLevel2) {
        this._key = key;
        this._name = name;
        this._speciesCount = speciesCount;
        this._isScientificSpeciesNameCount = isScientificSpeciesNameCount;
        this._isLevel1 = isLevel1;
        this._hasLevel2 = hasLevel2;
        this._isLevel2 = isLevel2;
    }

    TaxonInputCategory(String key, String name, int speciesCount, boolean isScientificSpeciesNameCount, boolean isLevel1, boolean hasLevel2, boolean isLevel2, TreeSet<Character> speciesLetters) {
        this._key = key;
        this._name = name;
        this._speciesCount = speciesCount;
        this._isScientificSpeciesNameCount = isScientificSpeciesNameCount;
        this._isLevel1 = isLevel1;
        this._hasLevel2 = hasLevel2;
        this._isLevel2 = isLevel2;
        this._speciesLetters = speciesLetters;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaxonInputCategory) {
            if (((TaxonInputCategory)obj).getKey().equals(this.getKey()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this._key != null ? this._key.hashCode() : 0);
        return hash;
    }
    
}
