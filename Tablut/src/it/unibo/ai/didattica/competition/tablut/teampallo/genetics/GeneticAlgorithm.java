package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.teampallo.util.GameManager;

public class GeneticAlgorithm {
	
	private GameManager game=GameManager.getInstance();
	private Environment env=Environment.getInstance();
	private String[] parametersW;
	private String[] parametersB;
	
	private int MUTATION_WEIGHT=1;
	
	public GeneticAlgorithm() {
		parametersW=env.getVariablesW().keySet().toArray(new String[0]);
		parametersB=(String[])env.getVariablesB().keySet().toArray(new String[0]);
	}
	
	public void mutate(boolean renforce) {
		Random rand=new Random();
		switch(game.getPlayer()){
		case "white":{
			/*SCELGO UN PARAMETRO*/
			String scelta=parametersW[rand.nextInt(parametersW.length)];
			double previous=env.getWeight(scelta);
			
			if(renforce) {
				previous+=MUTATION_WEIGHT;
			}else {
				previous-=MUTATION_WEIGHT;
			}
			
			/*AGGIORNO VALORE*/
			env.setWeight(scelta, previous);
			break;
		}
		
		case "black":{
			/*SCELGO UN PARAMETRO*/
			String scelta=parametersB[rand.nextInt(parametersB.length)];
			double previous=env.getWeight(scelta);

			if(renforce) {
				previous+=MUTATION_WEIGHT;
			}else {
				previous-=MUTATION_WEIGHT;
			}
			
			/*AGGIORNO VALORE*/
			env.setWeight(scelta, previous);
			break;
		}
		
		default:{
			break;
		}
		}
	}

}
