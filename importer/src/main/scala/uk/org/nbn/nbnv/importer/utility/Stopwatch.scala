package uk.org.nbn.nbnv.importer.utility

// http://thelastdegree.wordpress.com/2012/07/11/a-scala-stopmatch-for-benchmarking/

class Stopwatch {

  private var startTime = -1L
  private var stopTime = -1L
  private var running = false

  def start() = {
    startTime = System.currentTimeMillis()
    running = true
    this
  }

  def stop() = {
    stopTime = System.currentTimeMillis()
    running = false
    this
  }

  def isRunning: Boolean = running

  def elapsedMilliseconds = {
    if (startTime == -1) {
      0
    }
    if (running) {
      System.currentTimeMillis() - startTime
    }
    else {
      stopTime - startTime
    }
  }

  def elapsedSeconds = elapsedMilliseconds / 1000

  def reset() {
    startTime = -1
    stopTime = -1
    running = false
  }
}
