import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class simThread extends java.lang.Thread{
    private SunData sundata;        // contains land and trees
    private mutableBoolean runSim;  // whether or not the simulation is running
    private YearCount yearCount;    // current year of the simulation
    private JLabel year;            // label on the GUI

    private ForkJoinPool fjPool = new ForkJoinPool();

    public simThread(SunData s, JLabel y, YearCount count, mutableBoolean b){
        this.sundata = s;
        this.runSim = b;
        this.yearCount = count;
        this.year = y;
    }

    public void run(){
        ArrayList<Tree> treeSelection;
        Land sunMap = sundata.sunmap;
        Tree[] trees = sundata.trees;
        System.out.println("Running Simulation (in Event Dispatch Thread?: "+javax.swing.SwingUtilities.isEventDispatchThread()+")");


        while(runSim.getValue()){ // while the simulation is running: one loop per year

	    try{
                Thread.sleep(1000);     // to ensure the rendering of the view doesn't lag behind the model update
            } catch(InterruptedException e){ }

            float maxEx = 100.0f; // very tall trees still absorb shade even though they do not increase their extent beyond 20
            float minEx = 20.0f;

            treeSelection = getTreeSelection(minEx, maxEx, trees);
            sum(treeSelection, sunMap);

            maxEx = minEx; // begin simulation for trees who's extent may increase (20-0)
            minEx = maxEx-2;

            for(int i=0; i<10; i++){
                treeSelection = getTreeSelection(minEx, maxEx, trees);
                sum(treeSelection, sunMap);
                maxEx = minEx;
                minEx = minEx - 2;
            }

            sundata.sunmap.resetShade();
            year.setText("year "+ yearCount.getCount());
            yearCount.plusCount();
        }
    }

    private ArrayList<Tree> getTreeSelection(float min, float max, Tree[] trees){
        ArrayList<Tree> toReturn = new ArrayList<Tree>();
        for(Tree t : trees) {
            if(t.inrange(min, max)){
                toReturn.add(t);
            }
        }
        return toReturn;
    }

    private void sum(ArrayList<Tree> treeList, Land sunMap){
        fjPool.commonPool().invoke(new SumArray(0, treeList.size(), treeList, sunMap));
    }
}
