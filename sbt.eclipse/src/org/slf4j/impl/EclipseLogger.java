package org.slf4j.impl;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class EclipseLogger extends MarkerIgnoringBase {

	private static final long serialVersionUID = 5706637616051879847L;

	private final ILog log;

	private final String pluginId;

	public EclipseLogger(ILog log, String pluginId) {
		this.log = log;
		this.pluginId = pluginId;
	}

	private void log(int severity, String msg) {
		log.log(new Status(severity, pluginId, msg));
	}

	private void log(int severity, String msg, Throwable t) {
		log.log(new Status(severity, pluginId, msg, t));
	}

	public void debug(String msg) {
		log(Status.INFO, msg);
	}

	public void debug(String format, Object arg) {
		log(Status.INFO, MessageFormatter.format(format, arg));
	}

	public void debug(String format, Object[] argArray) {
		log(Status.INFO, MessageFormatter.arrayFormat(format, argArray));
	}

	public void debug(String msg, Throwable t) {
		log(Status.INFO, msg, t);
	}

	public void debug(String format, Object arg1, Object arg2) {
		log(Status.INFO, MessageFormatter.format(format, arg1, arg2));
	}

	public void error(String msg) {
		log(Status.ERROR, msg);
	}

	public void error(String format, Object arg) {
		log(Status.ERROR, MessageFormatter.format(format, arg));
	}

	public void error(String format, Object[] argArray) {
		log(Status.INFO, MessageFormatter.arrayFormat(format, argArray));
	}

	public void error(String msg, Throwable t) {
		log(Status.ERROR, msg, t);
	}

	public void error(String format, Object arg1, Object arg2) {
		log(Status.ERROR, MessageFormatter.format(format, arg1, arg2));
	}

	public void info(String msg) {
		log(Status.INFO, msg);
	}

	public void info(String format, Object arg) {
		log(Status.INFO, MessageFormatter.format(format, arg));
	}

	public void info(String format, Object[] argArray) {
		log(Status.INFO, MessageFormatter.arrayFormat(format, argArray));
	}

	public void info(String msg, Throwable t) {
		log(Status.INFO, msg, t);
	}

	public void info(String format, Object arg1, Object arg2) {
		log(Status.INFO, MessageFormatter.format(format, arg1, arg2));
	}

	public boolean isDebugEnabled() {
		return false;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public void trace(String msg) {
	}

	public void trace(String format, Object arg) {
	}

	public void trace(String format, Object[] argArray) {
	}

	public void trace(String msg, Throwable t) {
	}

	public void trace(String format, Object arg1, Object arg2) {
	}

	public void warn(String msg) {
		log(Status.WARNING, msg);
	}

	public void warn(String format, Object arg) {
		log(Status.WARNING, MessageFormatter.format(format, arg));
	}

	public void warn(String format, Object[] argArray) {
		log(Status.WARNING, MessageFormatter.arrayFormat(format, argArray));
	}

	public void warn(String msg, Throwable t) {
		log(Status.WARNING, msg, t);
	}

	public void warn(String format, Object arg1, Object arg2) {
		log(Status.WARNING, MessageFormatter.format(format, arg1, arg2));
	}

}
