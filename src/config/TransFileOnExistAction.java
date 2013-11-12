package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TransFileOnExistAction implements CommandAction {
	
	public Command doAction(Command cmd){
		TransFileOnExistRequest request = (TransFileOnExistRequest)cmd;
		File file = new File(request.getPath());
		
		TransFileOnExistResponse resp = new TransFileOnExistResponse();
		if(file.exists() && file.isFile() && Integer.MAX_VALUE>file.length()){
			resp.setFileExist(true);
			byte[] buf = new byte[(int)file.length()];
			FileInputStream fis = null;
			try{
				if( (fis=new FileInputStream(file)).read() != buf.length)
					resp.setFileExist(false);
				resp.setTransFile(buf);
			}catch(Exception e){
				resp.setFileExist(false);
			}finally{
				try{
					if( fis!=null)
						fis.close();
				}catch(IOException e){}
			}
		}
		return resp;
	}
}
