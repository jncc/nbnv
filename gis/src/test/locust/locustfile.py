from locust import HttpLocust, TaskSet, task, events
import time
import Queue
import cgi
import random

class OutputHolderSet(TaskSet):
	
  def imageGetCheck(self, uri, name):
    start_time = time.time()
    with self.client.get(uri, name=name, catch_response=True) as response:
      if response.headers['content-type'] != 'image/png;charset=ISO-8859-1':
        response.failure(cgi.escape(response.content))

  # The zoom value here is offset by 4 e.g. zoom 0 = 4
  def randomBBox(self, zoom):
    bottom  = 6224100
    left   = -1864174
    xExtent = 1366685 - left   #Get x extent right - left
    yExtent = 8415702 - bottom #Get y extent top - bottom

    resolution = 1252344 / 2**zoom 

    randomBottom  = bottom + (random.randint(0, yExtent/resolution) * resolution)
    randomLeft    = left + (random.randint(0, xExtent/resolution) * resolution)


    return "%.2f,%.2f,%.2f,%.2f" % (randomLeft, randomBottom, randomLeft + resolution, randomBottom + resolution) 

  # Generate the grid layer (based on the auto resolution) for a given zoom
  def getGridLayer(self, zoom):
    if 9 < zoom: return 'Grid-100m'
    elif 6 < zoom: return 'Grid-1km'
    elif 4 < zoom: return 'Grid-2km'
    else: return 'Grid-10km'

  def wmsTile(self, service):
    zoom = random.randint(0,14) #get random zoom level
    tileUrl = (service + "FORMAT=image%2Fpng&"
               "TRANSPARENT=TRUE&"
               "SERVICE=WMS&"
               "VERSION=1.1.1&"
               "REQUEST=GetMap&"
               "STYLES=&"
               "WIDTH=256&"
               "HEIGHT=256&"
               "SRS=EPSG%3A3857&"
               "BBOX=" + self.randomBBox(zoom) + "&"
               "LAYERS=" + self.getGridLayer(zoom) + "&")

    grouping = 'WMS call ' + service
    self.imageGetCheck(tileUrl, grouping)

#Define Brains
class SingleSpeciesBrain(OutputHolderSet):
  def on_start(self):
    self.tvk = "NHMSYS%010d" % random.randint(0, 532935)		

  @task
  def imtView(self):
    for i in range(24):
      self.wmsTile('/SingleSpecies/' + self.tvk + '?abundance=presence&')
	  
class DatasetDensityBrain(OutputHolderSet):
  def on_start(self):
    self.datasetKey = "GA%06d" % random.randint(0, 300)
	
  @task
  def imtView(self):
    for i in range(24):
      self.wmsTile('/DatasetDensity/' + self.datasetKey)

class UserBehavior(OutputHolderSet):
	tasks = {SingleSpeciesBrain:1, DatasetDensityBrain:1}

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait=1000
    max_wait=1500
