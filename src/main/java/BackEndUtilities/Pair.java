package BackEndUtilities;

import java.util.Objects;

public class Pair {
    private String variable;
    private String value;

    public Pair(String variable, String value) {
        this.variable = variable;
        this.value = value;
    }

    public Pair(String variable) {
        this.variable = variable;
    }

    public String get() {
        return this.variable + "=" + this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVariable(){
        return this.variable;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair pair)) return false;
        return variable.equals(pair.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }
}
