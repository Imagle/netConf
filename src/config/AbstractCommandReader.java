package config;

import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public abstract class AbstractCommandReader {

	protected ObjectInputStream in;
	
	public AbstractCommandReader(){
		super();
	}
	
	public AbstractCommandReader(ObjectInputStream in){
		this.in = in;
	}
	
	public InputStream getInputStream(){
		return this.in;
	}
	
	public void setInputStream(InputStream in) throws IOException{
		in = new ObjectInputStream(in);
	}
	
	public void setInputStream(ObjectInputStream in){
		this.in = in;
	}
	
	public boolean tryClose(){
		try{
			in.close();
			return true;
		}catch(IOException e){
			return false;
		}
		
	}
}
