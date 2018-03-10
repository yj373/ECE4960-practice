package homework_4;

import java.util.ArrayList;
import java.util.HashMap;


public class SparseMatrix {
	ArrayList<Double> value;
	//row-compressed
	ArrayList<Integer> rowPtr;
	ArrayList<Integer> colInd;
	int rowNum;
	int colNum;
	//Constructor
	public SparseMatrix(double[] v, int[] rP, int[]cI, int colN) {
		value= new ArrayList<Double>();
		rowNum= rP.length-1;
		colNum= colN;
		for(int i=0; i<v.length; i++) {
			value.add(v[i]);
		}
		rowPtr= new ArrayList<Integer>();
		for(int i=0; i<rP.length; i++) {
			rowPtr.add(rP[i]);
		}
		colInd= new ArrayList<Integer>();
		for(int i=0; i<cI.length; i++) {
			colInd.add(cI[i]);
		}

	}
	public SparseMatrix(double[]v, int[] rP, int[]cI) {
		value= new ArrayList<Double>();
		rowNum= rP.length-1;
		colNum=0;
		for(int i=0; i<v.length; i++) {
			value.add(v[i]);
		}
		rowPtr= new ArrayList<Integer>();
		for(int i=0; i<rP.length; i++) {
			rowPtr.add(rP[i]);
		}
		colInd= new ArrayList<Integer>();
		for(int i=0; i<cI.length; i++) {
			colInd.add(cI[i]);
			if(cI[i]+1>colNum) colNum=cI[i]+1;
		}
	}
	public SparseMatrix(ArrayList<Double>v, ArrayList<Integer> rP, ArrayList<Integer> cI, int colN) {
		value= v;
		rowPtr= rP;
		colInd= cI;
		rowNum= rP.size()-1;
		colNum=colN;
	}
	public SparseMatrix(ArrayList<Double>v, ArrayList<Integer>rP, ArrayList<Integer>cI) {
		value=v;
		rowPtr= rP;
		colInd= cI;
		rowNum= rP.size()-1;
		colNum=0;
		for(Integer i: colInd) {
			if(i+1>colNum) colNum=i+1;
		}
	}
	public SparseMatrix() {
		
	}
	public int getRowNum() {
		return rowNum;
	}
	public int getColNum() {
		return colNum;
	}
	public ArrayList<Double> getValue(){
		return value;
	}
	public ArrayList<Integer> getRowPtr(){
		return rowPtr;
	}
	public ArrayList<Integer> getColInd(){
		return colInd;
	}
	//Switch row[x] and row[y] for the sparse matrix
	public void rowPermute(int x, int y) {
		if(x>=rowPtr.size()||y>=rowPtr.size()) return;
		if(x<1||y<1) return;
		int row1Start= rowPtr.get(x-1);
		int row1End= rowPtr.get(x);
		int row2Start= rowPtr.get(y-1);
		int row2End= rowPtr.get(y);
		int bottom= row1Start;
		int top= row2End-1;
		for(int i=1; i<=row2End-row2Start; i++) {
			sinkDown(colInd, top, bottom);
			sinkDown(value, top, bottom);
			row1Start++;
			row1End++;
		}
		bottom= row1Start;
		for(int i=1; i<=row1End-row1Start; i++) {
			moveUp(colInd, bottom, top);
			moveUp(value, bottom,top);
		}
		int[] rowPtrHelper= new int[rowPtr.size()-1];
		for(int i=0; i<rowPtrHelper.length; i++) {
			rowPtrHelper[i]=rowPtr.get(i+1)-rowPtr.get(i);
		}
		int temp= rowPtrHelper[x-1];
		rowPtrHelper[x-1]= rowPtrHelper[y-1];
		rowPtrHelper[y-1]= temp;
		for(int i=1; i<rowPtr.size(); i++) {
			rowPtr.add(i, rowPtr.get(i-1)+rowPtrHelper[i-1]);
			rowPtr.remove(i+1);
		}
		return;
		
	}
	
	//Add a*row[x] to row[y] for the sparse matrix
	public void rowScale(int x, int y, double a) {
		if(x>=rowPtr.size()||y>=rowPtr.size()) return;
		if(x<1||y<1) return;
		
		//Find the starting point and ending point for the row[x] in colInd[] and value[]
		int row1Start= rowPtr.get(x-1);
		int row1End= rowPtr.get(x);
		
		//Find the starting point and ending point for the row[y] in colInd[] and value[] 
		int row2Start= rowPtr.get(y-1);
		int row2End= rowPtr.get(y);
		
		//Store the column number and the corresponding positions in a HashMap for row[y]
		HashMap<Integer, Integer> row2Col= new HashMap<Integer, Integer>();
		for(int i= row2Start; i<row2End; i++) {
			row2Col.put(colInd.get(i), i);
		}
		
		for (int i= row1Start; i<row1End; i++) {
			int colIndex= colInd.get(i);
			double val= a*this.retrieveElementRC(x, colIndex+1)+this.retrieveElementRC(y, colIndex+1);
			if(val==0&&!(row2Col.containsKey(colIndex))) continue;
			if(val==0&&row2Col.containsKey(colIndex)) {
				int pos= row2Col.get(colIndex);
				value.remove(pos);
				colInd.remove(pos);
				for(int j=y; j<rowPtr.size();j++) {
					int nonZeroNum= rowPtr.get(j);
					rowPtr.add(j, nonZeroNum-1);
					rowPtr.remove(j+1);
				}
			}else if(row2Col.containsKey(colIndex)) {
				int pos= row2Col.get(colIndex);
				value.add(pos, val);
				value.remove(pos+1);
			}else {
				value.add(row2Start, val);
				colInd.add(row2Start, colIndex);
				for(int j=y; j<rowPtr.size();j++) {
					int nonZeroNum= rowPtr.get(j);
					rowPtr.add(j, nonZeroNum+1);
					rowPtr.remove(j+1);
				}
			}
		}
	}
	
	//Return the product of Ax=b
	public Vector productAx(Vector x) {
		int len= x.length;
		ArrayList<Double> v= new ArrayList<Double>();
		if(len!=colNum) return new Vector();
		for(int i=0;i<rowNum; i++) {
			double val=0;
			for(int j=0; j<colNum; j++) {
				val=val+this.retrieveElementRC(i+1, j+1)*x.value[j];
			}
			v.add(val);
		}
		Vector res= new Vector(v);
		return res;
		
	}
	//Retrieve the element of an sparse matrix at (x, y)
	public double retrieveElementRC(int x, int y) {
		int start= rowPtr.get(x-1);
		int end= rowPtr.get(x);
		for (int i=start; i<end; i++) {
			if(y-1==colInd.get(i)) return value.get(i);
		}
		
		return 0;
	}
	
	//Helper functions to swap two parts of an ArrayList
	public <T> void sinkDown(ArrayList<T> array, int target, int bottom) {
		if(target>=array.size()||bottom<0||bottom>=target) return;
		T temp= array.get(target);
		array.add(bottom, temp);
		array.remove(target+1);
		return;
	}
	public <T> void moveUp(ArrayList<T> array, int target, int top) {
		if(top>=array.size()||target<0||top<=target) return;
		T temp= array.get(target);
		array.add(top+1,temp);
		array.remove(target);
		return;
	}
	
	//Helper function to traverse the matrix
	public SparseMatrix traverse() {
		ArrayList<Double> valueT= new ArrayList<Double>();
		ArrayList<Integer> rowPtrT= new ArrayList<Integer>();
		ArrayList<Integer> colIndT= new ArrayList<Integer>();
		rowPtr.add(0);
		for(int i=0; i<colNum; i++) {
			int nonZero=0;
			for(int j=0; j<rowNum; j++) {
				double val= this.retrieveElementRC(j+1, i+1);
				if(val!=0.0) {
					valueT.add(val);
					colInd.add(j);
					nonZero++;
				} 
			}
			rowPtrT.add(rowPtrT.get(rowPtrT.size()-1)+nonZero);
		}
		return new SparseMatrix(valueT, rowPtrT, colIndT,this.rowNum);
	}
	//A is a diagonal matrix, compute the product of two matrices A and B
		public SparseMatrix specialProductAB(SparseMatrix A, SparseMatrix B) {
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
		//A is a diagonal matrix, compute the product oa A and a vectorX
		public Vector specialProductAX(SparseMatrix A, Vector x) {
			if(A.getColNum()!=x.getLength()) return new Vector();
			double[] value= new double[x.getLength()];
			for(int i=1; i<=x.getLength(); i++) {
				value[i-1]= A.retrieveElementRC(i, i)*x.getValue()[i-1];
			}
			return new Vector(value);
		}
	//Return the product of two sparse matrices A*B
	public SparseMatrix productAB(SparseMatrix B) {
		if(this.colNum!=B.getRowNum()) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rP= new ArrayList<Integer>();
		ArrayList<Integer> cI= new ArrayList<Integer>();
		rP.add(0);
		for(int i=1; i<=this.rowNum; i++) {
			int count=0;
			for(int j=1; j<=B.colNum; j++) {
				double ele=0;
				for(int k=1; k<=this.colNum; k++) {
					ele= ele+this.retrieveElementRC(i, k)*B.retrieveElementRC(k, j);
				}
				if(ele!=0) {
					count++;
					value.add(ele);
					cI.add(j-1);
				}
			}
			rP.add(rP.get(i-1)+count);
		}
		return new SparseMatrix(value, rP, cI, B.colNum);
	}
	//Get the sum of two matrices A+B
	public SparseMatrix add(SparseMatrix B) {
		if(this.colNum!=B.getColNum()||this.rowNum!=B.getRowNum()) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rP= new ArrayList<Integer>();
		ArrayList<Integer> cI= new ArrayList<Integer>();
		rP.add(0);
		for(int i=1; i<=this.rowNum; i++) {
			int count=0;
			for(int j=1; j<=this.colNum; j++) {
				double ele= this.retrieveElementRC(i, j)+B.retrieveElementRC(i, j);
				if(ele!=0) {
					count++;
					value.add(ele);
					cI.add(j-1);
				}
			}
			rP.add(rP.get(i-1)+count);
		}
		return new SparseMatrix(value, rP, cI, this.colNum);
	}
	public SparseMatrix subtract(SparseMatrix B) {
		if(this.colNum!=B.getColNum()||this.rowNum!=B.getRowNum()) return new SparseMatrix();
		ArrayList<Double> value= new ArrayList<Double>();
		ArrayList<Integer> rP= new ArrayList<Integer>();
		ArrayList<Integer> cI= new ArrayList<Integer>();
		rP.add(0);
		for(int i=1; i<=this.rowNum; i++) {
			int count=0;
			for(int j=1; j<=this.colNum; j++) {
				double ele= this.retrieveElementRC(i, j)-B.retrieveElementRC(i, j);
				if(ele!=0) {
					count++;
					value.add(ele);
					cI.add(j-1);
				}
			}
			rP.add(rP.get(i-1)+count);
		}
		return new SparseMatrix(value, rP, cI, this.colNum);
	}

}
