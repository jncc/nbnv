define [
  'underscore'
], (_) ->
  isTemporalFilterable: true #expose that this layer can be filtered by time

  earliestRecordDate: 1600

  latestRecordDate: new Date().getFullYear()

  initialize: ->

  getStartDate: -> @get "startDate" if @get("startDate") > @earliestRecordDate

  getEndDate: -> @get "endDate" if @get("endDate") < @latestRecordDate

  isYearFiltering: -> @getStartDate()? or @getEndDate()?

  ###
  Check if the values start date and end date are valid. If not
  return an errors array with objects who have the name 'temporalFilter'
  ###
  validate: (attrs) ->
    messages = []

    if isNaN attrs.startDate then messages.push "Start date is not a number"
    if isNaN attrs.endDate then messages.push "End date is not a number"
    if attrs.startDate < @earliestRecordDate then messages.push "Start date is too early"
    if attrs.endDate > @latestRecordDate then messages.push "End date is too late"
    if attrs.endDate < attrs.startDate then messages.push "End date is after start date"

    errors = _.map messages, (curr) -> name: 'temporalFilter', message: curr
    return errors if errors.length isnt 0