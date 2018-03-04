package homeworkAndPractice;
import homework_4.*;

public class Practice3_1 {
//The first hacker practice is about LU Decomposition Matrix Solver, and the output of this practice is as following:
//	0.2915360501567396 
//	2.6300940438871456 
//	-1.7194357366771147 
//	0.2 
//	-0.1839080459770113 
//	The second norm of A*x-b is: 3.66205343881779E-15
//The second hacker practice is about the first norm and infinite norm of a matrix, and the results are as following:
//  The upper bound of first norm of A:
//	Sparse-matrix format: 24.0
//	Full-matrix format: 24.0
//	The upper bound of infinite norm of A:
//	Sparse-matrix format: 24.0
//	Full-matrix format: 24.0
	public static void main(String[] args) {
		// The forth Hacker Practice on Note 4: LU Decomposition Matrix Solver
		double[][] aValue= {{1,2,0,0,3}, {4,5,6,0,0}, {0,7,8,0,9}, {0,0,0,10,0}, {11,0,0,0,12}};
		FullMatrix a= new FullMatrix(aValue);
		double[] bValue= {5,4,3,2,1};
		Vector b= new Vector(bValue);
		Vector x= LUMatrixSolver(a, b);
		// check the correctness by computing the second norm
		Vector ax= a.productAx(x);
		double[] axValue= ax.getValue();
		double[] xV= x.getValue();
		System.out.println("The results of the matrix solver are as following:");
		for(Double d: xV) {
			System.out.println(d+" ");
		}
		System.out.println("The second norm of A*x-b is: "+ computeSecondNorm(a.productAx(x),b));
		
		//The fifth Hacker Practice on Note 4: Compute the upper bounds of first norm and infinite norm
		double[] sValue= {1,2,3,4,5,6,7,8,9,10,11,12};
		int[] sRPtr= {0,3,6,9,10,12};
		int[] sCInd= {0,1,4,0,1,2,1,2,4,3,0,4};
		SparseMatrix sa= new SparseMatrix(sValue, sRPtr, sCInd);
		
		System.out.println("The upper bound of first norm of A:");
		System.out.println("Sparse-matrix format: "+ upBoundFirNormSM(sa));
		System.out.println("Full-matrix format: "+ upBoundFirNormFM(a));
		System.out.println("The upper bound of infinite norm of A:");
		System.out.println("Sparse-matrix format: "+ upBoundInfNormSM(sa));
		System.out.println("Full-matrix format: "+ upBoundInfNormFM(a));

	}
	//Minimal fill_in algorithm to choose the pivot
	public static int minFillIn(FullMatrix A) {
		//The initial value of pivot is 0
		int pivot= 0;
		int fillIn=2147483647;
		//Check different pivots using row permutation
		for(int i=0; i< A.getRowNum(); i++) {
			//If the first element of this row is 0, it cannot be the pivot
			if(A.getValue()[i][0]==0) continue;
			int count=0;
			for(int j=0; j<A.getRowNum();j++) {
				//If the first element of this row is 0, the sparse structure of this row will not change during LU decomposition 
				if(A.getValue()[j][0]==0) continue;
				for(int k=1; k<A.getColNum(); k++) {
					if(A.getValue()[j][k]==0&&A.getValue()[i][k]!=0) count++;
				}
			}
			//Choose the pivot causing minimal fill_ins
			if(count<fillIn) {
				fillIn= count;
				pivot=i;
			} 
		}
		
		return pivot;
	}
	
	public static FullMatrix LUDecomposition(FullMatrix A, int level) {
		
		double[][] aValue= A.getValue();
		//Initialize the matrix M as a unit diagonal matrix
		double[][] mValue= new double[aValue.length][aValue[0].length];
		for(int i=0; i<mValue.length; i++) {
			for(int j=0; j<mValue[0].length; j++) {
				if(j==i) mValue[i][j]=1;
				else mValue[i][j]=0;
			}
		}
		for(int i=level; i<mValue.length; i++) {
			mValue[i][level-1]= -aValue[i][level-1]/aValue[level-1][level-1];
		}
		FullMatrix m= new FullMatrix(mValue);
		return m;
	}
	public static Vector backwardSubstitution(FullMatrix a, Vector b) {
		if(b.getLength()!=a.getRowNum()) return new Vector();
		double[] xValue= new double[b.getLength()];
		double[] bValue= b.getValue();
		double[][] aValue= a.getValue();
		for(int i= a.getRowNum()-1; i>=0; i--) {
			xValue[i]= bValue[i]/aValue[i][i];
			for(int j=i-1; j>=0; j--) {
				bValue[j]= bValue[j]- xValue[i]*aValue[j][i];
			}
		}
		return new Vector(xValue);
	}
	public static Vector LUMatrixSolver(FullMatrix a, Vector b) {
		int pivot= minFillIn(a);
		a.rowPermute(pivot+1, 1);
		b.rowPermute(pivot+1, 1);
		//U=M4*M3*M2*M1*A
		FullMatrix u=a;
		Vector b2=b;
		for(int i=1; i<a.getColNum(); i++) {
			FullMatrix mi= LUDecomposition(u, i);
			u= mi.productAB(u);
			b2= mi.productAx(b2);
		}
		Vector x= backwardSubstitution(u, b2);
		a.rowPermute(pivot+1, 1);
		b.rowPermute(pivot+1, 1);
		return x;
	}
	public static double computeSecondNorm(Vector v1, Vector v2) {
		if(v1.getLength()!=v2.getLength()) return -1;
		double res=0;
		for(int i=0; i<v1.getLength(); i++) {
			res=res+Math.pow(v1.getValue()[i]-v2.getValue()[i], 2);
		}
		res= Math.sqrt(res);
		return res;
	}
	//
	//Compute the upper bound of infinite norm
	public static double upBoundInfNormFM(FullMatrix m) {
		double[][] mValue= m.getValue();
		double res=-1;
		for(int i=0; i< mValue.length; i++) {
			double can=0;
			for(int j=0; j<mValue[0].length; j++) {
				can=can+Math.abs(mValue[i][j]);
			}
			if(can>res) res=can;
		}
		return res;
	}
	public static double upBoundInfNormSM(SparseMatrix m) {
		double res=-1;
		for(int i=1; i<=m.getRowNum(); i++) {
			double can=0;
			for(int j=1; j<=m.getColNum(); j++) {
				can=can+Math.abs(m.retrieveElementRC(i, j));
			}
			if(can>res) res=can;
		}
		return res;
	}
	//Compute the upper bound of first norm
	public static double upBoundFirNormFM(FullMatrix m) {
		double[][] mValue= m.getValue();
		double res=-1;
		for(int i=0; i< mValue[0].length; i++) {
			double can=0;
			for(int j=0; j<mValue.length; j++) {
				can=can+Math.abs(mValue[j][i]);
			}
			if(can>res) res=can;
		}
		return res;
	}
	public static double upBoundFirNormSM(SparseMatrix m) {
		double res=-1;
		for(int i=1; i<= m.getColNum(); i++) {
			double can=0;
			for(int j=1; j<=m.getRowNum(); j++) {
				can=can+Math.abs(m.retrieveElementRC(j, i));
			}
			if(can>res) res=can;
		}
		return res;
	}

}
