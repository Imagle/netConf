package config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

public abstract class AbstractCommandWriter {

	protected ObjectOutputStream out;
	
	public AbstractCommandWriter(){
		super();
	}
	
	public AbstractCommandWriter(ObjectOutputStream out){
		this.out = out;
	}
	
	public OutputStream getOutputStream(){
		return this.out;
	}
	
	public void setOutputStream(OutputStream out) throws IOException{
		out = new ObjectOutputStream(out);
	}
	
	public void setOutputStream(ObjectOutputStream out){
		this.out = out;
	}
	
	public boolean tryClose(){
		try{
			out.close();
			return true;
		}catch(IOException e){
			return false;
		}
		
	}
	
}
