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

    private static FileSystemRepository storage;

   public static void putSaveState(String fileName) {
       storage.put(fileName, getProjectFolderPath());
   }

   public static void getSaveState(String fileName) {
       storage.get(fileName, getProjectFolderPath());
   }

    private static String getUDMFolderPath()
    {
        return UserSettings.getWorkingDirectory()  + "/" + Constants.UDM_FOLDER;
    }

    private static String getProjectFolderPath()
    {
        return UserSettings.getWorkingDirectory() + "/" + Constants.PROJECTS_FOLDER;
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

    public static void init() {
        loadedDataSets = new ArrayList<>();
        storage = new FileSystemRepository();
        storage.init();
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

    public static void buildWD() {
       storage.init();
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
