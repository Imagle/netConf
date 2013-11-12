package config;

import java.io.IOException;

public class DefaultCommandWriter extends AbstractCommandWriter implements
		CommandWriter {

	public void write(Command cmd) throws IOException {
		out.writeObject(cmd);
		out.flush();
	}

}
