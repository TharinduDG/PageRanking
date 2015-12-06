package com.tharindu.mmds.pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRank {

	private static final String FILE_NAME = "web-Google.txt";
//	private static final String FILE_NAME = "test.txt";
	private static final double TELEPORTATION_PROBABILITY = 0.2;
	private static final int POWER_ITERATIONS = 2;
	private static int NUMBER_OF_NODES = 875713;
	
	public static void main(String[] args) throws Exception {
		Map<Integer,List<Integer>> connectionsMap = new HashMap<>();
		Set<Integer> nodesTracker = new HashSet<>();
		
		FileReader in = new FileReader(FILE_NAME);
		BufferedReader br = new BufferedReader(in);
		
		String line = null;
		while((line = br.readLine()) != null){
			String[] splits = line.split("\t");
			
			int x = Integer.parseInt(splits[0]);
			int y = Integer.parseInt(splits[1]);
			
			if(connectionsMap.containsKey(x)){
				connectionsMap.get(x).add(y);
			}else{
				List<Integer> l = new ArrayList<>();
				l.add(y);
				connectionsMap.put(x, l);
			}
			
			nodesTracker.add(x);
			nodesTracker.add(y);
		}
		br.close();
		
		NUMBER_OF_NODES = nodesTracker.size();
		nodesTracker = null;
		System.out.println(NUMBER_OF_NODES + " Nodes Found!");
		// done composing the connnection Matrix
		
		Map<Integer, Map<Integer,Double>> mapM = new HashMap<>();
		
		// A = BM + (1-B)/N
		double oneMinBetaOverN = (1 - TELEPORTATION_PROBABILITY)/NUMBER_OF_NODES;
		
		for(Integer i : connectionsMap.keySet()){
			List<Integer> list = connectionsMap.get(i);
			int degree = list.size();
			double beetaTimesM =  TELEPORTATION_PROBABILITY/degree;
			
			for(Integer j : list){
				 if(mapM.containsKey(j)){
//					 if(mapM.get(i).containsKey(key)){
//						 mapM.get(i).put(key, beetaTimesM + oneMinBetaOverN);
//					 }else{
//						 Map<Integer, Double> m = new HashMap<>();
//						 m.put(key, beetaTimesM + oneMinBetaOverN);
//						 mapM.put(i, m);
//					 }
					 
					 mapM.get(j).put(i, beetaTimesM + oneMinBetaOverN);
					 
				 }else{
					 Map<Integer, Double> m = new HashMap<>();
					 m.put(i, beetaTimesM + oneMinBetaOverN);
					 mapM.put(j, m);
				 }
			}			
		}
		
		System.out.println("Matrix A is ready!");
		connectionsMap = null;
		// Matrix A is composed
		
		double oneOverN = 1.0/NUMBER_OF_NODES;
		
		double[] arrayR = new double[NUMBER_OF_NODES];
		Arrays.fill(arrayR, oneOverN);
		
		double[] newArrayR = new double[NUMBER_OF_NODES];
		
		for (int iteration = 0; iteration < POWER_ITERATIONS; iteration++) {
			// multiplying A and R
			System.out.println("Power Iteration Round " + iteration + 1);
			for (int k = 0; k < arrayR.length; k++) {
				Map<Integer, Double> m = new HashMap<>();
				if (mapM.containsKey(k)) {
					m = mapM.get(k);
				}

				double rank = 0;
				for (int j = 0; j < arrayR.length; j++) {
					double element = 0;
					if (!m.containsKey(j)) {
						element = oneOverN * (1 - TELEPORTATION_PROBABILITY);
					} else {
						element = m.get(j);
					}
					rank += element * arrayR[j];
				}
				newArrayR[k] = rank;
				rank = 0;
			}
			
			arrayR = newArrayR;
			newArrayR = new double[NUMBER_OF_NODES];
		}

		System.out.println("Done");
		
		System.out.println(newArrayR[99]);
	}

}
