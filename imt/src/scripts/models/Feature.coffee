define [
  'backbone' 
], (Backbone) -> Backbone.Model.extend
	defaults:
		layerName: ""
		info: ""

	getLayerName: -> @get "layerName"
	getInfo: -> @get "info"