package org.robobinding.plugins.validator.eclipse;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

public class ResourceDeltaAdapter {

	private final IResourceDelta resourceDelta;

	public ResourceDeltaAdapter(IResourceDelta resourceDelta) {
		this.resourceDelta = resourceDelta;
	}

	public List<File> getFiles() {
		List<File> affectedFiles = newArrayList();
		IResourceDelta[] affectedChildren = resourceDelta.getAffectedChildren();

		if (affectedChildren == null)
			return affectedFiles;
		
		for (IResourceDelta affectedChild : affectedChildren) {
			IResource resource = affectedChild.getResource();

			if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				affectedFiles.add(new File(file.getRawLocationURI().getPath()));
			}
		}

		return affectedFiles;
	}

}
