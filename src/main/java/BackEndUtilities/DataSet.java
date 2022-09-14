package BackEndUtilities;

import javax.management.ValueExp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataSet implements Cloneable{

    private List<Sample> samples;

    public DataSet(){
        this.samples = new ArrayList<>();
    }

    public DataSet(List<Sample> samples) {
        this.samples = samples;
    }

    public DataSet(Sample[] samples) {
        this.samples = Arrays.asList(samples);
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
        if(index > 0 && index < this.samples.size()) {
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

    @Override
    public DataSet clone() {
        try {
            return (DataSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
