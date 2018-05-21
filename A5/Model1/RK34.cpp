//
//  RK34.cpp
//
//
//  Created by Beitong Tian on 5/21/18.
//  Copyright © 2018 Beitong Tian. All rights reserved.
//

#include "RK34.hpp"

//Summary:  This method is to test RK34 model with time adaption with the function in program assignment 4

//Parameters:

//       t: total simulation time

//       deltaT : base time step used in the simulation

//       last : initial value; last(0) -> initial i; last(1) -> initial V

void RK34_test(double t, double deltaT, double last) {
    double time = 0.0;
    double Er = 1e-4;
    double Ea = 1e-7;
    double deltaTZero = deltaT;
    double timerequirement = time + deltaTZero;
    double truth = last;
    
    printf("RK34_test\n");
    printf("%-15s%-15s%-15s%-15s%-15s\n", "Time", "Step", "Result", "Groundtruth", "Error");
    printf("%-15.5f%-15.5f%-15.5f%-15.5f%.5f%%\n",0.0,0.0,truth,truth,0.0);
    
    double halfDeltaT, tempDeltaT, temphalfDeltaT;
    double time1, time2, time3, time4;
    double K1, K2, K3, K4;
    double x1, x2, x3, x4;
    double result, error;
    
    while (time < t) {
        
        if(time + deltaT > timerequirement) {
            tempDeltaT = timerequirement - time;
            temphalfDeltaT = tempDeltaT / 2;
            
            time1 = time;
            x1 = last;
            K1 = 4 * exp(0.8 * time1) - 0.5 * x1;
            
            time2 = time + temphalfDeltaT;
            x2 = last + K1 * temphalfDeltaT;
            K2 = 4 * exp(0.8 * time2) - 0.5 * x2;
            
            time3 = time + temphalfDeltaT;
            x3 = last + K2 * temphalfDeltaT;
            K3 = 4 * exp(0.8 * time3) - 0.5 * x3;
            
            time4 = time + tempDeltaT;
            x4 = last + K3 * tempDeltaT;
            K4 = 4 * exp(0.8 * time4) - 0.5 * x4;
            
            result = last + 1.0 / 6.0 * tempDeltaT * (K1 + 2.0 * K2 + 2.0 * K3 + K4);
            
            truth = groundTruth_Test(timerequirement);
            printf("%-15.5f%-15.5f%-15.5f%-15.5f%.5f%%\n", timerequirement, deltaT, result, truth, calError_test(truth,result));
            timerequirement += deltaTZero;
        }
        
        halfDeltaT = deltaT / 2;
        
        time1 = time;
        x1 = last;
        K1 = 4 * exp(0.8 * time1) - 0.5 * x1;
        
        time2 = time + halfDeltaT;
        x2 = last + K1 * halfDeltaT;
        K2 = 4 * exp(0.8 * time2) - 0.5 * x2;
        
        time3 = time + halfDeltaT;
        x3 = last + K2 * halfDeltaT;
        K3 = 4 * exp(0.8 * time3) - 0.5 * x3;
        
        time4 = time + deltaT;
        x4 = last + K3 * deltaT;
        K4 = 4 * exp(0.8 * time4) - 0.5 * x4;
        
        result = last + 1.0 / 6.0 * deltaT * (K1 + 2.0 * K2 + 2.0 * K3 + K4);
        
        error = 1.0 / 72.0 * (-5.0 * K1 + 6.0 * K2 + 8.0 * K3 - 9.0 * K4) * deltaT;
        
        time += deltaT;
        deltaT = deltaT * pow((Er / (abs(error) / (abs(result) + Ea))), 1.0/3);
        if(deltaT > deltaTZero) deltaT = deltaTZero;

        last = result;
        
        if(time == timerequirement) {
            truth = groundTruth_Test(timerequirement);
            printf("%-15.5f%-15.5f%-15.5f%-15.5f%.5f%%\n", timerequirement, deltaT, result, truth, calError_test(truth,result));
            timerequirement += deltaTZero;
        }
    }
    printf("\n");
    
}

//Summary:  This method use two resistors to replace the CMOS

//Parameters:

//       t: total simulation time

//       deltaT : base time step used in the simulation

//       last : initial value; last(0) -> initial i; last(1) -> initial V

//       Frequency: Switch frequency

void RK34Boost_Ideal(double t, double deltaT, Vector2d last,double Frequency) {
    
    printf("RK34Boost_Ideal\n");

    //parameters for the model
    double time = 0.0;
    double Er = 1e-9;
    double Ea = 1e-10;
    double E = 12;
    double L = 15.91 * 1e-3;
    double R = 52;
    double C = 50 * 1e-6;
    double Iprime = 0;
    double R0 = 0;
    double R1 = 0;
    
    double deltaTZero = deltaT;
    double timerequirement = time + deltaTZero;
    
    double halfDeltaT, tempDeltaT, temphalfDeltaT;
    double time1, time2, time3, time4;
    Vector2d K1, K2, K3, K4;
    Vector2d x1, x2, x3, x4;
    Vector2d result, error;
    

    while (time < t) {
        
        //get the R0 and R1 for the current time
        R0 = R0Helper(time, Frequency);
        R1 = R1Helper(time, Frequency);

        
        if(time + deltaT > timerequirement) {
            tempDeltaT = timerequirement - time;
            temphalfDeltaT = tempDeltaT / 2;
            
            time1 = time;
            time2 = time + temphalfDeltaT;
            time3 = time + temphalfDeltaT;
            time4 = time + tempDeltaT;
            
            x1(0) = last(0);    //i
            x1(1) = last(1);    //v1
            Iprime = (x1(1) + x1(0) * R1) / (R0 + R1);
            K1(0) = (E - Iprime*R0)/ L;
            K1(1) = (-x1(1)/R + (x1(0) - Iprime)) / C;
            
            x2(0) = last(0) + K1(0)*temphalfDeltaT;
            x2(1) = last(1) + K1(1)*temphalfDeltaT;
            Iprime = (x2(1) + x2(0) * R1) / (R0 + R1);
            K2(0) = (E - Iprime*R0)/ L;
            K2(1) = (-x1(1)/R + (x1(0) - Iprime)) / C;
            
            x3(0) = last(0) + K2(0)*temphalfDeltaT;
            x3(1) = last(1) + K2(1)*temphalfDeltaT;
            Iprime = (x3(1) + x3(0) * R1) / (R0 + R1);
            K3(0) = (E - Iprime*R0)/ L;
            K3(1) = (-x3(1)/R + (x3(0) - Iprime)) / C;
            
            x4(0) = last(0) + K3(0)*tempDeltaT;
            x4(1) = last(1) + K3(1)*tempDeltaT;
            Iprime = (x4(1) + x4(0) * R1) / (R0 + R1);
            K4(0) = (E - Iprime*R0)/ L;
            K4(1) = (-x4(1)/R + (x4(0) - Iprime)) / C;
            
            result(0) = last(0) + 1.0 / 6.0 * tempDeltaT * (K1(0) + 2.0 * K2(0) + 2.0 * K3(0) + K4(0));
            result(1) = last(1) + 1.0 / 6.0 * tempDeltaT * (K1(1) + 2.0 * K2(1) + 2.0 * K3(1) + K4(1));
            
            timerequirement += deltaTZero;
            printf("%e\n",result(1));
            
        }
        
        halfDeltaT = deltaT / 2;
        time1 = time;
        time2 = time + halfDeltaT;
        time3 = time + halfDeltaT;
        time4 = time + deltaT;
        
        x1(0) = last(0);    //i
        x1(1) = last(1);    //v1
        Iprime = (x1(1) + x1(0) * R1) / (R0 + R1);
        K1(0) = (E - Iprime*R0)/ L;
        K1(1) = (-x1(1)/R + (x1(0) - Iprime)) / C;
        
        x2(0) = last(0) + K1(0)*halfDeltaT;
        x2(1) = last(1) + K1(1)*halfDeltaT;
        Iprime = (x2(1) + x2(0) * R1) / (R0 + R1);
        K2(0) = (E - Iprime*R0)/ L;
        K2(1) = (-x1(1)/R + (x1(0) - Iprime)) / C;
        
        x3(0) = last(0) + K2(0)*halfDeltaT;
        x3(1) = last(1) + K2(1)*halfDeltaT;
        Iprime = (x3(1) + x3(0) * R1) / (R0 + R1);
        K3(0) = (E - Iprime*R0)/ L;
        K3(1) = (-x3(1)/R + (x3(0) - Iprime)) / C;
        
        x4(0) = last(0) + K3(0)*deltaT;
        x4(1) = last(1) + K3(1)*deltaT;
        Iprime = (x4(1) + x4(0) * R1) / (R0 + R1);
        K4(0) = (E - Iprime*R0)/ L;
        K4(1) = (-x4(1)/R + (x4(0) - Iprime)) / C;
        
        
        result(0) = last(0) + 1.0 / 6.0 * deltaT * (K1(0) + 2.0 * K2(0) + 2.0 * K3(0) + K4(0));
        result(1) = last(1) + 1.0 / 6.0 * deltaT * (K1(1) + 2.0 * K2(1) + 2.0 * K3(1) + K4(1));
        
        error(0) = 1.0 / 72.0 * (-5.0 * K1(0) + 6.0 * K2(0) + 8.0 * K3(0) - 9.0 * K4(0)) * deltaT;
        error(1) = 1.0 / 72.0 * (-5.0 * K1(1) + 6.0 * K2(1) + 8.0 * K3(1) - 9.0 * K4(1)) * deltaT;
        
        time += deltaT;
        deltaT = deltaT * pow((Er / (error.norm() / (result.norm() + Ea))), 1.0/3);
        if(deltaT > deltaTZero) deltaT = deltaTZero;

        last = result;
        
        
        if(time == timerequirement) {
            printf("%e\n",result(1));
            timerequirement += deltaTZero;
        }
        
    }
    printf("\n");

}


//Summary:  This method use EKV CMOS model

//Parameters:

//       t: total simulation time

//       deltaT : base time step used in the simulation

//       last : initial value; last(0) -> initial i; last(1) -> initial V

//       Frequency: Switch frequency

void RK34Boost_CMOS(double t, double deltaT, Vector2d last,double Frequency) {
    double time = 0.0;
    double Er = 1e-9;
    double Ea = 1e-10;
    double deltaTZero = deltaT;
    double timerequirement = time + deltaTZero;
    printf("RK34Boost_CMOS\n");
    
    double E = 12;
    double L = 15.91 * 1e-3;
    double R = 52;
    double C = 50 * 1e-6;
    
    
    double halfDeltaT, tempDeltaT, temphalfDeltaT;
    double time1, time2, time3, time4;
    Vector2d K1, K2, K3, K4;
    Vector2d x1, x2, x3, x4;
    Vector2d result, error;
    double Vgs = 0;
    while (time < t) {
        
        if(time + deltaT > timerequirement) {
            tempDeltaT = timerequirement - time;
            temphalfDeltaT = tempDeltaT / 2;
            
            time1 = time;
            time2 = time + temphalfDeltaT;
            time3 = time + temphalfDeltaT;
            time4 = time + tempDeltaT;
            
            x1(0) = last(0);    //i
            x1(1) = last(1);    //v1
            K1(0) = (E - x1(1))/ L;
            K1(1) = ((-x1(1) / R) + (x1(0) - currentHelper(Vgs,x1(1)))) / C;
            
            x2(0) = last(0) + K1(0)*temphalfDeltaT;
            x2(1) = last(1) + K1(1)*temphalfDeltaT;
            K2(0) = (E - x2(1))/ L;
            K2(1) = ((-x2(1) / R) + (x2(0) - currentHelper(Vgs,x2(1)))) / C;
            
            x3(0) = last(0) + K2(0)*temphalfDeltaT;
            x3(1) = last(1) + K2(1)*temphalfDeltaT;
            K3(0) = (E - x3(1))/ L;
            K3(1) = ((-x3(1) / R) + (x3(0) - currentHelper(Vgs,x3(1)))) / C;
            
            x4(0) = last(0) + K3(0)*tempDeltaT;
            x4(1) = last(1) + K3(1)*tempDeltaT;
            K4(0) = (E - x4(1))/ L;
            K4(1) = ((-x4(1) / R) + (x4(0) - currentHelper(Vgs,x4(1)))) / C;
            
            result(0) = last(0) + 1.0 / 6.0 * tempDeltaT * (K1(0) + 2.0 * K2(0) + 2.0 * K3(0) + K4(0));
            result(1) = last(1) + 1.0 / 6.0 * tempDeltaT * (K1(1) + 2.0 * K2(1) + 2.0 * K3(1) + K4(1));
            
            timerequirement += deltaTZero;
            Vgs = VgsHelper(timerequirement, Frequency);
            printf("%e\n",result(1));


        }
        
        halfDeltaT = deltaT / 2;
        time1 = time;
        time2 = time + halfDeltaT;
        time3 = time + halfDeltaT;
        time4 = time + deltaT;
        
        x1(0) = last(0);    //i
        x1(1) = last(1);    //v1
        K1(0) = (E - x1(1))/ L;
        K1(1) = ((-x1(1) / R) + (x1(0) - currentHelper(Vgs,x1(1)))) / C;
        
        x2(0) = last(0) + K1(0)*halfDeltaT;
        x2(1) = last(1) + K1(1)*halfDeltaT;
        K2(0) = (E - x2(1))/ L;
        K2(1) = ((-x2(1) / R) + (x2(0) - currentHelper(Vgs,x2(1)))) / C;
        
        x3(0) = last(0) + K2(0)*halfDeltaT;
        x3(1) = last(1) + K2(1)*halfDeltaT;
        K3(0) = (E - x3(1))/ L;
        K3(1) = ((-x3(1) / R) + (x3(0) - currentHelper(Vgs,x3(1)))) / C;
        
        x4(0) = last(0) + K3(0)*deltaT;
        x4(1) = last(1) + K3(1)*deltaT;
        K4(0) = (E - x4(1))/ L;
        K4(1) = ((-x4(1) / R) + (x4(0) - currentHelper(Vgs,x4(1)))) / C;
        
        
        result(0) = last(0) + 1.0 / 6.0 * deltaT * (K1(0) + 2.0 * K2(0) + 2.0 * K3(0) + K4(0));
        result(1) = last(1) + 1.0 / 6.0 * deltaT * (K1(1) + 2.0 * K2(1) + 2.0 * K3(1) + K4(1));
        
        error(0) = 1.0 / 72.0 * (-5.0 * K1(0) + 6.0 * K2(0) + 8.0 * K3(0) - 9.0 * K4(0)) * deltaT;
        error(1) = 1.0 / 72.0 * (-5.0 * K1(1) + 6.0 * K2(1) + 8.0 * K3(1) - 9.0 * K4(1)) * deltaT;
        
        time += deltaT;
        deltaT = deltaT * pow((Er / (error.norm() / (result.norm() + Ea))), 1.0/3);
        if(deltaT > deltaTZero) deltaT = deltaTZero;
        last = result;
        
        
        if(time == timerequirement) {
            printf("%e\n",result(1));
            Vgs = VgsHelper(timerequirement, Frequency);
            timerequirement += deltaTZero;
        }

    }
    printf("\n");

}





