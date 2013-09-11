define ["backbone"], (Backbone) -> Backbone.View.extend
  initialize: ->
    do @render

    @listenTo @model, 'change:loading', @render

  render: () ->
    if @model.get 'loading'
      @$el.show()
    else
      @$el.hide()