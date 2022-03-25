/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.api.server.spi.tools;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.input.ReaderInputStream;

import com.google.api.server.spi.IoUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;

/**
 * Implementation of (@link ClientLibGenerator} using a local tool for generation.<br>
 * The Python package <code>google-apis-client-generator</code> must be installed:
 * <pre>
 *     python2 -m pip install --user --upgrade setuptools wheel
 *     python2 -m pip install --user google-apis-client-generator
 * </pre>
 */
public class LocalClientLibGenerator implements ClientLibGenerator {
  
  private static final Logger log = Logger.getLogger(LocalClientLibGenerator.class.getName());
  
  @VisibleForTesting
  static final String GENERATOR_EXECUTABLE = "generate_library";
  private static final String GENERATOR_DISCOVERY_FILE_OPTION = "--input=";
  private static final String GENERATOR_LANGUAGE_OPTION = "--language=";
  private static final String GENERATOR_DESTINATION_DIRECTORY_OPTION = "--output_dir=";
  /* Put API version in package paths. */
  private static final String GENERATOR_API_VERSION_PACKAGE_OPTION = "--version_package";
  
  private static final List<String> GENERATOR_SUPPORTED_LANGUAGES = ImmutableList.of("java");
  
  /**
   * Generate the source code.
   * @param discoveryDoc Discovery document of the API
   * @param language Only <code>java</code> is supported.
   * @param languageVersion Ignored.
   * @param layout Ignored.
   * @param destinationDirectory Target directory to generate source code into.
   * @throws IOException on failure
   */
  @Override
  public void generateClientLib(String discoveryDoc, String language, String languageVersion,
      String layout, File destinationDirectory) throws IOException {
    Preconditions.checkArgument(GENERATOR_SUPPORTED_LANGUAGES.contains(language), "Unsupported language: " + language);

    // Creates the destination directory
    Files.createDirectories(destinationDirectory.toPath());

    // Creates a temporary discovery file
    File discoveryFile = File.createTempFile(GENERATOR_EXECUTABLE, "-discovery.tmp");
    try {
      IoUtil.copy(new ReaderInputStream(CharSource.wrap(discoveryDoc).openStream()), discoveryFile);
  
      File generateLibOut = File.createTempFile(GENERATOR_EXECUTABLE, ".out");
      File generateLibErr = File.createTempFile(GENERATOR_EXECUTABLE, ".err");
  
      List<String> command = new ArrayList<>(getLibraryGeneratorCommand());
      command.add(GENERATOR_DISCOVERY_FILE_OPTION + discoveryFile);
      command.add(GENERATOR_LANGUAGE_OPTION + language);
      command.add(GENERATOR_DESTINATION_DIRECTORY_OPTION + destinationDirectory.getAbsolutePath());
      command.add(GENERATOR_API_VERSION_PACKAGE_OPTION);
  
      ProcessBuilder builder = new ProcessBuilder()
              .command(command)
              .redirectOutput(generateLibOut)
              .redirectError(generateLibErr);
      int status;
      try {
        status = builder.start().waitFor();
      } catch (InterruptedException e) {
        throw new RuntimeException("Source code generation interrupted", e);
      }
      if (status == 0) {
        // Success: get rid of output files
        generateLibOut.delete();
        generateLibErr.delete();
      } else {
        throw new IOException("Failed to generate source code. See " + generateLibErr.getAbsolutePath() + " for details");
      }
    } finally {
      discoveryFile.delete();
    }
  }
  
  private List<String> getLibraryGeneratorCommand() {
    if (getProperty("os.name").toLowerCase().contains("win")) {
      log.warning("You are using the LocalClientLibGenerator on Windows.\n"
              + "You should specify the following environmental variables:\n"
              + "- GOOGLE_GENERATE_LIBRARY_PYTHON to point at your Python 2.7 installation\n"
              + "- GOOGLE_GENERATE_LIBRARY_SCRIPT_LOCATION to point at the location where your generate_library.py script was installed (it should be something like 'C:\\Users\\Armin\\AppData\\Roaming\\Python\\Python27\\site-packages\\googleapis\\codegen\\generate_library.py')");
    
      // The generate_library.exe runs library generation asynchronously on Windows due to https://bugs.python.org/issue9148
      // so we call the script directly
    
      String python = firstNonNull(
              getenv("GOOGLE_GENERATE_LIBRARY_PYTHON"),
              "python"
      );
    
      String scriptLocation = firstNonNull(
              getenv("GOOGLE_GENERATE_LIBRARY_SCRIPT_LOCATION"),
              getProperty("user.home") + "\\AppData\\Roaming\\Python\\Python27\\site-packages\\googleapis\\codegen\\generate_library.py"
      );
    
      File scriptLocationFile = new File(scriptLocation);
      Preconditions.checkArgument(scriptLocation.endsWith("generate_library.py"), "You should specify a generate_library.py in the GOOGLE_GENERATE_LIBRARY_SCRIPT_LOCATION env var");
      Preconditions.checkArgument(scriptLocationFile.isFile(), "Could not find script at '" + scriptLocationFile.getAbsolutePath() + "':  make the GOOGLE_GENERATE_LIBRARY_SCRIPT_LOCATION env var point at the generate_library.py script you have installed");
    
      return Arrays.asList(python, scriptLocationFile.getAbsolutePath());
    }
    
    return Collections.singletonList(GENERATOR_EXECUTABLE);
  }
}
