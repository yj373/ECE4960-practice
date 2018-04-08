package A3;

import homework_4.Vector;
//This class is for task 2. The purpose is to extract the parameters of power law.
//The validation of this class is in the Tests class
public class NewtonPowerLaw {
	
	//Module1: Compute the objective functions
	//f1=0; f2=0
	public static Vector fx(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return new Vector();
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double f1=0;
		double f2=0;
		for(int i=0; i<x.getLength(); i++) {
			f1=f1+(Math.pow(x_v[i], 2*m)*a-Math.pow(x_v[i], m)*y_v[i]);
			f2=f2+(Math.log(x_v[i])*Math.pow(x_v[i], 2*m)*a*a-Math.log(x_v[i])*Math.pow(x_v[i], m)*y_v[i]*a);
		}
		double[] value= {f1,f2};
		return new Vector(value);
	}
	
	//Module2: Compute delta_para
	public static Vector computeDeltaPara(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return new Vector();
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double h11=0;
		for(int i=0; i<x.getLength(); i++) {
			h11+=Math.pow(x_v[i], 2*m);
		}
		double h21=0;
		for(int i=0; i<x.getLength(); i++) {
			h21+=Math.log(x_v[i])*Math.pow(x_v[i], 2*m)*2*a-Math.log(x_v[i])*Math.pow(x_v[i], m)*y_v[i];
		}
		double[] amPerturb_v= {a, 1.0001*m};
		if(m==0) {
			amPerturb_v[1]= 0.0001+m;
		}
		
		Vector amPerturb= new Vector(amPerturb_v);
		Vector vxPerturb= fx(x, y, amPerturb);
		double f1_P=vxPerturb.getValue()[0];
		double f2_P=vxPerturb.getValue()[1];
		Vector vx0= fx(x, y, am);
		double f1=vx0.getValue()[0];
		double f2=vx0.getValue()[1];
		double h12= (f1_P-f1)/(amPerturb.getValue()[1]-am.getValue()[1]);
		double h22= (f2_P-f2)/(amPerturb.getValue()[1]-am.getValue()[1]);
		double determinant= h11*h22-h12*h21;
		double delta_a= -(h22*f1+(-h12)*f2)/determinant;
		double delta_m= -((-h21)*f1+h11*f2)/determinant;
		double[] deltaPara_v= {delta_a, delta_m};
		return new Vector(deltaPara_v);

	}
	//Module3: Compute Loss function
	public static double vx(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return 0;
		double[] x_v= x.getValue();
		double[] y_v= y.getValue();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		double res=0;
		for(int i=0; i<x.getLength(); i++) {
			double ele= a*Math.pow(x_v[i], m)-y_v[i];
			res= res+Math.pow(ele, 2);
		}
		return res;
	}
	
	//Module4:Quasi-Newton+line search
	public Vector newtonPowerLaw(Vector x, Vector y, Vector am) {
		if (x.getLength()!=y.getLength()||am.getLength()!=2) return new Vector();
		double a= am.getValue()[0];
		double m= am.getValue()[1];
		Vector delta_para= computeDeltaPara(x, y, am);
		double norm0= computeNorm(x,y,am,0,delta_para);
		double t=1;
		double norm1= computeNorm(x,y,am,t,delta_para);
		double norm2= computeNorm(x,y,am,t/2,delta_para);
		while(norm1>norm2&&norm1>Math.pow(10, -7)) {
			t=t/2;
			norm1=norm2;
			norm2=computeNorm(x,y,am,t/2,delta_para);
		}
		delta_para= delta_para.scaling(t);
		double deltaNorm= delta_para.computeSecondNorm();
		System.out.println("a= "+a+"\tm= "+m+"\tV(a,m)= "+norm0+ "\tThe second norm of delta_para: "+deltaNorm);
		am=am.add(delta_para);
		while(norm0>Math.pow(10, -7)&&deltaNorm>Math.pow(10, -7)) {
			a=am.getValue()[0];
			m=am.getValue()[1];
			delta_para= computeDeltaPara(x, y, am);
			norm0= computeNorm(x,y,am,0,delta_para);
			t=1;
			norm1= computeNorm(x,y,am,t,delta_para);
			norm2= computeNorm(x,y,am,t/2,delta_para);
			while(norm1>norm2&&norm1>Math.pow(10, -7)) {
				t=t/2;
				norm1=norm2;
				norm2=computeNorm(x,y,am,t/2,delta_para);
			}
			delta_para= delta_para.scaling(t);
			deltaNorm= delta_para.computeSecondNorm();
			System.out.println("a= "+a+"\tm= "+m+"\tV(a,m)= "+norm0+ "\tThe second norm of delta_para: "+deltaNorm);
			am=am.add(delta_para);
			
		}
		return am;
	}
	
	//Module5: Compute norm
		public static double computeNorm(Vector x, Vector y, Vector am, double t, Vector delta_para) {
			Vector newAm= am.add(delta_para.scaling(t));
			double v= vx(x, y, newAm);
			return Math.sqrt(v);
		}

}
