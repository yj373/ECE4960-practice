/*H**********************************************************************
 * FILENAME :  main.cpp
 *
 * DESCRIPTION :
 *      Final Exam
 *
 * AUTHOR :   Beitong Tian        START DATE :    18 May 14
 *
 * PLATFORM : C++ in MAC OS  IDE: Xcode
 *
 *H*/




#include "RK34.hpp"

//simulation parameters

//time: 0.2 s
//time step: 10 us
//Switch Frequency: 20KHZ

int main(int argc, const char * argv[]) {

// test R0helper
    printf("Test R0Helper\n");
    for (double time = 0; time < 1e-3; time += 1e-5) {
        printf("%e\n",R0Helper(time, 20.0 * 1e3)) ;
    }
    printf("\n");


// test R1helper
    printf("Test R1Helper\n");
    for (double time = 0; time < 1e-3; time += 1e-5) {
        printf("%e\n",R0Helper(time, 20.0 * 1e3)) ;
    }
    printf("\n");

// test VgsHelper
    printf("Test VgsHelper\n");
    for (double time = 0; time < 1e-3; time += 1e-5) {
        printf("%e\n",VgsHelper(time, 20.0 * 1e3)) ;
    }
    printf("\n");

// test RK34 Model
    RK34_test(5.0, 1.0, 2.0);
    printf("\n");

    
    
    Vector2d Initial;
    Initial << 0,0;
    RK34Boost_Ideal(0.2, 1e-5, Initial, 20.0 * 1e3 );
//
    RK34Boost_CMOS(0.2, 1e-5, Initial, 20.0 * 1e3 );

    return 0;


//for (double i = 0; i < 0.2; i+=1e-4) {
//    cout<< i << endl;
//}
}





