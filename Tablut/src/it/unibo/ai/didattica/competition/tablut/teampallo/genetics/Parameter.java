package it.unibo.ai.didattica.competition.tablut.teampallo.genetics;

public class Parameter {
	
	private String name;
	private double value;
	private double step;
	private double limitUp;
	private double limitDown;
	private boolean reinforcePositive;
	
	public Parameter(String name, double initialValue, double step, double limitDown, double limitUp,
			boolean reinforcePositive) {
		super();
		this.name = name;
		this.value = initialValue;
		this.step = limitUp*0.10;
		this.limitUp = limitUp;
		this.limitDown = limitDown;
		this.reinforcePositive = reinforcePositive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public double getLimitUp() {
		return limitUp;
	}

	public void setLimitUp(double limitUp) {
		this.limitUp = limitUp;
	}

	public double getLimitDown() {
		return limitDown;
	}

	public void setLimitDown(double limitDown) {
		this.limitDown = limitDown;
	}

	public boolean isReinforcePositive() {
		return reinforcePositive;
	}

	public void setReinforcePositive(boolean reinforcePositive) {
		this.reinforcePositive = reinforcePositive;
	}
	
	public boolean mutate(boolean positivamente) {
		if(positivamente && reinforcePositive) {
			double temp=this.value+this.step;
			if(temp<=this.limitUp && temp>=this.limitDown) {
				this.value=temp;
				return true;
			}else {
				return false;
			}
		}
		if(positivamente && !reinforcePositive) {
			double temp=this.value-this.step;
			if(temp<=this.limitUp && temp>=this.limitDown) {
				this.value=temp;
				return true;
			}else {
				return false;
			}	
		}
		if(!positivamente && reinforcePositive) {
			double temp=this.value-this.step;
			if(temp<=this.limitUp && temp>=this.limitDown) {
				this.value=temp;
				return true;
			}else {
				return false;
			}
		}
		if(!positivamente && !reinforcePositive) {
			double temp=this.value+this.step;
			if(temp<=this.limitUp && temp>=this.limitDown) {
				this.value=temp;
				return true;
			}else {
				return false;
			}
		}
		
		return false;
	}
	
	
}
