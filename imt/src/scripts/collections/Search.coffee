define [
    "backbone",
    "cs!models/SearchResult",
    "cs!helpers/Globals"
], (Backbone, SearchResult, Globals) -> Backbone.Collection.extend
  model: SearchResult,

  url: -> Globals.api "search"
  
  parse: (resp,xhr) -> Backbone.Model.prototype.parse.call this, resp.results, xhr