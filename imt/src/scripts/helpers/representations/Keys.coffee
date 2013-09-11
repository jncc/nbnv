define [
  'cs!helpers/Numbers'
], (Numbers) ->
  ###
  Define some lookups which represent the types of keys which we
  know about
  ###
  TVK_PROVIDERS = ['NHM','NBN','BMS','EHS']
  DATASET_TYPES = ['HL','GA','SB']

  ###
  A shrinkable tvk consistes of A 3 letter provider (@see TVK_PROVIDERS), 
  the characters 'SYS' and then a ten digit number. There are four main tvk
  providers which supply tvks in the above form. Therefore we can represent
  the providers as a 2 bit number. The 4 supported tvks and their corresponding
  bit ids are:

  * NHM - 0b00
  * NBN - 0b01
  * BMS - 0b10
  * EHS - 0b11

  The numeric part of a tvk is 10 digits, to support the largest possible value
  that a 10 digit number can take (i.e. 9999999999) a 34 bit number is required.
  Combine this with the 4 bits for the provider id, we can represent most tvks
  in 38bits (7 Base64 digits)
  ###
  shrinkTVK: (tvk) ->
    return tvk if not /(NHM|NBN|BMS|EHS)(SYS)[0-9]{10}/.test tvk #Check if the tvk is minifiable
    type = _.indexOf TVK_PROVIDERS, tvk.substring(0, 3)          #Get the 2bit provider
    numeric = parseInt tvk.substring(6), 10                      #Convert the last 10 characters of the tvk to a number
    Numbers.toBase64 numeric * 4 + type                          #Add the type flag to the number and encode

  ###
  Given the above definition, we can invert the miniTVK into the full tvk with the
  following function
  @param miniTVK - The base64 representation of the tvk
  ###
  expandTVK: (miniTVK) ->
    return miniTVK if miniTVK.length is 16 # If the representation is 16 chars long, treat as normal key
    lastCharater = miniTVK.substring(miniTVK.length - 1)      #Get the last character from the miniTVK, this holds the provider
    provider = Numbers.fromBase64(lastCharater) & 0x03        #Get the provider key (Last 2 bits) 
    numeric = Math.floor Numbers.fromBase64(miniTVK) / 4      #Remove the last 2 bits from the number
    "#{TVK_PROVIDERS[provider]}SYS#{Numbers.pad numeric, 10}" #0 pad the number to a make it 10 chars long

  ###
  The 2 low order bits of the minDatasetKey represent the type of dataset key.
  These are defined as:

  * HL - 0b00
  * GA - 0b01
  * SB - 0b10

  The numeric part of a datasetKey is a 6 digit number. The largest possible value
  for this is 999999, this can be represented in a 20bit number. Therefore we can
  represent a shrunken dataset key in 22bits (4 Base64 digits)
  ###
  shrinkDatasetKey: (datasetKey) ->
    return datasetKey if not /(GA|HL|SB)[0-9]{6}/.test datasetKey #check if we can shrink the key
    type = _.indexOf DATASET_TYPES, datasetKey.substring(0, 2) #get the 2 bit dataset type
    numeric = parseInt datasetKey.substring(2), 10
    Numbers.toBase64 numeric * 4 + type

  ###
  Given the above definition, we can invert the miniDatasetKey into the full datasetKey
  with the following function
  @param miniDatasetKey - The base64 representation of the datasetKey
  ###
  expandDatasetKey: (miniDatasetKey) ->
    return miniDatasetKey if miniDatasetKey.length is 8  #If the representation is 8 chars long, treat as normal key
    lastCharater = miniDatasetKey.substring(miniDatasetKey.length - 1)  #Get the last character from the miniDatasetKey, this holds the type
    type = Numbers.fromBase64(lastCharater) & 0x03                               #Get the type as an id
    numeric = Math.floor Numbers.fromBase64(miniDatasetKey) / 4         #Remove the last 2 bits from the number
    "#{DATASET_TYPES[type]}#{Numbers.pad numeric, 6}"                   #0 pad the number to make it 6 chars long