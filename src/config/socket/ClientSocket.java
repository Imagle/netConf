package config.socket;
import java.io.File;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
	public final static int PORT = 11111;
	private String iP;
	//public Socket socket;
	private ObjectInputStream in = null;
	private ObjectOutputStream Oout = null;
	public ClientSocket(String IP) {
		this.iP = IP;
		
	}
	//
	public static void main(String[] args){
		//getFilesList();
	//	getConfig("A");
		ClientSocket client = new ClientSocket("127.0.0.1");
		String filepath = null;
		client.saveConfig("A", filepath);
		client.getConfig("A", filepath);
		client.getFilesList();
		
		
	}

	/**
	 * 向服务端添加一个新的的分组
	 * 参数说明：只需给一个新分组的名称，不需要任何路径信息
	 * @param GroupName
	 * @return
	 */
	public boolean AddGroup(String GroupName) {
		//Client client = new Client("127.0.0.1");
		Socket socket = null;
		try {
			socket = new Socket(iP,PORT);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (socket != null) {
		
			CommandStruct command = new CommandStruct(5,GroupName);
			SendCommand(command,socket,null);
			command = recvCommand(socket);
			if(command.getType() == 51)//添加成功
			{
				return true;
			}
			try {
				socket.close();
				socket = null;
				if(Oout!= null){
					//Oout.reset();
					Oout.close();
					Oout = null;
				}
				if(in!=null){
					in.close();
					in = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		try {
//			if (client.Oout != null) {
//				client.Oout.close();
//			}
//			if (client.in != null) {
//				client.in.close();
//			}
//			if(client.socket != null){
//				client.socket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return false;
	}
	/**
	 * 向服务器获取相应组的配置文件
	 * @param GroupName
	 */
	public void getConfig(String GroupName, String savePath) {
		//Client client = new Client("127.0.0.1");
		Socket socket = null;
		try {
			socket = new Socket(iP,PORT);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (socket != null) {
		
			CommandStruct command = new CommandStruct(1,GroupName);
			SendCommand(command,socket, savePath);
			try {
				socket.close();
				socket = null;
				if(Oout!= null){
					//Oout.reset();
					Oout.close();
					Oout = null;
				}
				if(in!=null){
					in.close();
					in = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		try {
//			if (client.Oout != null) {
//				client.Oout.close();
//			}
//			if (client.in != null) {
//				client.in.close();
//			}
//			if(client.socket != null){
//				client.socket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * 向服务器提交保存好的文件
	 * @param GroupName
	 */
	public void saveConfig(String GroupName, String filepath) {
		//Client client = new Client("127.0.0.1");
		Socket socket = null;
		try {
			socket = new Socket(iP,PORT);
			
			
			if (socket != null) {
			
				CommandStruct command = new CommandStruct(2,GroupName);
				SendCommand(command,socket, filepath);
				
				socket.close();
				socket = null;
				if(Oout!= null){
					//Oout.reset();
					Oout.close();
					Oout = null;
				}
				if(in!=null){
					in.close();
					in = null;
				}
			}
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}finally{
			System.gc();
		}
		
//		try {
//			if (client.Oout != null) {
//				client.Oout.close();
//			}
//			if (client.in != null) {
//				client.in.close();
//			}
//			if(client.socket != null){
//				client.socket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * 获取服务器端的组列表
	 * @return
	 */
	public File[][] getFilesList() {
		//Client client = new Client("127.0.0.1");
		Socket socket = null;
		CommandStruct command = null;
		try {
			socket = new Socket(iP,PORT);
			
		
		
		if (socket != null) {
			
			command = new CommandStruct(3,null);
			SendCommand(command,socket, null);
			command = recvCommand(socket);
			
			socket.close();
			socket = null;
			if(Oout!= null){
				//Oout.reset();
				Oout.close();
				Oout = null;
			}
			if(in!=null){
				in.close();
				in = null;
			}
		} 
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}finally{
			System.gc();
		}
//			try {
//				if (client.Oout != null) {
//					client.Oout.close();
//				}
//				if (client.in != null) {
//					client.in.close();
//				}
//				if(client.socket != null){
//					client.socket.close();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		
		return command.getFiles();
	}
	
	public   CommandStruct recvCommand(Socket socket){
		
		CommandStruct command = null;
		try {
			in = new ObjectInputStream(socket.getInputStream());
		
			Object obj = null;
		
			obj = in.readObject();
		 
		command = (CommandStruct)obj;
//		if (command != null) {
//			//System.out.println(command.getFiles());
//			for (File file : command.getFiles()) {
//				System.out.println(file.getName());
//			}
//		}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return command;
	}
	public void  SendCommand(CommandStruct command,Socket socket, String filepath) {
		
		try {
			Oout = new ObjectOutputStream(socket.getOutputStream());
			Oout.writeObject(command);
			Oout.flush();
			int type = command.getType();
			if (type == 1 || type == 2) {
				//TranFile tranFile = new TranFile(socket, "F:/Workspaces/eclipse/Test/test/Config.mdb");
				TranFile tranFile = new TranFile(socket, filepath);
				//tranFile.doTranfer();
				if(type == 1){
					tranFile.doTranfer(2);
				}else{
					tranFile.doTranfer(1);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
