define [
  "underscore"
  "backbone"
  "hbs!templates/DatasetAcknowledgment"
], (_, Backbone, template) -> Backbone.View.extend

  initialize: ->
    @listenTo @collection, 'add remove change:usedDatasets', @render

  ###
  This datasets view renders the datasets which are currently
  being used by layers on maps grouped by the organisation which
  provided that dataset
  ###
  render: () ->
    usedDatasets = @collection.getUsedDatasets()
    organisations = _.chain(usedDatasets)
                      .groupBy((dataset) -> dataset.getOrganisationID() )
                      .map( (val,key) ->  
                        name: val[0].attributes.organisationName
                        href: val[0].attributes.organisationHref
                        id: val[0].attributes.organisationID
                        datasets: _(val).sortBy('title')
                      )
                      .sortBy('name')
                      .value()

    @$el.html template organisations: organisations
   