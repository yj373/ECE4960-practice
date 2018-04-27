//Jacobi iterative matrix solver
//This class contains all the functions needed for iterative solver
//For the functions  specialProductAB(SparseMatrix A, SparseMatrix B) and specialProductAX(SparseMatrix A, Vector x), I
//also add them to the SparseMatrix class to improve the reusability of my codes
package A2;
import java.util.ArrayList;
import homework_4.*;

public class Iterative_solver {
	SparseMatrix A;
	Vector b;
	public Iterative_solver(SparseMatrix A_, Vector b_) {
		A=A_;
		b=b_;
	}
	public Iterative_solver() {
		
	}
	//Get D
	public SparseMatrix getDiagonal() {
		int rowN= A.getRowNum();
		int colN= A.getColNum();
		if(rowN!=colN) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rowPtr= new ArrayList<Integer>();
		ArrayList<Integer> colInd= new ArrayList<Integer>();
		rowPtr.add(0);
		for(int i=1;i<=rowN; i++) {
			value.add(A.retrieveElementRC(i, i));
			rowPtr.add(rowPtr.get(i-1)+1);
			colInd.add(i-1);
		}
		return new SparseMatrix(value, rowPtr, colInd, this.A.getColNum());
	}
	
	//Get the inverse of D
	public SparseMatrix inverseD(SparseMatrix D) {
		ArrayList<Double> value= new ArrayList<Double>();
		for(Double d: D.getValue()) {
			value.add(1.0/d);
		}
		return new SparseMatrix(value, D.getRowPtr(), D.getColInd(), this.A.getColNum());
	}
	//Keep iterating until the result converges
	public Vector iterate() {
		Runtime r=Runtime.getRuntime();
		SparseMatrix D= this.getDiagonal();
		SparseMatrix DI= this.inverseD(D);
		Vector x0= specialProductAX(DI,b);
		SparseMatrix step1= D.subtract(A);
		SparseMatrix para= specialProductAB(DI, step1);
		Vector x= para.productAx(x0).add(DI.productAx(b));
		int count=1;
		long m= r.totalMemory()-r.freeMemory();
		//System.out.println("Memory used during iterations:"+m+" bytes");
		while(x.computeSecondNorm(x0)>Math.pow(10, -7)) {
			x0=x;
			x= para.productAx(x0).add(DI.productAx(b));
			count++;
			m= r.totalMemory()-r.freeMemory();
			//System.out.println("Memory used during iterations:"+m+" bytes");
			
		}
		System.out.println("The number of iterations: "+count);
		return x;
		
	}
	//A is a diagonal matrix, compute the product of two matrices A and B
	public static SparseMatrix specialProductAB(SparseMatrix A, SparseMatrix B) {
		if(A.getColNum()!=B.getRowNum()) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rP= new ArrayList<Integer>();
		ArrayList<Integer> cI= new ArrayList<Integer>();
		rP.add(0);
		for(int i=1; i<=A.getRowNum(); i++) {
			int count=0;
			for(int j=1; j<=B.getColNum(); j++) {
				double ele=A.retrieveElementRC(i, i)*B.retrieveElementRC(i, j);
				if(ele!=0) {
					value.add(ele);
					cI.add(j-1);
					count++;
				}
			}
			rP.add(rP.get(i-1)+count);
		}
		return new SparseMatrix(value, rP, cI, B.getColNum());
	}
	//A is a diagonal matrix, compute the product of A and a vectorX
	public static Vector specialProductAX(SparseMatrix A, Vector x) {
		if(A.getColNum()!=x.getLength()) return new Vector();
		double[] value= new double[x.getLength()];
		for(int i=1; i<=x.getLength(); i++) {
			value[i-1]= A.retrieveElementRC(i, i)*x.getValue()[i-1];
		}
		return new Vector(value);
	}
	
}
