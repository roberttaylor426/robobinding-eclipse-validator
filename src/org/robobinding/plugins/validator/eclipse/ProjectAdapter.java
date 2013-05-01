package org.robobinding.plugins.validator.eclipse;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class ProjectAdapter {

	private final IProject project;

	public ProjectAdapter(IProject project) {
		this.project = project;
	}

	public IFile getFile(File file) {
		return project.getFile(file.getPath());
	}

}
