package uk.org.nbn.nbnv.api.nxf.metadata;

public class WordImporter_3_2 extends WordImporter_3_0 {
    public static final int MAJOR = 3;
    public static final int MINOR = 2;
    
    public WordImporter_3_2() {
        super();
    }
    
    @Override
    public boolean supports(int major, int minor) {
        return major == MAJOR && minor == MINOR;
    }
}
