import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class treeSimulator {
    //GUI/View Variables
    static int frameX;
    static int frameY;
    static ForestPanel fp;
    static JFrame frame;
    static JLabel yearDisplay;
    static JOptionPane jOptionPane;

    //Model/Simulation Variables
    static long startTime = 0;
    static SunData sundata;
    static simThread sim;
    static mutableBoolean runSim;
    static YearCount yearCount;

    // start timer
    private static void tick(){
        startTime = System.currentTimeMillis();
    }

    // stop timer, return time elapsed in seconds
    private static float tock(){
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    private static void setupGUI(int frameX,int frameY,Tree [] trees) {
        Dimension fsize = new Dimension(800, 840);
        Dimension barSize = new Dimension(800, 40);
        // Frame init and dimensions
        frame = new JFrame("Photosynthesis");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(fsize);
        frame.setSize(800, 800);

        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setPreferredSize(fsize);

        fp = new ForestPanel(trees);
        fp.setPreferredSize(new Dimension(frameX,frameY));
        JScrollPane scrollFrame = new JScrollPane(fp);
        fp.setAutoscrolls(true);
        scrollFrame.setPreferredSize(fsize);
        g.add(scrollFrame);

        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.LINE_AXIS));
        bar.setPreferredSize(barSize);

        JButton Reset = new JButton("Reset");
        JButton Play = new JButton("Play");
        JButton Pause = new JButton("Pause");
        JButton End = new JButton("End");
        yearDisplay = new JLabel();

        Reset.addActionListener(e -> ResetPressed(e));
        Play.addActionListener(e -> PlayPressed(e));
        Pause.addActionListener(e -> PausePressed(e));
        End.addActionListener(e -> EndPressed(e));

        bar.add(yearDisplay);
        bar.add(Box.createRigidArea(new Dimension(100, 0)));
        bar.add(Reset);
        bar.add(Play);
        bar.add(Pause);
        bar.add(End);
        bar.add(Box.createRigidArea(new Dimension( 150, 0)));


        frame.setLocationRelativeTo(null);  // Center window on screen.
        frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.add(bar);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
    }

    public static void main(String[] args) throws InterruptedException {

        sundata = new SunData();
        String[] arg = new String[1]; // temporary
        arg[0] = "data/sample_input.txt";

        // check that number of command line arguments is correct
        if(arg.length != 1)
        {
            System.out.println("Incorrect number of command line arguments. Should have form: java treeGrow.java intputfilename");
            System.exit(0);
        }

        // read in forest and landscape information from file supplied as argument
        System.out.println("Loading Data from file "+arg[0]);
        sundata.readData(arg[0]);
        System.out.println("Data loaded");

        frameX = sundata.sunmap.getDimX();
        frameY = sundata.sunmap.getDimY();
        setupGUI(frameX, frameY, sundata.trees);

        // create and start simulation loop here as separate thread
        runSim = new mutableBoolean();
        yearCount = new YearCount();
        yearDisplay.setText("year "+yearCount.getCount());
    }

    public static void ResetPressed(ActionEvent e){
        System.out.println("RESET (in Event Dispatch Thread?: "+javax.swing.SwingUtilities.isEventDispatchThread()+")");
        if(runSim.getValue()){
            jOptionPane.showMessageDialog(frame, "Please pause the simulation before trying to reset.");
        }
        else{
            yearCount.resetCount();
            yearDisplay.setText("year "+yearCount.getCount());
            sundata.resetTrees();
        }
    }

    public static void PlayPressed(ActionEvent e){
        System.out.println("PLAY (in Event Dispatch Thread?: "+javax.swing.SwingUtilities.isEventDispatchThread()+")");
        runSim.setValue(true);
        sim = new simThread(sundata, yearDisplay, yearCount, runSim);
        sim.start();
    }

    public static void PausePressed(ActionEvent e){
        System.out.println("PUASE (in Event Dispatch Thread?: "+javax.swing.SwingUtilities.isEventDispatchThread()+")");
        runSim.setValue(false);
    }

    public static void EndPressed(ActionEvent e){
        System.out.println("END (in Event Dispatch Thread?: "+javax.swing.SwingUtilities.isEventDispatchThread()+")");
        System.exit(0);
    }

}