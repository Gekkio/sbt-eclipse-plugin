package sbt.eclipse.wtp;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetConstants;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import sbt.eclipse.api.IWarProjectHook;

public class WarProjectHook implements IWarProjectHook {

	public void importProject(IProject project) throws CoreException {
		IFacetedProject faceted = ProjectFacetsManager.create(project, true,
				null);
		if (!faceted.hasProjectFacet(JavaFacetUtils.JAVA_FACET)) {
			faceted.installProjectFacet(JavaFacetUtils.JAVA_60, null, null);
		}
		if (!faceted.hasProjectFacet(IJ2EEFacetConstants.DYNAMIC_WEB_FACET)) {
			IDataModel model = DataModelFactory
					.createDataModel(new WebFacetInstallDataModelProvider());
			model.setProperty(
					IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER,
					"src/main/webapp");
			faceted.installProjectFacet(IJ2EEFacetConstants.DYNAMIC_WEB_25,
					model, null);
		}
	}

}
