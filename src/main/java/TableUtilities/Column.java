package TableUtilities;

public class Column {
    private String header;

    public Column(String header) {
        this.header = header;
    }

    /**
     * This function returns the header of the current column.
     *
     * @return The header of the column.
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * This function sets the header of the column.
     *
     * @param header The header of the column.
     */
    public void setHeader(String header) {
        this.header = header;
    }
}
