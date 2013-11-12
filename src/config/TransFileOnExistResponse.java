package config;

import java.io.IOException;

public class TransFileOnExistResponse implements Command{

	private static final long serialVersionUID = 1L;
	
	private boolean fileHasExist = false;
	
	private byte[] transFile = null;
	
	public TransFileOnExistResponse(){
	}
	
	public TransFileOnExistResponse(boolean isFileExist){
		this.fileHasExist = isFileExist;
	}
	
	public byte[] getTransFile(){
		return transFile;
	}
	
	public void setTransFile(byte[] tfile){
		this.transFile = tfile;
	}
	
	public void setFileExist(boolean isFileExist){
		this.fileHasExist = isFileExist;
	}
	
	public boolean isFileExist(){
		return fileHasExist;
	}
	
	private void writeObject(java.io.ObjectOutputStream s) throws IOException{
		s.defaultWriteObject();
		if( fileHasExist == false) return;
		s.writeInt(transFile.length);
		s.write(transFile);
	}
	
	private void readObject(java.io.ObjectInputStream s) throws ClassNotFoundException, IOException{
		s.defaultReadObject();
		if( fileHasExist == false) return;
		int size = s.readInt();
		transFile = new byte[size];
		s.read(transFile);
	}
}
