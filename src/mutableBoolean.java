public class mutableBoolean {
    private static volatile boolean value=false;

    public mutableBoolean(){}

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
