public class Land{
	private static int xDim;
	private static int yDim;
	private static float[][] sunMatrix;

	private static int BLOCK_SIZE;
	private static int numBlocks_X;
	private static int numBlocks_Y;
	private static block[][] shadeBlocks;

	Land(int dx, int dy) {
		xDim = dx;
		yDim = dy;
		BLOCK_SIZE = constants.BLOCK_SIZE; 	// maximum size of a block (an NxN Float[][] array)
		numBlocks_X = dx/BLOCK_SIZE + 1; 	//in case of overflow, E.g. 3000/50 + 1 = 61 blocks along x-axis
		numBlocks_Y = dy/BLOCK_SIZE + 1; 	//in case of overflow, E.g. 3000/50 + 1 = 61 blocks along y-axis

		sunMatrix = new float[dy][dx]; 						// y -> rows, x->columns
		shadeBlocks = new block[numBlocks_Y][numBlocks_X]; 	// matrix of block objects E.g. 61x61 array (3721 blocks in total)
		initBlocks(); 										// initialise block objects
	}

	int getDimX() {
		return xDim;
	}

	int getDimY() {
		return yDim; // incorrect value
	}

	public static int getNumBlocks_X() {
		return numBlocks_X;
	}

	public static int getNumBlocks_Y() {
		return numBlocks_Y;
	}

	public block getBlock(int row, int col){
		return shadeBlocks[row/BLOCK_SIZE][col/BLOCK_SIZE];
	}

	//Set value in sun matrix
	void setFull(int x, int y, float val) {
		sunMatrix[y][x] = val; // y=row and x=col
	}

	// update a value in a block
	public static void setBlockValue(int col, int row, float val){
		int block_X = col/BLOCK_SIZE;
		int block_Y = row/BLOCK_SIZE;
		int block_col = col%BLOCK_SIZE;
		int block_row = row%BLOCK_SIZE;
		shadeBlocks[block_Y][block_X].setValue(block_row, block_col, val); //
	}

	public float getBlockValue(int row, int col){
		return getBlock(row, col).getValue(row%BLOCK_SIZE, col%BLOCK_SIZE);
	}
	private void initBlocks(){
		for(int row=0; row<numBlocks_Y; row++){
			for(int col=0; col<numBlocks_X; col++){
				int blockNum = row*numBlocks_X + col; 					// Unique block ID (for locking -> thread safety)
				shadeBlocks[row][col] = new block(BLOCK_SIZE, blockNum);
			}
		}
	}

	// Reset the shaded landscape (blocks) to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	void resetShade() {
		for(int row=0; row<yDim; row++){
			for (int col=0; col<xDim; col++){
				setBlockValue(col, row, sunMatrix[row][col]);
			}
		}
	}
}