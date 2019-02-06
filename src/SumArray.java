/*
 * @author Jeremy du Plessis
 * The sumArray class  is essentially a thread which inherits
 * from RecursiveTask, used by the fork-join framework, a fork-join
 * pool of threads distributes work to cores.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class SumArray extends RecursiveAction { // returns floating point value (a sum)

    private int lo; // point to start summing in array
    private int hi; // point to end summing in array
    private ArrayList<Tree> treeList;
    private Land sunMap;

    SumArray(int l, int h, ArrayList<Tree> tl, Land s){
        lo = l;
        hi = h;
        treeList = tl;
        this.sunMap = s;
    }

    @Override
    protected void compute(){

        if ((hi - lo) < constants.SEQUENTIAL_CUTOFF) {  // minimum tasks a thread must handle
            sequentialSum();
        }

        else{
            SumArray left = new SumArray(lo,(hi+lo)/2, treeList, sunMap);
            SumArray right= new SumArray((hi+lo)/2, hi, treeList, sunMap);

            left.fork(); // branch off thread
            right.compute(); // main thread does this
            left.join(); // wait until left is done
        }
    }

    private void sequentialSum(){
       for(int i=lo; i<hi; i++) {
           float avg = 0.0f;
           Tree tempTree = treeList.get(i);

           int xCenter = tempTree.getX();
           int yCenter = tempTree.getY();
           int treeExt = Math.round(tempTree.getExt());

           // get the boundaries of a tree on the landscape
           int topRow = Math.max(0, yCenter-treeExt);
           int bottomRow = Math.min(sunMap.getDimY() - 1, yCenter+treeExt);
           int leftCol = Math.max(0, xCenter-treeExt);
           int rightCol = Math.min(sunMap.getDimX()-1, xCenter+treeExt);

           // find out which blocks are occupied by the corners of each tree (and hence all blocks with which the tree intersects)
           block topLeftBlock = sunMap.getBlock(topRow, leftCol);
           block topRightBlock = sunMap.getBlock(topRow, rightCol);
           block bottomLeftBlock = sunMap.getBlock(bottomRow, leftCol);
           block bottomRightBlock = sunMap.getBlock(bottomRow, rightCol);

           block[] occupied_Blocks = {topLeftBlock, topRightBlock, bottomLeftBlock, bottomRightBlock};
           Arrays.sort(occupied_Blocks);

           //unlock blocks in order
            for(int n=0; n<4; n++){
                occupied_Blocks[n].lock.lock();
            }

	   float count = 0.0f;
           for(int row=topRow; row<=bottomRow; row++){
               for(int col=leftCol; col<=rightCol; col++){
                   avg += sunMap.getBlockValue(row, col); //blocks[row/50][col/50].stepValue(col%50, row%50);
		   count++;
               }
           }

           //lock blocks in order
           for(int n=0; n<4; n++){
               occupied_Blocks[n].lock.unlock();
           }

	   avg = avg/count;
           tempTree.sungrow(avg); // feed sunlight to tree
       }
    }
}