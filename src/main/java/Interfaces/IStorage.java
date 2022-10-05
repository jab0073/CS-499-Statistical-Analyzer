package Interfaces;

import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Measures.UserDefinedMeasure;

import java.io.File;
import java.util.List;

public interface IStorage {

    void init();

    boolean put(UserDefinedMeasure udm, String folder);

    boolean put(DataSet ds, String name, String folder);

    boolean put(Sample s, String name, String folder);

    UserDefinedMeasure get(String name, String folder);

    DataSet getDataSet(String name, String folder);

    List<UserDefinedMeasure> loadUserDefinedMeasures(String folder);

    List<DataSet> loadDataSets(String folder);

    void putFile(File file, String fileID, String folder);

    File getFile(String fileID, String folder);

    boolean deleteFile(String fileID, String folder);
}
