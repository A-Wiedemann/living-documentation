package ch.ifocusit.livingdoc.plugin;

import ch.ifocusit.livingdoc.plugin.domain.Cluster;
import ch.ifocusit.livingdoc.plugin.domain.Color;

import java.io.File;
import java.util.List;

public class DiagramPluginExtension {
    private static final Color DEFAULT_ROOT_COLOR = Color.from("wheat", null);

    private String[] excludes = new String[0];

    /**
     * Output diagram format
     */
    private DiagramType diagramType = DiagramType.plantuml;

    /**
     * Output diagram image format
     */
    private DiagramImageType diagramImageType = DiagramImageType.png;

    /**
     * Add link into diagram to glossary
     */
    private boolean diagramWithLink = true;

    /**
     * Link template to use in diagram.
     */
    private String diagramLinkPage = "glossary.html";

    /**
     * Define the root aggregare class.
     */
    private String rootAggregateClassMatcher;

    /**
     * Class color for @{@link ch.ifocusit.livingdoc.annotations.RootAggregate}
     * class.
     */
    private Color rootAggregateColor;

    /**
     * Indicate if cluster must automatically detected.
     */
    private boolean detectCluster = true;

    /**
     * Effective cluster list.
     */
    private List<Cluster> clusters;

    private boolean diagramShowMethods = true;

    private boolean diagramShowFields = true;

    /**
     * Header of the diagram
     */
    private File diagramHeader = new File("${project.basedir}/src/main/livingdoc/diagram.header");

    /**
     * Footer of the diagram
     */
    private File diagramFooter;

    private boolean interactive;

    private String diagramOutputFilename;

    private String diagramTitle;

    private boolean diagramWithDependencies;

    public enum DiagramType {
        plantuml;
    }

    public enum DiagramImageType {
        png, svg, txt;
    }
}
