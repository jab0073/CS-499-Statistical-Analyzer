package Validators;

import BackEndUtilities.DataSet;
import BackEndUtilities.Sample;
import Interfaces.IValidator;
import TableUtilities.Cell;
import TableUtilities.DataTable;
import TableUtilities.Row;
import org.mariuszgromada.math.mxparser.Expression;

public class DataValidator extends IValidator {
    public static ValidationStatus status = ValidationStatus.NOT_VALIDATED;

    public static boolean validate(DataTable dt) {
        for (Row r : dt.getRows()) {
            for (Cell c : r.getCells()) {
                if (!new Expression(c.data).checkSyntax()) {
                    dt.status = ValidationStatus.INVALID;
                    return false;
                }
            }
        }
        dt.status = ValidationStatus.VALID;
        return true;
    }

    public static boolean validate(DataSet ds) {
        for (Sample s : ds.getSamples()) {
            for (String str : s.getData()) {
                if (!new Expression(str).checkSyntax()) {
                    ds.status = ValidationStatus.INVALID;
                    return false;
                }
            }
        }
        ds.status = ValidationStatus.VALID;
        return true;
    }

    public static boolean validate(Sample sample) {
        for (String str : sample.getData()) {
            if (!new Expression(str).checkSyntax()) {
                sample.status = ValidationStatus.INVALID;
                return false;
            }
        }
        sample.status = ValidationStatus.VALID;
        return true;
    }

    public static boolean validate(String data) {
        return new Expression(data).checkSyntax();
    }
}
