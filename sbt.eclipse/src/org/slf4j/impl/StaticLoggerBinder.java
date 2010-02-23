package org.slf4j.impl;

import org.eclipse.core.runtime.ILog;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	private static EclipseLoggerFactory loggerFactory;

	public static void initialize(ILog log, String pluginId) {
		loggerFactory = new EclipseLoggerFactory(log, pluginId);
	}

	public static final StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	public String getLoggerFactoryClassStr() {
		return EclipseLoggerFactory.class.getName();
	}

}
