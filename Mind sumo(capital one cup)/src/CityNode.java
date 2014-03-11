

/**
 * This class holds all the data  for each city.
 * the name of the city the state and the population
 * in each year is stored in this class.
 * 
 * @author  Sohil Shah
 * 
 */
public class CityNode implements Comparable<CityNode> {
	String city;
	String state;
	int population_in_2010;
	int population_in_2011;
	int population_in_2012;

	public CityNode(String _city, String _state, int pop_2010, int pop_2011,
			int pop_2012) {
		city = _city;
		state = _state;
		population_in_2010 = pop_2010;
		population_in_2011 = pop_2011;
		population_in_2012 = pop_2012;
	}

	/**
	 * 
	 */
	public CityNode() {
		// TODO Auto-generated constructor stub
	}

	public int getPopulation2010() {
		return population_in_2010;
	}

	public String toString() {
		return "City : " + city + "   State : " + state + " Population in 2010 :  "
				+population_in_2010 +"     2011 : "+population_in_2011 +"      2012 : "
				+ population_in_2012 + " ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CityNode o) {
		System.out.println("Hello");
		if (this.population_in_2012 <= 0)
			return -1;
		if (o.population_in_2012 <= 0)
			return 1;
		int this_diff = this.population_in_2010 - this.population_in_2012;
		int that_diff = o.population_in_2010 - o.population_in_2012;
		double this_pc_growth = ((double)this_diff / this.population_in_2012) * 100;
		double that_pc_growth = ((double)that_diff / o.population_in_2012) * 100;

		if(this_pc_growth < that_pc_growth) return -1;
		else if(this_pc_growth == that_pc_growth ) return 0;
		else return 1;

	}
}
