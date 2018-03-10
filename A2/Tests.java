package A2;

import java.util.ArrayList;

import homework_4.*;

public class Tests {
	//A helper function to construct a n*n unit diagonal matrix
	public static SparseMatrix getUnitDiagonal(int n) {
		double[] value= new double[n];
		int[] colInd= new int[n];
		int[] rowPtr= new int[n+1];
		for (int i=0; i<n; i++) {
			value[i]=1.0;
			colInd[i]=i;
			rowPtr[i]=i;
		}
		rowPtr[n]= n;
		SparseMatrix unitDiagonal= new SparseMatrix(value, rowPtr, colInd);
		return unitDiagonal;
	}
	//Test function for iterative matrix solver by computing the second norm of the residual vector
	public double residuleNormTest(SparseMatrix A, Vector x, Vector b) {
		Vector test= A.productAx(x);
		if(test.getLength()!=b.getLength()) return -1;
		double[] value1= test.getValue();
		double[] value2= b.getValue();
		double norm=0;
		double div=0;
		for(int i=0; i<value1.length; i++) {
			norm=norm+Math.pow(value1[i]-value2[i], 2);
			div=div+Math.pow(value2[i], 2);
		}
		return Math.sqrt(norm)/Math.sqrt(div);
		
	}
	
	//Test function for iterative matrix solver by solving A*x=b with known x
	public String knownMatrixTest() {
		double[] valA= {5.0,1.0,1.0,5.0};
		int[] colInd= {0,1,0,1};
		int[] rowPtr= {0,2,4};
		SparseMatrix A= new SparseMatrix(valA, rowPtr, colInd, 2);
		double[] valB= {6.0,6.0};
		Vector b= new Vector(valB);
		Iterative_solver is= new Iterative_solver(A, b);
		Vector x= is.iterate();
		double norm= residuleNormTest(A, x, b);
		if(norm<Math.pow(10, -7)) return "Pass";
		else return "Fail";
	}
	
	//Test function for spercialProductAX(SparseMatrix A, Vector x)
	//D is a diagonal matrix, compute D*x, when x=[1.0, 1.0,...]
	public String specialProductAX_Test(SparseMatrix D) {
		double[] allOne= new double[D.getRowNum()];
		for (int i=0; i<allOne.length; i++) {
			allOne[i]=1.0;
		}
		Vector x0= new Vector(allOne);
		Iterative_solver is= new Iterative_solver();
		Vector test= is.specialProductAX(D, x0);
		double[] val= test.getValue();
		for(int i=0; i<val.length; i++) {
			if(val[i]!=D.retrieveElementRC(i+1, i+1)) return "Fail";
		}
		return "Pass";
	}
	//Test function for specialProductAB(SparseMatrix A, SparseMatrix B)
	//D is a diagonal matrix, compute D*B, when B is a n*n matrix with all 1's 
	public String specialProductAB_Test(SparseMatrix D) {
		int n= D.getColNum();
		double[] value= new double[n*n];
		int[] colInd= new int[n*n];
		int[] rowPtr= new int[n+1];
		for(int i=0; i<colInd.length; i++) {
			value[i]=1;
			colInd[i]=i%n;
		}
		for(int i=0; i<rowPtr.length; i++) {
			rowPtr[i]=n*i;
		}
		SparseMatrix test_B= new SparseMatrix(value, rowPtr, colInd);
		SparseMatrix test_res= D.specialProductAB(D, test_B);
		
		ArrayList<Double> val1= test_res.getValue();
		ArrayList<Integer> cI1= test_res.getColInd();
		ArrayList<Integer> rP1= test_res.getRowPtr();
		
		ArrayList<Double> val2= D.getValue();
		ArrayList<Integer> cI2= D.getColInd();
		ArrayList<Integer> rP2= D.getRowPtr();
		if(val1.equals(val2)&&cI1.equals(cI2)&&rP1.equals(rP2)) return "Pass";
		else return "Fail";
	}
	//Test function for getDiagonal()
	//The diagonal of A-D should all be zero
	public String getDiagonalTest(SparseMatrix A, SparseMatrix D) {
		int rowN= A.getRowNum();
		SparseMatrix noDiagonal= A.subtract(D);
		double norm=0;
		for(int i=1; i<=rowN; i++) {
			norm= norm+ Math.pow(noDiagonal.retrieveElementRC(i, i), 2);
		}
		norm= Math.sqrt(norm);
		if(norm<Math.pow(10, -7)) return "Pass";
		else return "Fail";
	}
	
	//Test function for inverseD(SparseMatrix D)
	//The product of D and its inverse should equal to unit diagonal matrix 
	public String inverseDTest(SparseMatrix D, SparseMatrix DI) {
		if(DI.getColNum()!=D.getColNum()||DI.getRowNum()!=D.getRowNum()) return "Fail";
		int n= D.getColNum();
		SparseMatrix unitDiagonal= getUnitDiagonal(n);
		SparseMatrix test= D.specialProductAB(D, DI);
		
		ArrayList<Double> val1= unitDiagonal.getValue();
		ArrayList<Integer> cI1= unitDiagonal.getColInd();
		ArrayList<Integer> rP1= unitDiagonal.getRowPtr();
		
		ArrayList<Double> val2= test.getValue();
		ArrayList<Integer> cI2= test.getColInd();
		ArrayList<Integer> rP2= test.getRowPtr();
		double norm=0;
		if(!cI1.equals(cI2)) return "Fail";
		if(!rP1.equals(rP2)) return "Fail";
		if(val1.size()!=val2.size()) return "Fail";
		for(int i=0; i<val1.size(); i++) {
			norm=norm+ Math.pow(val1.get(i)-val2.get(i), 2);
		}
		norm= Math.sqrt(norm);
		if(norm>Math.pow(10, -7)) return "Fail";
		else return "Pass";
	}
	
	

}
