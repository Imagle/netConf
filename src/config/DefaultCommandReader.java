package config;

import java.io.IOException;

public class DefaultCommandReader extends AbstractCommandReader implements CommandReader{

	public Command read() throws IOException {
		try{
			return (Command)in.readObject();
		}catch(ClassNotFoundException e){
			throw new IOException(e.getMessage());
		}
	}
}
