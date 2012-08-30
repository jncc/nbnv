/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbn.common.taxon.taxonomy;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import nbn.common.taxonreportingcategory.TaxonReportingCategory;

/**
 *
 * @author Administrator
 */
public class Taxonomy {
    private String _name;
    private int _taxonVersionKey; // Internal key
    private String _nbnTaxonVersionKey;
    private TaxonReportingCategory _category;
    private String _authority;
    private boolean _aggregate;
    private boolean _nameWellFormed;
    private boolean _nameScientific;
    private boolean _namePrefered;

    private LinkedList<Taxonomy> _synonyms;
    private LinkedList<Taxonomy> _children;
    private LinkedList<Taxonomy> _aggregates;
    private LinkedList<TaxonDesignation> designations;
    //    private String language = "";

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @return the _taxonVersionKey
     */
    public int getTaxonVersionKey() {
        return _taxonVersionKey;
    }

    /**
     * @return the _nbnTaxonVersionKey
     */
    public String getNBNTaxonVersionKey() {
        return _nbnTaxonVersionKey;
    }

    /**
     * @return the _category
     */
    public TaxonReportingCategory getTaxonReportingCategory() {
        return _category;
    }

    /**
     * @return the _authority
     */
    public String getAuthority() {
        return _authority;
    }

    /**
     * @return the _aggregate
     */
    public boolean isAggregate() {
        return _aggregate;
    }

    /**
     * @return the _nameWellFormed
     */
    public boolean isNameWellFormed() {
        return _nameWellFormed;
    }


    public List<Taxonomy> getSynonymList() {
        return Collections.unmodifiableList(this._synonyms);
    }

    public List<Taxonomy> getChildrenList() {
        return Collections.unmodifiableList(this._children);
    }

    public List<Taxonomy> getAggregateList() {
        return Collections.unmodifiableList(this._aggregates);
    }

    public List<TaxonDesignation> getTaxonDesignations() {
	return Collections.unmodifiableList(designations);
    }

    List<Taxonomy> getModifiableSynonymList() {
        return this._synonyms;
    }

    List<Taxonomy> getModifiableChildrenList() {
        return this._children;
    }

    List<Taxonomy> getModifiableAggregateList() {
        return this._aggregates;
    }

    /**
     * @param name the _name to set
     */
    void setName(String name) {
        this._name = name;
    }

    /**
     * @param taxonVersionKey the _taxonVersionKey to set
     */
    void setTaxonVersionKey(int taxonVersionKey) {
        this._taxonVersionKey = taxonVersionKey;
    }

    /**
     * @param nbnTaxonVersionKey the _nbnTaxonVersionKey to set
     */
    void setNBNTaxonVersionKey(String nbnTaxonVersionKey) {
        this._nbnTaxonVersionKey = nbnTaxonVersionKey;
    }

    /**
     * @param category the _category to set
     */
    void setCategory(TaxonReportingCategory category) {
        this._category = category;
    }

    /**
     * @param authority the _authority to set
     */
    void setAuthority(String authority) {
        this._authority = authority;
    }

    /**
     * @param aggregate the _aggregate to set
     */
    void setAggregate(boolean aggregate) {
        this._aggregate = aggregate;
    }

    /**
     * @param nameWellFormed the _nameWellFormed to set
     */
    void setNameWellFormed(boolean nameWellFormed) {
        this._nameWellFormed = nameWellFormed;
    }

    Taxonomy(String name, String NBNKey) {
        this._name = name;
        this._nbnTaxonVersionKey = NBNKey;

        this._aggregates = new LinkedList<Taxonomy>();
        this._children = new LinkedList<Taxonomy>();
        this._synonyms = new LinkedList<Taxonomy>();
	this.designations = new LinkedList<TaxonDesignation>();
    }

    /**
     * @return the _nameScientific
     */
    public boolean isNameScientific() {
        return _nameScientific;
    }

    /**
     * @return the _namePrefered
     */
    public boolean isNamePrefered() {
        return _namePrefered;
    }

    /**
     * @param nameScientific the _nameScientific to set
     */
    void setNameScientific(boolean nameScientific) {
        this._nameScientific = nameScientific;
    }

    /**
     * @param namePrefered the _namePrefered to set
     */
    void setNamePrefered(boolean namePrefered) {
        this._namePrefered = namePrefered;
    }

    boolean addAllTaxonDesignations(List<TaxonDesignation> taxonDesignationToAdd) {
	return designations.addAll(taxonDesignationToAdd);
    }
}
