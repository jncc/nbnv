define [
  "backbone"
  "cs!models/mixins/PolygonFillMixin"
], (Backbone, PolygonFillMixin) ->
  describe "PolygonFillMixin Tests", ->  
    polygonFillMixin = null

    beforeEach ->
      PolygonFillModel = Backbone.Model.extend PolygonFillMixin
      polygonFillMixin = new PolygonFillModel
      
    it "sets defaults as expected", ->
      expect(polygonFillMixin.get "isStyleable").toBeTruthy
    
  
  
#  isStyleable: true #expose that this layer is styleable
#
#  initialize: () ->
#    @set "colour", $.md5(@id).substring 0, 6
#
#    @on 'change:colour change:symbol', -> @trigger 'change:legendIcon'
#    @on 'change:colour change:symbol change:layer', -> @trigger 'change:sld'
#
#  ###
#  The following method will work out the sld template which
#  should be used depending on the current symbol. Once determined
#  it will be called with the current layer and selected colour
#  ###
#  getSLD: -> 
#    sldTemplate = switch @get "symbol" 
#      when "fill" then polygonFill
#      when "hatching" then polygonHatching
#
#    sldTemplate
#      layer: @getLayer(),
#      colour: @get "colour"
#
#  ###
#  Generate the current legend icon given the symbol and colour
#  ###
#  getLegendIcon: ->
#    switch @get "symbol"
#      when 'fill'
#        backgroundColor: "#" + @get "colour"
#        backgroundImage: ""
#      when 'hatching'
#        backgroundColor: "#" + @get "colour"
#        backgroundImage: "url('img/polygonHatching.png')"