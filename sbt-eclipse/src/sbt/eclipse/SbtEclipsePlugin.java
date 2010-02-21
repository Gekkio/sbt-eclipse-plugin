package sbt.eclipse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import sbt.eclipse.api.IWarProjectHook;

public class SbtEclipsePlugin extends AbstractUIPlugin {

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
		try {
			IConfigurationElement[] config = Platform.getExtensionRegistry()
					.getConfigurationElementsFor("sbt.eclipse.hooks");
			System.out.println(Arrays.asList(config));
			for (IConfigurationElement e : config) {
				if (!("warHook".equals(e.getName()))) {
					continue;
				}
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IWarProjectHook) {
					ISafeRunnable runnable = new ISafeRunnable() {
						public void handleException(Throwable exception) {
							System.out.println("Exception in client");
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
			System.out.println(ex.getMessage());
		}
	}

}
