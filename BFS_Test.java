/*
 * 
 * Project 1: Network Flow and Image Segmentation
 * 
 * Advanced Algorithms- Dr. Zhong-Hui Duan
 * 
 * BFS_Test class implements the functionality to find the Breadth First Search of the given input graph, 
 * and helps us find the shortest possible path between the given source and destination.
 * 
 * Inputs: Case 1- sample.txt having the nodes and weights associated with the edges
 * 		   Case 2- graph from FordFulkerson class
 *   
 * Outputs:  Retrieves the BFS traversal and shortest possible path between source s and destination t
 */

/**
 *
 * @author - Adithya
 */
import java.io.*;
import java.util.*;
class BFS_Impl
{
	HashMap<String,LinkedList<Node>> graphVE;
	
	HashMap<String,LinkedList<Node>> bfsG=new HashMap<String, LinkedList<Node>>();
	boolean visi[];
	
	/*BFS_Test Constructor */
	  BFS_Impl(HashMap<String,LinkedList<Node>> hm)
	  {
	    visi=new boolean[hm.keySet().size()];
	    for(int i=0;i<hm.keySet().size();i++)
	    	visi[i]=false;
	    graphVE=hm;
	  }
	  
	  /*Getters and Setter methods for input graph of BFS */
	  public HashMap<String,LinkedList<Node>> bfsGraph()
	  {
		  return bfsG;
	  }
	  
	  public HashMap<String, LinkedList<Node>> getGraphVE() {
		return graphVE;
	}
	public void setGraphVE(HashMap<String, LinkedList<Node>> graphVE) {
	    visi=new boolean[graphVE.keySet().size()];
	    for(int i=0;i<graphVE.keySet().size();i++)
	    	visi[i]=false;
		this.graphVE = graphVE;
		
	}
	
		/* Retrieves the BFS Node Traversal of the given Input graph */
	  public LinkedList<Node> bfsPath(Node src)
	  {
		  LinkedList<Node> bpath=new LinkedList<Node>();
		  LinkedList<Node> vertices=new LinkedList<Node>();
		  vertices.add(src);
		  bpath.add(src);
		  int levelInc=0;
		  src.setLevel(levelInc);
		  levelInc++;
		  while(!bpath.isEmpty())
		  {
			Node head=bpath.poll();
			visi[Integer.parseInt(head.getNodeName())]=true;
			levelInc=head.getLevel()+1;
			LinkedList<Node> temp=graphVE.get(head.getNodeName());
			ListIterator it=temp.listIterator();
			LinkedList<Node> bfsNodeList=new LinkedList<Node>();
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if(visi[Integer.parseInt(t1.getNodeName())]!=true && t1.getCapacity()>0)
	    		{
	    			t1.level=levelInc;
	    			vertices.add(t1);
	    			visi[Integer.parseInt(t1.getNodeName())]=true;
	    			bpath.add(t1);
	    			bfsNodeList.add(t1);
	    		}
	    	}
	    	bfsG.put(head.getNodeName(), bfsNodeList);
		  }
		  return vertices;
	  }
}

/* Node class to represent the Node Data structure which holds the attributes of each node in the node; 
 * nodeName,
 * edgeWeight,
 * nextVertex,
 * levelinBFS_Graph
 *  */
class Node
{
	
	  String NodeName;
	  String Next;
	  int capacity;
	  int level;
  
	 public boolean equals(Node a, Node b)
	{
		if(a.getNodeName().equals(b.getNodeName()))
			return true;
		else
			return false;
	}
  	public String getNodeName() {
		return NodeName;
	}
	public void setNodeName(String nodeName) {
		NodeName = nodeName;
	}
	public String getNext() {
		return Next;
	}
	public void setNext(String next) {
		Next = next;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int weight) {
		capacity = weight;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

  Node(String name,int wt)
  {
  NodeName=name;
  capacity=wt;
  }
  Node(String name)
  {
  NodeName=name;
  }
  
  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Next == null) ? 0 : Next.hashCode());
		result = prime * result
				+ ((NodeName == null) ? 0 : NodeName.hashCode());
		result = prime * result + capacity;
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
		Node other = (Node) obj;
		if (Next == null) {
			if (other.Next != null)
				return false;
		} else if (!Next.equals(other.Next))
			return false;
		if (NodeName == null) {
			if (other.NodeName != null)
				return false;
		} else if (!NodeName.equals(other.NodeName))
			return false;
		
		return true;
	}
  
}

/*BFS_Test class to test the functionality of BFS Module individually */

class BFS_Test
{
	static HashMap<String,LinkedList<Node>> nLL=new HashMap<String,LinkedList<Node>>();
	static HashMap<String,LinkedList<Node>> bfsCheckGraph=new HashMap<String,LinkedList<Node>>();

  public static void main (String[] a) throws IOException
  {
    System.out.println("Breadth First Search");
    
    BufferedReader r= new BufferedReader(new FileReader(a[0]));
    BufferedReader r1= new BufferedReader(new InputStreamReader(System.in));
    System.out.println("File Input : "+a[0]);
    String nw="";
    int i=0;
    int n=0;
    while((nw=r.readLine())!=null)
    {
       	StringTokenizer st=new StringTokenizer(nw," ");
       	LinkedList<Node> ll=new LinkedList<Node>();
       	int noTokens=st.countTokens();
       	while (st.hasMoreTokens())
       	{
       		String nd=st.nextToken();
       		int wt=Integer.parseInt(st.nextToken());
       		ll.add(new Node(nd,wt));
       	}
    	  nLL.put(i+"", ll);
          i++;
      n=i;
    }
      System.out.println("Graph Size: "+nLL.size());
    for(String k: nLL.keySet())
    {
    	LinkedList<Node> temp=nLL.get(k);
    	ListIterator it=temp.listIterator();
    	System.out.println("Node : "+k);
    	while(it.hasNext())
    	{
    		Node t1=(Node)it.next();
    		System.out.print(t1.getNodeName()+" ");
    		System.out.print(t1.getCapacity()+" ");
    	}
    	System.out.println("");
    }
    BFS_Impl bi=new BFS_Impl(nLL);
    System.out.println("Enter Source Node (Starting 0-N)");
    String source=r1.readLine();
    
    long startTime=System.currentTimeMillis();
    LinkedList<Node> bfPath=bi.bfsPath(new Node(source));
    
    ListIterator<Node> it = bfPath.listIterator();
	while (it.hasNext()) {
		System.out.print(((Node) it.next()).getNodeName());
		if (it.hasNext())
			System.out.print("--->");
	}
	System.out.println();

	bfsCheckGraph=bi.bfsGraph();
    
    System.out.println("BFS Graph Size: "+bfsCheckGraph.size());
    for(String k: bfsCheckGraph.keySet())
    {
    	LinkedList<Node> temp=bfsCheckGraph.get(k);
    	it=temp.listIterator();
    	System.out.println("Node : "+k);
    	while(it.hasNext())
    	{
    		Node t1=(Node)it.next();
    		System.out.print(t1.getNodeName()+" ");
    	}
    	System.out.println("");
    }
    ArrayList<Node> pathNode=new ArrayList<Node>(bfPath);
    ListIterator<Node> it1=bfPath.listIterator();
    LinkedList<Node> stPath=new LinkedList<Node>();
	System.out.println("Enter Destination Node (Starting 0-N)");
	String t=r1.readLine();
	Node dest=new Node(t);
	stPath.addFirst(dest);
    int l=-1;
    	while(it1.hasNext())
    	{	
    		Node t1=(Node)it1.next();
    		if(t1.getNodeName().equals(dest.getNodeName()))
    		{
    			l=t1.getLevel();
    		}
    		else
    		{
    			l=-1;
    		}
    	}
    	for(int ind=pathNode.size()-2;ind>=0;ind--)
    	{
    		Node t1=(Node)pathNode.get(ind);
    		if(t1.getLevel()==l-1 && checkConnected(t1,dest))
        		{
        					 stPath.addFirst(t1);
        					 dest=t1;
        					 l=t1.getLevel();
        		}
		}
    	System.out.println("Shortest Path from Source to Dest is: ");
    	ListIterator<Node> sti=stPath.listIterator();
    	while(sti.hasNext())
		 {
		 	System.out.print(sti.next().getNodeName()+"---");
		 }
    	long endTime=System.currentTimeMillis();
    	
    	System.out.println("\nThe time of execution is "+(endTime-startTime)+ " millisecs"); 
    	/*
    	System.out.println("Flow allowed on the path is : "+minFlow(stPath, new Node(source)));
    	*/
  }

  /*Checks if the nodes are connected from A->B, if an edge exists from Node A to Node B  */
	public static boolean checkConnected(Node A, Node B)
	{
		LinkedList<Node> temp=bfsCheckGraph.get(A.getNodeName());
		ListIterator it=temp.listIterator();
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if((t1.getNodeName()).equals(B.getNodeName()))
				{
				return true;
				}
	    	}
		return false;
	}
	
	/*Checks if the nodes are connected from A->B, i.e if an edge exists from Node A to Node B in the argument graph.*/
	public  boolean checkConnected(Node A, Node B, HashMap<String,LinkedList<Node>> bfsCheckGraph)
	{
		LinkedList<Node> temp=bfsCheckGraph.get(A.getNodeName());
		ListIterator it=temp.listIterator();
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if((t1.getNodeName()).equals(B.getNodeName()))
				{
				return true;
				}
	    	}
		return false;
	}
	
	/* Retrieves the max flow allowed on the path from source s to destination/sink t*/
	public static int minFlow(LinkedList<Node> pt, Node s)
	{
		LinkedList<Node> temp=bfsCheckGraph.get(s.getNodeName());
		ListIterator<Node> it=temp.listIterator();
		int cap=0;
		try{
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if(t1.getNodeName().equals((pt.get(1)).getNodeName()))
	    		{
	    			cap=t1.getCapacity();
	    			System.out.println("cap: "+cap);
	    			break;
	    		}
	    	}
	    	it=pt.listIterator();
	    	for(int ci=1;ci<pt.size()-1;ci++)
	    	{
	    		int tcap=getCapacity(pt.get(ci), pt.get(ci+1));
	    		if(cap>tcap)
	    			cap=tcap;
    		}
		return cap;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	/* Retrieves the max flow allowed on the path from source s to destination/sink t in the BFS generated traversal */
	public int minFlow(LinkedList<Node> pt, Node s,HashMap<String,LinkedList<Node>> bfsCheckGraph)
	{
		LinkedList<Node> temp=bfsCheckGraph.get(s.getNodeName());
		ListIterator<Node> it=temp.listIterator();
		int cap=0;
		try{
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if(t1.getNodeName().equals((pt.get(1)).getNodeName()))
	    		{
	    			cap=t1.getCapacity();
	    			System.out.println("cap: "+cap);
	    			break;
	    		}
	    	}
	    	it=pt.listIterator();
	    	for(int ci=1;ci<pt.size()-1;ci++)
	    	{
	    		int tcap=getCapacity(pt.get(ci), pt.get(ci+1),bfsCheckGraph);
	    		if(cap>tcap)
	    			cap=tcap;
    		}
		return cap;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	/*Retreives the capacity of the edge from Node A to Node B*/
	public static int getCapacity(Node a, Node b)
	{
		LinkedList<Node> temp=nLL.get(a.getNodeName());
		ListIterator<Node> it=temp.listIterator();
		int cap=0;
		try{
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if(t1.getNodeName().equals(b.getNodeName()))
	    			cap= t1.getCapacity();
	    	}
	    	return cap;
		}
		catch(Exception e)
		{
			return cap;
		}
	}
	
	/*Retreives the capacity of the edge from Node A to Node B in the argument graph*/
	public  int getCapacity(Node a, Node b,HashMap<String,LinkedList<Node>> bfsCheckGraph)
	{
		LinkedList<Node> temp=bfsCheckGraph.get(a.getNodeName());
		ListIterator<Node> it=temp.listIterator();
		int cap=0;
		try{
	    	while(it.hasNext())
	    	{
	    		Node t1=(Node)it.next();
	    		if(t1.getNodeName().equals(b.getNodeName()))
	    			cap= t1.getCapacity();
	    	}
	    	return cap;
		}
		catch(Exception e)
		{
			return cap;
		}
	}
	
	/*Retrieves the path from Source s to Sink t in the BFS generated traversal of nodes */
	public LinkedList<Node> getBFSPath(LinkedList<Node> ll,HashMap<String, LinkedList<Node>> bfsg,Node s,Node t)
	{
		ArrayList<Node> pathNode=new ArrayList<Node>(ll);
	    ListIterator<Node> it1=ll.listIterator();
	    LinkedList<Node> stPath=new LinkedList<Node>();
		stPath.addFirst(t);
	    int l=-1;
	    	while(it1.hasNext())
	    	{	
	    		Node t1=(Node)it1.next();
	    		if(t1.getNodeName().equals(t.getNodeName()))
	    		{
	    			l=t1.getLevel();
	    		}
	    	}
	    	for(int ind=pathNode.size()-2;ind>=0;ind--)
	    	{
	    		Node t1=(Node)pathNode.get(ind);
	    		if(t1.getLevel()==l-1 && checkConnected(t1,t,bfsg))
	        		{
	        					 stPath.addFirst(t1);
	        					 t=t1;
	        					 l=t1.getLevel();
	        		}
			}
	   return stPath;
	}
}
