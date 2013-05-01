package org.robobinding.plugins.validator.eclipse;

import static org.robobinding.plugins.validator.eclipse.EclipseFileChangeChecker.forFullBuild;
import static org.robobinding.plugins.validator.eclipse.EclipseFileChangeChecker.forIncrementalBuild;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.robobinding.binder.BindingAttributeResolver;
import org.robobinding.plugins.validator.BindingAttributesValidator;
import org.robobinding.plugins.validator.FileChangeChecker;
import org.robobinding.plugins.validator.FilesWithBindingAttributes;
import org.robobinding.plugins.validator.LayoutXmlValidator;

public class Validator extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "robobindingValidator";

	public IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		
		FileChangeChecker fileChangeChecker = initFileChangeChecker(kind);
		
		validateXml(fileChangeChecker);
		
		return null;
	}

	private FileChangeChecker initFileChangeChecker(int kind) {
		FileChangeChecker fileChangeChecker;
		
		if (kind == FULL_BUILD) {
			fileChangeChecker = forFullBuild();
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fileChangeChecker = forFullBuild();
			} else {
				ResourceDeltaAdapter resourceDeltaAdapter = new ResourceDeltaAdapter(delta);
				fileChangeChecker = forIncrementalBuild(resourceDeltaAdapter);
			}
		}
		return fileChangeChecker;
	}

	private void validateXml(FileChangeChecker fileChangeChecker) {
		BindingAttributesValidator bindingAttributeValidator = createBindingAttributesValidator();
		LayoutXmlValidator layoutXmlValidator = createLayoutXmlValidator(fileChangeChecker, bindingAttributeValidator);
		layoutXmlValidator.validate();
	}

	private BindingAttributesValidator createBindingAttributesValidator() {
		BindingAttributesValidator bindingAttributeValidator = new BindingAttributesValidator();
		bindingAttributeValidator.setBindingAttributeResolver(new BindingAttributeResolver());
		bindingAttributeValidator.setErrorReporter(new EclipseErrorReporter(new ProjectAdapter(getProject())));
		return bindingAttributeValidator;
	}

	private LayoutXmlValidator createLayoutXmlValidator(FileChangeChecker fileChangeChecker,
			BindingAttributesValidator bindingAttributeValidator) {
		LayoutXmlValidator layoutXmlValidator = new LayoutXmlValidator();
		layoutXmlValidator.setFileChangeChecker(fileChangeChecker);
		layoutXmlValidator.setFilesWithBindingAttributes(new FilesWithBindingAttributes());
		layoutXmlValidator.setBindingAttributeValidator(bindingAttributeValidator);
		return layoutXmlValidator;
	}

}
