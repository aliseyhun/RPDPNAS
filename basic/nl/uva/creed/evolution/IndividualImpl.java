package nl.uva.creed.evolution;

import nl.uva.creed.evolution.exceptions.InvalidStrategyException;
import nl.uva.creed.evolution.exceptions.UnnasignedFitnessException;
import nl.uva.creed.repeated.RepeatedGameStrategy;

public class IndividualImpl implements Individual {
	
	public static final  double NO_FITNESS = -1;
	private double fitness = NO_FITNESS;
	//additional
	private double payOff = 0;
	private double payOffNoComplexity = 0;
	
	private Strategy strategy;
	

	public IndividualImpl(Strategy strategy) {
		super();
		this.strategy = strategy;
	}
	
	public IndividualImpl(Strategy strategy, double fitness) {
		super();
		this.strategy = strategy;
		this.fitness = fitness;
	}

	public Individual getCopy() {
		try {
			return new IndividualImpl(this.strategy.getCopy());
		} catch (InvalidStrategyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public double getFitness() throws UnnasignedFitnessException {
		if (this.fitness != NO_FITNESS) return fitness;
		else {
			throw new UnnasignedFitnessException("No fitness!");
		}
	}



	public void setFitness(double d) {
	 this.fitness = d;

	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(RepeatedGameStrategy strategy) {
		this.strategy = strategy;
	}

	/**
		 * 
		 * @return 
		 * @author 
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(" ");
			buffer.append("fit = ").append(fitness);
			buffer.append(" str = ").append(strategy);
			buffer.append(" ");
			return buffer.toString();
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((strategy == null) ? 0 : strategy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IndividualImpl other = (IndividualImpl) obj;
		if (strategy == null) {
			if (other.strategy != null)
				return false;
		} else if (!this.strategy.equals(other.strategy)) return false;
		return true;
	}

	public double getPayOff() {
		return payOff;
	}

	public void setPayOff(double payOff) {
		this.payOff = payOff;
	}

	public double getPayOffNoComplexity() {
		return payOffNoComplexity;
	}

	public void setPayOffNoComplexity(double payOffNoComplexity) {
		this.payOffNoComplexity = payOffNoComplexity;
	}



}
