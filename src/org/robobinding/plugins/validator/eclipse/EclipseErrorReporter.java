package org.robobinding.plugins.validator.eclipse;

import static org.eclipse.core.resources.IMarker.LINE_NUMBER;
import static org.eclipse.core.resources.IMarker.MESSAGE;
import static org.eclipse.core.resources.IMarker.SEVERITY;
import static org.eclipse.core.resources.IMarker.SEVERITY_ERROR;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.robobinding.plugins.validator.ErrorReporter;

public class EclipseErrorReporter implements ErrorReporter {

	static final String MARKER_TYPE = "robobindingValidationProblem";
	private final ProjectAdapter projectAdapter;
	private boolean errorsReported;
	
	public EclipseErrorReporter(ProjectAdapter projectAdapter) {
		this.projectAdapter = projectAdapter;
	}

	@Override
	public void clearErrorsFor(File file) {
		IFile fileResource = projectAdapter.getFile(file);
		try {
			fileResource.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
		}
	}

	@Override
	public void errorIn(File file, int lineNumber, String errorMessage) {
		errorsReported = true;
		IFile fileResource = projectAdapter.getFile(file);
		try {
			IMarker errorMarker = fileResource.createMarker(MARKER_TYPE);
			errorMarker.setAttribute(LINE_NUMBER, lineNumber);
			errorMarker.setAttribute(MESSAGE, errorMessage);
			errorMarker.setAttribute(SEVERITY, SEVERITY_ERROR);
		} catch (CoreException e) {
		}
	}

	@Override
	public boolean errorsReported() {
		return errorsReported;
	}

}
