package BackEndUtilities;

import Interfaces.IMeasure;
import Settings.UserSettings;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicJavaClassLoader {
    private static final Logger logger = LogManager.getLogger(DynamicJavaClassLoader.class);

    public static void init() {

        File[] files = new File(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER + "/").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if (files == null) {
            logger.debug("No custom measures found.");
            return;
        }
        for (File javaFile : files) {
            if (javaFile.isFile()) {

                try {
                    logger.debug(FilenameUtils.getBaseName(javaFile.getName()) + ": Trying to load.");
                    String content = Files.readString(javaFile.toPath(), Charset.defaultCharset());

                    if (javaFile.getParentFile().exists() || javaFile.getParentFile().mkdirs()) {

                        try {

                            /** Compilation Requirements *********************************************************************************************/
                            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                            // This sets up the class path that the compiler will use.
                            // I've added the .jar file that contains the DoStuff interface within in it...
                            List<String> optionList = new ArrayList<String>();

                            Iterable<? extends JavaFileObject> compilationUnit
                                    = fileManager.getJavaFileObjectsFromFiles(List.of(javaFile));
                            JavaCompiler.CompilationTask task = compiler.getTask(
                                    null,
                                    fileManager,
                                    diagnostics,
                                    optionList,
                                    null,
                                    compilationUnit);
                            /********************************************************************************************* Compilation Requirements **/
                            if (task.call()) {
                                /** Load and execute *************************************************************************************************/
                                logger.debug(FilenameUtils.getBaseName(javaFile.getName()) + ": compiling");
                                // Create a new custom class loader, pointing to the directory that contains the compiled
                                // classes, this should point to the top of the package structure!
                                URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
                                // Load the class from the classloader by name....
                                Class<?> loadedClass = classLoader.loadClass("CustomMeasures." + FilenameUtils.getBaseName(javaFile.getName()));
                                // Create a new instance...
                                Object obj = loadedClass.getDeclaredConstructor().newInstance();
                                // Santity check
                                if (obj instanceof IMeasure measure) {
                                    MeasureManager.dynamicallyAddMeasure(measure);
                                    logger.debug(FilenameUtils.getBaseName(javaFile.getName()) + ": added to measures");
                                }
                                else {
                                    logger.error(FilenameUtils.getBaseName(javaFile.getName()) + ": doesn't implement IMeasure");
                                }
                                /************************************************************************************************* Load and execute **/
                            } else {
                                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                                    System.out.format("Error on line %d in %s%n",
                                            diagnostic.getLineNumber(),
                                            diagnostic.getSource().toUri());
                                }
                            }
                            fileManager.close();
                        } catch (IOException | ClassNotFoundException | InstantiationException |
                                 IllegalAccessException exp) {
                            exp.printStackTrace();
                        } catch (InvocationTargetException | NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
