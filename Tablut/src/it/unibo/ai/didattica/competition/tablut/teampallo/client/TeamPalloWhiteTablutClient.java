package it.unibo.ai.didattica.competition.tablut.teampallo.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class TeamPalloWhiteTablutClient {


		public static void main(String[] args) throws UnknownHostException, ClassNotFoundException, IOException {
			String[] array = new String[]{"WHITE"};
			if (args.length>0){
				array = new String[]{"WHITE", args[0]};
			}
			TeamPalloTablutClient.main(array);
		}

	

}

