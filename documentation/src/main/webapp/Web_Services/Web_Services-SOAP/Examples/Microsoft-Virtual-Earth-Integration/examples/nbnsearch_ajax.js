// ############# GENERAL VARIABLES

   var strAJAXProxy = "./../../NBNServlet/nbn.ws.clientTools.JSONConverter"   

   
   // #################### GENERAl FUNCTIONS ####################
    function $(id){return document.getElementById(id);};
            
    

   function Is(){   
    var agt=navigator.userAgent.toLowerCase();

    this.major = parseInt(navigator.appVersion);
    this.minor = parseFloat(navigator.appVersion);

    this.is_opera = (agt.indexOf("opera") != -1);
    this.is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
    this.is_nav  = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1)
                && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1)
                && (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));


}
   

   
 // Converts Latitude/Longitude coordinates to the BNG Grid

    function nbnConvertLatLontoOSGB(latlonarr){
             var osgbarr= new LatLng(latlonarr[1], latlonarr[0], this.targetgrid);                 
             osgbarr.WGS84ToOSGB36();
             var osgb =osgbarr.toOSRef();
             osgbarr=new Array(osgb.easting, osgb.northing);
             return osgbarr;             
             }
    
    // definition, which gid should be choosen -- big thank you to Andy Scott for the line function!!
    function nbnChooseTargetGrid(){
      if (this.actgrid == "EPSG_4326"){
            var x = null;
            var y = null;
          for (var z=0; z < this.polygonarray[0].length; z++){
           x = this.polygonarray[0][z];
           y = this.polygonarray[1][z];
           var decision = null;

           if (y > 55.75){
               if(y < 61.55 && x >-9.48 && x <3.66  ){this.targetgrid="osgb_BNG"} else {this.targetgrid=null};
               }
           else if (y >=51.25 && y <= 55.75){
               var pointOnLine = -0.0362 * Math.pow((y-52), 6) + 0.3153*Math.pow((y-52),5) -1.0111*Math.pow((y-52),4) +1.4113*Math.pow((y-52),3) -0.9018*Math.pow((y-52),2) +0.61368*(y-52)-5.7086;
               if ( x >pointOnLine && x < 3.66){ this.targetgrid="osgb_BNG"}
               else if( x < pointOnLine && x > - 11.17 ){this.targetgrid="osni_ING"; break;}
               else{this.targetgrid=null};
               }

           else if(y <51.25 ){
               if(y > 49.76 && x >-7.79 && x < 2.40  ){this.targetgrid="osgb_BNG"} else {this.targetgrid=null};

               }
            else {this.targetgrid=null}
           
          }
       }
      
      else{this.targetgrid=this.actgrid;}
    }
// Web Service for Category Search

function TaxRepCategorySearch(polygonarray, placeofresult, objectname){
    var i=0;  

    // props
    this.polygonarray=polygonarray;
    this.placeofresult=placeofresult;
    this.categorylist=null;
    this.JSONresp=null;
    this.overlayrule=null;
    this.minimumResolution=null;
    this.link=null;
    this.linkfct=null;
    this.title=null;
    this.actgrid="osgb_BNG";
    this.targetgrid=null;
    this.objectname=objectname;
    this.cleaned=false;
    this.searchstring="TaxonReportingCategoryListRequest";
    this.converted=false;

    
    // methods
    this.nbnSetData=nbnSetData;
    this.nbnSetOverlayRule=nbnSetOverlayRule;
    this.nbnSetMinimumResolution=nbnSetMinimumResolution;
    this.nbnGetPolygonArray=nbnGetPolygonArray;
    this.nbnSetLink=nbnSetLink;
    this.nbnSetTitle=nbnSetTitle;
    this.nbnCallSearch=nbnCallSearch;
    this.nbnShowTaxRepCategorySearchResult=nbnShowTaxRepCategorySearchResult;
    this.nbnSetActGrid=nbnSetActGrid;
    this.nbnConvertLatLontoOSGB=nbnConvertLatLontoOSGB;
    this.nbnChooseTargetGrid=nbnChooseTargetGrid;
    this.nbnCleanPolygon= nbnCleanPolygon;
    }


    // clean up polygon parameters by reducing to 2 positions after decimal point
function nbnCleanPolygon(){
    for(var i=0; i < this.polygonarray.length; i++){
        this.polygonarray[0][i]=Math.round(this.polygonarray[0][i]*100)/100
        this.polygonarray[1][i]=Math.round(this.polygonarray[1][i]*100)/100 
    }
    this.cleaned=true;
    };


function nbnCallSearch(){
    var JSONobjscript =null;
    var headnode=document.getElementsByTagName("head")[0];
    var is = new Is();
    var url = strAJAXProxy+"?call="+this.searchstring+"&"+this.nbnSetData();
    if(url.length>4000){
         $(this.placeofresult).innerHTML="<div class='NBNnoresults'>The polygone contains of too many points. Please choose a more simple polygon with fever points</div>";
        }
    else{
           if (is.is_ie) { // for ie, deny use of removechild 

               if ($("nbnJSONobjscript")!= null){

                   JSONobjscript=$("nbnJSONobjscript");
                   JSONobjscript.src=url;
                   }
               else
               {
                JSONobjscript =  document.createElement("script");
                headnode.appendChild(JSONobjscript);
                JSONobjscript.id="nbnJSONobjscript";
                JSONobjscript.type="text/javascript";
                JSONobjscript.src=url;
               }

           }
           else{ // for mozilla & co.
               if ($("nbnJSONobjscript")!= null){
                    headnode.removeChild($("nbnJSONobjscript"));
                    }
                JSONobjscript =  document.createElement("script");
                headnode.appendChild(JSONobjscript);
                JSONobjscript.id="nbnJSONobjscript";
                JSONobjscript.type="text/javascript";
                JSONobjscript.src=url;
               }
       }
}    
    
function nbnGetPolygonArray(){
    return this.polygonarray;
    }

function nbnSetTitle(title){
    if (title!=null){
        this.title=title;
        }
    }

function nbnSetActGrid(actgrid){
    if (actgrid!=null&& (actgrid=="EPSG_4326" ||actgrid=="osgb_BNG" || actgrid=="osni_ING")){
        this.actgrid=actgrid;
        }
    else {
        this.actgrid="osgb_BNG";
        }
    }

function nbnShowTaxRepCategorySearchResult(JSONrespobj){

     try{
        var JSONrespobj= eval('(' + JSONrespobj + ')');
    }
    catch(e){
        
        $(this.placeofresult).innerText=e.description+" the NBN SOAP Service delivered an error";
        return false;
        }
     
     if (JSONrespobj.error!= null){
         $(this.placeofresult).innerHTML="<div class='NBNnoresults'>"+JSONrespobj.error+"</div>";
         }
     else if (JSONrespobj.categorylist != null && JSONrespobj.categorylist[0] != null ){
    var JSONresptxt="";
    if (this.title!=null){JSONresptxt=JSONresptxt+"<div class='NBNtaxrepcategoryresulttitle'>"+this.title+"</div></br>"};
    for (var p=0; p< JSONrespobj.categorylist.length; p++){ 
        JSONresptxt=JSONresptxt+"<div class='NBNtaxrepcategoryresultitem'>";
        if (this.link==true){JSONresptxt=JSONresptxt+"<a href='' onclick='"+this.linkfct+"(\""+JSONrespobj.categorylist[p].categoryID+"\");  return false;' >";};
        JSONresptxt=JSONresptxt+JSONrespobj.categorylist[p].categoryName;
        if (this.link==true){JSONresptxt=JSONresptxt+"</a>"};
        JSONresptxt=JSONresptxt+"</div>";
        }
  $(this.placeofresult).innerHTML=JSONresptxt;
    }
    else if (JSONrespobj.error != null){
    $(this.placeofresult).innerHTML=JSONrespobj.error;
}
   else
  {
        $(this.placeofresult).innerHTML="<div class='NBNnoresults'>no results</div>";
    }
}

// Web Service for Species Search

function SpeciesSearch(polygonarray, placeofresult, objectname, categoryID){
    var i=0;
    // props
    this.categoryID=categoryID;
    this.polygonarray=polygonarray;
    this.placeofresult=placeofresult;
    this.categorylist=null;
    this.JSONresp=null;
    this.overlayrule=null;
    this.minimumResolution=null;
    this.link=null;
    this.linkfct=null;
    this.title=null;
    this.backfct=null;
    this.googleEarth=false;
    this.dataset=false;
    this.actgrid="osgb_BNG";
    this.targetgrid=null;
    this.onsitedata=null;
    this.objectname=objectname;
    this.cleaned=false;
    this.searchstring="SpeciesListRequest";
    this.converted=false;
    
    // methods
    this.nbnSetData=nbnSetData;
    this.nbnSetOverlayRule=nbnSetOverlayRule;
    this.nbnSetMinimumResolution=nbnSetMinimumResolution;
    this.nbnGetPolygonArray=nbnGetPolygonArray;
    this.nbnSetLink=nbnSetLink;
    this.nbnSetTitle=nbnSetTitle;
    this.nbnCallSearch=nbnCallSearch;
    this.nbnShowSpeciesSearchResult=nbnShowSpeciesSearchResult;
    this.nbnSetActGrid=nbnSetActGrid;
    this.nbnConvertLatLontoOSGB=nbnConvertLatLontoOSGB;
    this.nbnSetBackFct=nbnSetBackFct;
    this.nbnShowGoogleEarth=nbnShowGoogleEarth;
    this.nbnShowDataSet=nbnShowDataSet;
    this.nbnShowOnSiteData=nbnShowOnSiteData;
    this.nbnChooseTargetGrid=nbnChooseTargetGrid;
    this.nbnCleanPolygon= nbnCleanPolygon;
    }

function nbnSetMinimumResolution(minimumResolution){
    
        if (minimumResolution=="_100m" || minimumResolution=="_1km" || minimumResolution=="_2km" || minimumResolution=="_10km"){
        this.minimumResolution=minimumResolution;
        }
    else {
        $(this.placeofresult).innerHTML=("minimum Resolution contains wrong value!");
        }
    
    }

function nbnSetOverlayRule(overlayrule){
    
    if (overlayrule=="overlaps" || overlayrule=="within"){
        this.overlayrule=overlayrule;
        }
    else {
        $(this.placeofresult).innerHTML=("overlay rule contains wrong value!")
        }
    }

function nbnSetLink(link){
    if (link!=null){
        this.link=true;
        this.linkfct=link;
        }
    }
function nbnSetTitle(title){
    if (title!=null){
        this.title=title;
        }
    }
function nbnSetBackFct(backfct){
    if (backfct !=null){
        this.backfct=backfct;
        }
    }

function nbnShowGoogleEarth(googleEarth){
    if (googleEarth ==true){
        this.googleEarth=googleEarth;
        }
    }

function nbnShowDataSet(dataset){
    if (dataset ==true){
        this.dataset=dataset;
        }
    }
function nbnShowOnSiteData(onsitedata){
    if (onsitedata != null){
        this.onsitedata=onsitedata;
        }
    }

function nbnShowSpeciesSearchResult(JSONrespobj){
    
      try{
        var JSONrespobj= eval('(' + JSONrespobj + ')');
    }
    catch(e){
        
        $(this.placeofresult).innerHTML=e.description+" the NBN SOAP Service delivered an error";
        return false;
        }
        
      if (JSONrespobj.error!= null){
         $(this.placeofresult).innerHTML="<div class='NBNnoresults'>"+JSONrespobj.error+"</div>";
         }
         else if (JSONrespobj.error != null){
    $(this.placeofresult).innerHTML=JSONrespobj.error;
}
     else if (this.backfct!=null){
       var JSONresptxt="";
    JSONresptxt=JSONresptxt+"<p><a href='' onclick='"+this.backfct+"(); return false;'><img src='../../images/nbnAjaxAPI/back.gif' width='18' height='18' border='0' alt='back'></a></p>";
   }
   if (JSONrespobj.speciesList[0]!= null ){
  
    
    if (this.title!=null){JSONresptxt=JSONresptxt+"<div class='NBNspeciesresulttitle'>"+this.title+"</div></br>"};
   
    for (var p=0; p< JSONrespobj.speciesList.length; p++){ 
        JSONresptxt=JSONresptxt+"<div class='NBNspeciesresultitem'>";
        if (this.link==true){JSONresptxt=JSONresptxt+"<a href='' onclick='"+this.linkfct+"(\""+this.polygonarray+"\", \""+JSONrespobj.speciesList[p].taxonVersionKey+"\");  return false;' >";};
        JSONresptxt=JSONresptxt+JSONrespobj.speciesList[p].scientificName;
        if (JSONrespobj.speciesList[p].commonName != ""){JSONresptxt=JSONresptxt+" ("+JSONrespobj.speciesList[p].commonName+")"};
        if (this.link==true){JSONresptxt=JSONresptxt+"</a>"};
        JSONresptxt=JSONresptxt+"</div>";
        if (this.onsitedata!=null){JSONresptxt=JSONresptxt+"<div class='NBNshowonmap'><a href='' onclick='"+this.onsitedata+"(\""+this.categoryID+"\", \""+JSONrespobj.speciesList[p].taxonVersionKey+"\");  return false;'><img src='../../images/nbnAjaxAPI/show_on_map.gif' alt='show species on map'></a>&nbsp;show on map</div>"};
        if (this.googleEarth==true){JSONresptxt=JSONresptxt+"<div class='NBNshowongoogle'><form name='getData' target='_new' action='http://www.brc.ac.uk/servlet/ge' method='post'><input type='image' src='../../images/nbnAjaxAPI/google_earth.gif' alt='show species on Google Earth'><input type='hidden' name='tvk' value='"+JSONrespobj.speciesList[p].taxonVersionKey+"'></form>&nbsp;show on google earth</div>"};
        
        
        }
    
    if (this.dataset==true){
        JSONresptxt=JSONresptxt+"<div class='NBNdatasetresulttitle'>used Datasets</div></br>"
        for (var r=0; r< JSONrespobj.datasetList.length; r++){ 
            JSONresptxt=JSONresptxt+"<div class='NBNdatasetresultitem'>";
            JSONresptxt=JSONresptxt+JSONrespobj.datasetList[r].datasetName+" ("+JSONrespobj.datasetList[r].datasetProvider+")";
            JSONresptxt=JSONresptxt+"</div>";
            }
        
        }
    
    
    $(this.placeofresult).innerHTML=JSONresptxt;
    }
   else
  {
        $(this.placeofresult).innerHTML="<div class='NBNnoresults'>no results</div>";
    }
    
}

function OnSiteDataSearch(polygonarray, placeofresult, objectname, categoryID, speciesID){
    var i=0;
    // props
    this.speciesID=speciesID;
    this.categoryID=categoryID;
    this.polygonarray=polygonarray;
    this.placeofresult=placeofresult;
    this.sitelist=null;
    this.JSONresp=null;
    this.overlayrule=null;
    this.minimumResolution=null;
    this.link=null;
    this.linkfct=null;
    this.title=null;
    this.backfct=null;
    this.actgrid="osgb_BNG";
    this.targetgrid=null;
    this.callback=false;
    this.cleaned=false;
    this.searchstring="OnSiteDataRequest";
    this.objectname=objectname;
    this.converted=false;
    
    // methods
    this.nbnSetData=nbnSetData;
    this.nbnSetOverlayRule=nbnSetOverlayRule;
    this.nbnSetMinimumResolution=nbnSetMinimumResolution;
    this.nbnGetPolygonArray=nbnGetPolygonArray;
    this.nbnSetLink=nbnSetLink;
    this.nbnSetTitle=nbnSetTitle;
    this.nbnCallSearch=nbnCallSearch;
    this.nbnShowOnSiteDataSearchResult=nbnShowOnSiteDataSearchResult;
    this.nbnSetActGrid=nbnSetActGrid;
    this.nbnConvertLatLontoOSGB=nbnConvertLatLontoOSGB;
    this.nbnChooseTargetGrid=nbnChooseTargetGrid;
    this.nbnSetBackFct=nbnSetBackFct;
    this.nbnSetCallBack=nbnSetCallBack;
    this.nbnCleanPolygon= nbnCleanPolygon;
    
    }


function nbnSetData() {
    // create polygone parameter
    this.nbnChooseTargetGrid();
    if (this.actgrid=="EPSG_4326"&& this.converted==false){
        for (var q=0; q<this.polygonarray[0].length; q++){
         var polyarr=new Array(this.polygonarray[0][q], this.polygonarray[1][q]);
         this.polygonarray[0][q]=this.nbnConvertLatLontoOSGB(polyarr)[0];
         this.polygonarray[1][q]=this.nbnConvertLatLontoOSGB(polyarr)[1];
         
        }
      this.converted=true;   
      this.cleaned=false;
      };
    if (this.cleaned==false){this.nbnCleanPolygon()};
    
    if (this.polygonarray.length!=0){
     var JSONpolygon='{"polygon": [';
     for (var i=0; i<this.polygonarray[0].length; i++){
         JSONpolygon=JSONpolygon+'{"longitude":"'+this.polygonarray[0][i]+'", "latitude":"'+this.polygonarray[1][i]+'"}'
         if(i!=this.polygonarray[0].length-1){JSONpolygon=JSONpolygon+","};
         }
     JSONpolygon=JSONpolygon+']';
    }
    else {
        
        $(this.placeofresult).innerHTML=("polygon is not defined!");
        }
    
    //     create other parameters
    var parameters=', "parameters":{';
    if (this.overlayrule!=null){
        parameters=parameters+' "overlayRule":"'+this.overlayrule+'",'
        }
    
     if (this.minimumResolution!=null){
        parameters=parameters+' "minimumResolution":"'+this.minimumResolution+'",'
        }
    
    if (this.targetgrid!=null){
        parameters=parameters+' "grid":"'+this.targetgrid+'",'
        }
    
    if (this.categoryID!=null){
        parameters=parameters+' "categoryID":"'+this.categoryID+'",'
        }
    if (this.speciesID!=null){
        parameters=parameters+' "speciesID":"'+this.speciesID+'",'
        } 
    parameters=parameters+' "searchobj":"'+this.objectname+'",'
    parameters=parameters+'}}';
    return "JSONrequest="+JSONpolygon+parameters;
    }

function nbnShowOnSiteDataSearchResult(JSONrespobj){
    try{
        var JSONrespobj= eval('(' + JSONrespobj + ')');
    }
    catch(e){
        
        $(this.placeofresult).innerHTML=e.description+" the NBN SOAP Service delivered an error";
        return false;
        }
    
   
      if (JSONrespobj.error!= null){
         $(this.placeofresult).innerHTML="<div class='NBNnoresults'>"+JSONrespobj.error+"</div>";
         }
     else if (JSONrespobj.onsitelist[0]!= null ){
        var JSONresptxt="";
        var sitearray=new Array(JSONrespobj.onsitelist[0].length);
        for (var p=0; p< JSONrespobj.onsitelist.length; p++){ 
             sitearray[p]=new Array();
             sitearray[p][0]=JSONrespobj.onsitelist[p].boundingbox_min_x;
             sitearray[p][1]=JSONrespobj.onsitelist[p].boundingbox_min_y;
             sitearray[p][2]=JSONrespobj.onsitelist[p].boundingbox_max_x;
             sitearray[p][3]=JSONrespobj.onsitelist[p].boundingbox_max_y;
             
             if(this.actgrid == "EPSG_4326"){
                    // convert back to lat / lon
                    var latlonmin=new OSRef(sitearray[p][0], sitearray[p][1], this.targetgrid).toLatLng();
                    latlonmin.OSGB36ToWGS84(this.targetgrid);
                    sitearray[p][0] = latlonmin.lat;
                    sitearray[p][1] = latlonmin.lng;
                    
                    var latlonmax=new OSRef( sitearray[p][2], sitearray[p][3], this.targetgrid).toLatLng();
                    latlonmax.OSGB36ToWGS84(this.targetgrid);
                    sitearray[p][2] = latlonmax.lat; 
                    sitearray[p][3] = latlonmax.lng;
                    }
             
        }
        if (this.callback==true){
                
          nbnSiteSearchCallBack(sitearray);
        }
  
    }
    
}

function nbnSetCallBack(trigger){
    if (trigger==true){
        this.callback=true;
        }
    
    }

//================================================================== 
// BNG Transformation
// adapted from
// JScoord
// jscoord.js
// (c) 2005 Jonathan Stott
// Created on 21-Dec-2005
// version 1.1.1 - 16 Jan 2006
// GNU public licence
//--------------------------------------------------------------------------


function LatLng(lat, lng, grid) {
  this.lat = lat;
  this.lng = lng;
  this.grid=grid;
      
  this.toOSRef = LatLngToOSRef; 
  this.WGS84ToOSGB36 = WGS84ToOSGB36; 
  this.OSGB36ToWGS84 = OSGB36ToWGS84;
  this.toString = LatLngToString;
}

function LatLngToString() {
  return "(" + this.lat + ", " + this.lng + ")";
}


// References given with OSRef are accurate to 1m.
function OSRef(easting, northing, grid) {
  this.grid=grid;
  this.easting  = easting;
  this.northing = northing;
  
  this.toLatLng = OSRefToLatLng;
}



// RefEll

function RefEll(maj, min) {
  this.maj = maj;
  this.min = min;
  this.ecc = ((maj * maj) - (min * min)) / (maj * maj);
}


// Mathematical Functions

function sinSquared(x) {
  return Math.sin(x) * Math.sin(x);
}

function cosSquared(x) {
  return Math.cos(x) * Math.cos(x);
}

function tanSquared(x) {
  return Math.tan(x) * Math.tan(x);
}

function sec(x) {
  return 1.0 / Math.cos(x);
}
  
function deg2rad(x) {
  return x * (Math.PI / 180);
}
  
function rad2deg(x) {
  return x * (180 / Math.PI);
}
  
function chr(x) {
  var h = x.toString (16);
  if (h.length == 1)
    h = "0" + h;
  h = "%" + h;
  return unescape (h);
}
  
function ord(x) {
  var c = x.charAt(0);
  var i;
  for (i = 0; i < 256; ++ i) {
    var h = i.toString (16);
    if (h.length == 1)
      h = "0" + h;
    h = "%" + h;
    h = unescape (h);
    if (h == c)
      break;
  }
  return i;
}

/**
 * Convert a latitude and longitude into an OSGB grid reference
 *
 * @param latitudeLongitude the latitude and longitude to convert
 * @return the OSGB grid reference
 * @since 0.1
 */
function LatLngToOSRef() {
  // general  
  var phi = deg2rad(this.lat);
  var lambda = deg2rad(this.lng);
  
  var E = 0.0;
  var N = 0.0;
  
  if (this.grid=="osgb_BNG"){
  /// transfer variables to mercator OSGB
  var airy1830 = new RefEll(6377563.396, 6356256.909);    // (british national grid
  var OSGB_F0  = 0.9996012717;
  var OSGB_NO       = -100000.0;
  var OSGB_EO       = 400000.0;
  var OSGB_phi0     = deg2rad(49.0);
  var OSGB_lambda0  = deg2rad(-2.0);
  
  var OSGB_a        = airy1830.maj;
  var OSGB_b        = airy1830.min;
  var OSGB_eSquared = airy1830.ecc;
  var OSGB_n = (OSGB_a - OSGB_b) / (OSGB_a + OSGB_b);
  var OSGB_v = OSGB_a * OSGB_F0 * Math.pow(1.0 - OSGB_eSquared * sinSquared(phi), -0.5);
  
  var OSGB_rho =    OSGB_a * OSGB_F0 * (1.0 - OSGB_eSquared) * Math.pow(1.0 - OSGB_eSquared * sinSquared(phi), -1.5);
  var OSGB_etaSquared = (OSGB_v / OSGB_rho) - 1.0;
  
   var OSGB_M =
    (OSGB_b * OSGB_F0)
      * (((1 + OSGB_n + ((5.0 / 4.0) * OSGB_n * OSGB_n) + ((5.0 / 4.0) * OSGB_n * OSGB_n * OSGB_n))
        * (phi - OSGB_phi0))
        - (((3 * OSGB_n) + (3 * OSGB_n * OSGB_n) + ((21.0 / 8.0) * OSGB_n * OSGB_n * OSGB_n))
          * Math.sin(phi - OSGB_phi0)
          * Math.cos(phi + OSGB_phi0))
        + ((((15.0 / 8.0) * OSGB_n * OSGB_n) + ((15.0 / 8.0) * OSGB_n * OSGB_n * OSGB_n))
          * Math.sin(2.0 * (phi - OSGB_phi0))
          * Math.cos(2.0 * (phi + OSGB_phi0)))
        - (((35.0 / 24.0) * OSGB_n * OSGB_n * OSGB_n)
          * Math.sin(3.0 * (phi - OSGB_phi0))
          * Math.cos(3.0 * (phi + OSGB_phi0))));
     var OSGB_I = OSGB_M + OSGB_NO;
     var OSGB_II = (OSGB_v / 2.0) * Math.sin(phi) * Math.cos(phi);
     
     var OSGB_III =
    (OSGB_v / 24.0)
      * Math.sin(phi)
      * Math.pow(Math.cos(phi), 3.0)
      * (5.0 - tanSquared(phi) + (9.0 * OSGB_etaSquared));
    
     var OSGB_IIIA =
    (OSGB_v / 720.0)
      * Math.sin(phi)
      * Math.pow(Math.cos(phi), 5.0)
      * (61.0 - (58.0 * tanSquared(phi)) + Math.pow(Math.tan(phi), 4.0));
     var OSGB_IV = OSGB_v * Math.cos(phi);
     var OSGB_V = (OSGB_v / 6.0) * Math.pow(Math.cos(phi), 3.0) * ((OSGB_v / OSGB_rho) - tanSquared(phi));
     
     var OSGB_VI =
    (OSGB_v / 120.0)
      * Math.pow(Math.cos(phi), 5.0)
      * (5.0
        - (18.0 * tanSquared(phi))
        + (Math.pow(Math.tan(phi), 4.0))
        + (14 * OSGB_etaSquared)
        - (58 * tanSquared(phi) * OSGB_etaSquared));
     
    OSGB_N =
    OSGB_I
      + (OSGB_II * Math.pow(lambda - OSGB_lambda0, 2.0))
      + (OSGB_III * Math.pow(lambda - OSGB_lambda0, 4.0))
      + (OSGB_IIIA * Math.pow(lambda - OSGB_lambda0, 6.0));
    
     OSGB_E =
    OSGB_EO
      + (OSGB_IV * (lambda - OSGB_lambda0))
      + (OSGB_V * Math.pow(lambda - OSGB_lambda0, 3.0))
      + (OSGB_VI * Math.pow(lambda - OSGB_lambda0, 5.0));
     
     return new OSRef(OSGB_E, OSGB_N);
   }
  if (this.grid=="osni_ING"){
  /// transfer variables to mercator Ireland
   var airy1830mod = new RefEll(6377340.189, 6356034.447); // (irish national grid)
   var IRELAND_F0 = 1.000035;
   var IRELAND_NO       = 250000.0;
   var IRELAND_EO       = 200000.0;
   var IRELAND_phi0     = deg2rad(53.5);
   var IRELAND_lambda0  = deg2rad(-8.0);
   
   var IRELAND_a        = airy1830mod.maj;
   var IRELAND_b        = airy1830mod.min;
   var IRELAND_eSquared = airy1830mod.ecc;
   
   var IRELAND_n = (IRELAND_a - IRELAND_b) / (IRELAND_a + IRELAND_b);
   var IRELAND_v = IRELAND_a * IRELAND_F0 * Math.pow(1.0 - IRELAND_eSquared * sinSquared(phi), -0.5);
   var IRELAND_rho =    IRELAND_a * IRELAND_F0 * (1.0 - IRELAND_eSquared) * Math.pow(1.0 - IRELAND_eSquared * sinSquared(phi), -1.5);
   var IRELAND_etaSquared = (IRELAND_v / IRELAND_rho) - 1.0;
   var IRELAND_M =
    (IRELAND_b * IRELAND_F0)
      * (((1 + IRELAND_n + ((5.0 / 4.0) * IRELAND_n * IRELAND_n) + ((5.0 / 4.0) * IRELAND_n * IRELAND_n * IRELAND_n))
        * (phi - IRELAND_phi0))
        - (((3 * IRELAND_n) + (3 * IRELAND_n * IRELAND_n) + ((21.0 / 8.0) * IRELAND_n * IRELAND_n * IRELAND_n))
          * Math.sin(phi - IRELAND_phi0)
          * Math.cos(phi + IRELAND_phi0))
        + ((((15.0 / 8.0) * IRELAND_n * IRELAND_n) + ((15.0 / 8.0) * IRELAND_n * IRELAND_n * IRELAND_n))
          * Math.sin(2.0 * (phi - IRELAND_phi0))
          * Math.cos(2.0 * (phi + IRELAND_phi0)))
        - (((35.0 / 24.0) * IRELAND_n * IRELAND_n * IRELAND_n)
          * Math.sin(3.0 * (phi - IRELAND_phi0))
          * Math.cos(3.0 * (phi + IRELAND_phi0))));
 
  var IRELAND_I = IRELAND_M + IRELAND_NO;
  var IRELAND_II = (IRELAND_v / 2.0) * Math.sin(phi) * Math.cos(phi);
  
  var IRELAND_III =
    (IRELAND_v / 24.0)
      * Math.sin(phi)
      * Math.pow(Math.cos(phi), 3.0)
      * (5.0 - tanSquared(phi) + (9.0 * IRELAND_etaSquared));

  var IRELAND_IIIA =
    (IRELAND_v / 720.0)
      * Math.sin(phi)
      * Math.pow(Math.cos(phi), 5.0)
      * (61.0 - (58.0 * tanSquared(phi)) + Math.pow(Math.tan(phi), 4.0));
  
  var IRELAND_IV = IRELAND_v * Math.cos(phi);
  var IRELAND_V = (IRELAND_v / 6.0) * Math.pow(Math.cos(phi), 3.0) * ((IRELAND_v / IRELAND_rho) - tanSquared(phi));
  
  var IRELAND_VI =
    (IRELAND_v / 120.0)
      * Math.pow(Math.cos(phi), 5.0)
      * (5.0
        - (18.0 * tanSquared(phi))
        + (Math.pow(Math.tan(phi), 4.0))
        + (14 * IRELAND_etaSquared)
        - (58 * tanSquared(phi) * IRELAND_etaSquared));

  IRELAND_N =
    IRELAND_I
      + (IRELAND_II * Math.pow(lambda - IRELAND_lambda0, 2.0))
      + (IRELAND_III * Math.pow(lambda - IRELAND_lambda0, 4.0))
      + (IRELAND_IIIA * Math.pow(lambda - IRELAND_lambda0, 6.0));
  IRELAND_E =
    IRELAND_EO
      + (IRELAND_IV * (lambda - IRELAND_lambda0))
      + (IRELAND_V * Math.pow(lambda - IRELAND_lambda0, 3.0))
      + (IRELAND_VI * Math.pow(lambda - IRELAND_lambda0, 5.0));

  return new OSRef(IRELAND_E, IRELAND_N);
  }

}

function WGS84ToOSGB36() {
  var wgs84 = new RefEll(6378137.000, 6356752.3141);
  var a        = wgs84.maj;
  var b        = wgs84.min;
  var eSquared = wgs84.ecc;
  var phi = deg2rad(this.lat);
  var lambda = deg2rad(this.lng);
  var v = a / (Math.sqrt(1 - eSquared * sinSquared(phi)));
  var H = 0; // height
  var x = (v + H) * Math.cos(phi) * Math.cos(lambda);
  var y = (v + H) * Math.cos(phi) * Math.sin(lambda);
  var z = ((1 - eSquared) * v + H) * Math.sin(phi);
    
  if (this.grid=="osgb_BNG"){
  // helmet paramters for England
  var OSGB_tx =       -446.448;
  var OSGB_ty =        124.157;
  var OSGB_tz =       -542.060;
  var OSGB_s  =        0.0000204894;
  var OSGB_rx = deg2rad(-0.00004172222);
  var OSGB_ry = deg2rad(-0.00006861111);
  var OSGB_rz = deg2rad(-0.00023391666);
  
  var OSGB_xB = OSGB_tx + (x * (1 + OSGB_s)) + (-OSGB_rx * y)     + (OSGB_ry * z);
  var OSGB_yB = OSGB_ty + (OSGB_rz * x)      + (y * (1 + OSGB_s)) + (-OSGB_rx * z);
  var OSGB_zB = OSGB_tz + (-OSGB_ry * x)     + (OSGB_rx * y)      + (z * (1 + OSGB_s));
  
  var airy1830 = new RefEll(6377563.396, 6356256.909);
  var OSGB_a        = airy1830.maj;
  var OSGB_b        = airy1830.min;
  var OSGB_eSquared = airy1830.ecc;
  var OSGB_p = Math.sqrt((OSGB_xB * OSGB_xB) + (OSGB_yB * OSGB_yB));
  var OSGB_lambdaB = rad2deg(Math.atan(OSGB_yB / OSGB_xB));
  var OSGB_phiN = Math.atan(OSGB_zB / (OSGB_p * (1 - OSGB_eSquared)));
  for (var i = 1; i < 10; i++) {
    v = OSGB_a / (Math.sqrt(1 - OSGB_eSquared * sinSquared(OSGB_phiN)));
    OSGB_phiN1 = Math.atan((OSGB_zB + (OSGB_eSquared * v * Math.sin(OSGB_phiN))) / OSGB_p);
    OSGB_phiN = OSGB_phiN1;
  }
  var OSGB_phiB = rad2deg(OSGB_phiN);
  
  this.lat = OSGB_phiB;
  this.lng = OSGB_lambdaB;
  }
 if (this.grid=="osni_ING"){
     
  // Helmet parameters for Ireland
  var IRELAND_tx =       -482.530;
  var IRELAND_ty =        130.596;
  var IRELAND_tz =       -564.557;
  var IRELAND_s  =       -0.000008150;
  var IRELAND_rx = deg2rad(-0.00028944444);
  var IRELAND_ry = deg2rad(-0.00005944444);
  var IRELAND_rz = deg2rad(-0.00017527777);
  
  
  var IRELAND_xB = IRELAND_tx + (x * (1 + IRELAND_s)) + (-IRELAND_rx * y)     + (IRELAND_ry * z);
  var IRELAND_yB = IRELAND_ty + (IRELAND_rz * x)      + (y * (1 + IRELAND_s)) + (-IRELAND_rx * z);
  var IRELAND_zB = IRELAND_tz + (-IRELAND_ry * x)     + (IRELAND_rx * y)      + (z * (1 + IRELAND_s));
 
  var airy1830mod = new RefEll(6377340.189, 6356034.447);
  IRELAND_a        = airy1830mod.maj;
  IRELAND_b        = airy1830mod.min;
  IRELAND_eSquared = airy1830mod.ecc;

  var IRELAND_lambdaB = rad2deg(Math.atan(IRELAND_yB / IRELAND_xB));
  var IRELAND_p = Math.sqrt((IRELAND_xB * IRELAND_xB) + (IRELAND_yB * IRELAND_yB));
  var IRELAND_phiN = Math.atan(IRELAND_zB / (IRELAND_p * (1 - IRELAND_eSquared)));
  for (var i = 1; i < 10; i++) {
    IRELAND_v = IRELAND_a / (Math.sqrt(1 - IRELAND_eSquared * sinSquared(IRELAND_phiN)));
    IRELAND_phiN1 = Math.atan((IRELAND_zB + (IRELAND_eSquared * v * Math.sin(IRELAND_phiN))) / IRELAND_p);
    IRELAND_phiN = IRELAND_phiN1;
  }
  var IRELAND_phiB = rad2deg(IRELAND_phiN);
 
  
  
  this.lat = IRELAND_phiB;
  this.lng = IRELAND_lambdaB;
  }
}


function OSRefToLatLng() {
  var E        = this.easting;
  var N        = this.northing;
  var phi      = 0.0;
  var lambda   = 0.0;
  var M= 0.0;

  if (this.grid=="osgb_BNG"){        
  var airy1830 = new RefEll(6377563.396, 6356256.909);
  var OSGB_F0  = 0.9996012717;
  var OSGB_N0  = -100000.0;
  var OSGB_E0       = 400000.0;
  var OSGB_phi0     = deg2rad(49.0);
  var OSGB_lambda0  = deg2rad(-2.0);
  var OSGB_a        = airy1830.maj;
  var OSGB_b        = airy1830.min;
  var OSGB_eSquared = airy1830.ecc;
 

  var OSGB_n        = (OSGB_a - OSGB_b) / (OSGB_a + OSGB_b);
  var OSGB_phiPrime = ((N - OSGB_N0) / (OSGB_a * OSGB_F0)) + OSGB_phi0;
  do {
    OSGB_M =
      (OSGB_b * OSGB_F0)
        * (((1 + OSGB_n + ((5.0 / 4.0) * OSGB_n * OSGB_n) + ((5.0 / 4.0) * OSGB_n * OSGB_n * OSGB_n))
          * (OSGB_phiPrime - OSGB_phi0))
          - (((3 * OSGB_n) + (3 * OSGB_n * OSGB_n) + ((21.0 / 8.0) * OSGB_n * OSGB_n * OSGB_n))
            * Math.sin(OSGB_phiPrime - OSGB_phi0)
            * Math.cos(OSGB_phiPrime + OSGB_phi0))
          + ((((15.0 / 8.0) * OSGB_n * OSGB_n) + ((15.0 / 8.0) * OSGB_n * OSGB_n * OSGB_n))
            * Math.sin(2.0 * (OSGB_phiPrime - OSGB_phi0))
            * Math.cos(2.0 * (OSGB_phiPrime + OSGB_phi0)))
          - (((35.0 / 24.0) * OSGB_n * OSGB_n * OSGB_n)
            * Math.sin(3.0 * (OSGB_phiPrime - OSGB_phi0))
            * Math.cos(3.0 * (OSGB_phiPrime + OSGB_phi0))));
    OSGB_phiPrime += (N - OSGB_N0 - OSGB_M) / (OSGB_a * OSGB_F0);
  } while ((N - OSGB_N0 - OSGB_M) >= 0.001);
  var OSGB_v = OSGB_a * OSGB_F0 * Math.pow(1.0 - OSGB_eSquared * sinSquared(OSGB_phiPrime), -0.5);
  var OSGB_rho =
    OSGB_a
      * OSGB_F0
      * (1.0 - OSGB_eSquared)
      * Math.pow(1.0 - OSGB_eSquared * sinSquared(OSGB_phiPrime), -1.5);
  var OSGB_etaSquared = (OSGB_v / OSGB_rho) - 1.0;
  var OSGB_VII = Math.tan(OSGB_phiPrime) / (2 * OSGB_rho * OSGB_v);
  var OSGB_VIII =
    (Math.tan(OSGB_phiPrime) / (24.0 * OSGB_rho * Math.pow(OSGB_v, 3.0)))
      * (5.0
        + (3.0 * tanSquared(OSGB_phiPrime))
        + OSGB_etaSquared
        - (9.0 * tanSquared(OSGB_phiPrime) * OSGB_etaSquared));
  var OSGB_IX =
    (Math.tan(OSGB_phiPrime) / (720.0 * OSGB_rho * Math.pow(OSGB_v, 5.0)))
      * (61.0
        + (90.0 * tanSquared(OSGB_phiPrime))
        + (45.0 * tanSquared(OSGB_phiPrime) * tanSquared(OSGB_phiPrime)));
  var OSGB_X = sec(OSGB_phiPrime) / OSGB_v;
  var OSGB_XI =
    (sec(OSGB_phiPrime) / (6.0 * OSGB_v * OSGB_v * OSGB_v))
      * ((OSGB_v / OSGB_rho) + (2 * tanSquared(OSGB_phiPrime)));
  var OSGB_XII =
    (sec(OSGB_phiPrime) / (120.0 * Math.pow(OSGB_v, 5.0)))
      * (5.0
        + (28.0 * tanSquared(OSGB_phiPrime))
        + (24.0 * tanSquared(OSGB_phiPrime) * tanSquared(OSGB_phiPrime)));
  var OSGB_XIIA =
    (sec(OSGB_phiPrime) / (5040.0 * Math.pow(OSGB_v, 7.0)))
      * (61.0
        + (662.0 * tanSquared(OSGB_phiPrime))
        + (1320.0 * tanSquared(OSGB_phiPrime) * tanSquared(OSGB_phiPrime))
        + (720.0
          * tanSquared(OSGB_phiPrime)
          * tanSquared(OSGB_phiPrime)
          * tanSquared(OSGB_phiPrime)));
  OSGB_phi =
    OSGB_phiPrime
      - (OSGB_VII * Math.pow(E - OSGB_E0, 2.0))
      + (OSGB_VIII * Math.pow(E - OSGB_E0, 4.0))
      - (OSGB_IX * Math.pow(E - OSGB_E0, 6.0));
  OSGB_lambda =
    OSGB_lambda0
      + (OSGB_X * (E - OSGB_E0))
      - (OSGB_XI * Math.pow(E - OSGB_E0, 3.0))
      + (OSGB_XII * Math.pow(E - OSGB_E0, 5.0))
      - (OSGB_XIIA * Math.pow(E - OSGB_E0, 7.0));

  return new LatLng(rad2deg(OSGB_phi), rad2deg(OSGB_lambda));
  }
  
  if (this.grid=="osni_ING"){   
  var airy1830mod = new RefEll(6377340.189, 6356034.447);
  var IRELAND_F0 = 1.000035;
  var IRELAND_N0       = 250000.0;
  var IRELAND_EO       = 200000.0;
  var IRELAND_phi0     = deg2rad(53.5);
  var IRELAND_lambda0  = deg2rad(-8.0);
  var IRELAND_a        = airy1830mod.maj;
  var IRELAND_b        = airy1830mod.min;
  var IRELAND_eSquared = airy1830mod.ecc;
  var IRELAND_n        = (IRELAND_a - IRELAND_b) / (IRELAND_a + IRELAND_b);
 
  var IRELAND_phiPrime = ((N - IRELAND_N0) / (IRELAND_a * IRELAND_F0)) + IRELAND_phi0;
  do {
    IRELAND_M =
      (IRELAND_b * IRELAND_F0)
        * (((1 + IRELAND_n + ((5.0 / 4.0) * IRELAND_n * IRELAND_n) + ((5.0 / 4.0) * IRELAND_n * IRELAND_n * IRELAND_n))
          * (IRELAND_phiPrime - IRELAND_phi0))
          - (((3 * IRELAND_n) + (3 * IRELAND_n * IRELAND_n) + ((21.0 / 8.0) * IRELAND_n * IRELAND_n * IRELAND_n))
            * Math.sin(IRELAND_phiPrime - IRELAND_phi0)
            * Math.cos(IRELAND_phiPrime + IRELAND_phi0))
          + ((((15.0 / 8.0) * IRELAND_n * IRELAND_n) + ((15.0 / 8.0) * IRELAND_n * IRELAND_n * IRELAND_n))
            * Math.sin(2.0 * (IRELAND_phiPrime - IRELAND_phi0))
            * Math.cos(2.0 * (IRELAND_phiPrime + IRELAND_phi0)))
          - (((35.0 / 24.0) * IRELAND_n * IRELAND_n * IRELAND_n)
            * Math.sin(3.0 * (IRELAND_phiPrime - IRELAND_phi0))
            * Math.cos(3.0 * (IRELAND_phiPrime + IRELAND_phi0))));
    IRELAND_phiPrime += (N - IRELAND_N0 - IRELAND_M) / (IRELAND_a * IRELAND_F0);
  } while ((N - IRELAND_N0 - IRELAND_M) >= 0.001);
  var IRELAND_v = IRELAND_a * IRELAND_F0 * Math.pow(1.0 - IRELAND_eSquared * sinSquared(IRELAND_phiPrime), -0.5);
  var IRELAND_rho =
    IRELAND_a
      * IRELAND_F0
      * (1.0 - IRELAND_eSquared)
      * Math.pow(1.0 - IRELAND_eSquared * sinSquared(IRELAND_phiPrime), -1.5);
  var IRELAND_etaSquared = (IRELAND_v / IRELAND_rho) - 1.0;
  var IRELAND_VII = Math.tan(IRELAND_phiPrime) / (2 * IRELAND_rho * IRELAND_v);
  var IRELAND_VIII =
    (Math.tan(IRELAND_phiPrime) / (24.0 * IRELAND_rho * Math.pow(IRELAND_v, 3.0)))
      * (5.0
        + (3.0 * tanSquared(IRELAND_phiPrime))
        + IRELAND_etaSquared
        - (9.0 * tanSquared(IRELAND_phiPrime) * IRELAND_etaSquared));
  var IRELAND_IX =
    (Math.tan(IRELAND_phiPrime) / (720.0 * IRELAND_rho * Math.pow(IRELAND_v, 5.0)))
      * (61.0
        + (90.0 * tanSquared(IRELAND_phiPrime))
        + (45.0 * tanSquared(IRELAND_phiPrime) * tanSquared(IRELAND_phiPrime)));
  var IRELAND_X = sec(IRELAND_phiPrime) / IRELAND_v;
  var IRELAND_XI =
    (sec(IRELAND_phiPrime) / (6.0 * IRELAND_v * IRELAND_v * IRELAND_v))
      * ((IRELAND_v / IRELAND_rho) + (2 * tanSquared(IRELAND_phiPrime)));
  var IRELAND_XII =
    (sec(IRELAND_phiPrime) / (120.0 * Math.pow(IRELAND_v, 5.0)))
      * (5.0
        + (28.0 * tanSquared(IRELAND_phiPrime))
        + (24.0 * tanSquared(IRELAND_phiPrime) * tanSquared(IRELAND_phiPrime)));
  var IRELAND_XIIA =
    (sec(IRELAND_phiPrime) / (5040.0 * Math.pow(IRELAND_v, 7.0)))
      * (61.0
        + (662.0 * tanSquared(IRELAND_phiPrime))
        + (1320.0 * tanSquared(IRELAND_phiPrime) * tanSquared(IRELAND_phiPrime))
        + (720.0
          * tanSquared(IRELAND_phiPrime)
          * tanSquared(IRELAND_phiPrime)
          * tanSquared(IRELAND_phiPrime)));
  IRELAND_phi =
    IRELAND_phiPrime
      - (IRELAND_VII * Math.pow(E - IRELAND_EO, 2.0))
      + (IRELAND_VIII * Math.pow(E - IRELAND_EO, 4.0))
      - (IRELAND_IX * Math.pow(E - IRELAND_EO, 6.0));
  IRELAND_lambda =
    IRELAND_lambda0
      + (IRELAND_X * (E - IRELAND_EO))
      - (IRELAND_XI * Math.pow(E - IRELAND_EO, 3.0))
      + (IRELAND_XII * Math.pow(E - IRELAND_EO, 5.0))
      - (IRELAND_XIIA * Math.pow(E - IRELAND_EO, 7.0));

  return new LatLng(rad2deg(IRELAND_phi), rad2deg(IRELAND_lambda));
  }
}

function OSGB36ToWGS84(grid) {
  var phi = deg2rad(this.lat);
  var lambda = deg2rad(this.lng);
  var H = 0; // height
  var wgs84 = new RefEll(6378137.000, 6356752.3141);
  a        = wgs84.maj;
  b        = wgs84.min;
  eSquared = wgs84.ecc;
  
 if (grid=="osgb_BNG"){
  var airy1830 = new RefEll(6377563.396, 6356256.909);
  var OSGB_a        = airy1830.maj;
  var OSGB_b        = airy1830.min;
  var OSGB_eSquared = airy1830.ecc;

  var OSGB_v = OSGB_a / (Math.sqrt(1 - OSGB_eSquared * sinSquared(phi)));
  
  var OSGB_x = (OSGB_v + H) * Math.cos(phi) * Math.cos(lambda);
  var OSGB_y = (OSGB_v + H) * Math.cos(phi) * Math.sin(lambda);
  var OSGB_z = ((1 - OSGB_eSquared) * OSGB_v + H) * Math.sin(phi);

  var OSGB_tx =        446.448;
  var OSGB_ty =       -124.157;
  var OSGB_tz =        542.060;
  var OSGB_s  =         -0.0000204894;
  var OSGB_rx = deg2rad( 0.00004172222);
  var OSGB_ry = deg2rad( 0.00006861111);
  var OSGB_rz = deg2rad( 0.00023391666);

  var OSGB_xB = OSGB_tx + (OSGB_x * (1 + OSGB_s)) + (-OSGB_rx * OSGB_y)     + (OSGB_ry * OSGB_z);
  var OSGB_yB = OSGB_ty + (OSGB_rz * OSGB_x)      + (OSGB_y * (1 + OSGB_s)) + (-OSGB_rx * OSGB_z);
  var OSGB_zB = OSGB_tz + (-OSGB_ry * OSGB_x)     + (OSGB_rx * OSGB_y)      + (OSGB_z * (1 + OSGB_s));



  var OSGB_lambdaB = rad2deg(Math.atan(OSGB_yB / OSGB_xB));
  var OSGB_p = Math.sqrt((OSGB_xB * OSGB_xB) + (OSGB_yB * OSGB_yB));
  var OSGB_phiN = Math.atan(OSGB_zB / (OSGB_p * (1 - eSquared)));
  for (var i = 1; i < 10; i++) {
    OSGB_v = a / (Math.sqrt(1 - eSquared * sinSquared(OSGB_phiN)));
    OSGB_phiN1 = Math.atan((OSGB_zB + (eSquared * OSGB_v * Math.sin(OSGB_phiN))) / OSGB_p);
    OSGB_phiN = OSGB_phiN1;
    }
    var OSGB_phiB = rad2deg(OSGB_phiN);
    
     this.lat = OSGB_phiB;
     this.lng = OSGB_lambdaB;
    
  }
  if (grid=="osni_ING"){
      
   var airy1830mod = new RefEll(6377340.189, 6356034.447);
   var IRELAND_a        = airy1830mod.maj;
   var IRELAND_b        = airy1830mod.min;
   var IRELAND_eSquared = airy1830mod.ecc;   
   var IRELAND_v = IRELAND_a / (Math.sqrt(1 - IRELAND_eSquared * sinSquared(phi)));
   var IRELAND_x = (IRELAND_v + H) * Math.cos(phi) * Math.cos(lambda);
  var IRELAND_y = (IRELAND_v + H) * Math.cos(phi) * Math.sin(lambda);
  var IRELAND_z = ((1 - IRELAND_eSquared) * IRELAND_v + H) * Math.sin(phi);
  
    var IRELAND_tx =       -482.530;
      var IRELAND_ty =        130.596;
      var IRELAND_tz =       -564.557;
      var IRELAND_s  =       -0.000008150;
      var IRELAND_rx = deg2rad(-0.00028944444);
      var IRELAND_ry = deg2rad(-0.00005944444);
      var IRELAND_rz = deg2rad(-0.00017527777);


      var IRELAND_xB = IRELAND_tx + (IRELAND_x * (1 + IRELAND_s)) + (-IRELAND_rx * IRELAND_y)     + (IRELAND_ry * IRELAND_z);
      var IRELAND_yB = IRELAND_ty + (IRELAND_rz * IRELAND_x)      + (IRELAND_y * (1 + IRELAND_s)) + (-IRELAND_rx * IRELAND_z);
      var IRELAND_zB = IRELAND_tz + (-IRELAND_ry * IRELAND_x)     + (IRELAND_rx * IRELAND_y)      + (IRELAND_z * (1 + IRELAND_s));


      var IRELAND_lambdaB = rad2deg(Math.atan(IRELAND_yB / IRELAND_xB));
      var IRELAND_p = Math.sqrt((IRELAND_xB * IRELAND_xB) + (IRELAND_yB * IRELAND_yB));
      var IRELAND_phiN = Math.atan(IRELAND_zB / (IRELAND_p * (1 - eSquared)));
      for (var i = 1; i < 10; i++) {
        IRELAND_v = a / (Math.sqrt(1 - eSquared * sinSquared(IRELAND_phiN)));
        IRELAND_phiN1 = Math.atan((IRELAND_zB + (eSquared * IRELAND_v * Math.sin(IRELAND_phiN))) / IRELAND_p);
        IRELAND_phiN = IRELAND_phiN1;
      }
      var IRELAND_phiB = rad2deg(IRELAND_phiN);
    
     this.lat = IRELAND_phiB;
     this.lng = IRELAND_lambdaB;
    
    }
  }
  