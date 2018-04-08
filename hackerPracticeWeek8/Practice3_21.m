clear;
clc;
close all;
x=0:0.01:8;
y0=x-1;

t1= x-1.0;
a1= 1.5-1.0;
b1= -1.76544*10^(-9)+1.0;
y1=0.0+(1.0+a1)*t1+(b1-2*a1)*t1.^2-(b1-a1)*t1.^3;

t2= x-2.0;
a2= 1.76544*10^(-9)+1.0;
b2= 1.5-1.0;
y2=1.0+(-1.0+a2)*t2+(b2-2*a2)*t2.^2-(b2-a2)*t2.^3;


t3= x-3.0;
a3= -1.5+1.0;
b3= -1.08498*10^(-8)-1.0;
y3=0.0+(-1.0+a3)*t3+(b3-2*a3)*t3.^2-(b3-a3)*t3.^3;


t4= x-4.0;
a4= 1.08498*10^(-8)-1.0;
b4= -1.5+1.0;
y4=-1.0+(1.0+a4)*t4+(b4-2*a4)*t4.^2-(b4-a4)*t4.^3;


t5= x-5.0;
a5= 1.5-1.0;
b5= -7.5763*10^(-8)+1.0;
y5=0.0+(1.0+a5)*t5+(b5-2*a5)*t5.^2-(b5-a5)*t5.^3;


y6=x-5;

y=y0.*(x<=1.0)+y1.*((1.0<x)&(x<=2.0))+y2.*((2.0<x)&x<=3.0)+y3.*((3.0<x)&(x<=4.0))+y4.*((4.0<x)&(x<=5.0))+y5.*((5.0<x)&(x<=6.0))+y6.*(x>6.0);
plot(x,y);