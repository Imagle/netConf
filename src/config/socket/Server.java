package config.socket;


import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.ObjectInputStream;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	int PORT = 11111;
	//public Socket socket;
	public ServerSocket serverSocket;
	private Socket socket;
	
	public Server() {
		try {
			//socket = null;			
			serverSocket = new ServerSocket(PORT);	
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void service() {
		
		//DoService thiService = null;
		try {
			socket = serverSocket.accept();

			new DoService(socket).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void  main(String[] args) {
		Server server = new Server();
		while(true){
			server.service();
		}
	}
}
class DoService extends Thread{
	private Socket socket = null;
	//public boolean isEnd = false;
	public DoService(Socket socket) {
		this.socket = socket;
	}
	private boolean addGroup(String rootPath,String name) {
		
//		for (int i = 0; i < 3; i++) {
//			
//		}
		boolean result = false;
		for (File file : new File(rootPath).listFiles()) {
			String path = rootPath + file.getName()+"/"+name+"/Config.mdb";
			File f = new File(path);
			
			if (!f.exists()) {
				try {
					f.getParentFile().mkdirs();
					f.createNewFile();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String fromPath = rootPath+file.getName()+"/Config.mdb";
			result = copyFile(fromPath, path);
			
		}
		return result;
	}
	private boolean copyFile(String from,String toPath) {
		try {
			FileInputStream fin = new FileInputStream(new File(from));
			FileOutputStream fout = new FileOutputStream(new File(toPath));
			
			byte[] buffer = new byte[4096];
			int line = 0;
			
			line = fin.read(buffer);
			while(line != -1){
				fout.write(buffer, 0, line);
				line = fin.read(buffer);
				
			}
			fin.close();
			fout.close();
			
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
		}finally{
			System.gc();
		}
		
		return true;
	}
	/**
	 * 删除一个分组
	 * 参数说明：GroupName包含路径(绝对路径）
	 * @param GropName
	 * @return
	 */
	private boolean  DelGrop(String GropName) {
		File file = new File(GropName);
		if (file.exists()) {
			return file.delete();
		}
		return true;
	}
	@Override
	public void run() {
		// 接收命令，直到关闭
		//char[] buffer = new char[512];
		ObjectInputStream oin = null;
		ObjectOutputStream oout = null;
		//BufferedReader in = null;
		try {
				
				oin = new ObjectInputStream(socket.getInputStream());
				
				Object obj = oin.readObject();
				
				CommandStruct command = (CommandStruct)obj;
				String filePath = "D:/DTS/Groups/";//+command.getGroupName();
				
				int type = command.getType();
				switch(type){
				case 6:
					oout = new ObjectOutputStream(socket.getOutputStream());
					if(addGroup(filePath, command.getGroupName())){
						command.setType(61);
						oout.writeObject(command);
						oout.flush();
					}else{
						command.setType(62);
						oout.writeObject(command);
						oout.flush();
					}
					break;
				case 5:
					oout = new ObjectOutputStream(socket.getOutputStream());
					if(addGroup(filePath, command.getGroupName())){
						command.setType(51);
						oout.writeObject(command);
						oout.flush();
					}else{
						command.setType(52);
						oout.writeObject(command);
						oout.flush();
					}
					break;
				case 3:
					File file = new File(filePath);
					//File[] fileList = null;
					File[][] fileList = new File[3][];
					if (file.isDirectory()) {
//						fileList = file.listFiles();
						int i = 0;
						for (File f : file.listFiles()) {
							if(f.isDirectory()){
								fileList[i++] = f.listFiles();
							}
						}
					}
					command.setFiles(fileList);
					oout = new ObjectOutputStream(socket.getOutputStream());
					oout.writeObject(command);
					oout.flush();
					break;
				case 1:
				case 2://c->s
					filePath += command.getGroupName()+"/Config.mdb";
					TranFile tranFile = new TranFile(socket,filePath);
					tranFile.doTranfer(type);
//					if (type == 2) {
//						command.setType(4);
//						oout = new ObjectOutputStream(socket.getOutputStream());
//						oout.writeObject(command);
//						oout.flush();
//					}
					break;
				}
				obj = null;
				command = null;
//				oin.close();
//				oout.flush();
//				oout.close();
//				socket.shutdownInput();
//				socket.shutdownOutput();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				System.gc();
//				//isEnd = true;
//				//System.out.println("NININININININIIN");
				try {
					if (oin != null) {
						//oin.reset();
						oin.close();
						oin = null;
					}
					if(oout != null){
						//oout.reset();
						oout.close();
						oout = null;
					}
					if (socket != null) {
						socket.close();
						socket = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
		
	}
	
}