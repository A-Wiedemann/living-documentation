package ch.ifocusit.livingdoc.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LivingDocPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("livingDocSettings", LivingDocPluginExtension.class);
        project.getTasks().create("generateDiagram", DiagramTask.class);
    }
}
