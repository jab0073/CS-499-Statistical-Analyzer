package Respository;

import BackEndUtilities.Constants;
import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IStorage;
import Interop.UIServices;
import Settings.UserSettings;
import TableUtilities.DataTable;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RepositoryManager {
    public static List<DataSet> loadedDataSets;
    private static final Logger logger = LogManager.getLogger(RepositoryManager.class.getName());

    private static IStorage storage;

    public static List<DataSet> getAllDataSets() {
        return loadedDataSets;
    }

    public static DataSet getDataSetFromStorage(String name) {
        String folder = getDataSetFolderPath();
        return loadedDataSets.stream().filter(ds->ds.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(()->{
                    try {
                        DataSet r= storage.getDataSet(name, folder);
                        loadedDataSets.add(r);
                        return r;
                    } catch (Exception e) {
                        logger.error(e);
                        return null;
                    }
                });
    }

    public static DataSet getDataSet(String name) {
        return loadedDataSets.stream().filter(ds->ds.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static DataSet getDataSet(int index) {
        return loadedDataSets.get(index);
    }

    public static boolean hasDataSet(String name) {
        return loadedDataSets.stream().map(ds -> ds.getName().equalsIgnoreCase(name)).findAny().isPresent();
    }

    public static DataSet putDataSet(DataSet ds, String name) {
        if (ds == null) throw new AssertionError("This method must be called with a DataSet, even if it is an empty instance");
        DataSet ds2 = getDataSet(name);  //If the udm already exists, get the instance

        if (ds2 == null) {   //If it does not, make a new one and add it to loaded user defined measures
            ds2 = ds;
            loadedDataSets.add(ds2);
        }
        storage.put(ds2, name, getDataSetFolderPath());
        return ds2;
    }

    private static String getUDMFolderPath()
    {
        return UserSettings.getWorkingDirectory()  + "/" + Constants.UDM_FOLDER;
    }

    private static String getDataSetFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.DATASET_FOLDER;
    }

    private static String getExportFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.EXPORT_FOLDER;
    }

    public static boolean exportCSV(DataSet ds, String name) {
        String fileName = getExportFolderPath() + "/" + name + ".csv";
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            ds.getSamples().forEach(s -> {
                writer.writeNext(s.getData().toArray(String[]::new));
            });

            // closing writer connection
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportCSV(Sample s, String name) {
        String fileName = getExportFolderPath() + "/" + name + ".csv";
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            writer.writeNext(s.getData().toArray(String[]::new));

            // closing writer connection
            writer.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static DataSet importCSV(String fileName) {
        DataTable dt = UIServices.fromCSV(fileName);
        if (dt != null)
            return dt.toDataSet();
        return null;
    }

    public static void deleteDataSet(String name) {
        DataSet ds = getDataSet(name);
        if (ds != null && ds.getAllDataAsDouble() != null){
            storage.deleteFile(name, getDataSetFolderPath());
            loadedDataSets.remove(ds);
        }
    }

    public static void removeFromLoadedDataSets(String name) {
        DataSet ds = getDataSet(name);
        if(ds != null) {
            loadedDataSets.remove(ds);
        }
    }

    public static void init() {
        loadedDataSets = new ArrayList<>();
        storage = new FileSystemRepository();
        UserSettings.init();
        storage.init();
        loadedDataSets.addAll(storage.loadDataSets(getDataSetFolderPath()));
    }

    public static void openHelpDocument(){
        if (Desktop.isDesktopSupported()) {
            try {
                File helpDoc = new File(UserSettings.getWorkingDirectory() + "/Help Document.pdf");

                if(helpDoc.exists()){
                    Desktop.getDesktop().open(helpDoc);
                }else{
                    InputStream jarPdf = RepositoryManager.class.getClassLoader().getResourceAsStream("docs/Help Document.pdf");

                    try {
                        File pdfTemp = new File(UserSettings.getWorkingDirectory() + "/Help Document.pdf");

                        FileOutputStream fos = new FileOutputStream(pdfTemp);
                        while (true) {
                            assert jarPdf != null;
                            if (!(jarPdf.available() > 0)) break;

                            fos.write(jarPdf.read());
                        }
                        fos.close();

                        Desktop.getDesktop().open(pdfTemp);
                    }catch (IOException e) {
                        System.out.println("erreur : " + e);
                    }
                }

            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

    public static BufferedImage getImageResource(String name){
        String path = "images/"+name;
        try {
            URL resource = RepositoryManager.class.getClassLoader().getResource("images/logo.png");
            assert resource != null;
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
