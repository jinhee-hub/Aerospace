%% Axial Turbine Design

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% inputs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
mdot=16.09; %lb/sec
Tt1=2029; % ¡ÆR
Tt3=1766.3; % ¡ÆR
Pt1=58.293.*144; % 1lb/(ft*sec^2)
R=1717.71; % ft/(sec^2*¡ÆR)
RPM=30000;
N=RPM./60;
rt=0.4482; % ft Same as Compressor's rt
Ssigma_m=2.075; 
Rsigma_m=1.11;
SAR=1.12;
RAR=2.20;
gammat=1.33;
Cpt=0.276; % BTU/(lb*¡ÆR)
mu=((1.819.*10^(-5))*2.2/(3.28)); % lb/(ft*s)
g=32.174; % ft/sec^2
J=778.28; % ft*lbf/BTU
alpha=0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Parameters
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Tt2=Tt1;
M0=0.1:0.01:1;
W_psh=(M0.*gammat./sqrt(gammat-1)).*(1+((gammat-1).*(M0.^2)./2)-((gammat+1)./2).*M0.^(2.*(gammat-1)./(gammat+1)))./(1+((gammat-1)./2).*M0.^2).^((3.*gammat-1)./(2.*(gammat-1))); % Turbine Nozzle inlet Mach Number
plot(M0,W_psh)
xlabel('M0')
ylabel('W_Max.swirl/(Pt0*sqrt(ht0))')
title('Max Work output Mach Number')
M1=0.25;
T1=Tt1./(1+0.5.*(gammat-1).*M1); % Turbine Nozzle inlet Temperature (¡ÆR)
Ca1=M1.*sqrt(gammat.*R.*T1); % ft/sec (Ca1=w0)
P1=Pt1./((1+((gammat-1)./2).*(M1.^2)).^(gammat./(gammat-1)));
rho1=P1./(R.*T1);
A1=(((mdot.*sqrt(Tt1.*R))./(Pt1.*sqrt(gammat)))./((1+((gammat-1)./2).*M1.^2).^(-(gammat+1)./(2.*(gammat-1)))))./M1./g;% Nozzle inlet Area (ft.^2)
A2=A1;
theta2=acosd(1./sqrt((2./(gammat-1)).*((1+((gammat-1)./2).*M1.^2)./(((A1./A2).*M1).^(2.*(gammat-1)./(gammat+1)))-1))); 
% theta2=alpha2=(theta1 at compressor) inlet flow angle at Stator(Nozzle)
rh=sqrt(rt.^2-A1./pi);
rm=sqrt((rt.^2+rh.^2)./2);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Pitch line Analysis
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% at rm
% Velocity, Temperature, Pressure, Efficiency

Um=-pi.*(2.*rm).*N;
dht=Cpt.*(Tt1-Tt3);
lambdam=(Um.^2)./(g.*J.*dht);
dCthetam=Um./lambdam;
Ctheta3m=0; % Ctheta3=v2
Ctheta2m=dCthetam; % Ctheta2=v1
Ca2m=Ctheta2m.*cotd(theta2); % Ca2=w1 
C2m=Ca2m./cosd(theta2); % C2=V1;
E=1.2; % Expansion Ratio
Ca3m=(sqrt(E)).*Ca2m; % Ca3=w2;
C3m=sqrt(Ca3m.^2+Ctheta3m.^2); % C3=V2;
K=0.35; % Turbine Loss Parameter
Fro=2.0;
Fst=1.0;
Crom=(2.*((cotd(theta2)).^2).*((Ctheta2m./dCthetam).^2))+(Ctheta2m./dCthetam-lambdam).^2+(Ctheta3m./dCthetam-lambdam).^2;
Cstm=(1+2.*cotd(theta2)).*((Ctheta2m./dCthetam).^2);
Rem=2.*mdot./(mu.*2.*rm); % Reynold's Number
Am=(K.*(Rem.^(-0.2))./cotd(theta2)).*(Fst.*Cstm+Fro.*Crom);
Bm=(E.*((cotd(theta2)).^2)).*((Ctheta2m./dCthetam).^2)+(Ctheta3m./dCthetam).^2;
etat_staticm=lambdam./(lambdam+0.5.*(Am+Bm));
P3m=Pt1.*((1-dht./(Cpt.*Tt1.*etat_staticm)).^(gammat./(gammat-1)));
T3m=Tt3-(C3m.^2)./(2.*g.*J.*Cpt);
Pt3m=P3m.*((Tt3./T3m).^(gammat./(gammat-1)));
etat_totalm=dht./(Cpt.*Tt1.*(1-(Pt3m./Pt1).^((gammat-1)./gammat)));

%  Absolute Flow Angle, Relative Angle, Degree of Reaction

Wtheta2m=Ctheta2m-Um; % Wtheta2=v1-wr;
Wtheta3m=Ctheta3m-Um; % Wtheta3=v2-wr;

theta3m=atand(Ctheta3m./Ca3m); % theta3=alpha3 (theta3=theta2 at compressor) exit flow angle at Stator(Nozzle)
beta2m=atand(Wtheta2m./Ca2m); % (beta2=beta1 at compressor) inlet flow angle at Rotor
beta3m=atand(Wtheta3m./Ca3m); % (beta3=beta2 at compressor) exit flow angle at Rotor

W2m=Ca2m./(cosd(beta2m)); % W2=VR1
W3m=Ca3m./(cosd(beta3m)); % W3=VR2

DRm=1./((C2m.^2-Ca1.^2)./(W3m.^2-W2m.^2)+1);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% 3D Blade Shape Design
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Uh=-pi.*(2.*rh).*N;
Ut=-pi.*(2.*rt).*N;

K1=(rm.^((sind(theta2)).^2)).*Ctheta2m;
K2=K1./(tand(theta2));

Ctheta2h=K1./(rh.^((sind(theta2)).^2));
Ctheta2t=K1./(rt.^((sind(theta2)).^2));

Ca2h=K2./(rh.^((sind(theta2)).^2));
Ca2t=K2./(rt.^((sind(theta2)).^2));

beta2t=atand((Ctheta2t-Ut)./Ca2t);
beta2h=atand((Ctheta2h-Uh)./Ca2h);

Ca3h=(sqrt(E)).*Ca2h;
Ca3t=(sqrt(E)).*Ca2t;

C2h=sqrt(Ca2h.^2+Ctheta2h.^2);
C2t=sqrt(Ca2t.^2+Ctheta2t.^2);

Wtheta3h=Uh;
Wtheta3t=Ut;

beta3h=atand(-Uh./Ca3h);
beta3t=atand(-Ut./Ca3t);

theta2h=acosd(Ca2h./-C2h);
theta2m=acosd(Ca2m./C2m);
theta2t=acosd(Ca2t./-C2t);

theta3h=theta3m;
theta3t=theta3m;

Rgamma2h=beta2h-alpha;
Rgamma3h=((0.25./sqrt(1.11)).*Rgamma2h-beta3h)./((0.25./sqrt(1.11))-1);

Rgamma2m=beta2m-alpha;
Rgamma3m=((0.25./sqrt(1.11)).*Rgamma2m-beta3m)./((0.25./sqrt(1.11))-1);

Rgamma2t=beta2t-alpha;
Rgamma3t=((0.25./sqrt(1.11)).*Rgamma2t-beta3t)./((0.25./sqrt(1.11))-1);

Rlambdah=atand((tand(Rgamma2h)+tand(Rgamma3h))./2);
Rlambdam=atand((tand(Rgamma2m)+tand(Rgamma3m))./2);
Rlambdat=atand((tand(Rgamma2t)+tand(Rgamma3t))./2);

M3=-Ca3m./sqrt(gammat.*R.*T3m);

% Stator

Sgamma2h=theta2h-alpha;
Sgamma3h=((0.25./sqrt(1.11)).*Sgamma2h-theta3h)./((0.25./sqrt(1.11))-1);

Sgamma2m=theta2m-alpha;
Sgamma3m=((0.25./sqrt(1.11)).*Sgamma2m-theta3m)./((0.25./sqrt(1.11))-1);

Sgamma2t=theta2t-alpha;
Sgamma3t=((0.25./sqrt(1.11)).*Sgamma2t-theta3t)./((0.25./sqrt(1.11))-1);

Slambdah=atand((tand(Sgamma2h)+tand(Sgamma3h))./2);
Slambdam=atand((tand(Sgamma2m)+tand(Sgamma3m))./2);
Slambdat=atand((tand(Sgamma2t)+tand(Sgamma3t))./2);

RC=((rt-rh)./RAR);
SC=((rt-rh)./SAR);
RSm=RC./Rsigma_m;
SSm=SC./Ssigma_m;

RNo=(2.*pi.*rm)./RSm;
SNo=(2.*pi.*rm)./SSm;

% camberline

RDh=tand(Rgamma3h)-tand(Rgamma2h); aRh=RDh./(2.*RC.*cosd(Rlambdah)); bRh=tand(Rgamma2h);
% y=-12.8947x.^2+0.7884x

RDm=tand(Rgamma3m)-tand(Rgamma2m); aRm=RDm./(2.*RC.*cosd(Rlambdam)); bRm=tand(Rgamma2m);
% y=-19.3973x.^2+0.3599x

RDt=tand(Rgamma3t)-tand(Rgamma2t); aRt=RDt./(2.*RC.*cosd(Rlambdat)); bRt=tand(Rgamma2t);
% y=-26.0538x.^2-0.0354x

SDh=tand(Sgamma3h)-tand(Sgamma2h); aSh=SDh./(2.*SC.*cosd(Slambdah)); bSh=tand(Sgamma2h);
% y=-6.2372x.^2+1.4168x

SDm=tand(Sgamma3m)-tand(Sgamma2m); aSm=SDm./(2.*SC.*cosd(Slambdam)); bSm=tand(Sgamma2m);
% y=-6.2372x.^2+1.4168x

SDt=tand(Sgamma3t)-tand(Sgamma2t); aSt=SDt./(2.*SC.*cosd(Slambdat)); bSt=tand(Sgamma2t);
% y=-6.2372x.^2+1.4168x