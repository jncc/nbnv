define [], ->
  ###
  Load the google analytics code in asynchronously. Google provide a snipet on
  how to do this <https://developers.google.com/analytics/devguides/collection/analyticsjs/>
  ###
  `(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');`

  ###
  Register the NBN Gateways urchin key and send a page load
  ###
  ga 'create', 'UA-1444605-1'
  ga 'send', 'pageview'


  ###
  Hook into the various events from the instance of App and View to gather
  analytics on
  ###
  listen: (app, view) ->
    app.getLayers().on "add", (layer) ->
      ga "send", "event", "layeradded", layer.getName()

    app.getLayers().on "remove", (layer)->
      ga "send", "event", "layerremoved", layer.getName()

    app.getLayers().on "change:colour", (layer)->
      ga "send", "event", "styledlayer"

    app.on "change:baseLayer", (app, baseLayer)->
      ga "send", "event", "baselayerchanged", baseLayer

    app.getPicker().on "change:wkt", (picker, wkt) ->
      ga "send", "event", "picked", wkt