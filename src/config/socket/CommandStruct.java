package config.socket;
import java.io.File;


public class CommandStruct implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type;//1:从服务器获取文件，2：向服务器提交文件 ，3：向服务器索要分组列表 ,4:服务端确定得到用户提交的文件
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
