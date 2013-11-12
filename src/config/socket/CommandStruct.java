package config.socket;
import java.io.File;


public class CommandStruct implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type;//1:�ӷ�������ȡ�ļ���2����������ύ�ļ� ��3�����������Ҫ�����б� ,4:�����ȷ���õ��û��ύ���ļ�
	//private String path;
	private String groupName;
	private File[][] fileList;
	public CommandStruct(int type,String name) {
		this.type = type;
		//this.path = path;
		this.groupName = name;
		this.fileList = null;
	}
	public File[][] getFiles() {
		return fileList;
	}
	public void  setFiles(File[][] files) {
		this.fileList = files;
	}
	public int  getType() {
		return type;
	}
	public void  setType(int type) {
		this.type = type;
	}
//	public String getPath() {
//		return path;
//	}
//	public void setPath(String path) {
//		this.path = path;
//	}
	public String  getGroupName() {
		return groupName;
	}
	public void setGroupName(String name) {
		groupName = name;
	}
}
