import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class block implements Comparable<block>{
    volatile float [][] values;
    private int blockNum;
    Lock lock = new ReentrantLock();

    public block(int size, int num){
        values = new float[size][size];
        blockNum = num;
    }

    public void setValue(int y, int x, float val){
        values[y][x] = val;
    }

    synchronized float getValue(int row, int col){
        float temp = values[row][col];
        values[row][col] = temp * 0.1f; // reduce sunlight cell to 10% of their original value
        return temp;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public int compareTo(block other){
        return Integer.compare(this.getBlockNum(), other.getBlockNum());
    }
}
