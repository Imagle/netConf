package config.mockDB;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class ConfigModel extends AbstractTableModel{
	
	public List<DBConfig> list ;
	public String filepath;
	public DBAction dba;
	
	public ConfigModel(String filepath){
		this.filepath = filepath;
		dba = new DBAction();
		dba.setPath(filepath);
		list = dba.getScanConfig();
	}
	
	public int getRowCount() {
		return list.size();
	}

	public int getColumnCount() {
		return 5;
	}

	public String getColumnName(int columnIndex) {
		String name = null;
		switch(columnIndex){
		case 0:
			name = "Num";
			break;
		case 1:
			name = "Defect";
			break;
		case 2:
			name = "Category";
			break;
		case 3:
			name = "Description";
			break;
		case 4:
			name = "ScanFlag";
			break;
		default:
			name = "Unknown";
			break;
		}
		return name;
	}

	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0 )
			return Integer.class;
		else if(columnIndex == 4)
			return Boolean.class;
		else
			return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex == 4)
			return true;
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		DBConfig config = list.get(rowIndex);
		if(columnIndex == 0 )
			return Integer.valueOf(config.num);
		else if( columnIndex == 1)
			return config.defect;
		else if( columnIndex == 2 )
			return config.category;
		else if( columnIndex == 3 )
			return config.description;
		else if( columnIndex == 4)
			return Boolean.valueOf(config.scanFlag);
		return null;
	}

	//界面数据有变化时，模型对象的这个方法会被调用
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(columnIndex == 4){
			int num = list.get(rowIndex).num;
			int i = dba.update(aValue, num);
			if(i != 0)
				list.get(rowIndex).scanFlag  = Boolean.parseBoolean(aValue.toString());
		}
	}

	public void addTableModelListener(TableModelListener l) {
	}

	public void removeTableModelListener(TableModelListener l) {
	}

}