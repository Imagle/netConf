package config.socket;
import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.Socket;


public class TranFile {
	
	private String filePath = null;
	private Socket socket = null;
	public TranFile(Socket socket,String path) {
		this.filePath = path;
		this.socket = socket;

	}
	public void doTranfer(int type) {
		switch (type) {//，type = 1是服务器向客户端发文件，type=2是客户端向服务器提交包
		case 1:
			sendFile(filePath);			
			break;
		case 2: 
			recvFile(filePath);
			break;

		default:
			break;
		}
	}
	private void sendFile(String path) {
		int line = -1;		
		byte[] buffer = new byte[4096];
	
		try {
			//in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			DataOutputStream sout = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			//fins = new FileInputStream(path);
			FileInputStream fins = new FileInputStream(new File(path));
			line = fins.read(buffer);
			
			while(line != -1){
				sout.write(buffer,0,line);
				
				line = fins.read(buffer);
				
			}
			sout.flush();
			//in.close();
			
			sout.close();
			fins.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void recvFile(String path) {
		File file = new File(path);
		
		int line = -1;		
		byte[] buffer = new byte[4096];
		
		
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			FileOutputStream fileOut = new FileOutputStream(new File(path));
			line = in.read(buffer);
			while(line != -1){
				fileOut.write(buffer,0,line);
				line = in.read(buffer);
				
			}
			//in.reset();
			in.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
