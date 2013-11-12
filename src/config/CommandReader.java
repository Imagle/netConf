package config;

import java.io.IOException;

public interface CommandReader {
	
	public abstract Command read() throws IOException;
	public abstract boolean tryClose();

}
