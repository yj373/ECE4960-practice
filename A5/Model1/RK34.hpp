//
//  RK34.hpp
//  
//
//  Created by Beitong Tian on 5/21/18.
//  Copyright © 2018 Beitong Tian. All rights reserved.
//

#ifndef RK34_hpp
#define RK34_hpp

#include "ODEhelper.hpp"

void RK34_test(double t, double deltaT, double last);
void RK34Boost_Ideal(double t, double deltaT, Vector2d last,double Frequency);
void RK34Boost_CMOS(double t, double deltaT, Vector2d last,double Frequency);


#endif /* RK34_hpp */
