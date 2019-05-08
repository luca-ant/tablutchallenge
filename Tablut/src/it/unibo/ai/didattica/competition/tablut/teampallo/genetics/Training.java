package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

import it.unibo.ai.didattica.competition.tablut.teampallo.client.TeamPalloBlackTablutClient;
import it.unibo.ai.didattica.competition.tablut.teampallo.client.TeamPalloWhiteTablutClient;
import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import it.unibo.ai.didattica.competition.tablut.client.TablutHumanWhiteClient;
import it.unibo.ai.didattica.competition.tablut.client.TablutRandomBlackClient;
import it.unibo.ai.didattica.competition.tablut.server.Server;

public class Training {

	private static int POPOLATION=10;
	private static double POSITIVE_BEHAVIOUR=0.75;
	
	private static AtomicInteger win=new AtomicInteger(0);
	private static AtomicInteger lose=new AtomicInteger(0);
	private static AtomicInteger draw=new AtomicInteger(0);
	
	private static Thread[] games=new Thread[POPOLATION];
	private static String[] ports=new String[POPOLATION*2];
	
	private static LocalDateTime inizio;
	
	public static void main(String[] args) {
		
		String daAllenare="white";
		GeneticAlgorithm alg=new GeneticAlgorithm();
		
		String[] empty=new String[0];
		
		int start=5000;
		for(int i=0;i<POPOLATION*2;i++) {
			ports[i]=""+(start+i);
		}
		
		while(true) {
			inizio=LocalDateTime.now();
			System.out.println(inizio);
			for(int i=0;i<POPOLATION;i++) {
				
				final String serverPortW=ports[i*2];
				final String serverPortB=ports[(i*2)+1];
				
				System.out.println("[TRAINING]: Creo partita "+(i+1));
				games[i]=new Thread(){
					public void run() {
						Thread server=new Thread() {
							public void run() {
								Server.main(new String[] {serverPortW,serverPortB});
							}
						};
						
						Thread myclient=new Thread() {
							public void run() {
								try {
									TeamPalloWhiteTablutClient.main(new String[] {serverPortW});
									//TablutHumanWhiteClient.main(empty);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						
						Thread randomblack=new Thread() {
							public void run() {
								try {
									//TeamPalloBlackTablutClient.main(new String[] {serverPortB});
									TablutRandomBlackClient.main(new String[] {serverPortB});
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
						};	
						
						server.start();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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
							draw.incrementAndGet();
						}
						else if(winner.compareTo(daAllenare)==0) {
							win.incrementAndGet();
						}
						else {
							lose.incrementAndGet();
						}
						/**/
						
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}
			
			/*avvio i thread*/
			for(int i=0;i<POPOLATION;i++) {
				games[i].start();
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("[TRAINING]: Partite avviate");
			
			
			for(int i=0;i<POPOLATION;i++) {
				while(games[i].isAlive()) {
					try {
						System.out.println("[TRAINING]: Partita "+(i+1)+" ancora in corso...");
						Thread.sleep(60000);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("[TRAINING]: Partite finite");
			
			publishStats(win.get(),lose.get(),draw.get());
			
			if(((double)win.get()/POPOLATION)>POSITIVE_BEHAVIOUR) {
				//alg.mutate(true,daAllenare);
			}else {
				alg.mutate(true,daAllenare);
				alg.mutate(false,daAllenare);
			}
			
			win.set(0);
			lose.set(0);
			draw.set(0);
		}
	}

	
	private static void publishStats(int vinte, int perse, int draw) {
		try {
			Environment env=Environment.getInstance();
			FileWriter writer=new FileWriter("results.txt",true);
			writer.append("******************************************************************\n");
			writer.append("START: "+inizio+"\n");
			writer.append("-------------------STATS------------------------\n");
			writer.append("\t\tWIN\t\tLOSE\t\tDRAW\n");
			writer.append("\t\t"+vinte+"\t\t"+perse+"\t\t\t"+draw+"\n");
			writer.append("\nENVIRONMENT:\n");
			writer.append("BLACK:\n");
			for(String s : env.getVariablesB().keySet()) {
				writer.append(s+":"+env.getVariablesB().get(s)+"\n");
			}
			writer.append("WHITE:\n");
			for(String s : env.getVariablesW().keySet()) {
				writer.append(s+":"+env.getVariablesW().get(s)+"\n");
			}
			writer.append("END: "+LocalDateTime.now()+"\n");
			writer.append("******************************************************************\n\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
