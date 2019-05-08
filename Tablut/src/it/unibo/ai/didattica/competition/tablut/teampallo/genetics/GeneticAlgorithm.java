package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

import java.util.Random;

public class GeneticAlgorithm {
	
	private Environment env=Environment.getInstance();
	private String[] parametersW;
	private String[] parametersB;
	
	public GeneticAlgorithm() {
		parametersW=env.getVariablesW().keySet().toArray(new String[0]);
		parametersB=env.getVariablesB().keySet().toArray(new String[0]);
	}
	
	public void mutate(boolean renforce, String daAllenare) {
		Random rand=new Random();
		switch(daAllenare){
		case "white":{
			/*//SCELGO UN PARAMETRO
			String scelta=parametersW[rand.nextInt(parametersW.length)];
			double previous=env.getWeight(scelta);
			
			if(renforce) {
				previous+=MUTATION_WEIGHT;
			}else {
				previous-=MUTATION_WEIGHT;
			}
			
			//AGGIORNO VALORE
			env.setWeight(scelta, previous);*/
			
			//2ND VERSION
			boolean modificato=false;
			String scelta="";
			Parameter previous=null;
			int tentativi=0;
			do {
				scelta=parametersW[rand.nextInt(parametersW.length)];
				previous=env.getParameter(scelta);
			
				modificato=previous.mutate(renforce);
			}while(!modificato && tentativi<=10);
			
			env.setWeight(scelta, previous.getValue());
			
			
			//
			break;
		}
		
		case "black":{
			/*//SCELGO UN PARAMETRO
			String scelta=parametersB[rand.nextInt(parametersB.length)];
			double previous=env.getWeight(scelta);

			if(renforce) {
				previous+=MUTATION_WEIGHT;
			}else {
				previous-=MUTATION_WEIGHT;
			}
			
			//AGGIORNO VALORE
			env.setWeight(scelta, previous);*/
			
			//2ND VERSION
			boolean modificato=false;
			String scelta="";
			Parameter previous=null;
			int tentativi=0;
			do {
				scelta=parametersB[rand.nextInt(parametersB.length)];
				previous=env.getParameter(scelta);
			
				modificato=previous.mutate(renforce);
				tentativi++;
			}while(!modificato && tentativi<=10);
			
			env.setWeight(scelta, previous.getValue());
			//
			break;
		}
		
		default:{
			break;
		}
		}
	}

}
