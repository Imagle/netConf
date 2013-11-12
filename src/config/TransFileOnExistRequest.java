package config;

public class TransFileOnExistRequest implements Command{

	private static final long serialVersionUID = 1L;
	
	private String filePath;
	
	public TransFileOnExistRequest(){
	}
	
	public TransFileOnExistRequest(String path){
		this.filePath = path;
	}
	
	public String getPath(){
		return filePath;
	}
	
	public void setPath(String path){
		this.filePath = path;
	}

}
