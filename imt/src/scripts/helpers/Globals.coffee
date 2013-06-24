define [
  'underscore'
], (_) ->
  api: (path) -> "http://staging.testnbn.net/api/#{path}?callback=?"
  gis: (path, attr) -> "http://staging.testnbn.net/gis/#{path}?#{@_buildQueryString(attr)}"

  ###
  The following function will build a query string from an object of
  filters. If an attribute is provided in that object which does not
  have a value or the value is "" (an empty string), then that attribute
  will be omited from the resultant querystring
  ###
  _buildQueryString: (attributes)->
    _.chain(attributes)
      .pairs()
      .reject( (curr) -> not curr[1]? or curr[1] is "")
      .map( (curr) -> "#{curr[0]}=#{curr[1]}" )
      .value()
      .join '&'