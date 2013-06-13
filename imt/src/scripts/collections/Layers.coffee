define [
  "backbone",
  "cs!models/HabitatLayer",
  "cs!models/SiteBoundaryLayer",
  "cs!models/SingleSpeciesLayer"
], (Backbone, HabitatLayer, SiteBoundaryLayer, SingleSpeciesLayer) -> Backbone.Collection.extend
  model: (attr, options)->
    switch attr.entityType
      when "habitatdataset"       then return new HabitatLayer attr, options
      when "site boundarydataset" then return new SiteBoundaryLayer attr, options
      when "taxondataset"         then return new SingleSpeciesLayer attr, options