package GUI;

public enum CardTypes {
    ONE_DATA_NO_VARIABLE ("ODNV"),
    TWO_DATA_NO_VARIABLE ("TDNV"),
    NO_DATA_ONE_VARIABLE ("NDOV"),
    NO_DATA_TWO_VARIABLE ("NDTV"),
    ONE_DATA_ONE_VARIABLE ("ODOV");

    private final String name;
    CardTypes(String name){
        this.name = name;
    }

    String getName(){ return this.name; }
}
