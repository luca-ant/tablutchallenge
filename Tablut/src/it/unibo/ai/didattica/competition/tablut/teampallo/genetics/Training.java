package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

import it.unibo.ai.didattica.competition.tablut.teampallo.client.TeamPalloWhiteTablutClient;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import it.unibo.ai.didattica.competition.tablut.client.TablutRandomBlackClient;
import it.unibo.ai.didattica.competition.tablut.server.Server;

public class Training {

	private static int POPOLATION=2;
	private static double POSITIVE_BEHAVIOUR=75.0;
	
	public static void main(String[] args) {
		
		String daAllenare="white";
		int vinte=0;
		int perse=0;
		int draw=0;
		GeneticAlgorithm alg=new GeneticAlgorithm();
		
		String[] empty=new String[0];
		
		
		
		while(true) {
			for(int i=0;i<POPOLATION;i++) {
				
				Thread server=new Thread() {
					public void run() {
						Server.main(empty);
					}
				};
				
				Thread myclient=new Thread() {
					public void run() {
						try {
							TeamPalloWhiteTablutClient.main(empty);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				
				Thread randomblack=new Thread() {
					public void run() {
						try {
							TablutRandomBlackClient.main(empty);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				};	
				
			server.start();
			myclient.start();
			randomblack.start();
			
			while(server.isAlive()) {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			/*CAPIRE CHI VINCE*/
			String winner=Server.WINNER;
			System.out.println("winner:"+winner);
			
			/*AGGIORNARE STATS*/
			if(winner.compareTo("draw")==0) {
				draw++;
			}
			else if(winner.compareTo(GameManager.getInstance().getPlayer())==0) {
				vinte++;
			}
			else {
				perse++;
			}
			/**/
			}
			
			publishStats(vinte,perse,draw);
			
			if(((double)vinte/POPOLATION)>POSITIVE_BEHAVIOUR) {
				alg.mutate(true,daAllenare);
			}else {
				alg.mutate(false,daAllenare);
			}
			
			vinte=0;
			perse=0;
			draw=0;
		}
	}

	
	private static void publishStats(int vinte, int perse, int draw) {
		try {
			Environment env=Environment.getInstance();
			FileWriter writer=new FileWriter("results.txt",true);
			writer.append("******************************************************************\n");
			writer.append(""+System.currentTimeMillis()+"\n");
			writer.append("-------------------STATS------------------------\n");
			writer.append("\t\tWIN\t\tLOSE\t\tDRAW\n");
			writer.append("\t\t"+vinte+"\t\t"+perse+"\t\t"+draw+"\n");
			writer.append("\nENVIRONMENT:\n");
			writer.append("BLACK:\n");
			for(String s : env.getVariablesB().keySet()) {
				writer.append(s+":"+env.getVariablesB().get(s)+"\n");
			}
			writer.append("WHITE:\n");
			for(String s : env.getVariablesW().keySet()) {
				writer.append(s+":"+env.getVariablesW().get(s)+"\n");
			}
			writer.append("******************************************************************\n\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
