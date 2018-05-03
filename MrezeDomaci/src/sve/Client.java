package sve;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {

	private static String PUTANJA = "C:/Users/Siki/Desktop/p.txt";

	public Client() throws Exception {

		DatagramSocket socket = new DatagramSocket();
		Path path = Paths.get(PUTANJA);
		byte[] buffer = Files.readAllBytes(path);

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 2018);
		socket.send(packet);

		socket.close();
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
