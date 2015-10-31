/**
 * 
 */
package test.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

/**
 * @author : Ares
 * @createTime : Jul 14, 2012 9:26:49 PM
 * @version : 1.0
 * @description :
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("192.168.254.110",88);
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
//		dos.writeUTF(new BufferedReader(new InputStreamReader(System.in)).readLine());
		boolean isFirst = true;
		for(int i=0 ;i < 10 ; i++){
			String message = "";
			if(isFirst){
				message = "123_123";
				isFirst = false;
			}else{
				message = "message{"+new Random().nextInt()+"}";
			}
			dos.writeUTF(message);
			System.out.println("Server response me:"+dis.readUTF());
			Thread.sleep(1000 * 60 * 1);
		}
		
		dos.close();
		dis.close();
		socket.close();
	}

}
