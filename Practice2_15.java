package homeworkAndPractice;

public class Practice2_15 {

	public static void main(String[] args) {
		// The first Hacker Practice of Note 4
		int[] value= {1,2,3,4,5,6,7,8,9,10,11,12};
		int[] rowPtr= {0,3,6,9,10,12};
		int[] colInd= {0,1,4,0,1,2,1,2,4,3,0,4};
		//verification
		
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				System.out.print(retrieveElementRC(value, rowPtr, colInd, i+1, j+1)+"\t");
			}
			System.out.print("\n");
		}
		//The second Hacker Practice of NOte 4
		int[][] matrix= {
				{1,2,0,0,3},{4,5,6,0,0},{0,7,8,0,9},{0,0,0,10,0},{11,0,0,0,12}
		};
		int[] b= {5,4,3,2,1};
		int[] res= productAx(value, rowPtr, colInd, b);
		for(int i=0; i<res.length; i++) {
			System.out.println(res[i]);
		}
		System.out.println(validate(matrix,b,res));
		

	}
	
	//The methods for the first Hacker Practice
	public static int retrieveElementRC(int[] value, int[] rowPtr, int[] colInd, int x, int y) {
		int start= rowPtr[x-1];
		int end= rowPtr[x];
		for (int i=start; i<end; i++) {
			if(y-1==colInd[i]) return value[i];
		}
		
		return 0;
	}
	
	//The methods for the second Hacker Practice
	
	public static int[] productAx(int[] value, int[] rowPtr, int[] colInd, int[] b) {
		int row= rowPtr.length-1;
		int col=0;
		for(int i=0; i<colInd.length; i++) {
			col=Math.max(col, colInd[i]);
		}
		col=col+1;
		int[] res= new int[row];
		for(int i=1; i<=row; i++) {
			for(int j=1; j<=col; j++) {
				res[i-1]=res[i-1]+retrieveElementRC(value, rowPtr, colInd, i, j)*b[j-1];
			}
		}
		return res;
	}
	public static boolean validate(int[][] matrix, int[] b, int[] res) {
		int[] stan= new int[res.length];
		for(int i=0; i< matrix.length; i++) {
			for(int j=0; j< matrix[0].length; j++) {
				stan[i]= stan[i]+matrix[i][j]*b[j];
			}
		}
		for(int i=0; i<res.length; i++) {
			if(stan[i]!=res[i]) return false;
		}
		return true;
	}

}
