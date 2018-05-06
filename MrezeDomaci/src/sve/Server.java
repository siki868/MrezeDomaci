package sve;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Server {

	
	private ArrayList<String> fajlovi = new ArrayList<>();

	public Server() throws Exception {

		
		
		
		
		
		DatagramSocket socket = new DatagramSocket(2018);
		
		
		
		while(true) {
			
			//prima opciju
			byte[] buffer = new byte[500];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			InetAddress sender = packet.getAddress();
			int portSendera = packet.getPort();
			String op = new String(buffer).trim();
			
			System.out.println("Klijent " + sender + " je izabrao " + op);
			
			
			//skidanje
			if(op.equalsIgnoreCase("download") || op.equalsIgnoreCase("read")) {
				//salje opcije svih fajlova koji mogu da se skinu
				String opcije = "";
				for(String f: fajlovi) {
					File file = new File(f);
					opcije += file.getName() + "\n";
				}
				buffer = opcije.getBytes();
				packet = new DatagramPacket(buffer, buffer.length, sender, portSendera);
				socket.send(packet);
				
				//prima odabir od klijenta
				buffer = new byte[100];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String odabir = new String(buffer).trim();
				for(String s: fajlovi) {
					if(s.contains(odabir)) {
						odabir = s;
					}
				}
				//slanje fajla
				Path path = Paths.get(odabir);
				byte[] data = Files.readAllBytes(path);
				byte[] chunk = new byte[512];			//deo fajla od 512b koji se salje klijentu u vidu paketa
				if(data.length < 512) {
					packet = new DatagramPacket(data, data.length, sender, portSendera);
					socket.send(packet);
				}else {
					int j = 0;
					for(int i = 0; i < data.length; i++) {
						if((i % 511 == 0 && i != 0) || i == data.length-1) {
							if(i == data.length -1) {
								chunk[i%511] = data[i];
							}
							packet = new DatagramPacket(chunk, chunk.length, sender, portSendera);
							socket.send(packet);
							
							packet = new DatagramPacket(buffer, buffer.length);
							socket.receive(packet);
							String odg = new String(buffer).trim();
							if(!odg.equals("Primio")) {
								 i-=512;
								 if(i < 0) i = 0;
								 continue;
							}
							
							chunk = new byte[512];
							j = 0;
						}
						chunk[j++] = data[i];
					}
				}
				String kraj = "KRAJ";
				buffer = kraj.getBytes();
				packet = new DatagramPacket(buffer, buffer.length, sender, portSendera);
				socket.send(packet);
			}else if(op.equalsIgnoreCase("upload")) {
				buffer = new byte[100];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String noviPath = new String(buffer).trim();
				File f = new File(noviPath);
				System.out.println("Dodat novi file: " + f.getName());
				fajlovi.add(noviPath);
			}else if(op.equalsIgnoreCase("QUIT")) {
				socket.close();
				break;
			}
		}
		
		
		
		
		
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
