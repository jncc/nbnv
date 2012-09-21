/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Matt Debont
 */
public class OrganisationGroupXMLParser extends DefaultHandler {

    private static OrganisationGroupXMLParser organisationGroupXMLParser;
    private boolean run = false;
    private List<List<Integer>> groups;
    private List<Integer> groupID;
    private List<Integer> group;
    
    
    private OrganisationGroupXMLParser() {
        
    }
    
    public static OrganisationGroupXMLParser getInstance() {
        if (organisationGroupXMLParser == null) {
            organisationGroupXMLParser = new OrganisationGroupXMLParser();
        }
        return organisationGroupXMLParser;
    }

    public boolean hasBeenRun() {
        return run;
    }
    
    public void parserRun() {
        run = true;
    }
    
    
    @Override
    public void startDocument() throws SAXException {
        groups = new ArrayList<List<Integer>>();
        groupID = new ArrayList<Integer>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("organisationgroup")) {
            group = new ArrayList<Integer>();
            groupID.add(Integer.parseInt(attributes.getValue("id")));
        }

        if (qName.equalsIgnoreCase("organisation")) {
            group.add(Integer.parseInt(attributes.getValue("id")));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("organisationgroup")) {
            groups.add(group);
        }
    }

    public List<Integer> getGroups(int id) {
        List<Integer> retVal = new ArrayList<Integer>();
        for (int i = 0; i < groups.size(); i++) {
            for (Integer orgID : groups.get(i)) {
                if (orgID == id) {
                    retVal.add(groupID.get(i));
                }
            }
        }

        return retVal;
    }
}
