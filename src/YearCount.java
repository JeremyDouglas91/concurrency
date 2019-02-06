public class YearCount {
    private static volatile int count=0;

    public YearCount(){}

    public String getCount(){
        return Integer.toString(count);
    }

    public void plusCount(){
        count++;
    }

    public void resetCount(){
        count = 0;
    }
}
