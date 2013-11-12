package config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleServer implements Runnable{

	private int port = 8000;
	private boolean running = true;
	protected Map<Class, CommandAction> config = null;
	
	public SimpleServer(){
		config = new HashMap<Class, CommandAction>();
		config.put(TransFileOnExistRequest.class, new TransFileOnExistAction());
	}
	
	public void run(){
		Executor pool = Executors.newFixedThreadPool(5);
		try{
			System.out.println("server started.\n");
			ServerSocket serverSocket = new ServerSocket(port);
			while(running){
				Socket socket = serverSocket.accept();
				pool.execute(new CmdProcessor(socket));
			}
		}catch(IOException e){
		}
	}

	class CmdProcessor implements Runnable{
		private Socket socket;
		public CmdProcessor(Socket socket){
			this.socket = socket;
		}
		public void run(){
			System.out.println("server accepted.\n");
			DefaultCommandReader reader = new DefaultCommandReader();
			DefaultCommandWriter writer = new DefaultCommandWriter();
			try{
				reader.setInputStream(socket.getInputStream());
				writer.setOutputStream(socket.getOutputStream());
				Command req = reader.read();
				CommandAction act = config.get(req.getClass());
				Command rsp = act.doAction(req);
				writer.write(rsp);
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				reader.tryClose();
				writer.tryClose();
			}
		}
	}
	public static void main(String[] args) {
		SimpleServer server = new SimpleServer();
		new Thread(server).start();
	}
}
