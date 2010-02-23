package sbt.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import sbt.eclipse.api.IWarProjectHook;

public class SbtEclipsePlugin extends AbstractUIPlugin {

	private Logger log;

	private static SbtEclipsePlugin instance;

	private List<IWarProjectHook> hooks = new ArrayList<IWarProjectHook>();

	public SbtEclipsePlugin() {
		instance = this;
	}

	public static SbtEclipsePlugin getInstance() {
		return instance;
	}

	public List<IWarProjectHook> getWarProjectHooks() {
		return hooks;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		StaticLoggerBinder.initialize(getLog(), getBundle().getSymbolicName());
		this.log = LoggerFactory.getLogger(this.getClass());
		try {
			IConfigurationElement[] config = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("sbt.eclipse.hooks");
			for (IConfigurationElement e : config) {
				if (!("warHook".equals(e.getName()))) {
					continue;
				}
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IWarProjectHook) {
					ISafeRunnable runnable = new ISafeRunnable() {
						public void handleException(Throwable exception) {
							log.error("Exception in WAR hook", exception);
						}

						public void run() throws Exception {
							IWarProjectHook hook = (IWarProjectHook) o;
							hooks.add(hook);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (Exception ex) {
			log.error("Exception in WAR hook collection", ex);
		}
	}

}
