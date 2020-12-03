package ch.ifocusit.livingdoc.plugin;

import ch.ifocusit.livingdoc.annotations.UbiquitousLanguage;
import ch.ifocusit.livingdoc.plugin.baseMojo.AbstractAsciidoctorMojo;
import ch.ifocusit.livingdoc.plugin.diagram.PlantumlClassDiagramBuilder;
import io.github.robwin.markup.builder.asciidoc.AsciiDocBuilder;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DiagramPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("I am applied");
    }
//    @Override
//    protected String getOutputFilename() {
//        return diagramOutputFilename;
//    }
//
//    @Override
//    protected String getTitle() {
//        return diagramTitle;
//    }

//    @Override
//    public void executeMojo() throws MojoExecutionException {
//        if (interactive) {
//            diagramWithLink = true;
//            diagramImageType = DiagramMojo.DiagramImageType.svg;
//        }
//        // generate diagram
//        String diagram = generateDiagram();
//
//        if (StringUtils.isBlank(diagram)) {
//            // nothing to generate
//            return;
//        }
//
//        switch (format) {
//            case html:
//            case adoc:
//            case asciidoc:
//                AsciiDocBuilder asciiDocBuilder = this.createAsciiDocBuilder();
//                appendTitle(asciiDocBuilder);
//
//                switch (diagramType) {
//                    case plantuml:
//                        asciiDocBuilder.textLine(String.format("[plantuml, %s, format=%s, opts=interactive]",
//                                getOutputFilename(), diagramImageType));
//                }
//                asciiDocBuilder.textLine("----");
//                asciiDocBuilder.textLine(diagram);
//                asciiDocBuilder.textLine("----");
//                // write to file
//                write(asciiDocBuilder);
//                break;
//
//            case plantuml:
//                write(diagram, getOutput(getOutputFilename(), AbstractAsciidoctorMojo.Format.plantuml));
//        }
//    }
//
//    String generateDiagram() throws MojoExecutionException {
//
//        getLog().info("generate diagram with packageRoot=" + packageRoot);
//
//        if (diagramType == DiagramMojo.DiagramType.plantuml) {
//            PlantumlClassDiagramBuilder builder = new PlantumlClassDiagramBuilder(project, packageRoot,
//                    Stream.of(excludes).map(s -> s.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""))
//                            .toArray(String[]::new),
//                    rootAggregateClassMatcher,
//                    rootAggregateColor == null || rootAggregateColor.isEmpty() ? DEFAULT_ROOT_COLOR
//                            : rootAggregateColor,
//                    diagramHeader, diagramFooter, diagramShowMethods, diagramShowFields, diagramWithDependencies,
//                    diagramLinkPage);
//            if (onlyAnnotated) {
//                builder.filterOnAnnotation(UbiquitousLanguage.class);
//            }
//            if (diagramWithLink && !DiagramMojo.DiagramImageType.png.equals(diagramImageType)) {
//                builder.mapNames(glossaryMapping);
//            }
//            return builder.generate();
//        }
//        throw new NotImplementedException(String.format("format %s is not implemented yet", diagramType));
//    }
}
