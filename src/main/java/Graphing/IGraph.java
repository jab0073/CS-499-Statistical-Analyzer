package Graphing;

import FrontEndUtilities.GUIMeasure;
import Interfaces.IMeasure;

public interface IGraph {
    //MAKE SURE ALL IGRAPH IMPLEMENTATIONS CONTAIN A CONSTRUCTOR
    //IT DOESN'T MATTER IF THE CONSTRUCTOR IS EMPTY, IT MUST HAVE ONE

    void setData(Object data, DataFormat format);

    void graphData(GUIMeasure measure);

    GraphTypes getGraphType();
}
