package sve;

import java.io.File;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client {
	//C:/Users/Siki/Desktop/download.txt
	private static String PUTANJA = JOptionPane.showInputDialog("Unesite path") + "/download.txt";

	public Client() throws Exception {

		Scanner sc = new Scanner(System.in);
		
		
		DatagramSocket socket = new DatagramSocket();
		InetAddress ip = InetAddress.getByName("localhost");
		FileWriter fw = new FileWriter(PUTANJA, true);
		
		
		while(true) {
			System.out.println("Download Upload Read Quit");
			
			String msg = sc.nextLine();
			byte[] buffer = msg.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ip, 2018);
			socket.send(packet);
			
			if(msg.equalsIgnoreCase("download")) {
				buffer = new byte[300];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String a = new String(buffer).trim();
				System.out.println(a);
				
				
				String odabir = sc.nextLine();
				System.out.println("Izabrali ste " + odabir);
				packet = new DatagramPacket(odabir.getBytes(), odabir.getBytes().length, ip, 2018);
				socket.send(packet);
				
				//primanje file-a
				while(true) {
					buffer = new byte[512];
					packet = new DatagramPacket(buffer, buffer.length);
					socket.receive(packet);
					String por = new String(buffer).trim();
					if(por.equals("KRAJ")) break;
					fw.write(por);
					String dobro = "Primio";
					packet = new DatagramPacket(dobro.getBytes(), dobro.getBytes().length, ip, 2018);
					socket.send(packet);
				}
				fw.close();
			}else if(msg.equalsIgnoreCase("upload")) {
				String noviFile = JOptionPane.showInputDialog("Path fajla za upload");
				File f = new File(noviFile);
				if(f.exists()) {
					buffer = noviFile.getBytes();
					packet = new DatagramPacket(buffer, buffer.length, ip, 2018);
					socket.send(packet);
					System.out.println("Poslao uspesno!");
				}
			}else if(msg.equalsIgnoreCase("read")) {
				
				
				
				buffer = new byte[300];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String a = new String(buffer).trim();
				System.out.println(a);
				
				String odabir = sc.nextLine();
				System.out.println("Izabrali ste " + odabir);
				packet = new DatagramPacket(odabir.getBytes(), odabir.getBytes().length, ip, 2018);
				socket.send(packet);
				
				
				JFrame frame = new JFrame("Read");
				JTextArea area = new JTextArea();
				JScrollPane pane = new JScrollPane(area);
				pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				frame.setSize(300, 240);
				frame.add(pane);
				
				while(true) {
					buffer = new byte[512];
					packet = new DatagramPacket(buffer, buffer.length);
					socket.receive(packet);
					String por = new String(buffer).trim();
					if(por.equals("KRAJ")) break;
					area.append(por);
					String dobro = "Primio";
					packet = new DatagramPacket(dobro.getBytes(), dobro.getBytes().length, ip, 2018);
					socket.send(packet);
				}
			}else if(msg.equalsIgnoreCase("quit")) {
				String q = "QUIT";
				buffer = q.getBytes();
				packet = new DatagramPacket(buffer, buffer.length, ip, 2018);
				socket.send(packet);
				socket.close();
				sc.close();
				break;
			}
			
			
		}
		
		
		
		
		
		
		
		
		

		socket.close();
	}
	
	
	
	public boolean isEmptyArray(byte [] array){
		for (byte b : array) {
		    if (b != 0) {
		        return false;
		    }
		}
		return true;
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
