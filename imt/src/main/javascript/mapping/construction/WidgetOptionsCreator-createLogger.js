nbn.mapping.construction.WidgetOptionsCreator.prototype.createLogger = function () {
	var toReturn =  new nbn.util.Logger();
	dhtmlxError.catchError('ALL',function(type, desc, erData) { //log dhtmlxErrors
		toReturn.error('nbn.widget.treeWidget', type + ' ' + desc + ' ' + erData[1].filePath);
	});
	return toReturn;
};