package sve;

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

	private static String PUTANJA = "C:/Users/Siki/Desktop/a.txt";

	public Server() throws Exception {

		DatagramSocket socket = new DatagramSocket(2018);

		byte[] buffer = new byte[1500];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		socket.receive(packet);


		
		String sve = new String(buffer).trim();
		
		PrintWriter pw = new PrintWriter(PUTANJA);
		pw.print(sve);
		pw.close();
		socket.close();

	}


	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
