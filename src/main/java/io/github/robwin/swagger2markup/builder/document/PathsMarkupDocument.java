/*
 * AOL CONFIDENTIAL INFORMATION
 *
 * Date: Feb 25, 2015
 *
 * Copyright 2015 America Online, Inc.
 *
 * All Rights Reserved.  Unauthorized reproduction, transmission, or
 * distribution of this software is a violation of applicable laws.
 */
package io.github.robwin.swagger2markup.builder.document;

import java.io.IOException;
import java.util.Map;

import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.models.Swagger;
import io.github.robwin.markup.builder.MarkupLanguage;

/**
 * @author colmg <colm.geraghty@teamaol.com>
 */
public abstract class PathsMarkupDocument extends MarkupDocument {


  PathsMarkupDocument(Swagger swagger, MarkupLanguage markupLanguage) {
    super(swagger, markupLanguage);
  }

  public  abstract void documentHeader(Info info);

  public abstract void paths(Map<String, Path> paths) throws IOException;

  public abstract void path(String httpMethod, String resourcePath, Operation operation) throws IOException;


  public abstract void pathTitle(String httpMethod, String resourcePath, Operation operation);

  public abstract void descriptionSection(Operation operation);

  public abstract void parametersSection(Operation operation);

  public abstract void consumesSection(Operation operation);

  public abstract void producesSection(Operation operation);

  /**
   * Builds the example section of a Swagger Operation
   *
   * @param operation the Swagger Operation
   * @throws IOException if the example file is not readable
   */
  public abstract void examplesSection(Operation operation) throws IOException;

  /**
   * Builds a concrete example
   *
   * @param title the title of the example
   * @param exampleFolder the name of the folder where the example file resides
   * @param exampleFileName the name of the example file
   * @throws IOException
   */
  public abstract void example(String title, String exampleFolder, String exampleFileName) throws IOException;

  public abstract void responsesSection(Operation operation);

}



