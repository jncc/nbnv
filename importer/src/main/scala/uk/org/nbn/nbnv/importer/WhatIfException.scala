package uk.org.nbn.nbnv.importer

/// Represents an import failure due to the whatIf option being set.
/// Using an exception is a simple way to roll back a guice-persist Transactional method.
case class WhatIfException(message: String) extends Exception(message)
