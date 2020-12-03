package ch.ifocusit.livingdoc.plugin.baseTask;

import io.github.robwin.markup.builder.asciidoc.AsciiDocBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.asciidoctor.SafeMode.UNSAFE;

public abstract class AbstractAsciidoctorTask extends DefaultTask {
    protected static final String TEMPLATES_OUTPUT = "${project.build.directory}/asciidoc-templates";
    private static final String TEMPLATES_CLASSPATH_PATTERN = "templates/*";

    public enum Format {
        asciidoc, adoc, html, plantuml
    }

    protected Project project;

    /**
     * Directory where the documents will be generated
     */
    protected File generatedDocsDirectory;

    /**
     * Templates directories.
     */
    private File asciidocTemplates;

    /**
     * Templates directories.
     */
    private String newlineCharacter;

    public void execute() {
        System.setProperty("line.separator", "\r\n");
        if (StringUtils.isNotBlank(newlineCharacter)) {
            System.setProperty("line.separator", newlineCharacter);
        }
        this.executeTask();
    }

    public abstract void executeTask();

    protected void write(AsciiDocBuilder asciiDocBuilder, AbstractAsciidoctorTask.Format format, String outputFilename) {
        generatedDocsDirectory.mkdirs();
        File output = getOutput(outputFilename, AbstractAsciidoctorTask.Format.adoc);
        try {
            // write adco file
            asciiDocBuilder.writeToFile(generatedDocsDirectory.getAbsolutePath(), FilenameUtils.removeExtension(outputFilename), StandardCharsets.UTF_8);
            if (AbstractAsciidoctorTask.Format.html.equals(format)) {
                // convert adoc to html
                createAsciidoctor().convertFile(output, options());
            }
        } catch (IOException e) {
            throw new GradleException(String.format("Unable to convert asciidoc file '%s' to html !", output.getAbsolutePath()), e);
        }
    }

    protected File getOutput(String filename, AbstractDocsGeneratorTask.Format desiredExtension) {
        filename = FilenameUtils.isExtension(filename, desiredExtension.name()) ? filename : filename + "." + desiredExtension;
        return new File(generatedDocsDirectory, filename);
    }

    protected Asciidoctor createAsciidoctor() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.requireLibrary("asciidoctor-diagram");
        return asciidoctor;
    }

    protected Options options() {
        this.asciidocTemplates.mkdirs();

        String imagesOutputDirectory = generatedDocsDirectory.getAbsolutePath();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("imagesoutdir", imagesOutputDirectory);
        attributes.put("outdir", imagesOutputDirectory);

        return OptionsBuilder.options()
                .backend("html5")
                .safe(UNSAFE)
                .baseDir(generatedDocsDirectory)
                .templateDirs(asciidocTemplates)
                .attributes(attributes)
                .get();
    }

    protected void extractTemplatesFromJar() {
        this.asciidocTemplates.mkdirs();
        try {
            Arrays.asList(new PathMatchingResourcePatternResolver().getResources(TEMPLATES_CLASSPATH_PATTERN))
                    .forEach(templateResource -> {
                        try {
                            copyInputStreamToFile(templateResource.getInputStream(), new File(this.asciidocTemplates, templateResource.getFilename()));
                        } catch (IOException e) {
                            throw new RuntimeException("Could not write template to target file", e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
