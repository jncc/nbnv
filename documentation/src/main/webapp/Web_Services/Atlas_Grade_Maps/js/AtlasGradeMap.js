window.nbn = window.nbn || {};
nbn.mapping = nbn.mapping || {};
nbn.mapping.AtlasGradeMap = function() {
	var _me = this;
	
	this.FORMAT = {
		GIF: 'gif',
		JPEG: 'jpeg',
		PNG: 'png'
	};
	
	this.BACKGROUND = {
		GB: 'gb',
		GBI: 'gbi',
		GB100KGRID: 'gb100kgrid',
		GBI100KGRID: 'gbi100kgrid',
		VICECOUNTY: 'vicecounty'
	};
	
	$.extend(this,
		new nbn.util.ObservableAttribute('Format', _me.FORMAT.PNG),
		new nbn.util.ObservableAttribute('Background'),
		new nbn.util.ObservableAttribute('Datasets'),
		new nbn.util.ObservableAttribute('StartYear'),
		new nbn.util.ObservableAttribute('EndYear'),
		new nbn.util.ObservableAttribute('FillColour'),
		new nbn.util.ObservableAttribute('OutlineColour'),
		new nbn.util.ObservableAttribute('ImageSize', 4),
		new nbn.util.ObservableAttribute('Taxon', 'NHMSYS0000459714'),
		new nbn.util.ObservableAttribute('MapUrl', 'NHMSYS0000459714')
	);
	
	delete this.setMapUrl;
	
	function _createRequestObject() {
		var toReturn =  {};
		if(_me.getFormat()) toReturn.format = _me.getFormat();
		if(_me.getBackground()) toReturn.background = _me.getBackground();
		if(_me.getDatasets()) toReturn.datasets = _me.getDatasets();
		if(_me.getStartYear()) toReturn.startyear = _me.getStartYear();
		if(_me.getEndYear()) toReturn.endyear = _me.getEndYear();
		if(_me.getFillColour()) toReturn.fillcolour = _me.getFillColour();
		if(_me.getOutlineColour()) toReturn.outlinecolour = _me.getOutlineColour();
		if(_me.getImageSize()) toReturn.imagesize = _me.getImageSize();
		return toReturn;
	}	
	
	this.getImageURL = function() {
		if(_me.getTaxon())
			return 'http://gis.nbn.org.uk/arcgis/atlas/SingleSpeciesMap/' + _me.getTaxon() + '/map'+ nbn.util.ArrayTools.joinAndPrepend(nbn.util.ArrayTools.fromObject(_createRequestObject()),"&","?");
		return false;
	}
	
	
}