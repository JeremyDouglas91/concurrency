import java.io.*;
import java.util.StringTokenizer;

// Trees define a canopy which covers a square area of the landscape
public class SunData{
	
	Land sunmap; // regular grid with average daily sunlight stored at each grid point
	Tree [] trees; // array of individual tress located on the sunmap
	
	void readData(String fileName){ 
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			// load sunmap
			String[] dims = br.readLine().trim().split(" "); 	// Dimensions of landscape
			int dimx = Integer.parseInt(dims[0]); 					// columns => X
			int dimy = Integer.parseInt(dims[1]); 					// rows => Y
			StringTokenizer sunDigits = new StringTokenizer(br.readLine());	// sunlight per square meter

			sunmap = new Land(dimx,dimy);

			for(int x = 0; x < dimx; x++) {
				for (int y = 0; y < dimy; y++) {
					sunmap.setFull(x, y, Float.parseFloat(sunDigits.nextToken())); // y->row, x->column
				}
			}
			// Set shade blocks to full sunlight values
			sunmap.resetShade();
			
			// load forest
			int numt = Integer.parseInt(br.readLine().trim()); // number of trees
			trees = new Tree[numt];

			for(int t=0; t < numt; t++)
			{
				String [] treeData = br.readLine().trim().split(" ");
				int xloc = Integer.parseInt(treeData[0]);
				int yloc = Integer.parseInt(treeData[1]);
				trees[t] = new Tree(xloc, yloc, constants.STARTING_EXTENT);
			}
			br.close();

			//print NB data info
			System.out.println("Dimensions of land (x,y): ("+sunmap.getDimX()+", "+sunmap.getDimY()+")");
			System.out.println("Block size (square): "+constants.BLOCK_SIZE);
			System.out.println("Dimension of block matrix (x,y): ("+sunmap.getNumBlocks_X()+", "+sunmap.getNumBlocks_Y()+")");
			System.out.println("Number of trees: "+numt);

		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
	
	void writeData(String fileName){
		 try{ 
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d\n", trees.length);
			 for(int t=0; t < trees.length; t++)
				 printWriter.printf("%d %d %f\n", trees[t].getX(), trees[t].getY(), trees[t].getExt());
			 printWriter.close();
		 }
		 catch (IOException e){
			 System.out.println("Unable to open output file "+fileName);
				e.printStackTrace();
		 }
	}

	void resetTrees(){
		for(Tree t : trees)
			t.setExt(0.4f);
	}
	
}