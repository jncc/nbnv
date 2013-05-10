package uk.org.nbn.importer.orglogo;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.Properties;
import javax.imageio.ImageIO;

/**
 *
 * @author Matt Debont
 */
public class OLD {
    private static final int MAX_HEIGHT = 1000;
    private static final int MAX_WIDTH = 150;
    private static final int MAX_SMALL_HEIGHT = 32;
    private static final int MAX_SMALL_WIDTH = 32;
    
    private String url;
    private String database;
    private int port;
    private String username;
    private String password;
    
    private LinkedList<String> errors;
        
    public OLD () {
        parseConfig();    
        errors = new LinkedList<String>();
    }
    
    public void process(String control, String logoDir) {
        ImageWrangler wrangler = new ImageWrangler(logoDir);
        processList(control, wrangler);
    }
    
    private void parseConfig() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));

            url = props.getProperty("url");
            if (url == null || url.equals("")) {
                System.err.println("Could not find url entry in the properties file");
                System.exit(1);
            }            
            
            database = props.getProperty("database");
            if (database == null || database.equals("")) {
                System.err.println("Could not find database entry in the properties file");
                System.exit(1);
            }
            port = Integer.parseInt(props.getProperty("port"));
            
            username = props.getProperty("username");
            if (username == null || username.equals("")) {
                System.err.println("Could not find username entry in the properties file");
                System.exit(1);
            }
            password = props.getProperty("password");
            if (password == null || password.equals("")) {
                System.err.println("Could not find password entry in the properties file");
                System.exit(1);
            }
        } catch (IOException ex) {
            System.err.println("Could not open config.properties file make sure it is present");
            System.exit(1);
        }
    }
    
    private void processList(String control, ImageWrangler wrangler) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(control));
            String line; 
            
            try { 
                SQLServerDataSource ds = new SQLServerDataSource();
                ds.setUser(username);
                ds.setPassword(password);
                ds.setServerName(url);
                ds.setPortNumber(port);
                ds.setDatabaseName(database);

                Connection con = ds.getConnection();

                while ((line = br.readLine()) != null) {
                    processLine(line, wrangler, con);
                }
                
                br.close();
                con.close();
                
                if (errors.size() > 0) {
                    for (String error : errors) {
                        System.err.println(error);
                    }
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            System.out.println("error");
        }
    }
    
    private void processLine(String line, ImageWrangler wrangler, Connection con) throws Exception {
        String[] inputs = line.split(",");
        if (inputs.length == 3) {
            BufferedImage logo = wrangler.wrangleImage(inputs[1], MAX_HEIGHT, MAX_WIDTH);
            BufferedImage smallLogo = wrangler.wrangleImage(inputs[2], MAX_SMALL_HEIGHT, MAX_SMALL_WIDTH);
            writeOutput(Integer.parseInt(inputs[0]), smallLogo, logo, con);
        } else if (inputs.length == 2) {
            BufferedImage logo = wrangler.wrangleImage(inputs[1], MAX_HEIGHT, MAX_WIDTH);
            BufferedImage smallLogo = wrangler.wrangleImage("th_" + inputs[1], MAX_SMALL_HEIGHT, MAX_SMALL_WIDTH);
            writeOutput(Integer.parseInt(inputs[0]), smallLogo, logo, con);
        } else {
            throw new IOException("Data supplied in control file is of an incorrect format");
        }
    }
    
    private void writeOutput(int orgId, BufferedImage small, BufferedImage logo, Connection con) throws Exception {
        if (logo == null) {
            String out = ">>>>>>>>> Could not find logo associated with Organistaion with ID " + orgId;
            errors.add(out);
            System.err.println(out);
            return;
        }
        if (small == null) {
            String out = ">>>>>>>>> Could not find small logo associated with Organistaion with ID " + orgId;
            errors.add(out);
            System.err.println(out);
            return;
        }

        
        System.out.println("Processing Logos for ORG ID " + orgId + " small " + small.getHeight() + " " + small.getWidth() + " large " + logo.getHeight() + " " + logo.getWidth());
                    
        PreparedStatement pstmt = con.prepareStatement("UPDATE Organisation SET logoSmall = ?, logo = ? WHERE id = ?");

        ByteArrayOutputStream oss = new ByteArrayOutputStream();
        ImageIO.write(small, "png", oss);
        InputStream iss = new ByteArrayInputStream(oss.toByteArray());

        ByteArrayOutputStream osl = new ByteArrayOutputStream();
        ImageIO.write(logo, "png", osl);
        InputStream isl = new ByteArrayInputStream(osl.toByteArray());

        pstmt.setBinaryStream(1, iss);
        pstmt.setBinaryStream(2, isl);
        pstmt.setInt(3, orgId);   
        
        pstmt.executeUpdate();
        
        iss.close();
        isl.close();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here#
        OLD old = new OLD();
        if (args.length != 2) {
            System.err.println("Usage: java -jar OLD.jar path-to-control path-to-logo-directory");
        } else {
            File file = new File(args[1]);
            if (file.isDirectory()) {
                if (file.list().length == 0) {
                    System.err.println("Directory is Empty");
                    System.exit(1);
                }
                
                System.out.println("Processing From " + args[0] + " with images located in " + args[1]);
                
                old.process(args[0], args[1]);
                
            } else {
                System.err.println("Directory does not exist or is not a Directory");
                System.exit(1);
            }
        }
    }
}
