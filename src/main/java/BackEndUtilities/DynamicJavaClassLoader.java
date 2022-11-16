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

    public static void init() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {

        File[] files = new File(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if (files == null) {
            logger.debug("No custom measures found.");
            return;
        }
        for (File file : files) {
            if (!file.getName().endsWith(".java"))
                continue;
            logger.debug(file.getName() + ": checking");
            if (file.isFile()) {
                try {
                    logger.debug(FilenameUtils.getBaseName(file.getName()) + ": Trying to load.");
                    String content = Files.readString(file.toPath(), Charset.defaultCharset());

                    File javaFile = new File("CustomMeasures/" + FilenameUtils.getBaseName(file.getName()) + ".java");
                    File classFile = new File("CustomMeasures/" + FilenameUtils.getBaseName(file.getName()) + ".class");
                    if(classFile.exists()) {
                        if (classFile.delete()) {
                            logger.debug(FilenameUtils.getBaseName(file.getName()) + ".class" + ": deleted and ready for compile.");
                        } else {
                            logger.debug(FilenameUtils.getBaseName(file.getName()) + ".class" + ": was not deleted.");
                        }
                    }
                    if (javaFile.getParentFile().exists() || javaFile.getParentFile().mkdirs()) {

                        try {
                            Writer writer = null;
                            try {
                                writer = new FileWriter(javaFile);
                                writer.write(content);
                                writer.flush();
                                logger.debug(FilenameUtils.getBaseName(file.getName()) + ": content re-written");
                            } finally {
                                try {
                                    writer.close();
                                } catch (Exception e) {
                                    logger.error("An error occurred.");
                                }
                            }

                            /** Compilation Requirements *********************************************************************************************/
                            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                            // This sets up the class path that the compiler will use.
                            List<String> optionList = new ArrayList<>();

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
                                logger.debug(FilenameUtils.getBaseName(file.getName()) + ": compiling");
                                // Create a new custom class loader, pointing to the directory that contains the compiled
                                // classes, this should point to the top of the package structure!
                                URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
                                // Load the class from the classloader by name....
                                Class<?> loadedClass = classLoader.loadClass("CustomMeasures." + FilenameUtils.getBaseName(file.getName()));
                                // Create a new instance...
                                Object obj = loadedClass.getDeclaredConstructor().newInstance();

                                if (obj instanceof IMeasure measure) {
                                    MeasureManager.dynamicallyAddMeasure(measure);
                                    logger.debug(FilenameUtils.getBaseName(file.getName()) + ": added to measures");
                                }
                                else {
                                    logger.error(FilenameUtils.getBaseName(file.getName()) + ": doesn't implement IMeasure");
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
