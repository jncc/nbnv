define [
  "jquery",
  "backbone",
  "hbs!templates/PickerView"
], ($, Backbone, template) -> Backbone.View.extend


  initialize: ->
    do @render
    @listenTo @collection, 'change', @render

  render: -> 

    ###
    features = _.chain(@collection)
                .groupBy((feature) -> feature.getLayerName())
                .map((val,key) -> 
                  layerName: val[0].attributes.layerName
                  info: val[0].attributes.featureInfo
                  )
                .value()
    ###

#    features = @collection.groupBy('layerName')
#    console.log features

    @$el.html template features: @collection.toJSON();


#    features = @collection.getUsedDatasets()
#    organisations = _.chain(usedDatasets)
#                      .groupBy((dataset) -> dataset.getOrganisationID() )
#                      .map( (val,key) ->  
#                        name: val[0].attributes.organisationName
#                        href: val[0].attributes.organisationHref
#                        id: val[0].attributes.organisationID
#                        datasets: val
#                      )
#                      .value()

#    @$el.html template organisations: organisations
