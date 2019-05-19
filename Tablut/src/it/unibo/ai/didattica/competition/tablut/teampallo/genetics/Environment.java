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
	private Map<String,Parameter> parameter;
	
	public static Environment getInstance() {
		if(instance==null) {
			instance=new Environment();
		}
		
		return instance;
	}
	
	private Environment() {
		this.envW=new HashMap<>();
		this.envB=new HashMap<>();
		this.parameter=new HashMap<>();
		
		/*PESI BIANCHI*/
		envW.put("WHITE_WEIGHT_COUNT_WHITE_PAWNS",4.0);
		envW.put("WHITE_WEIGHT_COUNT_BLACK_PAWNS",3.0);
		envW.put("WHITE_WEIGHT_SINGLE_FREE_WAY_KING",5.65);		//5
		envW.put("WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING",10.0);
		envW.put("WHITE_WEIGHT_KING_ON_STAR",13.0);
		envW.put("WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED",1.0);	//3
		envW.put("WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED",0.5);	//1
		envW.put("WHITE_WEIGHT_KING_OVERHANGED",18.0);
		envW.put("WHITE_WEIGHT_KING_FAVOURITE",5.0);			//9
		envW.put("WHITE_WEIGHT_GUARDS",14.0);
		envW.put("WHITE_WEIGHT_STRATEGY",7.0);
		//envW.put("WHITE_WEIGHT_DIS_POS",4.0);
		//envW.put("WHITE_WEIGHT_KING_ON_THRONE",4.0);
		//envW.put("WHITE_WEIGHT_KING_NEAR_THRONE",1.5);
		//envW.put("WHITE_WEIGHT_BLACK_NEAR_KING",3.0);
		//envW.put("WHITE_WEIGHT_WHITE_NEAR_KING",1.5);
		//envW.put("WHITE_WEIGHT_DIFF_PAWNS",2.0);
				
		
		/*PESI NERI*/
		//envB.put("BLACK_WEIGHT_DIFF_PAWNS",7.0);
		envB.put("BLACK_WEIGHT_COUNT_WHITE_PAWNS",2.5);
		envB.put("BLACK_WEIGHT_COUNT_BLACK_PAWNS",2.5);
		envB.put("BLACK_WEIGHT_BLACK_NEAR_KING",2.5);
		envB.put("BLACK_WEIGHT_FREE_WAY_KING",2.5);
		envB.put("BLACK_WEIGHT_KING_ON_STAR",5.0);
		envB.put("BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED",2.5);
		envB.put("BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED",2.5);
		envB.put("BLACK_WEIGHT_BLACKBARRIER", 2.5);
		
		/*PARAMETRI*/
		parameter.put("WHITE_WEIGHT_COUNT_WHITE_PAWNS",new Parameter("WHITE_WEIGHT_COUNT_WHITE_PAWNS",3,0.5,0,10,true));
		parameter.put("WHITE_WEIGHT_COUNT_BLACK_PAWNS",new Parameter("WHITE_WEIGHT_COUNT_BLACK_PAWNS",1,0.5,0,10,true));
		parameter.put("WHITE_WEIGHT_SINGLE_FREE_WAY_KING",new Parameter("WHITE_WEIGHT_SINGLE_FREE_WAY_KING",5,0.5,0,6.5,true));
		parameter.put("WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING",new Parameter("WHITE_WEIGHT_MULTIPLE_FREE_WAY_KING",10,0.5,0,20,true));
		parameter.put("WHITE_WEIGHT_KING_ON_THRONE",new Parameter("WHITE_WEIGHT_KING_ON_THRONE",4,0.5,0,7,true));
		parameter.put("WHITE_WEIGHT_KING_ON_STAR",new Parameter("WHITE_WEIGHT_KING_ON_STAR",13,0.5,0,20,true));
		parameter.put("WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED",new Parameter("WHITE_WEIGHT_BLACK_PAWNS_OVERHANGED",3,0.5,0,5,true));
		parameter.put("WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED",new Parameter("WHITE_WEIGHT_WHITE_PAWNS_OVERHANGED",1,0.5,0,5,true));
		parameter.put("WHITE_WEIGHT_KING_OVERHANGED",new Parameter("WHITE_WEIGHT_KING_OVERHANGED",18,0.5,10,20,true));
		parameter.put("WHITE_WEIGHT_KING_FAVOURITE",new Parameter("WHITE_WEIGHT_KING_FAVOURITE",5,0.5,0,12,true));
		parameter.put("WHITE_WEIGHT_GUARDS",new Parameter("WHITE_WEIGHT_GUARDS",14,0.5,5,15,true));
		parameter.put("WHITE_WEIGHT_STRATEGY",new Parameter("WHITE_WEIGHT_STRATEGY",10,0.5,5,15,true));
		parameter.put("WHITE_WEIGHT_DIS_POS",new Parameter("WHITE_WEIGHT_DIS_POS",4,0.5,0,8,true));
		//parameter.put("WHITE_WEIGHT_DIFF_PAWNS",new Parameter("WHITE_WEIGHT_DIFF_PAWNS",2.5,0.5,0,5,true));
		//parameter.put("WHITE_WEIGHT_KING_NEAR_THRONE",new Parameter("WHITE_WEIGHT_KING_NEAR_THRONE",1.5,0.5,0,5,true));
		//parameter.put("WHITE_WEIGHT_BLACK_NEAR_KING",new Parameter("WHITE_WEIGHT_BLACK_NEAR_KING",3.0,0.5,0,5,true));
		//parameter.put("WHITE_WEIGHT_WHITE_NEAR_KING",new Parameter("WHITE_WEIGHT_WHITE_NEAR_KING",1.5,0.5,0,5,true));		
		
		//parameter.put("BLACK_WEIGHT_DIFF_PAWNS",new Parameter("BLACK_WEIGHT_DIFF_PAWNS",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_COUNT_WHITE_PAWNS",new Parameter("BLACK_WEIGHT_COUNT_WHITE_PAWNS",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_COUNT_BLACK_PAWNS",new Parameter("BLACK_WEIGHT_COUNT_BLACK_PAWNS",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_BLACK_NEAR_KING",new Parameter("BLACK_WEIGHT_BLACK_NEAR_KING",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_FREE_WAY_KING",new Parameter("BLACK_WEIGHT_SINGLE_FREE_WAY_KING",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_FREE_WAY_KING",new Parameter("BLACK_WEIGHT_MULTIPLE_FREE_WAY_KING",20,0.5,0,45,true));
		parameter.put("BLACK_WEIGHT_KING_ON_STAR",new Parameter("BLACK_WEIGHT_KING_ON_STAR",2.5,0.5,0,10,true));
		parameter.put("BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED",new Parameter("BLACK_WEIGHT_BLACK_PAWNS_OVERHANGED",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED",new Parameter("BLACK_WEIGHT_WHITE_PAWNS_OVERHANGED",2.5,0.5,0,5,true));
		parameter.put("BLACK_WEIGHT_BLACKBARRIER",new Parameter("BLACK_WEIGHT_BLACKBARRIER",2.5,0.5,0,5,true));

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
	
	public Parameter getParameter(String parameterName) {
		if(this.parameter.containsKey(parameterName)) {
			if(this.parameter.get(parameterName)==null) {
				return null;
			}
			
			return this.parameter.get(parameterName);
		}
		
		return null;
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
