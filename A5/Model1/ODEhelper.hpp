//
//  ODEhelper.hpp
//  
//
//  Created by Beitong Tian on 5/21/18.
//  Copyright © 2018 Beitong Tian. All rights reserved.
//

#ifndef ODEhelper_hpp
#define ODEhelper_hpp

#include <stdio.h>
#include <math.h>
#include <Eigen/Dense>
#include <math.h>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <cstdlib>
#include <string>

using namespace Eigen;
using namespace std;

double groundTruth_Test(double t);
double calError_test(double truth, double simulation);

double VgsHelper(double t,double Frequency);
double currentHelper(double V1, double V2);

double R0Helper(double time, double Frequency);
double R1Helper(double time, double Frequency);



#endif /* ODEhelper_hpp */
