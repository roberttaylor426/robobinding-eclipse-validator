package org.robobinding.plugins.validator.eclipse;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceDeltaAdapterTest {

	private static final String FILE_PATH_1 = "somepath1";
	private static final String FILE_PATH_2 = "somepath2";
	@Mock IResourceDelta resourceDelta;
	@Mock IResourceDelta affectedChild1, affectedChild2;
	@Mock IFile affectedFileResource1, affectedFileResource2;
	@Mock IResource affectedNonFileResource;
	private URI uri1, uri2;
	
	@Before
	public void setupResourceDelta() throws Exception {
		uri1 = new URI(FILE_PATH_1);
		uri2 = new URI(FILE_PATH_2);
		when(resourceDelta.getAffectedChildren()).thenReturn(new IResourceDelta[]{affectedChild1, affectedChild2});
		when(affectedChild1.getResource()).thenReturn(affectedFileResource1);
		when(affectedChild2.getResource()).thenReturn(affectedFileResource2);
		when(affectedFileResource1.getRawLocationURI()).thenReturn(uri1);
		when(affectedFileResource2.getRawLocationURI()).thenReturn(uri2);
	}
	
	@Test
	public void shouldReturnAffectedFiles() {
		ResourceDeltaAdapter resourceDeltaAdapter = new ResourceDeltaAdapter(resourceDelta);
		List<File> expectedFiles = newArrayList(new File(FILE_PATH_1), new File(FILE_PATH_2));
		
		List<File> deltaFiles = resourceDeltaAdapter.getFiles();
		
		assertThat(deltaFiles, equalTo(expectedFiles));
	}
	
	@Test
	public void shouldNotReturnNonFileResources() {
		when(affectedChild2.getResource()).thenReturn(affectedNonFileResource);
		ResourceDeltaAdapter resourceDeltaAdapter = new ResourceDeltaAdapter(resourceDelta);
		List<File> expectedFiles = newArrayList(new File(FILE_PATH_1));
		
		List<File> deltaFiles = resourceDeltaAdapter.getFiles();
		
		assertThat(deltaFiles, equalTo(expectedFiles));
	}
	
	@Test
	public void givenPluginApiUnexpectedlyReturnsNullAffectedChildren_thenReturnEmptyList() {
		when(resourceDelta.getAffectedChildren()).thenReturn(null);
		ResourceDeltaAdapter resourceDeltaAdapter = new ResourceDeltaAdapter(resourceDelta);
		
		List<File> deltaFiles = resourceDeltaAdapter.getFiles();

		assertThat(deltaFiles.size(), is(0));
	}
}
