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
import java.util.List;

public class DynamicJavaClassLoader {
    private static final Logger logger = LogManager.getLogger(DynamicJavaClassLoader.class);
    private static File templateFile;
    /**
     * It reads all the .java files in the UDM folder, writes them to the CustomMeasures folder, compiles them, and then
     * loads them into the program
     */
    public static void init(){
        templateFile = new File(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER + "/TEMPLATE.java");
        if(!templateFile.exists())
            generateTemplate();
        File[] files = new File(UserSettings.getWorkingDirectory() + "/" + Constants.UDM_FOLDER).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        if (files == null) {
            logger.debug("No custom measures found.");
            return;
        }
        for (File file : files) {
            if (!file.getName().endsWith(".java"))
                continue;
            if(file.getName().equals("TEMPLATE.java"))
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
                                    assert writer != null;
                                    writer.close();
                                } catch (Exception e) {
                                    logger.error("An error occurred.");
                                }
                            }

                            /*
                            Compilation Requirements
                            */
                            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
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

                            /*
                            Compilation Requirements
                            */
                            if (task.call()) {
                                /*
                                Load and execute
                                */
                                logger.debug(FilenameUtils.getBaseName(file.getName()) + ": compiling");
                                // Create a new custom class loader, pointing to the directory that contains the compiled
                                // classes, this should point to the top of the package structure!
                                try {
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
                                } catch (NullPointerException | SecurityException e) {
                                    return;
                                }
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

    private static void generateTemplate() {
        String template = """
                package CustomMeasures;
                                
                import BackEndUtilities.DataSet;
                import BackEndUtilities.Expressions;
                import BackEndUtilities.MeasureConstants;
                import BackEndUtilities.Sample;
                import FrontEndUtilities.ErrorManager;
                import GUI.CardTypes;
                import Graphing.DataFormat;
                import Graphing.GraphTypes;
                import Interfaces.IMeasure;
                import Interfaces.IValidator;
                import org.apache.commons.math3.distribution.BinomialDistribution;
                                
                import java.util.ArrayList;
                import java.util.Arrays;
                import java.util.Collections;
                import java.util.List;
                                
                /*
                Graph Types:
                    HORIZONTAL_BAR
                    VERTICAL_BAR
                    X_Y
                    NORMAL_CURVE
                    PIE_CHART
                    NONE
                                
                Card Types:
                    ONE_DATA_NO_VARIABLE
                    TWO_DATA_NO_VARIABLE
                    NO_DATA_ONE_VARIABLE
                    NO_DATA_TWO_VARIABLE
                    ONE_DATA_ONE_VARIABLE
                    ONE_DATA_TWO_VARIABLE
                    BLANK
                 */
                                
                public class TEMPLATE implements IMeasure {
                                
                    private DataSet inputData;
                    private final String name = "TEMPLATE";
                    private final int minimumSamples = 1;
                    private final List<String> requiredVariables = Arrays.asList();
                    private final boolean isGraphable = true;
                    private final List<GraphTypes> validGraphs = List.of(GraphTypes.NONE);
                    private final CardTypes cardType = CardTypes.BLANK;
                                
                    public boolean isGraphable(){ return this.isGraphable; }
                                
                    public List<GraphTypes> getValidGraphs(){ return this.validGraphs; }
                                
                    public TEMPLATE() {
                        this.inputData = new DataSet();
                    }
                                
                    public TEMPLATE(DataSet inputData) {
                        this.inputData = inputData;
                    }
                                
                    // Don't touch the following functions till...
                                
                    @Override
                    public String getName() {
                        return this.name;
                    }
                                
                    @Override
                    public int getMinimumSamples() {
                        return this.minimumSamples;
                    }
                                
                    @Override
                    public List<String> getRequiredVariables() {
                        return this.requiredVariables;
                    }
                                
                    @Override
                    public void setInputData(DataSet inputData) {
                        this.inputData = inputData;
                    }
                                
                    @Override
                    public DataSet getInputData() {
                        return this.inputData;
                    }
                                
                    @Override
                    public boolean validate() {
                        if (this.inputData == null || this.inputData.getAllDataAsDouble().size() == 0) {
                            ErrorManager.sendErrorMessage(name, "No Data supplied to evaluate");
                            return false;
                        }
                        if (this.inputData.getNumberOfSamples() < this.minimumSamples) {
                            ErrorManager.sendErrorMessage(name, "Invalid number of samples");
                            return false;
                        }
                        if (this.inputData.status == IValidator.ValidationStatus.INVALID) {
                            ErrorManager.sendErrorMessage(name, "Input Data not able to be validated");
                            return false;
                        }
                        if(this.requiredVariables.size() > 0) {
                            return this.requiredVariables.stream()
                                    .anyMatch(Expressions::ensureArgument);
                        }
                        return true;
                    }
                                
                    @Override
                    public DataFormat getOutputFormat(){ return DataFormat.DOUBLE_LIST; }
                                
                    @Override
                    public CardTypes getCardType(){ return cardType; }
                                
                    // here
                                
                    // Change the return type, add functionality, return the results
                                
                    @Override
                    public Object run() {
                                
                        // Don't touch
                        logger.debug("Running " + this.name);
                                
                        // Don't touch
                        if(Expressions.isEvaluationOn()){
                            DataSet newDS = new DataSet();
                            for(Sample s : inputData.getSamples()){
                                List<Double> eval = Expressions.eval(s);
                                Sample newS = new Sample(eval);
                                
                                newDS.addSample(newS);
                            }
                                
                            inputData = newDS;
                        }
                                
                        // Don't touch
                        if(!this.validate())
                            return null;
                                
                        /*
                        // This is how you get the variable values
                                
                        int n = Integer.parseInt(Expressions.getArgument("n"));
                                
                        double p = Double.parseDouble(Expressions.getArgument("p"));
                                
                        */
                                
                        // Add functionality below here
                       
                        List<Double> result = new ArrayList<>();
                                
                        return result;
                    }
                }
                                
                """;
        try {
            FileWriter fw = new FileWriter(templateFile);
            fw.write(template);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
