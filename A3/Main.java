package A3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import homework_4.Vector;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tests tester= new Tests();
		tester.test();
		System.out.println("Tests and validations are done!***********************************************************************************");
		//Load the measure results
		ArrayList<Double> Vgs_v= new ArrayList<Double>();
		ArrayList<Double> Vds_v= new ArrayList<Double>();
		ArrayList<Double> Ids_v= new ArrayList<Double>();
		try {
			File measure= new File("src/A3/outputNMOS.txt");
			BufferedReader br_mea= new BufferedReader(new FileReader(measure));
			String st= br_mea.readLine();
			st= br_mea.readLine();
			while(st!=null) {
				String[] s= st.split("\\t");
				if(s.length==3) {
					Vgs_v.add(Double.valueOf(s[0]));
					Vds_v.add(Double.valueOf(s[1]));
					Ids_v.add(Double.valueOf(s[2]));
				}
				st= br_mea.readLine();
			}
			br_mea.close();
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		EKV_Model ekv= new EKV_Model();
		double k1=1;
		double Vth1= 1;
		double Is1=1E-7;
		Vector Vgs= new Vector(Vgs_v);
		Vector Vds= new Vector(Vds_v);
		Vector Id_measure= new Vector(Ids_v);
		System.out.println("Qusi-Newton method:*************************************************************************");
		Vector paraNewton= ekv.newtonEKV(k1, Vgs, Vth1, Vds, Is1, Id_measure);
		double[] paraNewton_v= paraNewton.getValue();
		System.out.println("Results: Is= "+paraNewton_v[0]+"\tk= "+paraNewton_v[1]+"\tVth= "+paraNewton_v[2]+"The second norm of V is: "+paraNewton_v[3]);
		if(paraNewton_v[8]==0) System.out.println("Diverge!");
		else System.out.println("Converge!");
		double p_k=1;
		double p_Vth=1;
		double p_Is= 1E-7;
		double k=0.9588;
		double Vth= 1.1665;
		double Is= 7.3218E-7;
		System.out.println("Secant method:*******************************************************************************");
		Vector paraSecant= ekv.newtonEKV_s(k, p_k, Vgs, Vth, p_Vth, Vds, Is, p_Is, Id_measure);
		double[] paraSecant_v= paraSecant.getValue();
		System.out.println("Results: Is= "+paraSecant_v[0]+"\tk= "+paraSecant_v[1]+"\tVth= "+paraSecant_v[2]+"The second norm of V is: "+paraSecant_v[3]);
		if(paraSecant_v[8]==0) System.out.println("Diverge!");
		else System.out.println("Converge!");
		System.out.println("Task 5:**************************************************************************************");
//		double Is_model= paraNewton.getValue()[0];
//		double k_model= paraNewton.getValue()[1];
//		double Vth_model= paraNewton.getValue()[2];
//		Vector Id_model= ekv.computeId_model(k_model, Vgs, Vth_model, Vds, Is_model);
//		double[] Id_model_v= Id_model.getValue();
//		for (int i=0; i<Id_model.getLength(); i++) {
//			double q= Id_model_v[i];
//			double w= Ids_v.get(i);
//			System.out.println("Vgs= \t"+Vgs_v.get(i)+"\tVds= \t"+Vds_v.get(i)+"\tId= \t"+q/w);
//		}
		System.out.println("Task 6:*************************************************************************************");
		double[] Is_ini= {1E-8, 3E-8, 1E-7, 3E-7, 1E-6, 3E-6, 1E-5, 3E-5};
		double[] k_ini= {0.5, 0.6, 0.7, 0.8, 0.9};
		double[] Vth_ini= {0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
		try {
			File task6Output= new File("src/A3/task6Output.txt");
			task6Output.createNewFile();
			BufferedWriter bw= new BufferedWriter(new FileWriter(task6Output));
			for(int i=0; i<Is_ini.length; i++) {
				for(int j=0; j<k_ini.length; j++) {
					for(int z=0; z<Vth_ini.length; z++) {
						bw.write("Task 4 (Quasi Newton) initial guess: Is= "+Is_ini[i]+"\tk= "+k_ini[j]+"\tVth= "+Vth_ini[z]+"\n");
						paraNewton= ekv.newtonEKV(k_ini[j], Vgs, Vth_ini[z], Vds, Is_ini[i], Id_measure);
						paraNewton_v= paraNewton.getValue();
						bw.write("||V||= "+paraNewton_v[3]+"\tThe second norm of increment vector: "+paraNewton_v[4]+
								"\tIs sencitivity: "+paraNewton_v[5]+"\tk sensitivity: "+paraNewton_v[6]+"\tVth sensitivity: "+paraNewton_v[7]+"\n");
						double p_Is_v=0;
						double p_k_v=0;
						double p_Vth_v=0;
						if(i==0) p_Is_v= Is_ini[7];
						else p_Is_v= Is_ini[i-1];
						if(j==0) p_k_v= k_ini[4];
						else p_k_v= k_ini[j-1];
						if(z==0) p_Vth_v= Vth_ini[12];
						else p_k_v= Vth_ini[z-1];
						bw.write("Task 4 (Secant) initial guesses: Is1= "+Is_ini[i]+"\tk1= "+k_ini[j]+"\tVth1= "+Vth_ini[z]
								+"\tIs2= "+p_Is_v+"\tk2= "+p_k_v+"\tVth= "+p_Vth_v+"\n");
						paraSecant= ekv.newtonEKV_s(k_ini[j], p_k_v, Vgs, Vth_ini[z], p_Vth_v, Vds, Is_ini[i], p_Is_v, Id_measure);
						paraSecant_v= paraSecant.getValue();
						bw.write("||V||= "+paraSecant_v[3]+"\tThe second norm of increment vector: "+paraSecant_v[4]+
								"\tIs sencitivity: "+paraSecant_v[5]+"\tk sensitivity: "+paraSecant_v[6]+"\tVth sensitivity: "+paraSecant_v[7]+"\n");
					}
				}
			}
			bw.close();
		}catch(Exception e) {
			e.getMessage();
		}
		System.out.println("The output of Task 6 is written in the text file task6Output.txt!");
		System.out.println("Task 7:***************************************************************************");
		tester.validation1(k, Vgs, Vth, Vds, Is, Id_measure);
		tester.validation2(k, Vgs, Vth, Vds, Is, Id_measure);
		tester.validation3(k, Vgs, Vth, Vds, Is, Id_measure);
		

	}

}
