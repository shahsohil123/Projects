
/**
 * This class stores all the data for each state.
 * the name of the state and the cumulative additions of 
 * all values in its cities is stored here.
 * 
 * @author Sohil Shah
 *
 */
public class StateNode {
	String state;
	double cummulativeValue;
	
	/**
	 * @param string 	the name of the state.
	 * @param value 	the cummulative value of that state.
	 */
	public StateNode(String string, double value) {
		state=string;
		cummulativeValue=value;
		
	}
	/*
	 * returns the name of the state.
	 */
	public String getState(){
		return state;
	}
	
	/*
	 * returns the string that needs to be printed.
	 * 
	 */
	public String toString(){
		return "State : " + state + " cummulative value : "+ cummulativeValue;
	}
	
	/* returns the cumulative value of the particular state.
	 * 
	 */
	public double getCummulativeValue(){
		return cummulativeValue;
	}
}
