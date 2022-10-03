package Interfaces;

import Measures.UserDefinedMeasure;

import java.io.File;
import java.util.List;

public interface IStorage {

    void init();

    boolean put(UserDefinedMeasure udm, String folder);

    UserDefinedMeasure get(String GUID, String folder);

    List<UserDefinedMeasure> loadUserDefinedMeasures(String folder);

    void putFile(File file, String fileID, String folder);

    File getFile(String fileID, String folder);

    boolean deleteFile(String fileID, String folder);
}
