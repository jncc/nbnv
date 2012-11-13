<%@ page import="net.searchnbn.webservices.service.GatewayWebService"%>
<%@ page import="net.searchnbn.webservices.data.*"%>
<%@ page import="net.searchnbn.webservices.query.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.util.Properties"%>

<!--
@author Jon Cooper, CEH Monks Wood
@date   22/02/2006
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
  	<title>NBN user defined polygon web service, java example</title>
	<link href="examples.css" rel="stylesheet" type="text/css" media="screen" />
  </head>

  <body>
  <div id="container">

  <h1>NBN user defined polygon web service, java example</h1>

  <p>
	  This page is built using the NBN's web services.  It gives a map of your polyon and a list of the species
	  found in and around it.  It illustrates 5 different shape filters - point, point-buffer, rectangle, simple
	  polygon and complex polygon, and allows you to change the colours.
	  <a href="toSource.jsp?fname=ws-speciesListUserPoly.jsp">Here is the jsp source code</a>
  </p><br>
  <%

	  //Start by getting any parameters that may have been sent by the user
	  String type = request.getParameter("type");//This defines the type of geographical filter to use
	  if((type==null)||(type.equalsIgnoreCase("")))
		  type = "simple_polygon";
	  String fill = request.getParameter("fill");//This defines the fill colour
	  if((fill==null)||(fill.equalsIgnoreCase("")))
		  fill = "#00ff00";
	  String outline = request.getParameter("outline");//This defines the outline colour
	  if((outline==null)||(outline.equalsIgnoreCase("")))
		  outline = "#000000";
	  String owidth = request.getParameter("owidth");//This defines the outline width
	  if((owidth==null)||(owidth.equalsIgnoreCase("")))
		  owidth = "1";
	  String transparency = request.getParameter("transparency");//This defines the fill transparency (0.0 is 100% transparent, 1.0 = 100% opaque)
	  if((transparency==null)||(transparency.equalsIgnoreCase("")))
		  transparency = "0.7";


%>

<!--******************Build the form that allows the user to change the geographic filter and colours***************%>-->
<fieldset>
	<legend>Change the geographic filter and colour scheme:</legend>
		<form action="ws-speciesListUserPoly.jsp">
			<table  ID="choices"><tr><td>
				 Shape filter:
				 <select name="type">
					<%String selected = type.equalsIgnoreCase("point") ? " selected " : "";%>
					<option value="point" <%=selected%>>point</option>
					<%selected = type.equalsIgnoreCase("point_buffer") ? " selected " : "";%>
					<option value="point_buffer" <%=selected%>>point buffer</option>
					<%selected = type.equalsIgnoreCase("rectangle") ? " selected " : "";%>
					<option value="rectangle" <%=selected%>>rectangle</option>
					<%selected = type.equalsIgnoreCase("simple_polygon") ? " selected " : "";%>
					<option value="simple_polygon" <%=selected%>>simple polygon</option>
					<%selected = type.equalsIgnoreCase("complex_polygon") ? " selected " : "";%>
					<option value="complex_polygon" <%=selected%>>multi-part polygon with hole</option>
				  </select>
			</td><td>
				Fill colour:
				<select name="fill">
					<%selected = fill.equalsIgnoreCase("#ff0000") ? " selected " : "";%>
					<option value="#ff0000" <%=selected%>>red</option>
					<%selected = fill.equalsIgnoreCase("#00ff00") ? " selected " : "";%>
					<option value="#00ff00" <%=selected%>>green</option>
					<%selected = fill.equalsIgnoreCase("#0000ff") ? " selected " : "";%>
					<option value="#0000ff" <%=selected%>>blue</option>
					<%selected = fill.equalsIgnoreCase("#000000") ? " selected " : "";%>
					<option value="#000000" <%=selected%>>black</option>
				</select>
			</td><td>
				Fill transparency:
				<select name="transparency">
					<%selected = transparency.equalsIgnoreCase("0.0") ? " selected " : "";%>
					<option value="0.0" <%=selected%>>completely transparent</option>
					<%selected = transparency.equalsIgnoreCase("0.3") ? " selected " : "";%>
					<option value="0.3" <%=selected%>>very transparent</option>
					<%selected = transparency.equalsIgnoreCase("0.5") ? " selected " : "";%>
					<option value="0.5" <%=selected%>>half transparent</option>
					<%selected = transparency.equalsIgnoreCase("0.7") ? " selected " : "";%>
					<option value="0.7" <%=selected%>>slightly transparent</option>
					<%selected = transparency.equalsIgnoreCase("1.0") ? " selected " : "";%>
					<option value="1.0" <%=selected%>>opaque</option>
				</select>
			</td></tr><tr><td colspan="3">
				&nbsp;
			</td></tr><tr><td>Outline colour:
				<select name="outline">
					<%selected = outline.equalsIgnoreCase("#ff0000") ? " selected " : "";%>
					<option value="#ff0000" <%=selected%>>red</option>
					<%selected = outline.equalsIgnoreCase("#00ff00") ? " selected " : "";%>
					<option value="#00ff00" <%=selected%>>green</option>
					<%selected = outline.equalsIgnoreCase("#0000ff") ? " selected " : "";%>
					<option value="#0000ff" <%=selected%>>blue</option>
					<%selected = outline.equalsIgnoreCase("#000000") ? " selected " : "";%>
					<option value="#000000" <%=selected%>>black</option>
				</select>
			</td><td>
					 Outline width:
					 <select name="owidth">
					<%selected = owidth.equalsIgnoreCase("1") ? " selected " : "";%>
						 <option value="1" <%=selected%>>1</option>
					<%selected = owidth.equalsIgnoreCase("2") ? " selected " : "";%>
						 <option value="2" <%=selected%>>2</option>
					<%selected = owidth.equalsIgnoreCase("3") ? " selected " : "";%>
						 <option value="3" <%=selected%>>3</option>
					<%selected = owidth.equalsIgnoreCase("4") ? " selected " : "";%>
						 <option value="4" <%=selected%>>4</option>
					<%selected = owidth.equalsIgnoreCase("5") ? " selected " : "";%>
						 <option value="5" <%=selected%>>5</option>
					 </select>
			</td><td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit">Go</button>
			</td></tr></table>
		</form>
</fieldset>

<!--******************************Finished building the form*********************************-->

<%
//*******************************Start the section that sets up the Gateway webservice request**************

  		//Get the NBN Gateway webservice
		//URL baseUrl = net.searchnbn.webservices.service.GatewayWebService.class.getResource(".");
        //    URL secureUrl = new URL(baseUrl, "http://www.testnbn.net/ws/webservice");
            // call the web service with your new url
        //    GatewayWebService gws = new GatewayWebService(secureUrl);
         GatewayWebService gws = new GatewayWebService();

              
		//***********Build the geographical filter corresponding to the user's request (type)***************

		GeographicalFilter gf = new GeographicalFilter();//This holds all the geographical filter parameters, which are set below

		if(type.equalsIgnoreCase("point")){
			//Point example
			Point p = new Point();
			p.setX(519700);
			p.setY(279800);
			p.setSrs(SpatialReferenceSystem.EPSG_27700);
			gf.setPoint(p);
		}else if(type.equalsIgnoreCase("point_buffer")){
			//Buffer example
			Point p = new Point();
			p.setX(519700);
			p.setY(279800);
			p.setSrs(SpatialReferenceSystem.EPSG_27700);
			Buffer b = new Buffer();
			b.setPoint(p);
			b.setDistance((float) 500.0);
			gf.setBuffer(b);
		}else if(type.equalsIgnoreCase("rectangle")){
			//Rectangle example
			BoundingBox bb = new BoundingBox();
			bb.setMinx(519700.0);
			bb.setMiny(279800.0);
			bb.setMaxx(520700.0);
			bb.setMaxy(281300.0);
			bb.setSrs(SpatialReferenceSystem.EPSG_27700);
			gf.setBoundingBox(bb);
		}else if(type.equalsIgnoreCase("simple_polygon")){
			//Simple single polygon around Monks Wood
			
                        net.searchnbn.webservices.data.Polygon.Boundary boundary= new net.searchnbn.webservices.data.Polygon.Boundary();
			net.searchnbn.webservices.data.Ring ring = new net.searchnbn.webservices.data.Ring();
                      
                        
                        Coordinate c0=new Point();
                        c0.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c0.setX(519222.0);
                        c0.setY(279259.0);
			ring.getV().add(c0);
                        
                        
                        Coordinate c1=new Point();
                        c1.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c1.setX(518988.0);
                        c1.setY(279651.0);
			ring.getV().add(c1);
                        
                        Coordinate c2=new Point();
                        c2.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c2.setX(519388.0);
                        c2.setY(280590.0);
			ring.getV().add(c2);
                        
                        Coordinate c3=new Point();
                        c3.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c3.setX(520173.0);
                        c3.setY(280847.0);
			ring.getV().add(c3);
                        
                        Coordinate c4=new Point();
                        c4.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c4.setX(520690.0);
                        c4.setY(279727.0);
			ring.getV().add(c4);
                        
                        Coordinate c5=new Point();
                        c5.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c5.setX(520467.0);
                        c5.setY(279745.0);
			ring.getV().add(c5);
                        
                        Coordinate c6=new Point();
                        c6.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c6.setX(520363.0);
                        c6.setY(279911.0);
			ring.getV().add(c6);
                        
                        Coordinate c7=new Point();
                        c7.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c7.setX(519222.0);
                        c7.setY(279259.0);
			ring.getV().add(c7);
                        
			  
                        boundary.setRing(ring);
			Polygon p = new Polygon();
			p.setSrs(SpatialReferenceSystem.EPSG_27700);
			p.setBoundary(boundary);
			gf.setPolygon(p);
		}else if(type.equalsIgnoreCase("complex_polygon")){
			//Irregular polygon - 2 part around Monks Wood with a hole in the larger part
			
                        net.searchnbn.webservices.data.Polygon.Boundary pb1 = new net.searchnbn.webservices.data.Polygon.Boundary();
                        net.searchnbn.webservices.data.Ring ring = new net.searchnbn.webservices.data.Ring();
                    
                        Coordinate c1 = new Point();
                        c1.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c1.setX(519222.0);
                        c1.setY(279259.0);
                        ring.getV().add(c1);
                        
                        Coordinate c2 = new Point();
                        c2.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c2.setX(518988.0);
                        c2.setY(279651.0);
                        ring.getV().add(c2);
                        
			Coordinate c3 = new Point();
                        c3.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c3.setX(519388.0);
                        c3.setY(280590.0);
                        ring.getV().add(c3);
                        
                        Coordinate c4 = new Point();
                        c4.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c4.setX(520173.0);
                        c4.setY(280847.0);
                        ring.getV().add(c4);
                        
                        Coordinate c5 = new Point();
                        c5.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c5.setX(520690.0);
                        c5.setY(279727.0);
                        ring.getV().add(c5);        
                        
                        Coordinate c6 = new Point();
                        c6.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c6.setX(520467.0);
                        c6.setY(279745.0);
                        ring.getV().add(c6);
                        
                        Coordinate c7 = new Point();
                        c7.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c7.setX(520363.0);
                        c7.setY(279911.0);
                        ring.getV().add(c7);
                        
                        Coordinate c8 = new Point();
                        c8.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c8.setX(519222.0);
                        c8.setY(279259.0);
                        ring.getV().add(c8);
                        
			pb1.setRing(ring);

                        net.searchnbn.webservices.data.Ring ring2 = new net.searchnbn.webservices.data.Ring();
                    
                        Coordinate c1hole = new Point();
                        c1hole.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c1hole.setX(519550.0);
                        c1hole.setY(280300.0);
                        ring2.getV().add(c1hole);
                        
                        Coordinate c2hole = new Point();
                        c2hole.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c2hole.setX(519999.0);
                        c2hole.setY(280300.0);
                        ring2.getV().add(c2hole);
                        
			Coordinate c3hole = new Point();
                        c3hole.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c3hole.setX(519999.0);
                        c3hole.setY(279950.0);
                        ring2.getV().add(c3hole);
                        
			Coordinate c4hole = new Point();
                        c4hole.setSrs(SpatialReferenceSystem.EPSG_27700);
                        c4hole.setX(519550.0);
                        c4hole.setY(279950.0);
                        ring2.getV().add(c4hole);
                        
			Polygon.Hole ph1 = new Polygon.Hole();
			ph1.setRing(ring2);
			Polygon p1 = new Polygon();
			p1.setBoundary(pb1);
			p1.getHole().add(ph1);
                        
                        net.searchnbn.webservices.data.Ring ring3 = new net.searchnbn.webservices.data.Ring();
			Coordinate cord1 = new Point();
                        cord1.setSrs(SpatialReferenceSystem.EPSG_27700);
                        cord1.setX(518338.0);
                        cord1.setY(280192.0);
                        ring3.getV().add(cord1);
                        
			Coordinate cord2 = new Point();
                        cord2.setSrs(SpatialReferenceSystem.EPSG_27700);
                        cord2.setX(518263.0);
                        cord2.setY(280553.0);
                        ring3.getV().add(cord2);
                        
                        Coordinate cord3 = new Point();
                        cord3.setSrs(SpatialReferenceSystem.EPSG_27700);
                        cord3.setX(518819.0);
                        cord3.setY(280558.0);
                        ring3.getV().add(cord3);
                        
                        Coordinate cord4 = new Point();
                        cord4.setSrs(SpatialReferenceSystem.EPSG_27700);
                        cord4.setX(518338.0);
                        cord4.setY(280192.0);
                        ring3.getV().add(cord4);
                       
			Polygon.Boundary pb2 = new Polygon.Boundary();
			pb2.setRing(ring3);
			Polygon p2 = new Polygon();
			p2.setBoundary(pb2);
			Polygon[] polys = new Polygon[2];
			polys[0] = p1;
			polys[1] = p2;
			MultiPartPolygon mpp = new MultiPartPolygon();
			mpp.setSrs(SpatialReferenceSystem.EPSG_27700);
                        mpp.getPolygon().add(p1);
                        mpp.getPolygon().add(p2);
                        gf.setMultiPartPolygon(mpp);
		}

		//Set additional spatial query parameters
		gf.setOverlayRule(OverlayRule.OVERLAPS);//Could use overlaps or within to indicate whether records are allowed to overlap the edge of the polygon or be completely contained by it.
		gf.setMinimumResolution("_1km");//Defines the minimum resolution of the data used, e.g. Resolution._1km indicates to use 100m and 1km records only and NOT 2km and 10km.



		//Set the map settings - controls how the image looks
		MapSettings ms = new MapSettings();
		ms.setWidth(new Integer(400));//Image width in pixels
		ms.setHeight(new Integer(400));//Image height in pixels
		ms.setOutlineColour(outline);//Boundary outline colour
		ms.setOutlineWidth(new Integer(owidth));//Boundary outline width
		ms.setFillColour(fill);//Fill colour for the polygon
		ms.setFillTransparency(new Double(transparency));//Fill transparency (0.0 is 100% transparent, 1.0 = 100% opaque)
		gf.setMapSettings(ms);

	  //***********Finished building the geographical filter***************


	  //***********Build the SpeciesListRequest and set its parameters***************

	  SpeciesListRequest slr = new SpeciesListRequest();
	  slr.setGeographicalFilter(gf);
      slr.setTaxonReportingCategoryKey("NHMSYS0000080067");
	  slr.setSort(SpeciesSort.SCIENTIFIC);

	  //***********Finished building the SpeciesListRequest and setting its parameters***************


//*******************************The Gateway webservice request is now ready to use**************


//*************************************Process the results***************************************

  		SpeciesListResponse slresp = gws.getGatewaySoapPort().getSpeciesList(slr);

  		//Get the map image url
  		MapImage mi = slresp.getMap();
%>
<div align="center">
		<br><h3>Map of your '<%=type%>'</h3>
		<img src="<%=mi.getUrl()%>" border="1">
		<br><br><h3>Species with records overlapping your '<%=type%>'</h3>
		<table cellspacing="0" cellpadding="2"><tr><th>Species name <font size="-1">(links to gridmap)</font></th><th>Common name</th></tr>

<%
		//Get the list of species and loop through them
		SpeciesList nbnSpeciesList = slresp.getSpeciesList();
                List<Species> species = nbnSpeciesList.getSpecies();
		for(int i=0;i<species.size(); i++)
			out.println("<tr><td><a href=\"ws-gridmap.jsp?tvk=" + species.get(i).getTaxonVersionKey() + "\">" + species.get(i).getScientificName() + "</a></td><td>" + (species.get(i).getCommonName().equalsIgnoreCase("null")?"-":species.get(i).getCommonName()) + "</td></tr>");
%>

			</table>
			<br><h3>Datasets with records overlapping your '<%=type%>'</h3>

	  <div class="bottomLine">
		  <table class="tblBorder" cellspacing="0" cellpadding="2" width="100%">
			  <tr>
				  <th>Dataset</th>
				  <th>Provider</th>
					<%  // Get an array of datasts
                                  DatasetSummaryList nbnDatasetSummaryList = slresp.getDatasetSummaryList();
                                  List<DatasetSummary> datasets = nbnDatasetSummaryList.getDatasetSummary();
				  for (int i = 0; i < datasets.size(); i++) { %>
					  <tr>
						  <td width="50%"><a href="ws-dataset.jsp?dsKey=<%=datasets.get(i).getId()%>"><%=datasets.get(i).getProviderMetadata().getDatasetTitle()%></a></td>
						  <td width="50%"><%=datasets.get(i).getProviderMetadata().getDatasetProvider()%></td>
					  </tr>
					<% } %>
		  </table>
	  </div>

</div>



<%//*************************************Finished processing the results***************************************

%>
  <!-- Make sure to display the the link to the NBN Gateway terms and conditions and the powered by NBN logo. -->
  <div id="footer">
  <div id="tandc"><a href="http://data.nbn.org.uk/help/popups/generalTerms.jsp" class="popup">Gateway terms and conditions</a></div>
  <div id="NBNlogo"><a href="http://data.nbn.org.uk" target="_blank"><img alt="Powered by the NBN Gateway" title="Clicking on this logo will take you to the NBN Gateway website" border="0" src="http://data.nbn.org.uk/images/NBNPower.gif" /></a></div>
  </div>
  </div>
	</body>
</html>