package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	/*
	 * Singleton
	 */
	
	private static Environment instance;
	
	private Map<String,Double> envW;
	private Map<String,Double> envB;
	
	public static Environment getInstance() {
		if(instance==null) {
			instance=new Environment();
		}
		
		return instance;
	}
	
	private Environment() {
		this.envW=new HashMap<>();
		this.envB=new HashMap<>();
		
		/*PESI BIANCHI*/
		envW.put("WHITE_WEIGHT_DIFF_PAWNS",2.0);
		envW.put("WHITE_WEIGHT_COUNT_WHITE_PAWNS",5.0);
		envW.put("WHITE_WEIGHT_COUNT_BLACK_PAWNS",3.0);
		envW.put("WHITE_WEIGHT_BLACK_NEAR_KING",3.0);
		envW.put("WHITE_WEIGHT_WHITE_NEAR_KING",1.5);
		envW.put("WHITE_WEIGHT_FREE_WAY_KING",10.0);
		envW.put("WHITE_WEIGHT_KING_ON_THRONE",2.0);
		envW.put("WHITE_WEIGHT_KING_NEAR_THRONE",1.5);
		envW.put("WHITE_WEIGHT_KING_ON_STAR",10.0);
		envW.put("WHITE_WEIGHT_KING_FROM_BORDER",8.0);
		envW.put("WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED",2.0);
		envW.put("WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED",1.5);
		
		/*PESI NERI*/
		envB.put("BLACK_WEIGHT_DIFF_PAWNS",7.0);
		envB.put("BLACK_WEIGHT_COUNT_WHITE_PAWNS",5.0);
		envB.put("BLACK_WEIGHT_COUNT_BLACK_PAWNS",2.0);
		envB.put("BLACK_WEIGHT_BLACK_NEAR_KING",3.0);
		envB.put("BLACK_WEIGHT_WHITE_NEAR_KING",1.2);
		envB.put("BLACK_WEIGHT_FREE_WAY_KING",7.0);
		envB.put("BLACK_WEIGHT_KING_ON_THRONE",1.5);
		envB.put("BLACK_WEIGHT_KING_NEAR_THRONE",1.2);
		envB.put("BLACK_WEIGHT_KING_ON_STAR",10.0);
		envB.put("BLACK_WEIGHT_KING_FROM_BORDER",0.0);
		envB.put("BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED",1.5);
		envB.put("BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED",2.0);
	}
	
	public double getWeight(String parameterName) {
		if(this.envW.containsKey(parameterName)) {
			if(this.envW.get(parameterName)==null) {
				return 0.0;
			}
			
			return this.envW.get(parameterName);
		}
		if(this.envB.containsKey(parameterName)) {
			if(this.envB.get(parameterName)==null) {
				return 0.0;
			}
			
			return this.envB.get(parameterName);
		}
		
		return 0.0;
	}
	
	public Map<String,Double> getVariablesW(){
		return this.envW;
	}
	
	public Map<String,Double> getVariablesB(){
		return this.envB;
	}
	
	public void setWeight(String parameter,double value) {
		if(this.envW.containsKey(parameter)) {
			this.envW.put(parameter, value);
		}
		if(this.envB.containsKey(parameter)) {
			this.envB.put(parameter, value);
		}
		
	}
}
