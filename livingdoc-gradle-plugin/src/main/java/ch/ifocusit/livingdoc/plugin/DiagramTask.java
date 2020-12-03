package ch.ifocusit.livingdoc.plugin;

import ch.ifocusit.livingdoc.annotations.UbiquitousLanguage;
import ch.ifocusit.livingdoc.plugin.baseTask.AbstractDocsGeneratorTask;
import ch.ifocusit.livingdoc.plugin.diagram.PlantumlClassDiagramBuilder;
import io.github.robwin.markup.builder.asciidoc.AsciiDocBuilder;
import org.gradle.api.tasks.TaskAction;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public class DiagramTask extends AbstractDocsGeneratorTask {
    private boolean diagramWithLink;
    private LivingDocPluginExtension.DiagramImageType diagramImageType;
    private LivingDocPluginExtension extension;

    @TaskAction
    public void execute() {
        extension = getProject().getExtensions().findByType(LivingDocPluginExtension.class);
        if (extension == null) {
            extension = new LivingDocPluginExtension();
        }
        if (extension.isInteractive()) {
            diagramWithLink = true;
            diagramImageType = LivingDocPluginExtension.DiagramImageType.svg;
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

                switch (extension.getDiagramType()) {
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
                write(diagram, getOutput(getOutputFilename(), Format.plantuml));
        }
    }

    @Override
    public void executeTask() {

    }

    String generateDiagram() {

        // getLog().info("generate diagram with packageRoot=" + packageRoot);

        PlantumlClassDiagramBuilder builder = new PlantumlClassDiagramBuilder(
                project,
                packageRoot,
                Stream.of(extension.getExcludes())
                        .map(s -> s.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""))
                        .toArray(String[]::new),
                extension.getRootAggregateClassMatcher(),
                extension.getRootAggregateColor() == null || extension.getRootAggregateColor().isEmpty()
                        ? extension.getDefaultRootColor()
                        : extension.getRootAggregateColor(),
                extension.getDiagramHeader(),
                extension.getDiagramFooter(),
                extension.isDiagramShowMethods(),
                extension.isDiagramShowFields(),
                extension.isDiagramWithDependencies(),
                extension.getDiagramLinkPage());
        if (onlyAnnotated) {
            builder.filterOnAnnotation(UbiquitousLanguage.class);
        }
        if (diagramWithLink && !LivingDocPluginExtension.DiagramImageType.png.equals(diagramImageType)) {
            builder.mapNames(glossaryMapping);
        }
        return builder.generate();
    }

    @Override
    protected String getOutputFilename() {
        return null;
    }

    @Override
    protected String getTitle() {
        return null;
    }
}
