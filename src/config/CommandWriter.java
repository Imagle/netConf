package config;

import java.io.IOException;

public interface CommandWriter {
	public abstract void write(Command cmd) throws IOException;
	public abstract boolean tryClose();
}
