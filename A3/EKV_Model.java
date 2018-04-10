package A3;
import java.util.ArrayList;

import homework_4.*;

//This class is for task 4
//Get the three parameters of EKV model using quisi-Newton or Secant method
public class EKV_Model {
	//Module1: Compute the three objective functions
	//f1=0, f2=0, f3=0
	public static double helper1(double k, double Vgs, double Vth) {
		double Vt= 0.026;
		return Math.exp(k*(Vgs-Vth)/(2*Vt));
	}
	public static double helper2(double k, double Vgs, double Vth, double Vds) {
		double Vt= 0.026;
		return Math.exp((k*(Vgs-Vth)-Vds)/(2*Vt));
	}
	public static double helper3(double k, double Vgs, double Vth) {
		double term= 1+helper1(k, Vgs, Vth);
		return Math.log(term);
	}
	public static double helper4(double k, double Vgs, double Vth, double Vds) {
		double term= 1+helper2(k, Vgs, Vth, Vds);
		return Math.log(term);
	}
	//Compute Id_model
	public static double helper5(double k, double Vgs, double Vth, double Vds, double Is) {
		double term1= helper3(k, Vgs, Vth);
		double term2= helper4(k, Vgs, Vth, Vds);
		double Id= Is*Math.pow(term1, 2)-Is*Math.pow(term2, 2);
		return Id;
	}
	public Vector computeId_model(double k, Vector Vgs, double Vth, Vector Vds, double Is) {
		if(Vgs.getLength()!=Vds.getLength()) return new Vector();
		ArrayList<Double> Id_model_v= new ArrayList<Double>();
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		for (int i=0; i< Vgs.getLength(); i++) {
			Id_model_v.add(helper5(k, Vgs_v[i], Vth, Vds_v[i], Is));
		}
		return new Vector(Id_model_v);
		
	}
	//Partial derivative with respect to Is
	public static double f_Is(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		double[] Id_v= Id_measure.getValue();
		if(Vgs_v.length!=Vds_v.length) return 0;
		int len= Vgs_v.length;
		double f1=0;
		for(int i=0; i<len; i++) {
			double Id_model=helper5(k, Vgs_v[i], Vth, Vds_v[i], Is);
			double term1= helper3(k, Vgs_v[i], Vth);
			double term2= helper4(k, Vgs_v[i], Vth, Vds_v[i]);
			double deri_Is= term1*term1-term2*term2;
			f1=f1+Id_model*deri_Is-Id_v[i]*deri_Is;
		}
		return f1;
	}
	//Partial derivative with respect to k
	public static double f_k(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		double[] Id_v= Id_measure.getValue();
		if(Vgs_v.length!=Vds_v.length) return 0;
		int len= Vgs_v.length;
		double f2=0;
		double Vt= 0.026;
		for(int i=0; i<len; i++) {
			double Id_model=helper5(k, Vgs_v[i], Vth, Vds_v[i], Is);
			double term1= helper3(k, Vgs_v[i], Vth);
			double term2= helper1(k, Vgs_v[i], Vth);
			double term3= (Vgs_v[i]-Vth)/(2*Vt);
			double term4= helper4(k, Vgs_v[i], Vth, Vds_v[i]);
			double term5= helper2(k, Vgs_v[i], Vth, Vds_v[i]);
			double deri_k= 2*Is*term3*(term1*term2/(1+term2)-term4*term5/(1+term5));
			f2=f2+Id_model*deri_k-Id_v[i]*deri_k;
		}
		return f2;
	}
	//Partial derivative with respect to Vth
	public static double f_Vth(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		double[] Id_v= Id_measure.getValue();
		if(Vgs_v.length!=Vds_v.length) return 0;
		int len= Vgs_v.length;
		double f3=0;
		double Vt= 0.026;
		for(int i=0; i<len; i++) {
			double Id_model=helper5(k, Vgs_v[i], Vth, Vds_v[i], Is);
			double term1= helper3(k, Vgs_v[i], Vth);
			double term2= helper1(k, Vgs_v[i], Vth);
			double term3= -k/(2*Vt);
			double term4= helper4(k, Vgs_v[i], Vth, Vds_v[i]);
			double term5= helper2(k, Vgs_v[i], Vth, Vds_v[i]);
			double deri_k= 2*Is*term3*(term1*term2/(1+term2)-term4*term5/(1+term5));
			f3=f3+Id_model*deri_k-Id_v[i]*deri_k;
		}
		return f3;
	}
	
	//Module 2: Compute delta_para
	//Step1: Compute the Heissian Matrix
	//Quasi-Newton method
	public static FullMatrix getHeissian_Newton(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[][]heissianValue= new double[3][3];
		if(Is!=0) {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k, Vgs, Vth, Vds, Is*1.0001, Id_measure);
			heissianValue[0][0]=(f2-f1)/(Is*0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k, Vgs, Vth, Vds, Is*1.0001, Id_measure);
			heissianValue[1][0]=(f2-f1)/(Is*0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k, Vgs, Vth, Vds, Is*1.0001, Id_measure);
			heissianValue[2][0]=(f2-f1)/(Is*0.0001);
		}else {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k, Vgs, Vth, Vds, Is+0.0001, Id_measure);
			heissianValue[0][0]=(f2-f1)/(0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k, Vgs, Vth, Vds, Is+0.0001, Id_measure);
			heissianValue[1][0]=(f2-f1)/(0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k, Vgs, Vth, Vds, Is+0.0001, Id_measure);
			heissianValue[2][0]=(f2-f1)/(0.0001);
		}
		if(k!=0) {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k*1.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[0][1]=(f2-f1)/(k*0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k*1.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[1][1]=(f2-f1)/(k*0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k*1.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[2][1]=(f2-f1)/(k*0.0001);
		}else {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k+0.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[0][1]=(f2-f1)/(0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k+0.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[1][1]=(f2-f1)/(0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k+0.0001, Vgs, Vth, Vds, Is, Id_measure);
			heissianValue[2][1]=(f2-f1)/(0.0001);
		}
		if(Vth!=0) {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k, Vgs, Vth*1.0001, Vds, Is, Id_measure);
			heissianValue[0][2]=(f2-f1)/(Vth*0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k, Vgs, Vth*1.0001, Vds, Is, Id_measure);
			heissianValue[1][2]=(f2-f1)/(Vth*0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k, Vgs, Vth*1.0001, Vds, Is, Id_measure);
			heissianValue[2][2]=(f2-f1)/(Vth*0.0001);
		}else {
			double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
			double f2= f_Is(k, Vgs, Vth+0.0001, Vds, Is, Id_measure);
			heissianValue[0][2]=(f2-f1)/(0.0001);
			f1= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_k(k, Vgs, Vth+0.0001, Vds, Is, Id_measure);
			heissianValue[1][2]=(f2-f1)/(0.0001);
			f1= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
			f2= f_Vth(k, Vgs, Vth+0.0001, Vds, Is, Id_measure);
			heissianValue[2][2]=(f2-f1)/(0.0001);
		}
		return new FullMatrix(heissianValue);	
	}
	//Secant Method
	public static FullMatrix getHeissian_Secant(double k, double p_k, Vector Vgs, double Vth, double p_Vth, Vector Vds, double Is, double p_Is, Vector Id_measure) {
		double[][]heissianValue= new double[3][3];
		double f2_Is= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
		double f1_Is= f_Is(p_k, Vgs, p_Vth, Vds, p_Is, Id_measure);
		double f2_k= f_k(k, Vgs, Vth, Vds, Is, Id_measure); 
		double f1_k= f_k(p_k, Vgs, p_Vth, Vds, p_Is, Id_measure);
		double f2_Vth= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
		double f1_Vth=  f_Vth(p_k, Vgs, p_Vth, Vds, p_Is, Id_measure);

		heissianValue[0][0]=(f2_Is-f1_Is)/(Is-p_Is);
		heissianValue[1][0]=0;
		heissianValue[2][0]=0;
		
		heissianValue[0][1]=0;
		heissianValue[1][1]=(f2_k-f1_k)/(k-p_k);
		heissianValue[2][1]=0;
		
		heissianValue[0][2]=0;
		heissianValue[1][2]=0;
		heissianValue[2][2]=(f2_Vth-f1_Vth)/(Vth-p_Vth);
		
		return new FullMatrix(heissianValue);
	}
	//Step2: Solve the matrix equation: H*delta_para = -f
	//Use Newton method to get Hessian matrix
	public static Vector computeDelta_para(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		FullMatrix H= getHeissian_Newton(k, Vgs, Vth, Vds, Is, Id_measure);
		FullMatrixSolver ms= new FullMatrixSolver();
		double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
		double f2= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
		double f3= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
		double[] f_v= {-f1, -f2, -f3};
		Vector neg_f= new Vector(f_v);
		Vector delta_para= ms.solve(H, neg_f);
		return delta_para;
	}
	//Use Secant method to get Hessian matrix
	public static Vector computeDelta_para_s(double k, double p_k, Vector Vgs, double Vth, double p_Vth, Vector Vds, double Is, double p_Is, Vector Id_measure) {
		FullMatrix H= getHeissian_Secant(k, p_k, Vgs, Vth, p_Vth, Vds, Is, p_Is, Id_measure);
		FullMatrixSolver ms= new FullMatrixSolver();
		double f1= f_Is(k, Vgs, Vth, Vds, Is, Id_measure);
		double f2= f_k(k, Vgs, Vth, Vds, Is, Id_measure);
		double f3= f_Vth(k, Vgs, Vth, Vds, Is, Id_measure);
		double[] f_v= {-f1, -f2, -f3};
		Vector neg_f= new Vector(f_v);
		Vector delta_para= ms.solve(H, neg_f);
		return delta_para;
	}
	
	//Module 3: Compute Loss Function V
	public static double vx(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		double[] Vgs_v= Vgs.getValue();
		double[] Vds_v= Vds.getValue();
		double[] Id_v= Id_measure.getValue();
		if(Vgs_v.length!=Vds_v.length) return Double.MAX_VALUE;
		int len= Vgs_v.length;
		double loss=0;
		for(int i=0; i<len; i++) {
			double Id_model= helper5(k, Vgs_v[i], Vth, Vds_v[i], Is);
			double Id_mea= Id_v[i];
			loss= loss+ Math.pow((Id_model-Id_mea), 2);
		}
		return loss;

	}
	
	//Module 4: Newton + line search
	//Use Newton method to get Heissian matrix
	public Vector newtonEKV(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure) {
		if(Vgs.getLength()!=Vds.getLength()||Vgs.getLength()!=Id_measure.getLength()) return new Vector();
		Vector delta_para= computeDelta_para(k, Vgs, Vth, Vds, Is, Id_measure);
		double norm0= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, 0, delta_para);
		double t=1;
		double norm1= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t, delta_para);
		double norm2= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
		double sens_Is=0;
		double sens_k=0;
		double sens_Vth=0;
		double increVector=0;
		double temp= 0;
		//Used to store the values of ||Vk||/(||Vk-1||)^2 to help detect quadratic convergence
		ArrayList<Double> convergeTest= new ArrayList<Double>();
		do {
			delta_para= computeDelta_para(k, Vgs, Vth, Vds, Is, Id_measure);
			norm0= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, 0, delta_para);
			if(temp!=0) convergeTest.add(norm0/(temp*temp));
			temp=norm0;
			t=1;
			norm1= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t, delta_para);
			norm2= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
			while(norm1>norm2&&norm1>Math.pow(10, -7)) {
				t=t/2;
				norm1=norm2;
				norm2=computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
			}
			delta_para= delta_para.scaling(t);
			double delta_Is= delta_para.getValue()[0];
			double delta_k= delta_para.getValue()[1];
			double delta_Vth= delta_para.getValue()[2];
			
			increVector= (delta_Is*delta_Is)/(Is*Is)+(delta_k*delta_k)/(k*k)+(delta_Vth*delta_Vth)/(Vth*Vth);
			increVector= Math.sqrt(increVector);
			//Select reasonable values for Vgs and Vds
			//Compute parameter sensitivities
			sens_Is= (vx(k, Vgs, Vth, Vds, Is+delta_Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((Is+delta_Is)/Is);
			sens_k= (vx(k+delta_k, Vgs, Vth, Vds, Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((k+delta_k)/k);
			sens_Vth= (vx(k, Vgs, Vth+delta_Vth, Vds, Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((Vth+delta_Vth)/Vth);
			//System.out.println("Is= "+Is+"\tsens: "+sens_Is+"\tk= "+k+"\tsens: "+sens_k+"\tVth= "+Vth+"\tsens: "+sens_Vth+
			//		"\tThe second norm of V is: "+norm0+"The second norm of increment Vector: "+increVector);
			Is= Is+delta_Is;
			k= k+delta_k;
			Vth= Vth+delta_Vth;
			
		}while(norm0>Math.pow(10, -7)&&delta_para.computeSecondNorm()>Math.pow(10, -7));
		double flag=0;
		if(norm0<=0.1) {
			flag=1;
		}
		//Detect quadratic convergence
		if(convergeTest.size()>=2&&Math.abs(convergeTest.get(convergeTest.size()-1)-convergeTest.get(convergeTest.size()-2))<1E-3) {
			System.out.println("Newton method: Quadratic convergence is detected");
		}else {
			System.out.println("Newton method: Quadratic convergence is not detected");
		}
		double[] para= {Is, k, Vth, norm0, increVector, sens_Is, sens_k, sens_Vth, flag};
		return new Vector(para);
		
	}
	//Use Secant method to get Heissian matrix
	public Vector newtonEKV_s(double k, double p_k, Vector Vgs, double Vth, double p_Vth, Vector Vds, double Is, double p_Is, Vector Id_measure) {
		if(Vgs.getLength()!=Vds.getLength()||Vgs.getLength()!=Id_measure.getLength()) return new Vector();
		Vector delta_para= computeDelta_para_s(k, p_k, Vgs, Vth, p_Vth, Vds, Is, p_Is, Id_measure);
		double norm0= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, 0, delta_para);
		double t=1;
		double norm1= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t, delta_para);
		double norm2= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
		double sens_Is=0;
		double sens_k=0;
		double sens_Vth=0;
		double increVector=0;
		double temp= 0;
		//Used to store the values of ||Vk||/(||Vk-1||)^2 to help detect quadratic convergence
		ArrayList<Double> convergeTest= new ArrayList<Double>();
		do {
			delta_para= computeDelta_para_s(k, p_k, Vgs, Vth, p_Vth, Vds, Is, p_Is, Id_measure);
			norm0= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, 0, delta_para);
			t=1;
			if(temp!=0) convergeTest.add(norm0/(temp*temp));
			temp=norm0;
			norm1= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t, delta_para);
			norm2= computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
			while(norm1>norm2&&norm1>Math.pow(10, -7)) {
				t=t/2;
				norm1=norm2;
				norm2=computeNorm(k, Vgs, Vth, Vds, Is, Id_measure, t/2, delta_para);
			}
			delta_para= delta_para.scaling(t);
			double delta_Is= delta_para.getValue()[0];
			double delta_k= delta_para.getValue()[1];
			double delta_Vth= delta_para.getValue()[2];
			
			increVector= (delta_Is*delta_Is)/(Is*Is)+(delta_k*delta_k)/(k*k)+(delta_Vth*delta_Vth)/(Vth*Vth);
			increVector= Math.sqrt(increVector);
			//Select reasonable values for Vgs and Vds
			//Compute parameter sensitivities
			sens_Is= (vx(k, Vgs, Vth, Vds, Is+delta_Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((Is+delta_Is)/Is);
			sens_k= (vx(k+delta_k, Vgs, Vth, Vds, Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((k+delta_k)/k);
			sens_Vth= (vx(k, Vgs, Vth+delta_Vth, Vds, Is, Id_measure)/vx(k, Vgs, Vth, Vds, Is, Id_measure))/((Vth+delta_Vth)/Vth);
			//System.out.println("Is= "+Is+"\tsens: "+sens_Is+"\tk= "+k+"\tsens: "+sens_k+"\tVth= "+Vth+"\tsens: "+sens_Vth+
			//		"\tThe second norm of V is: "+norm0+"The second norm of increment Vector: "+increVector);
			p_Is= Is;
			p_k= k;
			p_Vth=Vth;
			Is= Is+delta_Is;
			k= k+delta_k;
			Vth= Vth+delta_Vth;
			
		}while(norm0>Math.pow(10, -7)&&delta_para.computeSecondNorm()>Math.pow(10, -7));
		//Detect converge or not
		double flag=0;
		if(norm0<=0.1) {
			flag=1;
		}
		//Detect quadratic convergence
		if(convergeTest.size()>=2&&Math.abs(convergeTest.get(convergeTest.size()-1)-convergeTest.get(convergeTest.size()-2))<1E-3) {
			System.out.println("Secant method: Quadratic convergence is detected");
		}else {
			System.out.println("Secant method: Quadratic convergence is not detected");
		}
		double[] para= {Is, k, Vth, norm0, increVector, sens_Is, sens_k, sens_Vth, flag};
		
		return new Vector(para);
	}
	//Module 5: Compute norm
	public static double computeNorm(double k, Vector Vgs, double Vth, Vector Vds, double Is, Vector Id_measure, double t, Vector delta_para) {
		double delta_Is= t*delta_para.getValue()[0];
		double delta_k= t*delta_para.getValue()[1];
		double delta_Vth= t*delta_para.getValue()[2];
		double v= vx(k+delta_k, Vgs, Vth+delta_Vth, Vds, Is+delta_Is, Id_measure);
		return Math.sqrt(v);
	}

}
