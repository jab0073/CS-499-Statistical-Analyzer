package BackEndUtilities;

import TableUtilities.DataTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataSet implements Cloneable{

    private List<Sample> samples;
    private static final Logger logger = LogManager.getLogger(DataSet.class.getName());

    public DataSet(){
        logger.debug("Creating empty DataSet");
        this.samples = new ArrayList<>();
    }

    public DataSet(List<Sample> samples) {
        logger.debug("Creating DataSet with size of " + samples.size());
        this.samples = samples;
    }

    public DataSet(Sample[] samples) {
        this.samples = Arrays.asList(samples);
        logger.debug("Creating DataSet with size of " + this.samples.size());
    }

    public void addSample(Sample sample) {
        this.samples.add(sample);
    }

    public void addSample(int index, Sample sample) {
        if(index > 0 && index < this.samples.size()) {
            this.samples.add(index, sample);
        }
        throw new IndexOutOfBoundsException();
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

    public List<BigDecimal> getDataAsDouble(Boolean evaluate) {
        return this.samples.stream().map(s -> s.getDataAsDouble(evaluate)).toList().stream().flatMap(List::stream).collect(Collectors.toList());
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

    @Override
    public DataSet clone() {
        try {
            return (DataSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
