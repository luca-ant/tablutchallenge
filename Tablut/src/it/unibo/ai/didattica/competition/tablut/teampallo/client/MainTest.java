package it.unibo.ai.didattica.competition.tablut.teampallo.client;

import java.io.IOException;
import java.net.ServerSocket;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServerSocket socketWhite1;
		try {
			socketWhite1 = new ServerSocket(5000);	socketWhite1.setReuseAddress(true);
			ServerSocket socketWhite2 = new ServerSocket(5001);	socketWhite2.setReuseAddress(true);
			ServerSocket socketWhite3 = new ServerSocket(5002);	socketWhite3.setReuseAddress(true);
			ServerSocket socketWhite4 = new ServerSocket(5003);	socketWhite4.setReuseAddress(true);
			ServerSocket socketWhite5 = new ServerSocket(5004);	socketWhite5.setReuseAddress(true);
			ServerSocket socketWhite6 = new ServerSocket(5005);	socketWhite6.setReuseAddress(true);
			ServerSocket socketWhite7 = new ServerSocket(5006);	socketWhite7.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


	}

}
