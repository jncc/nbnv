define ["backbone"], (Backbone) -> Backbone.View.extend
  initialize: ->
    do @render

    @listenTo @model, 'change:loading', @render

  render: () ->
    targetWidth = if @model.get 'loading' then 16 else 0
    @$el.stop(true, false).animate width: targetWidth, 'slow'