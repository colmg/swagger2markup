package io.github.robwin.swagger2markup;

import com.wordnik.swagger.models.Swagger;
import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.builder.document.DefinitionsDocument;
import io.github.robwin.swagger2markup.builder.document.MarkupDocument;
import io.github.robwin.swagger2markup.builder.document.PathsDocument;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Robert Winkler
 */
public class Swagger2MarkupConverter {
    private static final Logger LOG = LoggerFactory.getLogger(Swagger2MarkupConverter.class);

    private final Swagger swagger;
    private final MarkupLanguage markupLanguage;
    private final String examplesFolderPath;
    private final String schemasFolderPath;
    private static final String PATHS_DOCUMENT = "paths";
    private static final String DEFINITIONS_DOCUMENT = "definitions";
    private final MarkupDocument pathsDocument;

    /**
     * @param markupLanguage the markup language which is used to generate the files
     * @param swagger the Swagger object
     * @param examplesFolderPath the folderPath where examples are stored
     * @param schemasFolderPath the folderPath where (XML, JSON)-Schema  files are stored
     * @param pathsDocument
     */
    Swagger2MarkupConverter(MarkupLanguage markupLanguage, Swagger swagger, String examplesFolderPath,
                            String schemasFolderPath, MarkupDocument pathsDocument){
        this.markupLanguage = markupLanguage;
        this.swagger = swagger;
        this.examplesFolderPath = examplesFolderPath;
        this.schemasFolderPath = schemasFolderPath;
        this.pathsDocument = pathsDocument;
    }

    /**
     * Creates a Swagger2MarkupConverter.Builder using a given Swagger source.
     *
     * @param swaggerSource the Swagger source. Can be a HTTP url or a path to a local file.
     * @return a Swagger2MarkupConverter
     */
    public static Builder from(String swaggerSource){
        Validate.notEmpty(swaggerSource, "swaggerSource must not be null!");
        return new Builder(swaggerSource);
    }

    /**
     * Builds the document with the given markup language and stores
     * the files in the given folder.
     *
     * @param targetFolderPath the target folder
     * @throws IOException if the files cannot be written
     */
    public void intoFolder(String targetFolderPath) throws IOException {
        Validate.notEmpty(targetFolderPath, "folderPath must not be null!");
        buildDocuments(targetFolderPath);
    }

    /**
     * Writes a file for the Paths (API) and a file for the Definitions (Model)

     * @param directory the directory where the generated file should be stored
     * @throws IOException if a file cannot be written
     */
    private void buildDocuments(String directory) throws IOException {

        MarkupDocument markupDocument = pathsDocument;
        if (markupDocument == null)
            markupDocument = new PathsDocument(swagger, markupLanguage, examplesFolderPath);

        markupDocument.build().writeToFile(directory, PATHS_DOCUMENT, StandardCharsets.UTF_8);
        new DefinitionsDocument(swagger, markupLanguage, schemasFolderPath).build().writeToFile(directory, DEFINITIONS_DOCUMENT, StandardCharsets.UTF_8);
    }


    public static class Builder{
        private final Swagger swagger;
        private String examplesFolderPath;
        private String schemasFolderPath;
        private MarkupLanguage markupLanguage = MarkupLanguage.ASCIIDOC;
        private MarkupDocument pathsDocument;

        /**
         * Creates a Builder using a given Swagger source.
         *
         * @param swaggerSource the Swagger source. Can be a HTTP url or a path to a local file.
         */
        Builder(String swaggerSource){
            swagger = new SwaggerParser().read(swaggerSource);
        }

        public Swagger2MarkupConverter build(){
            return new Swagger2MarkupConverter(markupLanguage, swagger, examplesFolderPath,
                                            schemasFolderPath, pathsDocument);
        }

        /**
         * Specifies the markup language which should be used to generate the files
         *
         * @param markupLanguage the markup language which is used to generate the files
         * @return the Swagger2MarkupConverter.Builder
         */
        public Builder withMarkupLanguage(MarkupLanguage markupLanguage){
            this.markupLanguage = markupLanguage;
            return this;
        }

        /**
         * Include examples into the Paths document
         *
         * @param examplesFolderPath the path to the folder where the example documents reside
         * @return the Swagger2MarkupConverter.Builder
         */
        public Builder withExamples(String examplesFolderPath){
            this.examplesFolderPath = examplesFolderPath;
            return this;
        }
        public Builder withPathsDocument(MarkupDocument pathsDocument) {
            this.pathsDocument = pathsDocument;
            return this;
        }

        /**
         * Include (JSON, XML) schemas into the Definitions document
         *
         * @param schemasFolderPath the path to the folder where the schema documents reside
         * @return the Swagger2MarkupConverter.Builder
         */
        public Builder withSchemas(String schemasFolderPath){
            this.schemasFolderPath = schemasFolderPath;
            return this;
        }

    }

}
