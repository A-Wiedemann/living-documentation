package ch.ifocusit.livingdoc.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class DiagramTask extends DefaultTask {
    private boolean diagramWithLink;
    private

    @TaskAction
    public void execute() {
        LivingDocPluginExtension extension = getProject().getExtensions().findByType(LivingDocPluginExtension.class);
        if (extension == null) {
            extension = new LivingDocPluginExtension();
        }
        if (extension.isInteractive()) {
            diagramWithLink = true;
            diagramImageType = DiagramMojo.DiagramImageType.svg;
        }
        // generate diagram
        String diagram = generateDiagram();

        if (StringUtils.isBlank(diagram)) {
            // nothing to generate
            return;
        }

        switch (format) {
            case html:
            case adoc:
            case asciidoc:
                AsciiDocBuilder asciiDocBuilder = this.createAsciiDocBuilder();
                appendTitle(asciiDocBuilder);

                switch (diagramType) {
                    case plantuml:
                        asciiDocBuilder.textLine(String.format("[plantuml, %s, format=%s, opts=interactive]",
                                getOutputFilename(), diagramImageType));
                }
                asciiDocBuilder.textLine("----");
                asciiDocBuilder.textLine(diagram);
                asciiDocBuilder.textLine("----");
                // write to file
                write(asciiDocBuilder);
                break;

            case plantuml:
                write(diagram, getOutput(getOutputFilename(), AbstractAsciidoctorMojo.Format.plantuml));
        }
    }

    String generateDiagram() throws MojoExecutionException {

        getLog().info("generate diagram with packageRoot=" + packageRoot);

        if (diagramType == DiagramMojo.DiagramType.plantuml) {
            PlantumlClassDiagramBuilder builder = new PlantumlClassDiagramBuilder(project, packageRoot,
                    Stream.of(excludes).map(s -> s.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""))
                            .toArray(String[]::new),
                    rootAggregateClassMatcher,
                    rootAggregateColor == null || rootAggregateColor.isEmpty() ? DEFAULT_ROOT_COLOR
                            : rootAggregateColor,
                    diagramHeader, diagramFooter, diagramShowMethods, diagramShowFields, diagramWithDependencies,
                    diagramLinkPage);
            if (onlyAnnotated) {
                builder.filterOnAnnotation(UbiquitousLanguage.class);
            }
            if (diagramWithLink && !DiagramMojo.DiagramImageType.png.equals(diagramImageType)) {
                builder.mapNames(glossaryMapping);
            }
            return builder.generate();
        }
        throw new NotImplementedException(String.format("format %s is not implemented yet", diagramType));
    }
}
