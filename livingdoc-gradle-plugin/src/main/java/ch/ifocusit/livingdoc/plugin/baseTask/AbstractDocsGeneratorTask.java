package ch.ifocusit.livingdoc.plugin.baseTask;

import io.github.robwin.markup.builder.asciidoc.AsciiDoc;
import io.github.robwin.markup.builder.asciidoc.AsciiDocBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.repository.RepositorySystem;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractDocsGeneratorTask extends AbstractAsciidoctorTask {
    private static final String TITLE_MARKUP = AsciiDoc.DOCUMENT_TITLE.toString();

    protected RepositorySystem repositorySystem;

    protected String packageRoot;

    /**
     * Output format of the glossary (default html, others : adoc)
     */
    protected AbstractAsciidoctorTask.Format format;

    /**
     * File to use for UbiquitousLanguage mapping.
     */
    protected File glossaryMapping;

    // TODO active header/footer capabilities
//    /**
//     * Header of the generated asciidoc file
//     */
//    @Parameter
//    private File headerAsciidoc;

//    /**
//     * Footer of generated asciidoc file
//     */
//    @Parameter
//    private File footerAsciidoc;

    /**
     * Indicate that only annotated classes/fields will be used.
     */
    protected boolean onlyAnnotated;

    /**
     * @return the filename is defined by each mojo
     */
    protected abstract String getOutputFilename();

    /**
     * @return the document title is defined by each mojo
     */
    protected abstract String getTitle();

    /**
     * Simple write content to a file.
     *
     * @param newContent : file content
     * @param output     : destination file
     */
    protected void write(final String newContent, final File output) {
        try {
            output.getParentFile().mkdirs();
            IOUtils.write(newContent, new FileOutputStream(output), Charset.defaultCharset());
        } catch (IOException e) {
            throw new GradleException(String.format("Unable to write output file '%s' !", output), e);
        }
    }

    /**
     * Write asciidoc to defined output in defined format
     *
     * @param asciiDocBuilder : asciidoc content
     */
    protected void write(AsciiDocBuilder asciiDocBuilder) {
        write(asciiDocBuilder, getOutputFilename());
    }

    protected void write(AsciiDocBuilder asciiDocBuilder, String outputFilename) {
        write(asciiDocBuilder, format, outputFilename);
    }

    protected AsciiDocBuilder createAsciiDocBuilder() {
        AsciiDocBuilder asciiDocBuilder = new AsciiDocBuilder();
        asciiDocBuilder.textLine(":sectlinks:");
        asciiDocBuilder.textLine(":sectanchors:");
        return asciiDocBuilder;
    }

    protected void appendTitle(AsciiDocBuilder asciiDocBuilder) {
        String definedTitle = getTitle();
        if (StringUtils.isNotBlank(definedTitle)) {
            String title = definedTitle.startsWith(TITLE_MARKUP) ? definedTitle : TITLE_MARKUP + definedTitle;
            appendTitle(asciiDocBuilder, title);
        }
    }

    protected void appendTitle(AsciiDocBuilder asciiDocBuilder, String title) {
        asciiDocBuilder.textLine(title).newLine();
    }
}
