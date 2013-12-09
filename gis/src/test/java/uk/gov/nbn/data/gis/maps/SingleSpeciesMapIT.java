package uk.gov.nbn.data.gis.maps;

import com.sun.jersey.api.client.WebResource;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import uk.ac.ceh.dynamo.GridMapImage;
import uk.ac.ceh.dynamo.GridMapImageBuilder;
import static org.junit.Assert.*;
import uk.ac.ceh.dynamo.GridSquare;

/**
 * 
 * @author Christopher Johnson
 */
public class SingleSpeciesMapIT extends AbstractGridMapIT {
    @Test
    public void checkThatWeCanGridMapWithGif() throws IOException {
        //Given (Tree Creeper in GA000466)
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                    .queryParam("datasets", "GA000466")
                                    .queryParam("format", "gif");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
     
    @Test
    public void publicUserCannotSeeTreeCreeperInGA000466() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                        .queryParam("datasets", "GA000466");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
    
    @Test
    public void publicUserCannotSeeTreeCreeperInGA000466After1601() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("startyear", "1601");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
       
    @Test
    public void publicUserCannotSeeTreeCreeperInGA000466Before2010() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("endyear", "2010");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
    
    @Test
    public void publicUserCannotSeeTreeCreeperInGA000466Between1650And2013() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("staryear", "1650")
                                        .queryParam("endyear", "2013");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
        
    @Test
    public void testUser1CanSeeTreeCreeperInGA000466() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("username", "gistestuser1")
                                        .queryParam("userkey", "5a78c08ffaff510a264d3e89103ebc8f");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertEquals("Expected 3 10km grid squares to have been set", 3, gridSquares.size());
    }
    
    @Test
    public void testUser1CanSeeMuteSwanIn2008() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NBNSYS0000000010/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("startyear", "2008")
                                        .queryParam("endyear", "2008")
                                        .queryParam("username", "gistestuser1")
                                        .queryParam("userkey", "5a78c08ffaff510a264d3e89103ebc8f");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertEquals("Expected 1 10km grid squares to have been set", 1, gridSquares.size());
    }
    
    @Test
    public void testUser1CanSeeBlueTitInWoodWaltonFen() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0001688296/map")
                                        .queryParam("datasets", "GA000466")
                                        .queryParam("username", "gistestuser1")
                                        .queryParam("userkey", "5a78c08ffaff510a264d3e89103ebc8f");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertEquals("Expected 4 10km grid squares to have been set", 4, gridSquares.size());
    }
    
    @Test
    public void publicCanSeeTreeCreeperRecords() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0000530266/map");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertFalse("Expected to find at least one grid square highlighted", gridSquares.isEmpty());
    }
    
    @Test
    public void publicCantSeeSensitiveBlueTitRecords() throws IOException {
        //Given
        WebResource mapServer = gis.path("SingleSpecies/NHMSYS0001688296/map")
                                    .queryParam("datasets", "GA000680");
        
        //When
        GridMapImage image = gridMap(mapServer).build();
        
        //Then
        List<GridSquare> gridSquares = image.getGridSquaresByColour(Color.YELLOW);
        assertTrue("Didn't expect to find any records highlighed yellow", gridSquares.isEmpty());
    }
        
    private GridMapImageBuilder gridMap(WebResource resource) {
        return gridMap(resource, getGridMap(SingleSpeciesMap.class, "getSingleSpeciesModel"));
    }
}
