package config;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientProxy {

	private String desHost = "127.0.0.1";
	private int port = 8000;
	
	public Command Send(Command request) throws UnknownHostException, IOException{
		Socket socket = new Socket(desHost, port);
		DefaultCommandReader reader = new DefaultCommandReader();
		DefaultCommandWriter writer = new DefaultCommandWriter();
		try{
			reader.setInputStream(socket.getInputStream());
			writer.setOutputStream(socket.getOutputStream());
			writer.write(request);
			return reader.read();
		}catch(IOException e){
			throw e;
		}finally{
			reader.tryClose();
			writer.tryClose();
		}
	}
}
