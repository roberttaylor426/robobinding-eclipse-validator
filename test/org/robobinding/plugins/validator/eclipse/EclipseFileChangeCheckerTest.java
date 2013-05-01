package org.robobinding.plugins.validator.eclipse;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.robobinding.plugins.validator.eclipse.EclipseFileChangeChecker.forFullBuild;
import static org.robobinding.plugins.validator.eclipse.EclipseFileChangeChecker.forIncrementalBuild;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class EclipseFileChangeCheckerTest {

	@Mock ResourceDeltaAdapter resourceDeltaAdapter;
	@Mock File filePartOfResourceDelta, fileNotPartOfResourceDelta;

	@Before
	public void setupResourceDelta() {
		when(resourceDeltaAdapter.getFiles()).thenReturn(Lists.newArrayList(filePartOfResourceDelta));
	}
	
	@Test
	public void givenAFullBuild_thenAllFilesShouldReportChanges() {
		EclipseFileChangeChecker fileChangeChecker = forFullBuild();
		
		assertThat(fileChangeChecker.hasFileChangedSinceLastBuild(anyFile()), is(true));
	}

	@Test
	public void givenAnIncrementalBuild_thenFilesPartOfResourceDeltaShouldReportChanges() {
		EclipseFileChangeChecker fileChangeChecker = forIncrementalBuild(resourceDeltaAdapter);
		
		assertThat(fileChangeChecker.hasFileChangedSinceLastBuild(filePartOfResourceDelta), is(true));
	}
	
	@Test
	public void givenAnIncrementalBuild_thenFilesNotPartOfResourceDeltaShouldNotReportChanges() {
		EclipseFileChangeChecker fileChangeChecker = forIncrementalBuild(resourceDeltaAdapter);
		
		assertThat(fileChangeChecker.hasFileChangedSinceLastBuild(fileNotPartOfResourceDelta), is(false));
	}
	
	private File anyFile() {
		return new File(randomUUID().toString());
	}
	
}
