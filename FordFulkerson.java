/*
 * 
 * Project 1: Network Flow and Image Segmentation
 * 
 * Advanced Algorithms- Dr. Zhong-Hui Duan
 * 
 * FordFulkerson Class implements Ford Fulkerson Algorithm by starting with a BFS path from source to destination, 
 * finding the minimum flow along the path and continues to augment the flow until there is n further path from source to destination.
 * 
 *   Input: Graph to find the Maxflow using FordFulkerson Algorithm
 *   
 *   Output: maxflow and mincut, resulting the residual network to find the bipartite graph to separate foreground from the background
 *   
 *   
 */

/**
 *
 * @author - Adithya
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class FordFulkerson {
	static HashMap<String, LinkedList<Node>> nLL = new HashMap<String, LinkedList<Node>>();
	static HashMap<String, LinkedList<Node>> nLL1 = new HashMap<String, LinkedList<Node>>();
	static HashMap<String, LinkedList<Node>> bfsCheckGraph = new HashMap<String, LinkedList<Node>>();
	static HashMap<String, LinkedList<Node>> graph = new HashMap<String, LinkedList<Node>>();
	static HashMap<String, LinkedList<Node>> hmf1;
	static HashMap<String, LinkedList<Node>> bfsCheckGraphf1 ;
	static LinkedList<Node> nfl2; 
	static LinkedList<Node> nfl ;
	static LinkedList<Node> bfPath;
	static LinkedList<Node> stPath;
	static LinkedList<Node> stPathT;
	static int rNwCC = 0;
	static int maxFlowGraph = 0;
	static BFS_Impl bi = new BFS_Impl(graph);
	static BFS_Test bt = new BFS_Test();

	/*Test the FordFulkerson functionality by giving an input graph similar to BFS*/
	public static void main(String[] a) throws IOException {
		System.out.println("Ford Fulkerson Algorithm");
		BufferedReader r = new BufferedReader(new FileReader(a[0]));
		BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("File: " + a[0]);
		
		String nw = "";
		int i = 0;
		int n = 0;
		while ((nw = r.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(nw, " ");
			LinkedList<Node> ll = new LinkedList<Node>();
			int noTokens = st.countTokens();
			while (st.hasMoreTokens()) {
				String nd = st.nextToken();
				int wt = Integer.parseInt(st.nextToken());
				ll.add(new Node(nd, wt));
			}
			nLL.put(i + "", ll);
			i++;
			n = i;
		}
		nLL1 = nLL;
		BFS_Impl bi = new BFS_Impl(nLL);
		BFS_Test bt = new BFS_Test();
		System.out.println("Enter Source Node (Starting 0-N)");
		String source = r1.readLine();
		
		long startTime=System.currentTimeMillis();
		Node src = new Node(source);
		LinkedList<Node> bfPath = bi.bfsPath(src);
		bfsCheckGraph = bi.bfsGraph();
		System.out.println("Graph Read: ");
		printMap(nLL);
		//System.out.println("BFS Graph Size: " + bfsCheckGraph.size());
		//printMap(bfsCheckGraph);
		ArrayList<Node> pathNode = new ArrayList<Node>(bfPath);
		ListIterator<Node> it1 = bfPath.listIterator();
		LinkedList<Node> stPath = new LinkedList<Node>();
		System.out.println("Enter Destination Node (Starting 0-N)");
		String t = r1.readLine();
		Node dest = new Node(t);
		Node finalDest = new Node(t);
		stPath.addFirst(dest);
		int l = -1;
		while (it1.hasNext()) {
			Node t1 = (Node) it1.next();
			if (t1.getNodeName().equals(dest.getNodeName())) {
				l = t1.getLevel();
			}
		}
		for (int ind = pathNode.size() - 2; ind >= 0; ind--) {
			Node t1 = (Node) pathNode.get(ind);
			if (t1.getLevel() == l - 1
					&& bt.checkConnected(t1, dest, bfsCheckGraph)) {
				stPath.addFirst(t1);
				dest = t1;
				l = t1.getLevel();
			}
		}
		System.out.println("Shortest Path from Source to Dest is: ");
		printPath(stPath);
		//System.out.println();
		int maxFlowonSTPath = minFlow(stPath, new Node(source), bfsCheckGraph);
		//System.out.println("Max flow allowed on the path is : "
			//	+ maxFlowonSTPath);
		graph = nLL1;
		FordFulkerson.stPath = stPath;
		buildResidualNw(maxFlowonSTPath, stPath, nLL1, src, finalDest);
		long endTime=System.currentTimeMillis();
		
		System.out.println("\nThe time of execution is "+(endTime-startTime)+ " millisecs"); 
		
	}

	/*Determines the maxflow in the path, i.e minimum of all the edge capacities in the graph*/
	public static int minFlow(LinkedList<Node> pt, Node s,
			HashMap<String, LinkedList<Node>> bfsCheckGraph) {
		LinkedList<Node> temp = bfsCheckGraph.get(s.getNodeName());
		ListIterator<Node> it = temp.listIterator();
		int cap = 0;
		try {
			while (it.hasNext()) {
				Node t1 = (Node) it.next();
				if (t1.getNodeName().equals((pt.get(1)).getNodeName())) {
					cap = t1.getCapacity();
					break;
				}
			}
			it = pt.listIterator();
			for (int ci = 1; ci < pt.size() - 1; ci++) {
				int tcap = getCapacity(pt.get(ci), pt.get(ci + 1),
						bfsCheckGraph);
				if (cap > tcap)
					cap = tcap;
			}
			return cap;
		} catch (Exception e) {
			return -1;
		}
	}

	/*Retrieves the edge weigth from Node a to Node b*/
	public static int getCapacity(Node a, Node b) {
		LinkedList<Node> temp = nLL.get(a.getNodeName());
		ListIterator<Node> it = temp.listIterator();
		int cap = 0;
		try {
			while (it.hasNext()) {
				Node t1 = (Node) it.next();
				if (t1.getNodeName().equals(b.getNodeName()))
					cap = t1.getCapacity();
			}
			return cap;
		} catch (Exception e) {
			return cap;
		}
	}

	/*Retrieves the edge weigth from Node a to Node b in the argument graph*/
	public static int getCapacity(Node a, Node b,
			HashMap<String, LinkedList<Node>> nLL) {
		LinkedList<Node> temp = nLL.get(a.getNodeName());
		ListIterator<Node> it = temp.listIterator();
		int cap = 0;
		try {
			while (it.hasNext()) {
				Node t1 = (Node) it.next();
				if (t1.getNodeName().equals(b.getNodeName()))
					cap = t1.getCapacity();
			}
			return cap;
		} catch (Exception e) {
			return cap;
		}
	}

	/*Prints the Graph to the console*/
	public static void printMap(HashMap<String, LinkedList<Node>> hmap) {
		System.out.println("Graph Size: " + hmap.size());
		for (String k : hmap.keySet()) {
			LinkedList<Node> temp = hmap.get(k);
			ListIterator<Node> it = temp.listIterator();
			System.out.println("Node : " + k);
			while (it.hasNext()) {
				Node t1 = (Node) it.next();
				System.out.print(t1.getNodeName() + " ");
				System.out.print(t1.getCapacity() + " ");
			}
			System.out.println("");
		}
	}

	/*Prints the Node list to the console*/
	public static void printNodeList(LinkedList<Node> llist) {
		ListIterator<Node> it = llist.listIterator();
		while (it.hasNext()) {
			Node t1 = (Node) it.next();
			System.out.print(t1.getNodeName() + " ");
			System.out.print(t1.getCapacity() + " ");
		}
	}

	/*Prints the path to the console*/
	public static void printPath(LinkedList<Node> llist) {
		ListIterator<Node> it = llist.listIterator();
		while (it.hasNext()) {
			System.out.print(((Node) it.next()).getNodeName());
			if (it.hasNext())
				System.out.print("--->");
		}
		System.out.println();
	}


	/*Builds the residual network and applies the fordfulkerson algorithm*/
	public static HashMap<String, LinkedList<Node>> buildResidualNw(int maxFlowST, LinkedList<Node> stPath,HashMap<String, LinkedList<Node>> hm, Node src, Node dest) {
		maxFlowGraph += maxFlowST;
		//System.out.println("Path is ");
		//printPath(stPath);
		//System.out.println("MaxFlow on the Path: " + maxFlowST);
		hmf1 = hm;
		for (int ind = 0; ind <= stPath.size() - 2; ind++) {
			Node one = stPath.get(ind);
			Node two = stPath.get(ind + 1);
			// System.out.println("Node 1 : "+ one.getNodeName() +"  Node 2 : "+
			// two.getNodeName());
			nfl = new LinkedList<Node>(hm.get(one.getNodeName()));
			// nl=hm.get(one.getNodeName());
			ListIterator<Node> nli = nfl.listIterator();
			while (nli.hasNext()) {
				Node sec = nli.next();
				if (sec.getNodeName().equals(two.getNodeName())) {
					if (sec.getCapacity() == maxFlowST) {
						nfl.remove(sec);
					} else {
						int updCap = sec.getCapacity() - maxFlowST;
						sec.setCapacity(updCap);
					}
					nfl2 = new LinkedList<Node>(hm.get(sec.getNodeName()));
					// nl2=hm.get(sec.getNodeName());
					ListIterator<Node> nl2i = nfl2.listIterator();
					int flag = -1;
					while (nl2i.hasNext()) {
						if (nl2i.next().getNodeName().equals(one.getNodeName())) {
							flag = 1;
						}
					}
					if (flag == -1) {
						Node nn = new Node(one.getNodeName(), maxFlowST);
						nfl2.add(nn);
						flag = -1;
					}
					hmf1.put(one.getNodeName(), nfl);
					hmf1.put(two.getNodeName(), nfl2);
					break;
				}
			}
		}
		//System.out.println("Updated Graph after Residual Network Build; Iteration : "+ (++rNwCC));
		//printMap(hm1);
		BFS_Impl bi = new BFS_Impl(hmf1);
		BFS_Test bt = new BFS_Test();
		bfPath = bi.bfsPath(src);
		bfsCheckGraphf1 = new HashMap<String, LinkedList<Node>>();
		bfsCheckGraphf1 = bi.bfsGraph();
		stPathT = bt.getBFSPath(bfPath, bfsCheckGraphf1, src,dest);
		ListIterator<Node> stpti = stPathT.listIterator();
		
		if (stpti.hasNext() && stPathT.size()!=1) {
			printPath(stPathT);
			int tempFlow = minFlow(stPathT, src, bfsCheckGraphf1);
			if (tempFlow != -1 && tempFlow!=0 )
			{
				  return buildResidualNw(tempFlow, stPathT, hmf1, src, dest);
			}
			else {
				System.out.println("Max Flow of Graph source : "
								+ src.getNodeName() + " to destination : "
								+ dest.getNodeName() + " is : "
								+ maxFlowGraph);
				return hmf1;
			}
		}
		else {
			System.out.println("No Further Path Exists from source : "
					+ src.getNodeName() + " to destination : "
					+ dest.getNodeName());
			System.out.println("Max Flow of Graph source : "
					+ src.getNodeName() + " to destination : "
					+ dest.getNodeName() + " is : " + maxFlowGraph);
			return hmf1;
		}
	}
	
	/* Called by ImageSegmentation Class for applying FordFulkerson algorithm to the graph generated from the image*/
	public static HashMap<String, LinkedList<Node>> startFordFulkerson(
			HashMap<String, LinkedList<Node>> igraph, Node src, Node dest) {
		LinkedList<Node> nl2;
		LinkedList<Node> nl;
		 HashMap<String, LinkedList<Node>> hm1;
		 HashMap<String, LinkedList<Node>> bfsCheckGraph1;
		bi.setGraphVE(igraph);
		LinkedList<Node> bfPath = bi.bfsPath(src);
		bfsCheckGraph = bi.bfsGraph();
		System.out.println("BFS Graph Size: " + bfsCheckGraph.size());
		printMap(bfsCheckGraph);
		ArrayList<Node> pathNode = new ArrayList<Node>(bfPath);
		ListIterator<Node> it1 = bfPath.listIterator();
		stPath = new LinkedList<Node>();
		Node finalDest = new Node(dest.getNodeName());
		stPath.addFirst(dest);
		int l = -1;
		while (it1.hasNext()) {
			Node t1 = (Node) it1.next();
			if (t1.getNodeName().equals(dest.getNodeName())) {
				l = t1.getLevel();
				break;
			}
		}
		if (l == -1) {
			System.out.println("No Path Exists from source : "
					+ src.getNodeName() + " to destination : "
					+ dest.getNodeName());
			System.out.println("Max Flow of Graph source : "
					+ src.getNodeName() + " to destination : "
					+ dest.getNodeName() + " is : " + maxFlowGraph);
			return graph;
		}
		for (int ind = pathNode.size() - 2; ind >= 0; ind--) {
			Node t1 = (Node) pathNode.get(ind);
			if (t1.getLevel() == l - 1
					&& bt.checkConnected(t1, dest, bfsCheckGraph)) {
				stPath.addFirst(t1);
				dest = t1;
				l = t1.getLevel();
			}
		}
		System.out.println("Shortest Path from Source to Dest is: ");
		printPath(stPath);
		ListIterator<Node> stpti = stPath.listIterator();
		System.out.println();
		int maxFlowonSTPath = minFlow(stPath, src, bfsCheckGraph);
		System.out.println("Max flow allowed on the path is : "
				+ maxFlowonSTPath);
		graph = igraph;

		while (stpti.hasNext() && stPath.size() != 1) {
			maxFlowGraph += maxFlowonSTPath;
			// System.out.println("Path is ");
			// printPath(stPath);
			//System.out.println("MaxFlow on the Path: " + maxFlowonSTPath);
			// hm1 = new HashMap<String, LinkedList<Node>>();
			hm1 = graph;
			for (int ind = 0; ind <= stPath.size() - 2; ind++) {
				Node one = stPath.get(ind);
				Node two = stPath.get(ind + 1);
				nl = new LinkedList<Node>(graph.get(one.getNodeName()));
				// nl=hm.get(one.getNodeName());
				ListIterator<Node> nli = nl.listIterator();
				while (nli.hasNext()) {
					Node sec = nli.next();
					if (sec.getNodeName().equals(two.getNodeName())) {
						if (sec.getCapacity() == maxFlowonSTPath) {
							nl.remove(sec);
						} else {
							int updCap = sec.getCapacity() - maxFlowonSTPath;
							sec.setCapacity(updCap);
						}
						nl2 = new LinkedList<Node>(graph.get(sec.getNodeName()));
						// nl2=hm.get(sec.getNodeName());
						ListIterator<Node> nl2i = nl2.listIterator();
						int updCap = maxFlowonSTPath, flag = -1;
						Node t2 = new Node("abc");
						while (nl2i.hasNext()) {
							t2 = nl2i.next();
							if (t2.getNodeName().equals(one.getNodeName())) {
								
								updCap = t2.getCapacity() + maxFlowonSTPath;
								
								break;
							}
						}
						nl2.remove(t2);
						Node nn = new Node(one.getNodeName(), updCap);
						nl2.add(nn);
						hm1.put(one.getNodeName(), nl);
						hm1.put(two.getNodeName(), nl2);
						break;
					}
				}
			}
			//System.out.println("Residual Network Build; Iteration : "+ (++rNwCC));
			//printMap(hm1);
			bi.setGraphVE(hm1);
			bfPath = bi.bfsPath(src);
			bfsCheckGraph1 = bi.bfsGraph();
			// printMap(bfsCheckGraph1);
			stPathT = bt.getBFSPath(bfPath, bfsCheckGraph1, src, finalDest);
			maxFlowonSTPath = minFlow(stPathT, src, bfsCheckGraph1);

			graph = hm1;
			stPath = stPathT;
			stpti = stPath.listIterator();
		}
		return graph;
	}
}
