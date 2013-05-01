package org.robobinding.plugins.validator.eclipse;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class Nature implements IProjectNature {

	public static final String NATURE_ID = "robobinding-eclipse-validator.nature";

	private IProject project;

	/**
	 * Configures this nature for its project. This is called by the workspace
	 * when natures are added to the project using IProject.setDescription and
	 * should not be called directly by clients. The nature extension id is
	 * added to the list of natures before this method is called, and need not
	 * be added here. Exceptions thrown by this method will be propagated back
	 * to the caller of IProject.setDescription, but the nature will remain in
	 * the project description.
	 */
	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		//If the project already contains the RoboBinding command
		//If the project now contains the Maven nature, disable validator nature?
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Validator.BUILDER_ID)) {
				return;
			}
		}

		//Add the RoboBinding command as the command, supplying the SampleBuilder id
		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(Validator.BUILDER_ID);
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
	}

	/**
	 * De-configures this nature for its project. This is called by the
	 * workspace when natures are removed from the project using
	 * IProject.setDescription and should not be called directly by clients. The
	 * nature extension id is removed from the list of natures before this
	 * method is called, and need not be removed here. Exceptions thrown by this
	 * method will be propagated back to the caller of IProject.setDescription,
	 * but the nature will still be removed from the project description. *
	 */
	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Validator.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				project.setDescription(description, null);
				return;
			}
		}
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
