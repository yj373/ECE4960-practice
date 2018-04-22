package hackerPracticeWeek12;
import A3.FullMatrixSolver;
import homework_4.*;
public class Practice4_17 {

	public static void main(String[] args) {
		// The first hacker practice of Note 7
		// 1-D elliptical PDE
		double[][] A_v= {{-2, 1, 0, 0}, {1, -2, 1, 0}, {0, 1, -2, 1}, {0, 0, 1, -2}};
		double[] b_v= {-1, 0, 0, 0};
		FullMatrix A= new FullMatrix(A_v);
		Vector b= new Vector(b_v);
		FullMatrixSolver solver= new FullMatrixSolver();
		Vector si= solver.solve(A,  b);
		double[] si_v= si.getValue();
		System.out.println("two Dirichlet conditions**********************************************************");
		for(int i=0; i<si_v.length; i++) {
			System.out.println("si"+i+"= "+si_v[i]);
		}
		double err= computeSecondNorm(A, si, b);
		System.out.println("The second norm of error: "+err);
		A_v[3][3]=-1;
		A.setValue(A_v);
		si= solver.solve(A, b);
		System.out.println("One Dirichlet condition and one Neumann condition*********************************");
		for(int i=0; i<si_v.length; i++) {
			System.out.println("si"+i+"= "+si_v[i]);
		}
		err= computeSecondNorm(A, si, b);
		System.out.println("The second norm of error: "+err);
		

	}
	
	public static double computeSecondNorm(FullMatrix A, Vector si, Vector b) {
		Vector res= A.productAx(si);
		return res.computeSecondNorm(b);
	}
	

}
