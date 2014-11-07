/*
 * 
 * Project 1: Network Flow and Image Segmentation
 * 
 * Advanced Algorithms- Dr. Zhong-Hui Duan
 * 
 * ImageSegmentation Class implements a functionality to read the input PGM(Portable Gray Map) file and convert the image into a graph,
 * makes use of FordFulkerson and BFS_Test classes to differentiate the foreground pixels from that of background.
 * 
 *  Inputs: Image -PGM of type P2
 *  
 *  Outputs: Image generated as a result of application of FordFulkerson method
 *   
 */

/**
 *
 * @author - Adithya
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class ImageSegmentation {

	static int height = 0, width = 0, maxPixVal;
	public static ArrayList<ArrayList<Integer>> pixelV;
	public static String fileName;
	static int treshold;
	
	public static void main(String a[]) throws IOException {
		BufferedReader imgrdr = new BufferedReader(new FileReader(a[0]));
		pixelV = new ArrayList<ArrayList<Integer>>();
		File filePointer = new File(a[0]);
		fileName = a[0];
		Scanner scanner = new Scanner(filePointer);
		System.out.println(a[0]);

		long startTime=System.currentTimeMillis();
		for (int i = 0; i < 5; i++)
			scanner.next();

		width = Integer.parseInt(scanner.next());
		height = Integer.parseInt(scanner.next());

		maxPixVal = Integer.parseInt(scanner.next());
		String entries = "";
		Node src = new Node("0");
		Node dest = new Node("" + ((height * width) + 1));
		

		for (int im = 0; im < height; im++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int jm = 0; jm < width; jm++) {
				temp.add(Integer.parseInt(scanner.next()));
			}

			pixelV.add(temp);
		}

		printPixVals(pixelV);
		createGraph(pixelV, src, dest, maxPixVal);
		long endTime=System.currentTimeMillis();
		
		System.out.println("\nThe time of execution is "+(endTime-startTime)+ " millisecs"); 
	}

	/*Prints the pixel values read from the input PGM image*/
	public static void printPixVals(ArrayList<ArrayList<Integer>> al) {
		System.out.println("Pixel Values Read from the file are: ");
		for (ArrayList<Integer> a : al) {
			for (Integer p : a)
			{
				treshold+=p;
				System.out.print(p + "\t");
			}
			System.out.println();
		}
	}

	/*Creates the graph from source image, PGM file and calls the fordfulkerson method to run the process of image segmentation*/
	public static void createGraph(ArrayList<ArrayList<Integer>> arl, Node src,
			Node dest, int maxpixval) {
		LinkedList<Node> srcal = new LinkedList<Node>();
		LinkedList<Node> destal = new LinkedList<Node>();
		HashMap<String, LinkedList<Node>> ghm = new HashMap<String, LinkedList<Node>>();
		int nodecount = 1;
		for (int i = 0; i < arl.size(); i++) {
			for (int j = 0; j < arl.get(i).size(); j++) {
				srcal.add(new Node(nodecount + "", arl.get(i).get(j)));
				nodecount++;
			}
		}
		ghm.put(src.getNodeName(), srcal);
		ghm.put(dest.getNodeName(), destal);
		nodecount = 1;
		int scalingFactor=treshold/(height*width);
		System.out.println("Node Numbers are");
		LinkedList<Node> eachPix;
		Node p;
		for (int i = 0; i < arl.size(); i++) {
			for (int j = 0; j < arl.get(i).size(); j++) {
				eachPix = new LinkedList<Node>();
				p = new Node("" + nodecount);
				if (i == 0) {
					if (j == 0) {
						/*corner pixels*/
						eachPix.add(new Node((nodecount + 1) + "", Math.round(1-Math.abs(arl
								.get(i).get(j + 1) - arl.get(i).get(j))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node(dest.getNodeName() + "", Math
								.abs(maxpixval - arl.get(i).get(j))));

					} else if (j == arl.get(i).size() - 1) {
						eachPix.add(new Node(src.getNodeName(), arl.get(i).get(
								j)));
						eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
								.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
					} else {
						/*pixels on image edge*/
						eachPix.add(new Node(src.getNodeName(), arl.get(i).get(
								j)));
						eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
								.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + 1) + "",Math.round(1-Math.abs( Math.abs(arl
								.get(i).get(j + 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node(dest.getNodeName() + "", Math
								.abs(maxpixval - arl.get(i).get(j))));
					}
				} else if (i == arl.size() - 1) {
					if (j == 0) {
						/*corner pixels*/
						eachPix.add(new Node((nodecount - arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + 1) + "", Math.round(1-Math.abs(Math.abs(arl
								.get(i).get(j + 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node(dest.getNodeName() + "", Math
								.abs(maxpixval - arl.get(i).get(j))));
					} else if (j == arl.get(i).size() - 1) {
						eachPix.add(new Node(src.getNodeName(), arl.get(i).get(
								j)));
						eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
								.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount - arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
					} else {
						/*pixels on edge of image*/
						eachPix.add(new Node(src.getNodeName(), arl.get(i).get(
								j)));
						eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
								.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount + 1) + "",Math.round(1-Math.abs( Math.abs(arl
								.get(i).get(j + 1) - arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node((nodecount - arl.get(i).size())
								+ "", Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j)
								- arl.get(i).get(j)))/255)*scalingFactor));
						eachPix.add(new Node(dest.getNodeName() + "", Math
								.abs(maxpixval - arl.get(i).get(j))));
					}
				} else if (j == 0 && i != 0 && i != arl.size() - 1) {
					/*pixels on edge of image*/
					eachPix.add(new Node((nodecount - arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount + 1) + "", Math.round(1-Math.abs(Math.abs(arl
							.get(i).get(j + 1) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount + arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node(dest.getNodeName() + "", Math
							.abs(maxpixval - arl.get(i).get(j))));
				} else if (j == arl.get(i).size() - 1 && i != 0
						&& i != arl.size() - 1) {
					/*pixels on edge of image*/
					eachPix.add(new Node(src.getNodeName(), arl.get(i).get(j)));
					eachPix.add(new Node((nodecount - arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
							.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount + arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
				} else {
					/*all the remaining pixels of image*/
					eachPix.add(new Node(src.getNodeName(), arl.get(i).get(j)));
					eachPix.add(new Node((nodecount - 1) + "", Math.round(1-Math.abs(Math.abs(arl
							.get(i).get(j - 1) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount - arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i - 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount + 1) + "", Math.round(1-Math.abs(Math.abs(arl
							.get(i).get(j + 1) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node((nodecount + arl.get(i).size()) + "",
							Math.round(1-Math.abs(Math.abs(arl.get(i + 1).get(j) - arl.get(i).get(j)))/255)*scalingFactor));
					eachPix.add(new Node(dest.getNodeName() + "", Math
							.abs(maxpixval - arl.get(i).get(j))));
				}
				ghm.put(p.getNodeName(), eachPix);
				nodecount++;
			}
		}
		//FordFulkerson.printMap(ghm);
		HashMap<String, LinkedList<Node>> resNetwork = FordFulkerson
				.startFordFulkerson(ghm, src, dest);

		BFS_Impl bfs = new BFS_Impl(resNetwork);
		LinkedList<Node> srcReach = bfs.bfsPath(src);
		LinkedList<Node> destReach = bfs.bfsPath(dest);
		int imagePix[][]=new int[height][width];
		for(int i=0;i<pixelV.size();i++)
		{
			for(int j=0;j<pixelV.get(i).size();j++)
				imagePix[i][j]=pixelV.get(i).get(j);
		}
		
		for (int i = 0; i < destReach.size(); i++) {
			if (Integer.parseInt(destReach.get(i).getNodeName()) != height
					* width + 1) {
				int ii = (Integer.parseInt(destReach.get(i).getNodeName()) - 1)
						/ width;
				int jj = (Integer.parseInt(destReach.get(i).getNodeName()) - 1)
						% width;
//				System.out.println(ii+"  "+jj);
				imagePix[ii][jj]=0;
			}
		}
		pixelV.clear();
		for (int im = 0; im < height; im++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int jm = 0; jm < width; jm++) {
				if(imagePix[im][jm]>scalingFactor)
				{
	//			System.out.print(imagePix[im][jm]+"    ");
				temp.add(imagePix[im][jm]);
				}
				else
				{
		//			System.out.print(0+"    ");
					temp.add(0);
				}
			}
			System.out.println();
			pixelV.add(temp);
		}
		createOutputPGM(pixelV);
	}

	/*Creates the output PGM image after segmentation has been performed by FordFulkerson*/
	public static void createOutputPGM(ArrayList<ArrayList<Integer>> al) {
		try {
			PrintWriter writer = new PrintWriter(fileName.substring(0,
					fileName.indexOf("."))
					+ "_output" + ".pgm");
			writer.println("P2");
			writer.println("# Created by IrfanView");
			writer.println(width + " " + height);
			writer.println(maxPixVal);
			for (ArrayList<Integer> ali : pixelV) {
				for (Integer i : ali) {
					writer.print(i + " ");
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot create output file");
		}

	}

	/*Prints all the Node Names that could be reached from the input Node*/
	public static void printNodeList(LinkedList<Node> llist) {
		ListIterator<Node> it = llist.listIterator();
		while (it.hasNext()) {
			Node t1 = (Node) it.next();
			System.out.print(t1.getNodeName() + " ");
		}
		System.out.println();
	}
}
