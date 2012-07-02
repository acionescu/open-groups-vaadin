package ro.zg.presentation.utils;

import java.util.ArrayList;
import java.util.List;

public class AbstractList extends AbstractModel<UserEvent>{
    public static final String HEADER_CONTAINER="header-container";
    public static final String HEADER_CELL="header-cell";
    public static final String CONTROLS_CONTAINER="controls-container";
    public static final String CONTAINER="container";
    public static final String ROW_CONTAINER="row-container";
    public static final String ROW_CELL="row-cell";
    private static final String CONTROLS_CONTAINER_CELL="controls-container-cell";
    
    private String name;
    private List<ListColumn> columns = new ArrayList<ListColumn>();
    
       
    public String getHeaderContainerStyle(){
	return name+"-"+HEADER_CONTAINER;
    }
    
    public String getHeaderCellStyle(){
	return name+"-"+HEADER_CELL;
    }
    
    public String getControlsContainerStyle(){
	return name+"-"+CONTROLS_CONTAINER;
    }
    
    public String getListContainerStyle(){
	return name+"-"+CONTAINER;
    }
    
    public String getRowContainerStyle(){
	return name+"-"+ROW_CONTAINER;
    }
    
    public String getRowCellStyle() {
	return name+"-"+ROW_CELL;
    }
    
    public String getControlsContainerCellStyle(){
	return name+"-"+CONTROLS_CONTAINER_CELL;
    }
    
    public void addColumn(ListColumn column){
	columns.add(column);
    }


    public List<ListColumn> getColumns() {
        return columns;
    }


    public void setColumns(List<ListColumn> columns) {
        this.columns = columns;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
