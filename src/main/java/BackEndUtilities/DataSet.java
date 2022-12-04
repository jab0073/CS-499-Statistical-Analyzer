package BackEndUtilities;

import Interfaces.IValidator;
import TableUtilities.DataTable;
import Validators.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It's basically a list of samples
 */
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

    /**
     * "If the sample is not null, add it to the samples list."
     *
     * Check if the sample is null. If it is, we don't do anything. If it isn't, we add it to the
     * samples list
     *
     * @param sample The sample to be added to the dataset
     */
    public void addSample(Sample sample) {
        if(sample != null)
            this.samples.add(sample);
        logger.debug("Sample of size " + (sample != null ? sample.getSize() : 0) + " added to dataset");
    }

    /**
     * "If the sample is not null, add it to the samples list at the specified index."
     *
     * Check if the sample is null. If it is, we don't do anything. If it isn't, we add it to the
     * samples list at the specified index
     *
     * @param index The index of the sample to be added
     * @param sample The sample to be added to the dataset
     */
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

    /**
     * Get all the data from all the samples as a list of doubles.
     *
     * @return A list of all the data in the samples as doubles.
     */
    public List<Double> getAllDataAsDouble() {
        return this.samples.stream().map(Sample::getDataAsDouble).toList().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * > Get all the data from all the samples as a list of strings
     *
     * @return A list of all the data in the samples.
     */
    public List<String> getAllDataAsString(){
        return this.samples.stream().map(Sample::getData).toList().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public int getSize() {
        return this.samples.stream().mapToInt(Sample::getSize).sum();
    }

    /**
     * This function returns the size of the samples in the given indices.
     *
     * @return The size of the samples.
     */
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

    /**
     * Convert a DataSet to a DataTable by adding each sample to the DataTable.
     *
     * @return A DataTable object
     */
    public DataTable toTable() {
        logger.debug("Converting DataSet to DataTable");
        DataTable dt = new DataTable();
        this.samples.forEach(dt::addRow);
        return dt;
    }

    /**
     * Takes a DataTable and returns a DataSet
     *
     * @param dt The DataTable object that you want to convert to a DataSet object.
     * @return A DataSet object
     */
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
