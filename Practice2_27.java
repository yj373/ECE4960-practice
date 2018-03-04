package homeworkAndPractice;
import homework_4.*;

public class Practice2_27 {

	public static void main(String[] args) {
		// The third Hacker Practice of Note4
		double e= Math.pow(10, -1);
		double[][] aValue= {{100,99},{99,98.01-e}};
		double[] bValue= {199,197};
		FullMatrix a= new FullMatrix(aValue);
		Vector b= new Vector(bValue);
		Vector x= new Vector();
		double secondNorm=0;
		//Each iteration perturb the element 98.01 a little bit, and observe its influence on the precision 
		for(int i=0; i<8; i++) {
			e=e/10.0;
			aValue[1][1]=98.01-e;
			a.setValue(aValue);
			x= MatrixSolver2By2(a,b);
			System.out.println("when e = "+e+"\tx = "+x.getValue()[0]+"\ty = "+x.getValue()[1]);
			
			//Compute the second norm between A*x and b
			secondNorm= computeSecondNorm(a.productAx(x),b);
			System.out.println("The second norm is: "+secondNorm);
		}
		

	}
	//Check the correctness by computing the second norm
	public static double computeSecondNorm(Vector v1, Vector v2) {
		if(v1.getLength()!=v2.getLength()) return -1;
		double res=0;
		for(int i=0; i<v1.getLength(); i++) {
			res=res+Math.pow(v1.getValue()[i]-v2.getValue()[i], 2);
		}
		res= Math.sqrt(res);
		return res;
	}
	//A matrix solver whose input is a 2*2 full matrix and a vector
	public static Vector MatrixSolver2By2(FullMatrix a, Vector b) {
		double[][] aValue= a.getValue();
		double[][] aInvValue= {{aValue[1][1], -aValue[0][1]},{-aValue[1][0], aValue[0][0]}};
		FullMatrix aInv= new FullMatrix(aInvValue);
		Vector x= aInv.productAx(b);
		double scale= aValue[0][0]*aValue[1][1]-aValue[0][1]*aValue[1][0];
		double[] val1= x.getValue();
		for(int i=0; i<val1.length; i++) {
			val1[i]=val1[i]/scale;
		}
		x.setValue(val1);
		return x;
	}

}
