package org.robobinding.plugins.validator.eclipse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robobinding.plugins.validator.eclipse.EclipseErrorReporter.MARKER_TYPE;

import java.io.File;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EclipseErrorReporterTest {

	@Mock ProjectAdapter projectAdapter;
	@Mock IFile fileResource;
	@Mock IMarker errorMarker;
	private EclipseErrorReporter errorReporter;
	private File file = new File("somePath");
	
	@Before
	public void setup() throws Exception {
		when(fileResource.createMarker(MARKER_TYPE)).thenReturn(errorMarker);
		when(projectAdapter.getFile(file)).thenReturn(fileResource);
		errorReporter = new EclipseErrorReporter(projectAdapter);
	}
	
	@Test
	public void shouldClearErrorsForFile() throws Exception {
		errorReporter.clearErrorsFor(file);
		
		verify(fileResource).deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
	}
	
	@Test
	public void shouldReportErrorsInFile() throws Exception {
		int lineNumber = anyLineNumber();
		
		errorReporter.errorIn(file, lineNumber, "errorMessage");
		
		verify(errorMarker).setAttribute(IMarker.MESSAGE, "errorMessage");
		verify(errorMarker).setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		verify(errorMarker).setAttribute(IMarker.LINE_NUMBER, lineNumber);
	}
	
	@Test
	public void shouldIndicateWhenErrorsHaveBeenReported() {
		assertFalse(errorReporter.errorsReported());
		
		errorReporter.errorIn(file, anyLineNumber(), "errorMessage");
		
		assertTrue(errorReporter.errorsReported());
	}
	
	private int anyLineNumber() {
		return new Random().nextInt(100);
	}

}
