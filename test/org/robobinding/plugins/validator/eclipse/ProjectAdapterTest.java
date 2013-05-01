package org.robobinding.plugins.validator.eclipse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProjectAdapterTest {

	@Mock IProject project;
	@Mock IFile fileResource;
	
	@Test
	public void shouldGetFileResourceForFile() {
		when(project.getFile("somePath")).thenReturn(fileResource);
		ProjectAdapter projectAdapter = new ProjectAdapter(project);

		IFile result = projectAdapter.getFile(new File("somePath"));
		
		assertThat(result, is(fileResource));
	}
	
}
