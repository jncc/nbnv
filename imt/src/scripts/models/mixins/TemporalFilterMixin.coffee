define [
], () ->
  isTemporalFilterable: true #expose that this layer can be filtered by time

  earliestRecordDate: 1600

  latestRecordDate: new Date().getFullYear()

  initialize: () ->

  getStartDate: () -> @get "startDate" if @get("startDate") is not @earliestRecordDate

  getEndDate: () -> @get "endDate" if @get("endDate") is not @latestRecordDate
