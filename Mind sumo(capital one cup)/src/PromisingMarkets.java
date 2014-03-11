import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.text.html.MinimalHTMLWriter;

/**
 * This Program give the value of top 5 state and top 5 cities that have max growth
 * and top 5 cities that have min growth.
 * @author Sohil Shah
 * 
 */
public class PromisingMarkets {
	public static CityNode[] maxGrowth = new CityNode[5];	//stores the values of top 5 cities with max % growth
	public static CityNode[] minGrowth = new CityNode[5];	//stores the vaues of  top 5 cities with least % growth
	public static Map<String, Double> cummulative;			//stores the values of all states and their cummulative growth		

	public static void main(String args[]) {
		int total_entries = 19516;	//total number of entries in the file
		PriorityQueue<CityNode> queue = new PriorityQueue<>(total_entries,
				percentGrowth);		//queue to store data for each city acording to priority specified by the comparator.
		CityNode node;	
		String[] max_states = new String[5];
		double[] max_value = new double[5];
		String[] US_STATES = { "Alabama", "Alaska", "Arizona", "Arkansas",
				"California", "Colorado", "Connecticut", "Delaware",
				"District of Columbia", "Florida", "Georgia", "Hawaii",
				"Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
				"Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan",
				"Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
				"Nevada", "New Hampshire", "New Jersey", "New Mexico",
				"New York", "North Carolina", "North Dakota", "Ohio",
				"Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
				"South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
				"Vermont", "Virginia", "Washington", "West Virginia",
				"Wisconsin", "Wyoming" };

		try {
			String filename = "C:\\Users\\v k shah\\Downloads\\Metropolitan_Populations__2010-2012_ (1).csv";
			String strLine = "";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int lineNumber = 0;
			br.readLine();
			while ((strLine = br.readLine()) != null) {
				lineNumber++;
				String city = extractCity(strLine);
				String state = extractState(strLine);
				int ext_2010 = extractPopulation(strLine, 1);
				int ext_2011 = extractPopulation(strLine, 2);
				int ext_2012 = extractPopulation(strLine, 3);
				node = new CityNode(city, state, ext_2010, ext_2011, ext_2012);
				queue.add(node);
			}
			while (true) {
				CityNode cn = queue.poll();
				if (cn == null)
					break;
				addToLists(cn);
			}
			System.out.println("Max growth : ");
			for (int i = 0; i < 5; i++) {
				
				System.out.println(maxGrowth[i]);

			}
			System.out.println();
			System.out.println("Min Growth : ");
			for (int i = 0; i < 5; i++) {
				
				System.out.println(minGrowth[i]);
			}
			System.out.println();
			System.out.println("Top 5 States :");
			PriorityQueue<StateNode> stateRanking = new PriorityQueue<>(50,
					state_order);
			for (int i = 0; i < US_STATES.length - 1; i++) {
				if (cummulative.get(US_STATES[i]) == null) {
					continue;
				} else {
					double value = cummulative.get(US_STATES[i]);
					stateRanking.add(new StateNode(US_STATES[i], value));
				}
			}

			for (int i = 0; i < 5; i++) {
				StateNode sn = stateRanking.poll();
				System.out.println(sn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param cn
	 */
	private static Boolean addToLists(CityNode cn) {
		if (cn.population_in_2010 > 50000) {
			checkForMax(cn);
			checkForLeast(cn);
		}
		if (cummulative == null) {
			cummulative = new TreeMap();
		}
		if (cummulative.containsKey(cn.state)) {
			double cur_cummulativeValue = calculateCummulative(cn);
			double prev_cummulativeValue = (double) cummulative.get(cn.state);
			double final_cummulativeValue = cur_cummulativeValue
					+ prev_cummulativeValue;
			cummulative.put(cn.state, final_cummulativeValue);
			return true;
		} else {
			cummulative.put(cn.state, (double) calculateCummulative(cn));
			return true;
		}
	}

	/**
	 * @param cn
	 * @return
	 */
	private static double calculateCummulative(CityNode cn) {
		int diff = cn.population_in_2012 - cn.population_in_2010;
		double pc_growth = ((double) diff / cn.population_in_2012) * 100;
		return pc_growth;
	}

	/**
	 * @param cn
	 */
	private static Boolean checkForLeast(CityNode cn) {
		for (int i = 0; i < minGrowth.length; i++) {
			if (minGrowth[i] == null) {
				minGrowth[i] = cn;
				return true;
			}
		}
		return false;
	}

	/**
	 * @param cn
	 */
	private static boolean checkForMax(CityNode cn) {
		if (maxGrowth[0] == null) {
			maxGrowth[0] = cn;
			return true;
		} else {
			for (int i = maxGrowth.length - 1; i > 0; i--) {
				maxGrowth[i] = maxGrowth[i - 1];
			}
			maxGrowth[0] = cn;
			return true;
		}
	}

	public static Comparator<StateNode> state_order = new Comparator<StateNode>() {

		@Override
		public int compare(StateNode o1, StateNode o2) {
			if (o1.getCummulativeValue() < o2.getCummulativeValue())
				return 1;
			else if (o1.getCummulativeValue() == o2.getCummulativeValue())
				return 0;
			else if (o1.getCummulativeValue() > o2.getCummulativeValue())
				return -1;
			return 10;
		}

	};
	public static Comparator<CityNode> percentGrowth = new Comparator<CityNode>() {

		@Override
		public int compare(CityNode o1, CityNode o2) {
			if (o1.population_in_2012 <= 0)
				return -1;
			if (o2.population_in_2012 <= 0)
				return 1;
			int this_diff = o1.population_in_2012 - o1.population_in_2010;
			int that_diff = o2.population_in_2012 - o2.population_in_2010;
			double this_pc_growth = ((double) this_diff / o1.population_in_2012) * 100;
			double that_pc_growth = ((double) that_diff / o2.population_in_2012) * 100;
			if (this_pc_growth < that_pc_growth)
				return -1;
			else if (this_pc_growth == that_pc_growth)
				return 0;
			else
				return 1;
		}

	};

	/**
	 * @param strLine
	 * @return
	 */
	private static int extractPopulation(String strLine, int year_no) {
		char[] inputString = strLine.toCharArray();
		String population = "";
		int count = 0;
		for (int i = 0; i < inputString.length; i++) {
			if (inputString[i] == ',') {
				if (count == year_no) {
					i++;
					while (i < inputString.length && inputString[i] != ',') {
						population = population + inputString[i];
						i++;
					}
					return Integer.parseInt(population);
				} else {
					count++;
				}
			}
		}
		return 0;
	}

	/**
	 * @param strLine
	 * @return
	 */
	private static String extractState(String strLine) {
		char[] inputString = strLine.toCharArray();
		String state = "";
		for (int i = 0; i < inputString.length; i++) {
			if (inputString[i] == ',') {
				i = i + 2;
				while (inputString[i] != '"') {
					state = state + inputString[i];
					i++;
				}
				return state;

			}
		}
		return null;
	}

	/**
	 * @param strLine
	 * @return
	 */
	private static String extractCity(String strLine) {
		char[] inputString = strLine.toCharArray();
		String city = "";
		for (int i = 0; i < inputString.length; i++) {
			if (inputString[i] == '"') {
				i++;
				while (inputString[i] != ',') {
					city = city + inputString[i];
					i++;
				}
				return city;
			}
		}
		return null;
	}
}