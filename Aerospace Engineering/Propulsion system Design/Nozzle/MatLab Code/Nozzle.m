%% Nozzle Design

M6=1.0; % Nozzle inlet Mach Number
P6_P0=0.9;
mdot=16.09; %lb/sec
Tt0=1766.3; % ¡ÆR
Pt0=4525.5; % 1lb/ft*sec^2
R=1717.71; % ft/(sec^2*¡ÆR)
rt0=0.4482; % ft
rh=0.2712:-0.02712:0; % ft
gammat=1.33;
Cpt=0.276; % BTU/(lb*¡ÆR)
g=32.174; % ft/sec^2
J=778.28; % ft*lbf/BTU
M0=0.6192; % Turbine outlet Mach Number

a=11;
b=zeros(a);
A1=0.4+b(1,:); % tail cone Area
rt=sqrt(A1./pi+rh.^2);
rt1=rt(11);

Tail_pipe=rt1; % Tail pipe radius

figure(1)
hold on
plot(rh,'b')
plot(rt,'r')
xlabel('length')
ylabel('red-rt, blue-rh')
title('radius change')

M1=M0;

% Case 1
Ae=(A1(11).*((1+(gammat-1)./2.*M1.^2)./(1+(gammat-1)./2.*1.^2)).^(-(gammat+1)./(2.*(gammat-1)))).*(M1./1);
re=sqrt(Ae./pi);
Ln=(rt1-re)./tand(15);
x=0:0.01:0.1004;
Ac=pi.*((Ln-x).*tand(15)+re).^2;
rc=sqrt(Ac./pi);
Ac_Ae=Ac./Ae; % Nozzle Table

Mc(1)=-0.9091.*1.1697+1.68; Mc(2)=-0.9524.*1.1522+1.7305; Mc(3)=-1.1111.*1.1348+1.9122;
Mc(4)=-1.1765.*1.1176+1.9859; Mc(5)=-1.25.*1.1005+2.0675; Mc(6)=-1.5385.*1.0835+2.3831;
Mc(7)=-1.8182.*1.0667+2.6818; Mc(8)=-2.*1.0499+2.874; Mc(9)=-2.5.*1.0334+3.395;
Mc(10)=-4.*1.0169+4.932; Mc(11)=1;

% Case 2
Mi=M1:0.01:1;

Ai=(A1(11).*((1+(gammat-1)./2.*M1.^2)./(1+(gammat-1)./2.*Mi.^2)).^(-(gammat+1)./(2.*(gammat-1)))).*(M1./Mi); % Case2 (Nozzle equation)
ri=sqrt(Ai./pi);


figure(2)
hold on
plot(Mi,ri,'b')
plot(Mc,rc,'r')
xlabel('Mach Number')
ylabel('Nozzle radius')
title('Nozzle Change')

figure(3)
hold on
plot(Mi,Ai,'b')
plot(Mc,Ac,'r')
xlabel('Mach Number')
ylabel('Nozzle Area')
title('Nozzle Area change')
