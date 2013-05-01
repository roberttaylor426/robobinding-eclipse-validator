package org.robobinding.plugins.validator.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.robobinding.plugins.validator.FileChangeChecker;

public class EclipseFileChangeChecker implements FileChangeChecker {
	
	private final List<File> resourceDeltaFiles;
	private final boolean fullBuild;
	
	private EclipseFileChangeChecker(List<File> resourceDeltaFiles, boolean fullBuild) {
		this.resourceDeltaFiles = resourceDeltaFiles;
		this.fullBuild = fullBuild;
	}

	public static EclipseFileChangeChecker forFullBuild() {
		return new EclipseFileChangeChecker(new ArrayList<File>(), true);
	}
	
	public static EclipseFileChangeChecker forIncrementalBuild(ResourceDeltaAdapter resourceDelta) {
		return new EclipseFileChangeChecker(resourceDelta.getFiles(), false);
	}

	@Override
	public boolean hasFileChangedSinceLastBuild(File file) {
		if (fullBuild)
			return true;
		
		return resourceDeltaFiles.contains(file);
	}

}
