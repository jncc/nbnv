define [], ->
  api: (path) -> "http://staging.testnbn.net/api/#{path}?callback=?"
  gis: (path) -> "http://staging.testnbn.net/gis/#{path}"