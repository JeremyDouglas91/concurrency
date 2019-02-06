public class Tree{
	
	private int xpos;	// x-coordinate of center of tree canopy
	private int ypos;	// y-coorindate of center of tree canopy
	private float ext;	// extent of canopy out in vertical and horizontal from center
	
	private static float growfactor = 1000.0f; // divide average sun exposure by this amount to get growth in extent
	
public Tree(int x, int y, float e){
		xpos=x;
		ypos=y;
		ext=e;
	}
	
	int getX() {
		return xpos;
	}
	
	int getY() {
		return ypos;
	}
	
	float getExt() {
		return ext;
	}

	void setExt(float e) {
		ext = e;
	}
	
	// is the tree extent within the provided range [minr, maxr)
	boolean inrange(float minr, float maxr) {
		return (ext >= minr && ext < maxr);
	}
	
	// grow a tree according to its sun exposure (a tree cannot grow larger than an extent of 20)
	void sungrow(float sun) {
		float update = ext+sun/growfactor;
		if(update <= 20.0f) {
			setExt(update);
		}
	}
}