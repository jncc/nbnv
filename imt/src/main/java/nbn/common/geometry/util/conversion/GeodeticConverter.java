package nbn.common.geometry.util.conversion;

/** A collection of static constants and functions to do Geodetic conversions.
* <ol>
* <li>convert WGS84 and OSGB36 longitude, latitude (and height) to Ordnance Survey GB and Ireland grid coordinates (Eastings and Northings in metres)</li>
* <li>transform OSI grid references to OSGB grid references and vice versa</li>
* <li>convert OSGB and OSI grid references to WGS84 longitude, latitude and height.
* </ol>
* References:
* <ul>
*  <li>Ordnance Survey (2002) A guide to coordinate systems in Great Britain</li>
*  <li>Ordnance Survey Northern Ireland & Ordnance Survey Ireland (1999) Making maps compatible with GPS</li>
* </ul>
*/
public class GeodeticConverter {
	//if VERBOSE == true, the methods give verbose output to System.out
	private static final boolean VERBOSE = false;
	
	//shape and size of the relevant geoids (biaxial ellipsoids)
	//WGS84 ellipsoid
	private static final double WGS84_A = 6378137.000;
	private static final double WGS84_B = 6356752.3141;
	//Airy 1830 (National Grid)
	private static final double OSGB36_A = 6377563.396;
	private static final double OSGB36_B = 6356256.910;
	//Airy 1830 modified (Irish National Grid)
	private static final double IRELAND65_A = 6377340.189;
	private static final double IRELAND65_B = 6356034.447;
	
	//transverse mercator projection parameters
	//National Grid
	private static final double OSGB_F0 = 0.9996012717;	//scale factor
	private static final double OSGB_PHI0 = 49;			//latitude of origin
	private static final double OSGB_LAM0 = -2;			//central meridian
	private static final double OSGB_E0 = 400000;		//false easting
	private static final double OSGB_N0 = -100000;		//false northing
	
	//Irish National Grid
	private static final double IRELAND_F0 = 1.000035;	//scale factor
	private static final double IRELAND_PHI0 = 53.5;	//latitude of origin
	private static final double IRELAND_LAM0 = -8;		//central meridian
	private static final double IRELAND_E0 = 200000;	//false easting
	private static final double IRELAND_N0 = 250000;	//false northing
	
	//Helmert transformation parameters for WGS84 to OSGB36
	//taken from reference 1
	//to reverse the transformation (OSGB36 to WGS84) mutliply them by -1 except OSGB_S, the scale
	//parameter, which you take the inverse of
	private static final double OSGB_DX = -446.448;
	private static final double OSGB_DY = 125.157;
	private static final double OSGB_DZ = -542.060;
	private static final double OSGB_S = 20.4894;
	private static final double OSGB_RX = -0.1502;
	private static final double OSGB_RY = -0.2470;
	private static final double OSGB_RZ = -0.8421;
	
	//Helmert transformation parameters for WGS84 to IRELAND65
	//adapted from reference 2, which gives the parameters for
	//ireland65 to wgs84 and uses a slightly different formula (signs reversed for
	//the rotations) than the helmert transformation this class uses from reference 1
	private static final double IRELAND_DX = -482.530;
	private static final double IRELAND_DY = 130.596;
	private static final double IRELAND_DZ = -564.557;
	private static final double IRELAND_S = -8.150;
	private static final double IRELAND_RX = 1.042; //
	private static final double IRELAND_RY = 0.214; //
	private static final double IRELAND_RZ = 0.631; //
	
	/** Converts longitude, latitude and height on the WGS84 geoid to an OSGB grid reference.
	* <ol>
	*  <li>convert longitude, latitude and height on the WGS84 geoid to cartesian coordinates</li>
	*  <li>transform WGS84 cartesian coordinates to OSGB36 cartesian coordinates using the helmert transformation</li>
	*  <li>convert OSGB36 cartesian coordinates to longitude, latitude and height on the OSGB36 geoid</li>
	*  <li>project longitude, latitude and height on the OSGB36 geoid to the OSGB grid</li>
	* </ol>
	**/
	public static double[] WGS84toOSGBGrid(double[] longLatHWGS84) {
		//convert longitude and latitude to cartesian coordinates
		double[] cartesianWGS84 = convertToCartesian(longLatHWGS84, WGS84_A, WGS84_B);
		//use the Helmert transformation to change to the OSGB36 ellipsoid
		double[] cartesianOSGB36 = helmertTransform(cartesianWGS84, OSGB_DX, OSGB_DY, OSGB_DZ, OSGB_S, OSGB_RX, OSGB_RY, OSGB_RZ);
		//convert OSGB36 cartesian coordinates back to longitude and latitude
		double[] longLatHOSGB36 = convertToLongLatH(cartesianOSGB36, OSGB36_A, OSGB36_B);
		//project longitude and latitude to OSGB National Grid
		double[] OSGBGridRef = projectToOSGB(longLatHOSGB36);
		
		if (VERBOSE) {
			System.out.println("            WGS84 x: " + cartesianWGS84[0]);
			System.out.println("            WGS84 y: " + cartesianWGS84[1]);
			System.out.println("            WGS84 z: " + cartesianWGS84[2]);
			
			System.out.println("             OSGB x: " + cartesianOSGB36[0]);
			System.out.println("             OSGB y: " + cartesianOSGB36[1]);
			System.out.println("             OSGB z: " + cartesianOSGB36[2]);
			
			System.out.println("   OSGB36 longitude: " + longLatHOSGB36[0]);
			System.out.println("    OSGB36 latitude: " + longLatHOSGB36[1]);
			System.out.println("           OSGB36 H: " + longLatHOSGB36[2]);
		}
		return OSGBGridRef;
	}
	
	/** Converts longitude, latitude and height on the WGS84 geoid to an OSI grid reference.
	* <ol>
	*   <li>convert longitdue, latitude and height on the WGS84 geoid to cartesian coordinates</li>
	*   <li>transform WGS84 cartesian coordinates to IRELAND65 cartesian coordinates using the helmert transformation</li>
	*   <li>convert IRELAND65 cartesian coordinates to longitude, latitude and height on the IRELAND65 geoid</li>
	*   <li>project longitude, latitude and height on the IRELAND65 geoid to the OSI grid</li>
	* </ol>
	**/
	public static double[] WGS84toOSIGrid(double[] longLatHWGS84) {
		//convert longitude and latitude to cartesian coordinates
		double[] cartesianWGS84 = convertToCartesian(longLatHWGS84, WGS84_A, WGS84_B);
		//use the Helmert transformation to change to the IRELAND65 ellipsoid
		double[] cartesianIRELAND65 = helmertTransform(cartesianWGS84, IRELAND_DX, IRELAND_DY, IRELAND_DZ, IRELAND_S, IRELAND_RX, IRELAND_RY, IRELAND_RZ);
		//convert IRELAND65 cartesian coordinates back to longitude and latitude
		double[] longLatHIRELAND65 = convertToLongLatH(cartesianIRELAND65, IRELAND65_A, IRELAND65_B);
		//project longitude and latitude to OSI National Grid
		double[] OSIGridRef = projectToOSI(longLatHIRELAND65);
		
		if (VERBOSE) {
			System.out.println("            WGS84 x: " + cartesianWGS84[0]);
			System.out.println("            WGS84 y: " + cartesianWGS84[1]);
			System.out.println("            WGS84 z: " + cartesianWGS84[2]);
	
			System.out.println("        IRELAND65 x: " + cartesianIRELAND65[0]);
			System.out.println("        IRELAND65 y: " + cartesianIRELAND65[1]);
			System.out.println("        IRELAND65 z: " + cartesianIRELAND65[2]);
			
			System.out.println("IRELAND65 longitude: " + longLatHIRELAND65[0]);
			System.out.println(" IRELAND65 latitude: " + longLatHIRELAND65[1]);
			System.out.println("        IRELAND65 H: " + longLatHIRELAND65[2]);
		}
		return OSIGridRef;
	}
	
	/** Converts cartesian coordinates to longitude (decimal degress), latitude  (decimal degrees) and height (metres).
	*
	* @param longLatH an array holding the longitude at index 0, the latitude at index 1, the height at index 2
	* @param a axis a of the ellipsoid
	* @param b axis b of the ellipsoid
	**/
	private static double[] convertToCartesian (double longLatH[], double a, double b) {
		//convert angle measures to radians
		//lam is longitude, phi is latitude, H is height
		double lam = longLatH[0] * Math.PI / 180;
		double phi = longLatH[1] * Math.PI / 180;
		double H = longLatH[2];
		//compute eccentricity square and nu
		double e2 = ((Math.pow(a, 2)) - (Math.pow(b, 2))) / (Math.pow(a, 2));
		double nu = a / (Math.sqrt(1 - (e2 * (Math.pow(Math.sin(phi), 2)))));
		//compute x
		double x = (nu + H) * Math.cos(phi) * Math.cos(lam);
		double y = (nu + H) * Math.cos(phi) * Math.sin(lam);
		double z = ((nu * (1 - e2)) + H) * Math.sin(phi);
		
		double[] cartesianCoord	 = {x, y, z};
		return cartesianCoord;
	}
	
	/** Transforms cartesian coordinates between two geoids using the helmert transformation.
	*
	* @param inputCoord x, y and z coordinates in metres at indices 0, 1 and 2 respectively
	* @param dx x translation
	* @param dy y translation
	* @param dz z translation
	* @param s scale factor
	* @param rx x rotation
	* @param ry y rotation
	* @param rz z rotation
	**/
	private static double[] helmertTransform(double[] inputCoord, double dx, double dy, double dz, double s, double rx, double ry, double rz) {
		double x = inputCoord[0];
		double y = inputCoord[1];
		double z = inputCoord[2];
		//convert rotations to radians and ppm scale to a factor
		double sFactor = s * 0.000001;
		double radRX = (rx / 3600) * Math.PI / 180;
		double radRY = (ry / 3600) * Math.PI / 180;
		double radRZ = (rz / 3600) * (Math.PI / 180);
		//compute transformed coordinates
		double helmertX = x + (x * sFactor) - (y * radRZ) + (z * radRY) + dx;
		double helmertY = (x * radRZ) + y + (y * sFactor) - (z * radRX) + dy;
		double helmertZ = (-1 * (x * radRY)) + (y * radRX) + z + (z * sFactor) + dz;
		double[] outputCoord = {helmertX, helmertY, helmertZ};
		return outputCoord;
	}
	
	/** converts cartesian coordinates to longitude, latitude and height.
	**/
	private static double[] convertToLongLatH(double[] cartesianCoord, double a, double b) {
		double x = cartesianCoord[0];
		double y = cartesianCoord[1];
		double z = cartesianCoord[2];
	
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double p = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		//calculate longitude
    	double lam = Math.atan(y / x);
    	//calculate latitude
		double phi1 = Math.atan(z / (p * (1 - e2)));
		double nu = a / Math.sqrt(1 - (e2 * Math.pow(Math.sin(phi1), 2)));
		double phi2 = Math.atan((z + (e2 * nu * Math.sin(phi1))) / p);
		while (Math.abs(phi1 - phi2) > 0.0000000001) {
			phi1 = phi2;
			nu = a / Math.sqrt(1 - (e2 * Math.pow(Math.sin(phi1), 2)));
			phi2 = Math.atan((z + (e2 * nu * Math.sin(phi1))) / p);
		}
		double phi = phi2;
		//calculate ellipsoid height
		double H = (p / Math.cos(phi)) - nu;
		//convert to decimal degrees
		double[] longLatHCoord = {lam * (180 / Math.PI), phi * (180 / Math.PI), H};
		return longLatHCoord;
	}
	
	/** Projects longitude, latitude and height on the OSGB36 geoid to an OSGB grid reference.
	**/
	public static double[] projectToOSGB(double[] longLatHOSGB36) {
		double a = OSGB36_A;
		double b = OSGB36_B;
		double F0 = OSGB_F0;
		double PHI0 = OSGB_PHI0 * Math.PI / 180;
		double LAM0 = OSGB_LAM0 * Math.PI / 180;
		double E0 = OSGB_E0;
		double N0 = OSGB_N0;
		
		double lam = longLatHOSGB36[0] * Math.PI / 180;
		double phi = longLatHOSGB36[1] * Math.PI / 180;
		
		double n = (a - b) / (a + b);
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double nu = a * F0 * Math.pow(1 - (e2 * Math.pow(Math.sin(phi), 2)), -0.5);
		double rho = a * F0 * (1 - e2)* Math.pow(1 - (e2 * Math.pow(Math.sin(phi), 2)), -1.5);
		double eta2 = (nu / rho) - 1;
		
		double M = calculateM(b, F0, n, PHI0, phi);
		double I = M + N0;
		double II = (nu / 2) * Math.sin(phi) * Math.cos(phi);
		double III = (nu / 24) * Math.sin(phi) * Math.pow(Math.cos(phi), 3) * (5 - Math.pow(Math.tan(phi), 2) + (9 * eta2));
		double IIIA = (nu / 720) * Math.sin(phi) * Math.pow(Math.cos(phi), 5) * (61 - (58 * Math.pow(Math.tan(phi), 2)) + Math.pow(Math.tan(phi), 4));
		double IV = nu * Math.cos(phi);
		double V = (nu / 6) * Math.pow(Math.cos(phi), 3) * ((nu / rho) - Math.pow(Math.tan(phi), 2));
		double VI = (nu / 120) * Math.pow(Math.cos(phi), 5) * (5 - 18 * Math.pow(Math.tan(phi), 2) + Math.pow(Math.tan(phi), 4) + 14 * eta2 - 58 * Math.pow(Math.tan(phi), 2) * eta2);
		
		if (VERBOSE) {
			System.out.println("               M: " + M);
			System.out.println("               I: " + I);
			System.out.println("              II: " + II);
			System.out.println("             III: " + III);
			System.out.println("            IIIA: " + IIIA);
			System.out.println("              IV: " + IV);
			System.out.println("              VI: " + VI);
		}
		double N = I + II * Math.pow(lam - LAM0, 2) + III * Math.pow(lam - LAM0, 4) + IIIA * Math.pow(lam - LAM0, 6);
		double E = E0 + IV * (lam - LAM0) + V * Math.pow(lam - LAM0, 3) + VI * Math.pow(lam - LAM0, 5);
		
		double[] OSGBGridRef = {E, N};
		return OSGBGridRef;
	}
	
	/** Projects longitude, latitude and height on the IRELAND65 geoid to an OSI grid reference.
	**/
	public static double[] projectToOSI(double[] longLatHIRELAND65) {
		double a = IRELAND65_A;
		double b = IRELAND65_B;
		double F0 = IRELAND_F0;
		double PHI0 = IRELAND_PHI0 * Math.PI / 180;
		double LAM0 = IRELAND_LAM0 * Math.PI / 180;
		double E0 = IRELAND_E0;
		double N0 = IRELAND_N0;
		
		double lam = longLatHIRELAND65[0] * Math.PI / 180;
		double phi = longLatHIRELAND65[1] * Math.PI / 180;
		
		double n = (a - b) / (a + b);
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double nu = a * F0 * Math.pow(1 - (e2 * Math.pow(Math.sin(phi), 2)), -0.5);
		double rho = a * F0 * (1 - e2)* Math.pow(1 - (e2 * Math.pow(Math.sin(phi), 2)), -1.5);
		double eta2 = (nu / rho) - 1;
		double M = calculateM(b, F0, n, PHI0, phi);
		double I = M + N0;
		double II = (nu / 2) * Math.sin(phi) * Math.cos(phi);
		double III = (nu / 24) * Math.sin(phi) * Math.pow(Math.cos(phi), 3) * (5 - Math.pow(Math.tan(phi), 2) + (9 * eta2));
		double IIIA = (nu / 720) * Math.sin(phi) * Math.pow(Math.cos(phi), 5) * (61 - (58 * Math.pow(Math.tan(phi), 2)) + Math.pow(Math.tan(phi), 4));
		double IV = nu * Math.cos(phi);
		double V = (nu / 6) * Math.pow(Math.cos(phi), 3) * ((nu / rho) - Math.pow(Math.tan(phi), 2));
		double VI = (nu / 120) * Math.pow(Math.cos(phi), 5) * (5 - 18 * Math.pow(Math.tan(phi), 2) + Math.pow(Math.tan(phi), 4) + 14 * eta2 - 58 * Math.pow(Math.tan(phi), 2) * eta2);
		
		if (VERBOSE) {
			System.out.println("                  M: " + M);
			System.out.println("                  I: " + I);
			System.out.println("                 II: " + II);
			System.out.println("                III: " + III);
			System.out.println("               IIIA: " + IIIA);
			System.out.println("                 IV: " + IV);
			System.out.println("                 VI: " + VI);
		}
		double N = I + II * Math.pow(lam - LAM0, 2) + III * Math.pow(lam - LAM0, 4) + IIIA * Math.pow(lam - LAM0, 6);
		double E = E0 + IV * (lam - LAM0) + V * Math.pow(lam - LAM0, 3) + VI * Math.pow(lam - LAM0, 5);
		
		double[] OSIGridRef = {E, N};
		return OSIGridRef;
	}
	
	/** Transforms (unprojects) OSGB grid references to longitude, latitude and height
	* on the OSGB36 geoid.
	**/
	public static double[] OSGBGridToLongLatH(double OSGBGridRef[]) {
		double a = OSGB36_A;
		double b = OSGB36_B;
		double N0 = OSGB_N0;
		double E0 = OSGB_E0;
		double F0 = OSGB_F0;
		double PHI0 = OSGB_PHI0 * Math.PI / 180;
		double LAM0 = OSGB_LAM0 * Math.PI / 180;
		
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double n = (a - b) / (a + b);
		
		
		double N = OSGBGridRef[1];
		double E = OSGBGridRef[0];
		
		double phiDash = (N - N0)/(a * F0) + PHI0;
		double M;
		do {
			M = calculateM(b, F0, n, PHI0, phiDash);
			phiDash = (N - N0 - M)/(a * F0) + phiDash;
			
		} while ((N - N0 - M) >= 0.0001);
		
		
		double nu = a * F0 * Math.pow(1 - (e2 * Math.pow(Math.sin(phiDash), 2)), -0.5);
		double rho = a * F0 * (1 - e2)* Math.pow(1 - (e2 * Math.pow(Math.sin(phiDash), 2)), -1.5);
		double eta2 = (nu / rho) - 1;
		
		double VII = Math.tan(phiDash)/(2 * rho * nu);
		double VIII = (Math.tan(phiDash) / (24 * rho * Math.pow(nu, 3))) * (5 + 3 * Math.pow(Math.tan(phiDash), 2) + eta2 - 9 * (Math.pow(Math.tan(phiDash), 2)* eta2));
		double IX = (Math.tan(phiDash) / (720 * rho * Math.pow(nu, 5))) * (61 + 90 * Math.pow(Math.tan(phiDash), 2) + 45 * Math.pow(Math.tan(phiDash), 4));
		double X = (1.0 / Math.cos(phiDash)) / nu;
		double XI = (1.0 / Math.cos(phiDash)) / (6 * Math.pow(nu, 3)) * (nu / rho + 2 * Math.pow(Math.tan(phiDash), 2));
		double XII = (1.0 / Math.cos(phiDash)) / (120 * Math.pow(nu, 5)) * (5 + 28 * Math.pow(Math.tan(phiDash), 2) + 24 * Math.pow(Math.tan(phiDash), 4));
		double XIIA = (1.0 / Math.cos(phiDash)) / (5040 * Math.pow(nu, 7)) * (61 + 662 * Math.pow(Math.tan(phiDash), 2) + 1320 * Math.pow(Math.tan(phiDash), 4) + 720 * Math.pow(Math.tan(phiDash), 6));
		
		double phi = phiDash - VII * Math.pow((E - E0), 2) + VIII * Math.pow((E - E0), 4) - IX * Math.pow((E - E0), 6);
		double lam = LAM0 + X * (E - E0) - XI * Math.pow((E - E0), 3) + XII * Math.pow((E - E0), 5) - XIIA * Math.pow((E - E0), 7);
		
		//convert to degrees
		phi = phi * 180 / Math.PI;
		lam = lam * 180 / Math.PI;
		
		if (VERBOSE) {
			System.out.println("phiDash: " + phiDash);
			System.out.println("M: " + M);
			System.out.println("nu: " + nu);
			System.out.println("rho: " + rho);
			System.out.println("eta2: " + eta2);
			System.out.println("VII: " + VII);
			System.out.println("VIII: " + VIII);
			System.out.println("IX: " + IX);
			System.out.println("X: " + X);
			System.out.println("XI: " + XI);
			System.out.println("XII: " + XII);
			System.out.println("XIIA: " + XIIA);
			System.out.println("-----------------------------");
			System.out.println("phi: " + phi);
			System.out.println("lam: " + lam);
		}
		
		return new double[] {lam, phi, 0};
		
	}
	
	/** Transforms (unprojects) OSI grid references to longitude, latitude and height
	* on the IRELAND65 geoid.
	**/
	public static double[] OSIGridToLongLatH(double OSIGridRef[]) {
		double a = IRELAND65_A;
		double b = IRELAND65_B;
		double N0 = IRELAND_N0;
		double E0 = IRELAND_E0;
		double F0 = IRELAND_F0;
		double PHI0 = IRELAND_PHI0 * Math.PI / 180;
		double LAM0 = IRELAND_LAM0 * Math.PI / 180;
		
		double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
		double n = (a - b) / (a + b);
		
		
		double N = OSIGridRef[1];
		double E = OSIGridRef[0];
		
		double phiDash = (N - N0)/(a * F0) + PHI0;
		double M;
		do {
			M = calculateM(b, F0, n, PHI0, phiDash);
			phiDash = (N - N0 - M)/(a * F0) + phiDash;
			
		} while ((N - N0 - M) >= 0.0001);
		
		
		double nu = a * F0 * Math.pow(1 - (e2 * Math.pow(Math.sin(phiDash), 2)), -0.5);
		double rho = a * F0 * (1 - e2)* Math.pow(1 - (e2 * Math.pow(Math.sin(phiDash), 2)), -1.5);
		double eta2 = (nu / rho) - 1;
		
		double VII = Math.tan(phiDash)/(2 * rho * nu);
		double VIII = (Math.tan(phiDash) / (24 * rho * Math.pow(nu, 3))) * (5 + 3 * Math.pow(Math.tan(phiDash), 2) + eta2 - 9 * (Math.pow(Math.tan(phiDash), 2)* eta2));
		double IX = (Math.tan(phiDash) / (720 * rho * Math.pow(nu, 5))) * (61 + 90 * Math.pow(Math.tan(phiDash), 2) + 45 * Math.pow(Math.tan(phiDash), 4));
		double X = (1.0 / Math.cos(phiDash)) / nu;
		double XI = (1.0 / Math.cos(phiDash)) / (6 * Math.pow(nu, 3)) * (nu / rho + 2 * Math.pow(Math.tan(phiDash), 2));
		double XII = (1.0 / Math.cos(phiDash)) / (120 * Math.pow(nu, 5)) * (5 + 28 * Math.pow(Math.tan(phiDash), 2) + 24 * Math.pow(Math.tan(phiDash), 4));
		double XIIA = (1.0 / Math.cos(phiDash)) / (5040 * Math.pow(nu, 7)) * (61 + 662 * Math.pow(Math.tan(phiDash), 2) + 1320 * Math.pow(Math.tan(phiDash), 4) + 720 * Math.pow(Math.tan(phiDash), 6));
		
		double phi = phiDash - VII * Math.pow((E - E0), 2) + VIII * Math.pow((E - E0), 4) - IX * Math.pow((E - E0), 6);
		double lam = LAM0 + X * (E - E0) - XI * Math.pow((E - E0), 3) + XII * Math.pow((E - E0), 5) - XIIA * Math.pow((E - E0), 7);
		
		//convert to degrees
		phi = phi * 180 / Math.PI;
		lam = lam * 180 / Math.PI;
		
		if (VERBOSE) {
			System.out.println("phiDash: " + phiDash);
			System.out.println("M: " + M);
			System.out.println("nu: " + nu);
			System.out.println("rho: " + rho);
			System.out.println("eta2: " + eta2);
			System.out.println("VII: " + VII);
			System.out.println("VIII: " + VIII);
			System.out.println("IX: " + IX);
			System.out.println("X: " + X);
			System.out.println("XI: " + XI);
			System.out.println("XII: " + XII);
			System.out.println("XIIA: " + XIIA);
			System.out.println("-----------------------------");
			System.out.println("phi: " + phi);
			System.out.println("lam: " + lam);
		}
		
		return new double[] {lam, phi, 0};
		
	}
	
	/** Calculates M, an intermediate value when projecting from longitude, latitude and height
	* to a (UTM?) grid
	**/
	private static double calculateM(double b, double F0, double n, double PHI0, double phi) {
		double M = b * F0 * ((1.0 + n + (5.0d / 4.0d) * Math.pow(n, 2) + (5.0d / 4.0d) * Math.pow(n, 3)) * (phi - PHI0)
				- ((3.0 * n) + 3 * Math.pow(n, 2) + (21.0d / 8.0d) * Math.pow(n, 3)) * Math.sin(phi - PHI0) * Math.cos(phi + PHI0)
				+ ((15.0d / 8.0d)* Math.pow(n, 2) + (15.0d / 8.0d) * Math.pow(n, 3))* Math.sin(2 * (phi - PHI0)) * Math.cos(2 * (phi + PHI0))
				- (35.0d / 24.0d) * Math.pow(n, 3) * Math.sin(3 * (phi - PHI0)) * Math.cos(3 * (phi + PHI0)));
		return M;
	}
	
	/** Converts an OSI (or OSNI) grid reference to an OSGB grid reference.
	* <ol>
	*   <li>convert OSI grid reference to longitude, latitude and height on the IRELAND65 geoid</ll>
	*   <li>convert longitude, latitude and height to cartesian coordinates</ll>
	*   <li>transform IRELAND65 cartesian coordinates to WGS84 cartesian coordinates</ll>
	*   <li>transform WGS84 cartesian coordinates to OSGB36 cartesian coordinates</ll>
	*   <li>convert OSGB36 cartesian coordinates to longitude, latidude and height on the OSGB36 geoid</ll>
	*   <li>project longitude, latitude and height onto the OSGB grid</ll>
	* </ol>
	**/
	public static double[] OSItoOSGB(double[] OSIGridRef) {
		double[] longLatHIRELAND65 = OSIGridToLongLatH(OSIGridRef);
		double[] cartesianIRELAND65 = convertToCartesian(longLatHIRELAND65, IRELAND65_A, IRELAND65_B);
		double[] cartesianWGS84 = helmertTransform(cartesianIRELAND65 , -IRELAND_DX, -IRELAND_DY, -IRELAND_DZ, 1.0/IRELAND_S, -IRELAND_RX, -IRELAND_RY, -IRELAND_RZ);
		double[] cartesianOSGB36 = helmertTransform(cartesianWGS84, OSGB_DX, OSGB_DY, OSGB_DZ, OSGB_S, OSGB_RX, OSGB_RY, OSGB_RZ);
		double[] longLatHOSGB36 = convertToLongLatH(cartesianOSGB36, OSGB36_A, OSGB36_B);
		double[] OSGBGridRef = projectToOSGB(longLatHOSGB36);
		return OSGBGridRef;
	}
	
	/** Converts an OSGB grid reference to an OSI grid reference.
	* <ol>
	*   <li>convert OSGB grid reference to longitude, latitude and height on the OSGB36 geoid</li>
	*   <li>convert longitude, latitude and height to cartesian coordinates</li>
	*   <li>transform OSGB36 cartesian coordinates to WGS84 cartesian coordinates</li>
	*   <li>transform WGS84 cartesian coordinates to IRELAND65 cartesian coordinates</li>
	*   <li>convert IRELAND65 cartesian coordinates to longitude, latidude and height on the IRELAND65 geoid</li>
	*   <li>project longitude, latitude and height onto the OSI grid</li>
	* </ol>
	**/
	public static double[] OSGBtoOSI(double[] OSGBGridRef) {
		double[] longLatHOSGB36 = OSGBGridToLongLatH(OSGBGridRef);
		double[] cartesianOSGB36 = convertToCartesian(longLatHOSGB36, OSGB36_A, OSGB36_B);
		double[] cartesianWGS84 = helmertTransform(cartesianOSGB36 , -OSGB_DX, -OSGB_DY, -OSGB_DZ, 1.0d / OSGB_S, -OSGB_RX, -OSGB_RY, -OSGB_RZ);
		double[] cartesianIRELAND65 = helmertTransform(cartesianWGS84, IRELAND_DX, IRELAND_DY, IRELAND_DZ, IRELAND_S, IRELAND_RX, IRELAND_RY, IRELAND_RZ);
		double[] longLatHIRELAND65 = convertToLongLatH(cartesianIRELAND65, IRELAND65_A, IRELAND65_B);
		double[] OSIGridRef = projectToOSI(longLatHIRELAND65);
		return OSIGridRef;
	}
	
	/** Converts an OSGB grid reference to a longitude, latitude and height on the WGS84 geoid.
	* <ol>
	*   <li>convert OSGB grid reference to longitude, latitude and height on the OSGB36 geoid</li>
	*   <li>convert longitude, latitude and height to cartesian coordinates</li>
	*   <li>transform OSGB36 cartesian coordinates to WGS84 cartesian coordinates</li>
	*   <li>convert WGS84 cartesian coordinates to longitude, latitude and height</li>
	* </ol>
	**/
	public static double[] OSGBtoWGS84(double[] OSGBGridRef) {
		double[] longLatHOSGB36 = OSGBGridToLongLatH(OSGBGridRef);
		double[] cartesianOSGB36 = convertToCartesian(longLatHOSGB36, OSGB36_A, OSGB36_B);
		double[] cartesianWGS84 = helmertTransform(cartesianOSGB36 , -OSGB_DX, -OSGB_DY, -OSGB_DZ, 1.0d / OSGB_S, -OSGB_RX, -OSGB_RY, -OSGB_RZ);
		double[] longLatHWGS84 = convertToLongLatH(cartesianWGS84, WGS84_A, WGS84_B);
		return longLatHWGS84;
	}
	
	/** Converts an OSI grid reference to a longitude, latitude and hieght on the WGS84 geoid.
	* <ol>
	*   <li>convert OSI grid reference to longitude, latitude and height on the IRELAND65 geoid</li>
	*   <li>convert longitude, latitude and height to cartesian coordinates</li>
	*   <li>transform IRELAND65 cartesian coordinates to WGS84 cartesian coordinates</li>
	*   <li>convert WGS84 cartesian coordinates to longitude, latitude and height</li>
	* </ol>
	**/
	public static double[] OSItoWGS84(double[] OSIGridRef) {
		double[] longLatHIRELAND65 = OSIGridToLongLatH(OSIGridRef);
		double[] cartesianIRELAND65 = convertToCartesian(longLatHIRELAND65, IRELAND65_A, IRELAND65_B);
		double[] cartesianWGS84 = helmertTransform(cartesianIRELAND65 , -IRELAND_DX, -IRELAND_DY, -IRELAND_DZ, 1.0d /IRELAND_S, -IRELAND_RX, -IRELAND_RY, -IRELAND_RZ);
		double[] longLatHWGS84 = convertToLongLatH(cartesianWGS84, WGS84_A, WGS84_B);
		return longLatHWGS84;
	}
	
	/** Main method for testing the conversion methods.
	**/
	public static void main(String[] args) {
		
		//convert an irish grid reference to longitude, latitude and height on the WGS84 geoid
		/*
		double[] OSIGridRef = {334500, 196200};
		double[] longLatHIRELAND65 = OSIGridToLongLatH(OSIGridRef);
		double[] cartesianIRELAND65 = convertToCartesian(longLatHIRELAND65, IRELAND65_A, IRELAND65_B);
		double[] cartesianWGS84 = helmertTransform(cartesianIRELAND65 , -IRELAND_DX, -IRELAND_DY, -IRELAND_DZ, 1.0d /IRELAND_S, -IRELAND_RX, -IRELAND_RY, -IRELAND_RZ);
		double[] longLatHWGS84 = convertToLongLatH(cartesianWGS84, WGS84_A, WGS84_B);
		System.out.println(longLatHWGS84[0]);
		System.out.println(longLatHWGS84[1]);
		*/
		
		//convert an OSGB grid reference to longitude, latitude and height on the WGS84 geoid
		double[] OSGBGridRef = {300000, 200000};
		double[] longLatHOSGB36 = OSGBGridToLongLatH(OSGBGridRef);
		double[] cartesianOSGB36 = convertToCartesian(longLatHOSGB36, OSGB36_A, OSGB36_B);
		double[] cartesianWGS84 = helmertTransform(cartesianOSGB36 , -OSGB_DX, -OSGB_DY, -OSGB_DZ, 1.0d /OSGB_S, -OSGB_RX, -OSGB_RY, -OSGB_RZ);
		double[] longLatHWGS84 = convertToLongLatH(cartesianWGS84, WGS84_A, WGS84_B);
		System.out.println(longLatHWGS84[0]);
		System.out.println(longLatHWGS84[1]);
		
	}	
}
