package BackEndUtilities;

import Interfaces.IValidator;
import Interop.UIServices;
import Measures.Measures;
import TableUtilities.DataTable;
import Validators.DataValidator;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataSet implements Cloneable{

    private List<Sample> samples;
    private String name;
    private static final Logger logger = LogManager.getLogger(DataSet.class.getName());
    public IValidator.ValidationStatus status;


    public DataSet(){
        logger.debug("Creating empty DataSet");
        this.samples = new ArrayList<>();
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public DataSet(List<Sample> samples) {
        logger.debug("Creating DataSet with size of " + samples.size());
        this.samples = samples;
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public DataSet(Sample[] samples) {
        this.samples = Arrays.asList(samples);
        logger.debug("Creating DataSet with size of " + this.samples.size());
        this.status = IValidator.ValidationStatus.NOT_VALIDATED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSample(Sample sample) {
        if(sample != null)
            this.samples.add(sample);
        logger.debug("Sample of size " + (sample != null ? sample.getSize() : 0) + " added to dataset");
    }

    public void addSample(int index, Sample sample) {
        if(sample != null)
            this.samples.add(index, sample);
        logger.debug("Sample of size " + (sample != null ? sample.getSize() : 0) + " added to dataset");
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public void setSamples(Sample[] samples) {
        this.samples = Arrays.asList(samples);
    }

    public Sample getSample(int index) throws IndexOutOfBoundsException {
        if(index >= 0 && index < this.samples.size()) {
            return this.samples.get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public List<Double> getAllDataAsDouble() {
        return this.samples.stream().map(Sample::getDataAsDouble).toList().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public int getSize() {
        return this.samples.stream().mapToInt(Sample::getSize).sum();
    }

    public int getSize(Integer... indices) {
        int size = 0;
        for(Integer i : indices) {
            if(i >= 0 && i < this.samples.size()) {
                size += this.samples.get(i).getSize();
            }
        }
        return size;
    }

    public int getNumberOfSamples() {
        return this.samples.size();
    }

    public DataTable toTable() {
        logger.debug("Converting DataSet to DataTable");
        DataTable dt = new DataTable();
        this.samples.forEach(dt::addRow);
        return dt;
    }

    public static DataSet fromTable(DataTable dt) {
        logger.debug("Creating DataSet from DataTable");
        DataSet ds = new DataSet();
        dt.getRows().forEach(r -> {
            Sample s = new Sample();
            s.addData(r.getCells().stream().map(c->c.data).collect(Collectors.toList()));
            ds.addSample(s);
        });
        return ds;
    }

    public boolean save(String fileName) {
        Gson gson = new Gson();
        try {
            Writer writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.flush();
            writer.close();
            logger.debug(this.name + " written to " + fileName);
            return true;
        } catch (IOException e) {
            logger.error(this.name + " failed to write to " + fileName);
            return false;
        }
    }

    public static DataSet load(String fileName) {
        Gson gson = new Gson();
        try {
            DataSet obj =  gson.fromJson(new FileReader(fileName), DataSet.class);
            logger.debug("Successfully loaded dataset from " + fileName);
            return obj;
        } catch (FileNotFoundException e) {
            logger.error("!!! Failed to load dataset from " + fileName);
            return null;
        }
    }

    public boolean exportCSV(String fileName) {
        File file = new File(fileName);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            this.samples.forEach(s -> {
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

    public static DataSet importCSV(String fileName) {
        DataTable dt = UIServices.fromCSV(fileName);
        if (dt != null)
            return dt.toDataSet();
        return null;
    }

    public boolean validate() {
        return DataValidator.validate(this);
    }

    @Override
    public DataSet clone() {
        try {
            return (DataSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
