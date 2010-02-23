package org.slf4j.impl;

import org.eclipse.core.runtime.ILog;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class EclipseLoggerFactory implements ILoggerFactory {

	private final EclipseLogger logger;

	public EclipseLoggerFactory(ILog log, String pluginId) {
		this.logger = new EclipseLogger(log, pluginId);
	}

	public Logger getLogger(String name) {
		return this.logger;
	}

}
