package Enums;

public enum CardTypes {
    ONE_DATA_NO_VARIABLE ("ODNV"),
    TWO_DATA_NO_VARIABLE ("TDNV"),
    NO_DATA_ONE_VARIABLE ("NDOV"),
    NO_DATA_TWO_VARIABLE ("NDTV"),
    ONE_DATA_ONE_VARIABLE ("ODOV"),
    ONE_DATA_TWO_VARIABLE ("ODTV"),
    BLANK ("BLANK");

    private final String name;
    CardTypes(String name){
        this.name = name;
    }

    public String getName(){ return this.name; }
}
