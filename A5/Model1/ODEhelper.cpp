//
//  ODEhelper.cpp
//  
//
//  Created by Beitong Tian on 5/21/18.
//  Copyright © 2018 Beitong Tian. All rights reserved.
//

#include "ODEhelper.hpp"



double groundTruth_Test(double t) {
    return 4.0 / 1.3 * (exp(0.8 * t) - exp(-0.5 * t)) + 2 * exp(-0.5 * t);
}

double calError_test(double truth, double simulation) {
    return abs((truth - simulation)/ truth) * 100;
}


//Summary:  This function calculate the Vgs for EKV model

//Parameters:

//       time: current time

//       Frequency : Switch Frequency

//       Return: Vgs (V)

double VgsHelper(double time,double Frequency) {
    double Period = 1 / Frequency;
    double Vout;
    if(int(time/Period) % 2 == 0){
        Vout = 2;
        return Vout;
    }
    else {
        Vout = 0;
        return Vout;
    }
}


//Summary:  This function calculate the Id for EKV model

//Parameters:

//       V1: Vgs

//       V2 : Vds

//       Return: Id (A)
double currentHelper(double V1, double V2) {
    double Is = 5 * 1.0e-6;
    double k = 0.7;
    double Vt = 0.026;
    double Vth = 1.0;
    double temp1 = log(1 + exp(k * (V1 - Vth) / (2 * Vt) ));
    double temp2 = log(1 + exp((k * (V1 - Vth) - V2) / (2 * Vt) ));
    return Is * pow(temp1, 2) - Is * pow(temp2, 2);
}

//Summary:  This function calculate the R0 for Ideal model

//Parameters:

//       time: current time

//       Frequency : Switch Frequency

//       Return: R0 (Ohm)
double R0Helper(double time, double Frequency) {
    double Period = 1.0 / Frequency;
    if(int(time/Period) % 2 == 0){
        return 1e6;
    }
    else return 1;
}

//Summary:  This function calculate the R1 for Ideal model

//Parameters:

//       time: current time

//       Frequency : Switch Frequency

//       Return: R1 (Ohm)
double R1Helper(double time, double Frequency) {
    double Period = 1 / Frequency;
    if(int(time/Period) % 2 == 0){
        return 1;
    }
    else return 1e6;
}

