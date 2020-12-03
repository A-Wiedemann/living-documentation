package ch.ifocusit.livingdoc.plugin;

import ch.ifocusit.livingdoc.plugin.domain.Cluster;
import ch.ifocusit.livingdoc.plugin.domain.Color;

import java.io.File;
import java.util.List;

public class LivingDocPluginExtension {
    private static final Color DEFAULT_ROOT_COLOR = Color.from("wheat", null);

    public String[] getExcludes() {
        return excludes;
    }

    private String[] excludes = new String[0];

    public DiagramType getDiagramType() {
        return diagramType;
    }

    /**
     * Output diagram format
     */
    private DiagramType diagramType = DiagramType.plantuml;

    public DiagramImageType getDiagramImageType() {
        return diagramImageType;
    }

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

    public String getRootAggregateClassMatcher() {
        return rootAggregateClassMatcher;
    }

    /**
     * Define the root aggregare class.
     */
    private String rootAggregateClassMatcher = "livingdoc.diagram.rootAggregate.class";

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

    public static Color getDefaultRootColor() {
        return DEFAULT_ROOT_COLOR;
    }

    public boolean isDiagramWithLink() {
        return diagramWithLink;
    }

    public String getDiagramLinkPage() {
        return diagramLinkPage;
    }

    public Color getRootAggregateColor() {
        return rootAggregateColor;
    }

    public boolean isDetectCluster() {
        return detectCluster;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public boolean isDiagramShowMethods() {
        return diagramShowMethods;
    }

    public boolean isDiagramShowFields() {
        return diagramShowFields;
    }

    public File getDiagramHeader() {
        return diagramHeader;
    }

    public File getDiagramFooter() {
        return diagramFooter;
    }

    public String getDiagramOutputFilename() {
        return diagramOutputFilename;
    }

    public String getDiagramTitle() {
        return diagramTitle;
    }

    public boolean isDiagramWithDependencies() {
        return diagramWithDependencies;
    }

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

    public boolean isInteractive() {
        return interactive;
    }

    private boolean interactive = true;

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
