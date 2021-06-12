%%Axial Compressor Design

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% inputs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
pic_all=4.4285; tauc_all=1.5874; DRm=0.5; gammac=1.4;
Cpc=0.24; % BTU/lb*¡ÆR
Tt0=510.69; % ¡ÆR
R=1717.71; % ft/(sec^2*¡ÆR)
Pt0=13.855.*32.174./((0.0833).^2); % 1lb/ft*sec^2
mdot=15.998; %lb/sec
w=650; %ft/sec
rt=0.42275; % ft
C_stator=1.2*0.0833; % ft
C_rotor=1*0.0833; % ft
RPM=30000;
N=RPM./60;
etac=(pic_all.^((gammac-1)./gammac)-1)./(tauc_all-1);
lambda1=1; lambda2=0.94; lambda3=0.91; lambda4=0.89;
g=32.174; % ft/sec^2
J=778.28; % ft*lbf/BTU
alpha=0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Design parameters
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Total stage & each stage enthalpy 
dhc=(Cpc.*Tt0.*(tauc_all-1));
dhc_1=(lambda1./(lambda1+lambda2+lambda3+lambda4)).*dhc;
dhc_2=(lambda2./(lambda1+lambda2+lambda3+lambda4)).*dhc;
dhc_3=(lambda3./(lambda1+lambda2+lambda3+lambda4)).*dhc;
dhc_4=(lambda4./(lambda1+lambda2+lambda3+lambda4)).*dhc;

% at compressor inlet('0') Temperature, Speed of sound, Mach Number, Flow Area
T0=Tt0-((gammac-1).*(w.^2))./(2.*gammac.*R);
a0=sqrt(gammac.*R.*T0);
Ma0=w./a0;
A0=(((((mdot.*sqrt(Tt0))./Pt0).*(sqrt(R)))./sqrt(gammac)).*((1+((gammac-1)./2).*(Ma0.^2)).^((gammac+1)./(2.*(gammac-1)))))./Ma0;

% each stage Pressure ratio
pic_1=((etac.*dhc_1)./(Cpc.*Tt0)+1).^(gammac./(gammac-1));
pic_2=((etac.*dhc_2)./(Cpc.*Tt0)+1).^(gammac./(gammac-1));
pic_3=((etac.*dhc_3)./(Cpc.*Tt0)+1).^(gammac./(gammac-1));
pic_4=((etac.*dhc_4)./(Cpc.*Tt0)+1).^(gammac./(gammac-1));

% each stage total Rressure
Pt_1=Pt0.*pic_1; Pt_2=Pt_1.*pic_2; Pt_3=Pt_2.*pic_3; Pt_4=Pt_3.*pic_4;

% each stage total Temperature
Tt_1=dhc_1./Cpc+Tt0; Tt_2=dhc_2./Cpc+Tt_1; Tt_3=dhc_3./Cpc+Tt_2; Tt_4=dhc_4./Cpc+Tt_3;

% each stage static Temperature
T_1=Tt_1-((gammac-1).*(w.^2))./(2.*gammac.*R);
T_2=Tt_2-((gammac-1).*(w.^2))./(2.*gammac.*R);
T_3=Tt_3-((gammac-1).*(w.^2))./(2.*gammac.*R);
T_4=Tt_4-((gammac-1).*(w.^2))./(2.*gammac.*R);

% each stage Speed of sound
a_1=sqrt(gammac.*R.*T_1); a_2=sqrt(gammac.*R.*T_2); a_3=sqrt(gammac.*R.*T_3); a_4=sqrt(gammac.*R.*T_4);

% each stage Axial Mach Number
Ma_1=w./a_1; Ma_2=w./a_2; Ma_3=w./a_3; Ma_4=w./a_4;

% each stage flow Area
A_1=(((A0.*Pt0./Pt_1).*sqrt(Tt_1./Tt0)).*((1+((gammac-1)/2).*(Ma0.^2))./(1+((gammac-1)/2).*(Ma_1.^2))).^(-(gammac+1)./(2*(gammac-1)))).*(Ma0./Ma_1);
A_2=(((A0.*Pt0./Pt_2).*sqrt(Tt_2./Tt0)).*((1+((gammac-1)/2).*(Ma0.^2))./(1+((gammac-1)/2).*(Ma_2.^2))).^(-(gammac+1)./(2*(gammac-1)))).*(Ma0./Ma_2);
A_3=(((A0.*Pt0./Pt_3).*sqrt(Tt_3./Tt0)).*((1+((gammac-1)/2).*(Ma0.^2))./(1+((gammac-1)/2).*(Ma_3.^2))).^(-(gammac+1)./(2*(gammac-1)))).*(Ma0./Ma_3);
A_4=(((A0.*Pt0./Pt_4).*sqrt(Tt_4./Tt0)).*((1+((gammac-1)/2).*(Ma0.^2))./(1+((gammac-1)/2).*(Ma_4.^2))).^(-(gammac+1)./(2*(gammac-1)))).*(Ma0./Ma_4);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% radius & etc..
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
  % at hub
rh_1=sqrt(rt.^2-(A_1./pi)); rh_2=sqrt(rt.^2-(A_2./pi)); rh_3=sqrt(rt.^2-(A_3./pi)); rh_4=sqrt(rt.^2-(A_4./pi));
  % at mean
rm_1=sqrt((rt.^2+rh_1.^2)./2); rm_2=sqrt((rt.^2+rh_2.^2)./2); rm_3=sqrt((rt.^2+rh_3.^2)./2); rm_4=sqrt((rt.^2+rh_4.^2)./2);
  % at tip
rt_1=rt; rt_2=rt; rt_3=rt; rt_4=rt;

% Constant from Deree of Reaction
K=(rm_1.^2).*(1-DRm);

% each stage Rotor velocity
  % at hub
U_1h=2.*pi.*rh_1.*N; U_2h=2.*pi.*rh_2.*N; U_3h=2.*pi.*rh_3.*N; U_4h=2.*pi.*rh_4.*N;
  % at mean
U_1m=2.*pi.*rm_1.*N; U_2m=2.*pi.*rm_2.*N; U_3m=2.*pi.*rm_3.*N; U_4m=2.*pi.*rm_4.*N;
  % at tip
U_1t=2.*pi.*rt_1.*N; U_2t=2.*pi.*rt_2.*N; U_3t=2.*pi.*rt_3.*N; U_4t=2.*pi.*rt_4.*N;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Calculate angles(Rotor & Stator) & Degree of Reactions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% at hub----------------------------------------------------------------

% 1st stage inlet
theta1_1h=0;

% 1st stage
beta1_1h=atand((U_1h./w)-tand(theta1_1h));
beta2_1h=atand(tand(beta1_1h)-(dhc_1.*g.*J./(lambda1.*U_1h.*w)));
theta2_1h=atand(U_1h./w-tand(beta2_1h));

DR_h1=(w./(2.*U_1h)).*(tand(beta1_1h)+tand(beta2_1h));

% 1st stage outlet=2nd stage inlet
theta3_1h=0;
theta1_2h=theta3_1h;

% 2nd stage
beta1_2h=atand((U_2h./w)-tand(theta1_2h));
beta2_2h=atand(tand(beta1_2h)-(dhc_2.*g.*J./(lambda2.*U_2h.*w)));
theta2_2h=atand(U_2h./w-tand(beta2_2h));

DR_h2=(w./(2.*U_2h)).*(tand(beta1_2h)+tand(beta2_2h));

% 2nd stage outlet=3rd stage inlet
theta3_2h=0;
theta1_3h=theta3_2h;

% 3rd stage
beta1_3h=atand((U_3h./w)-tand(theta1_3h));
beta2_3h=atand(tand(beta1_3h)-(dhc_3.*g.*J./(lambda3.*U_3h.*w)));
theta2_3h=atand(U_3h./w-tand(beta2_3h));

DR_h3=(w./(2.*U_3h)).*(tand(beta1_3h)+tand(beta2_3h));

% 3rd stage outlet=4th stage inlet
theta3_3h=0;
theta1_4h=theta3_3h;

% 4th stage
beta1_4h=atand((U_4h./w)-tand(theta1_4h));
beta2_4h=atand(tand(beta1_4h)-(dhc_4.*g.*J./(lambda4.*U_4h.*w)));
theta2_4h=atand(U_4h./w-tand(beta2_4h));

DR_h4=(w./(2.*U_4h)).*(tand(beta1_4h)+tand(beta2_4h));

% 4th stage outlet
theta3_4h=0;

% at mean--------------------------------------------------------------

% 1st stage inlet
theta1_1m=0;

% 1st stage
beta1_1m=atand((U_1m./w)-tand(theta1_1m));
beta2_1m=atand(tand(beta1_1m)-(dhc_1.*g.*J./(lambda1.*U_1m.*w)));
theta2_1m=atand(U_1m./w-tand(beta2_1m));

DR_m1=(w./(2.*U_1m)).*(tand(beta1_1m)+tand(beta2_1m));

% 1st stage otlet=2nd stage inlet
theta3_1m=0;
theta1_2m=theta3_1m;

% 2nd stage inlet
beta1_2m=atand((U_2m./w)-tand(theta1_2m));
beta2_2m=atand(tand(beta1_2m)-(dhc_2.*g.*J./(lambda2.*U_2m.*w)));
theta2_2m=atand(U_2m./w-tand(beta2_2m));

DR_m2=(w./(2.*U_2m)).*(tand(beta1_2m)+tand(beta2_2m));

% 2nd stage outet= 3rd stage inlet
theta3_2m=0;
theta1_3m=theta3_2m;

% 3rd stage
beta1_3m=atand((U_3m./w)-tand(theta1_3m));
beta2_3m=atand(tand(beta1_3m)-(dhc_3.*g.*J./(lambda3.*U_3m.*w)));
theta2_3m=atand(U_3m./w-tand(beta2_3m));

DR_m3=(w./(2.*U_3m)).*(tand(beta1_3m)+tand(beta2_3m));

% 3rd stage outlet=4th stage inlet
theta3_3m=0;
theta1_4m=theta3_3m;

% 4th stage
beta1_4m=atand((U_4m./w)-tand(theta1_4m));
beta2_4m=atand(tand(beta1_4m)-(dhc_4.*g.*J./(lambda4.*U_4m.*w)));
theta2_4m=atand(U_4m./w-tand(beta2_4m));

DR_m4=(w./(2.*U_4m)).*(tand(beta1_4m)+tand(beta2_4m));

% 4th stage outlet
theta3_4m=0;

% at tip----------------------------------------------------------------

% 1st stage inlet
theta1_1t=0;

% 1st stage
beta1_1t=atand((U_1t./w)-tand(theta1_1t));
beta2_1t=atand(tand(beta1_1t)-(dhc_1.*g.*J./(lambda1.*U_1t.*w)));
theta2_1t=atand(U_1t./w-tand(beta2_1t));

DR_t1=(w./(2.*U_1t)).*(tand(beta1_1t)+tand(beta2_1t));

% 1st stage outlet=2nd stage inlet
theta3_1t=0;
theta1_2t=theta3_1t;

% 2nd stage inlet
beta1_2t=atand((U_2t./w)-tand(theta1_2t));
beta2_2t=atand(tand(beta1_2t)-(dhc_2.*g.*J./(lambda2.*U_2t.*w)));
theta2_2t=atand(U_2t./w-tand(beta2_2t));

DR_t2=(w./(2.*U_2t)).*(tand(beta1_2t)+tand(beta2_2t));

% 2nd stage outlet=3rd stage inlet
theta3_2t=0;
theta1_3t=theta3_2t;

% 3rd stage
beta1_3t=atand((U_3t./w)-tand(theta1_3t));
beta2_3t=atand(tand(beta1_3t)-(dhc_3.*g.*J./(lambda3.*U_3t.*w)));
theta2_3t=atand(U_3t./w-tand(beta2_3t));

DR_t3=(w./(2.*U_3t)).*(tand(beta1_3t)+tand(beta2_3t));

% 3rd stage outlet=4th stage inlet
theta3_3t=0;
theta1_4t=theta3_3t;

% 4th stage
beta1_4t=atand((U_4t./w)-tand(theta1_4t));
beta2_4t=atand(tand(beta1_4t)-(dhc_4.*g.*J./(lambda4.*U_4t.*w)));
theta2_4t=atand(U_4t./w-tand(beta2_4t));

DR_t4=(w./(2.*U_4t)).*(tand(beta1_4t)+tand(beta2_4t));

% 4th stage outlet
theta3_4t=0;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Separation check
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% 1.Rotor

% hub-----------------------------------------------------------------
% blade turning angle
Rphi_1h=beta1_1h-beta2_1h; Rphi_2h=beta1_2h-beta2_2h; Rphi_3h=beta1_3h-beta2_3h; Rphi_4h=beta1_4h-beta2_4h;

% At Cascade Test Data Graph
RSpC_1h=0.8; % Rphi1_1h=22.6761, beta2_1h=32.5255 
RSpC_2h=1.0; % Rphi1_2h=15.1960, beta2_2h=43.4710
RSpC_3h=1.5; % Rphi1_3h=12.0109, beta2_3h=48.6228
RSpC_4h=1.5; % Rphi1_4h=10.3728, beta2_4h=51.4382 

% Solidity 
Rsigma_1h=1./RSpC_1h; Rsigma_2h=1./RSpC_2h; Rsigma_3h=1./RSpC_3h; Rsigma_4h=1./RSpC_4h;

% Diffusion Factor
DFR_h1=1-((secd(beta2_1h)-(U_1h./w-(tand(beta2_1h)-tand(theta1_1h)))./(2.*Rsigma_1h))./(sqrt(1+(U_1h./w-tand(theta1_1h)).^2)));
DFR_h2=1-((secd(beta2_2h)-(U_2h./w-(tand(beta2_2h)-tand(theta1_2h)))./(2.*Rsigma_2h))./(sqrt(1+(U_2h./w-tand(theta1_2h)).^2)));
DFR_h3=1-((secd(beta2_3h)-(U_3h./w-(tand(beta2_3h)-tand(theta1_3h)))./(2.*Rsigma_3h))./(sqrt(1+(U_3h./w-tand(theta1_3h)).^2)));
DFR_h4=1-((secd(beta2_4h)-(U_4h./w-(tand(beta2_4h)-tand(theta1_4h)))./(2.*Rsigma_4h))./(sqrt(1+(U_4h./w-tand(theta1_4h)).^2)));


% mean-----------------------------------------------------------------
% blade turning angle
Rphi_1m=beta1_1m-beta2_1m; Rphi_2m=beta1_2m-beta2_2m; Rphi_3m=beta1_3m-beta2_3m; Rphi_4m=beta1_4m-beta2_4m;

% At Cascade Test Data Graph
RSpC_1m=1.5; % Rphi1_1m=12.2628, beta2_1m=48.2014 
RSpC_2m=1.5; % Rphi1_2m=10.5781, beta2_2m=51.0776
RSpC_3m=1.5; % Rphi1_3m=9.5920, beta2_3m=52.8327 
RSpC_4m=1.5; % Rphi1_4m=8.9865, beta2_4m=53.9418 

% Solidity 
Rsigma_1m=1./RSpC_1m; Rsigma_2m=1./RSpC_2m; Rsigma_3m=1./RSpC_3m; Rsigma_4m=1./RSpC_4m;

% Diffusion Factor

DFR_m1=1-((secd(beta2_1m)-(U_1m./w-(tand(beta2_1m)-tand(theta1_1m)))./(2.*Rsigma_1m))./(sqrt(1+(U_1m./w-tand(theta1_1m)).^2)));
DFR_m2=1-((secd(beta2_2m)-(U_2m./w-(tand(beta2_2m)-tand(theta1_2m)))./(2.*Rsigma_2m))./(sqrt(1+(U_2m./w-tand(theta1_2m)).^2)));
DFR_m3=1-((secd(beta2_3m)-(U_3m./w-(tand(beta2_3m)-tand(theta1_3m)))./(2.*Rsigma_3m))./(sqrt(1+(U_3m./w-tand(theta1_3m)).^2)));
DFR_m4=1-((secd(beta2_4m)-(U_4m./w-(tand(beta2_4m)-tand(theta1_4m)))./(2.*Rsigma_4m))./(sqrt(1+(U_4m./w-tand(theta1_4m)).^2)));


% tip-----------------------------------------------------------------
% blade turning angle
Rphi_1t=beta1_1t-beta2_1t; Rphi_2t=beta1_2t-beta2_2t; Rphi_3t=beta1_3t-beta2_3t; Rphi_4t=beta1_4t-beta2_4t;

% At Cascade Test Data Graph
RSpC_1t=1.9; % Rphi1_1t=7.8807, beta2_1t=56.0413 
RSpC_2t=1.9; % Rphi1_2t=7.8807, beta2_2t=56.0413
RSpC_3t=1.9; % Rphi1_3t=7.8807, beta2_3t=56.0413 
RSpC_4t=1.9; % Rphi1_4t=7.8807, beta2_4t=56.0413 

% Solidity 
Rsigma_1t=1./RSpC_1t; Rsigma_2t=1./RSpC_2t; Rsigma_3t=1./RSpC_3t; Rsigma_4t=1./RSpC_4t;

% Diffusion Factor
DFR_t1=1-((secd(beta2_1t)-(U_1t./w-(tand(beta2_1t)-tand(theta1_1t)))./(2.*Rsigma_1t))./(sqrt(1+(U_1t./w-tand(theta1_1t)).^2)));
DFR_t2=1-((secd(beta2_2t)-(U_2t./w-(tand(beta2_2t)-tand(theta1_2t)))./(2.*Rsigma_2t))./(sqrt(1+(U_2t./w-tand(theta1_2t)).^2)));
DFR_t3=1-((secd(beta2_3t)-(U_3t./w-(tand(beta2_3t)-tand(theta1_3t)))./(2.*Rsigma_3t))./(sqrt(1+(U_3t./w-tand(theta1_3t)).^2)));
DFR_t4=1-((secd(beta2_4t)-(U_4t./w-(tand(beta2_4t)-tand(theta1_4t)))./(2.*Rsigma_4t))./(sqrt(1+(U_4t./w-tand(theta1_4t)).^2)));


% 2.Stator

% at hub---------------------------------------------------------------
Sphi_1h=theta2_1h-theta3_1h; Sphi_2h=theta2_2h-theta3_2h; Sphi_3h=theta2_3h-theta3_3h; Sphi_4h=theta2_4h-theta3_4h;

% At Cascade Test Data Graph
SSpC_1h=0.75; % Sphi1_1h=38.5146, theta3_1h=0 
SSpC_2h=0.9; % Sphi1_2h=34.7829, theta3_2h=0
SSpC_3h=1.05; % Sphi1_3h=32.6994, theta3_3h=0 
SSpC_4h=1.15; % Sphi1_4h=31.4441, theta3_4h=0 

% Solidity 
Ssigma_1h=1./SSpC_1h; Ssigma_2h=1./SSpC_2h; Ssigma_3h=1./SSpC_3h; Ssigma_4h=1./SSpC_4h;

% Diffusion Factor
DFS_h1=1-((secd(theta3_1h)-(U_1h./w-(tand(beta2_1h)-tand(theta3_1h)))./(2.*Ssigma_1h))./(sqrt(1+(U_1h./w-tand(beta1_1h)).^2)));
DFS_h2=1-((secd(theta3_2h)-(U_2h./w-(tand(beta2_2h)-tand(theta3_2h)))./(2.*Ssigma_2h))./(sqrt(1+(U_2h./w-tand(beta1_2h)).^2)));
DFS_h3=1-((secd(theta3_3h)-(U_3h./w-(tand(beta2_3h)-tand(theta3_3h)))./(2.*Ssigma_3h))./(sqrt(1+(U_3h./w-tand(beta1_3h)).^2)));
DFS_h4=1-((secd(theta3_4h)-(U_4h./w-(tand(beta2_4h)-tand(theta3_4h)))./(2.*Ssigma_4h))./(sqrt(1+(U_4h./w-tand(beta1_4h)).^2)));


% at mean----------------------------------------------------------------
% blade turning angle
Sphi_1m=theta2_1m-theta3_1m; Sphi_2m=theta2_2m-theta3_2m; Sphi_3m=theta2_3m-theta3_3m; Sphi_4m=theta2_4m-theta3_4m;

% At Cascade Test Data Graph
SSpC_1m=1.1; % Sphi1_1m=32.5146, theta3_1m=0 
SSpC_2m=1.15; % Sphi1_2m=31.6101, theta3_2m=0
SSpC_3m=1.2; % Sphi1_3m=30.7873, theta3_3m=0 
SSpC_4m=1.25; % Sphi1_4m=30.2470, theta3_4m=0 

% Solidity 
Ssigma_1m=1./SSpC_1m; Ssigma_2m=1./SSpC_2m; Ssigma_3m=1./SSpC_3m; Ssigma_4m=1./SSpC_4m;

% Diffusion Factor
DFS_m1=1-((secd(theta3_1m)-(U_1m./w-(tand(beta2_1m)-tand(theta3_1m)))./(2.*Ssigma_1m))./(sqrt(1+(U_1m./w-tand(beta1_1m)).^2)));
DFS_m2=1-((secd(theta3_2m)-(U_2m./w-(tand(beta2_2m)-tand(theta3_2m)))./(2.*Ssigma_2m))./(sqrt(1+(U_2m./w-tand(beta1_2m)).^2)));
DFS_m3=1-((secd(theta3_3m)-(U_3m./w-(tand(beta2_3m)-tand(theta3_3m)))./(2.*Ssigma_3m))./(sqrt(1+(U_3m./w-tand(beta1_3m)).^2)));
DFS_m4=1-((secd(theta3_4m)-(U_4m./w-(tand(beta2_4m)-tand(theta3_4m)))./(2.*Ssigma_4m))./(sqrt(1+(U_4m./w-tand(beta1_4m)).^2)));

% at tip-----------------------------------------------------------------
Sphi_1t=theta2_1t-theta3_1t; Sphi_2t=theta2_2t-theta3_2t; Sphi_3t=theta2_3t-theta3_3t; Sphi_4t=theta2_4t-theta3_4t;

% At Cascade Test Data Graph
SSpC_1t=1.3; % Sphi1_1t=29.1778, theta3_1t=0 
SSpC_2t=1.3; % Sphi1_2t=29.1778, theta3_2t=0
SSpC_3t=1.3; % Sphi1_3t=29.1778, theta3_3t=0 
SSpC_4t=1.3; % Sphi1_4t=29.1778, theta3_4t=0 

% Solidity 
Ssigma_1t=1./SSpC_1t; Ssigma_2t=1./SSpC_2t; Ssigma_3t=1./SSpC_3t; Ssigma_4t=1./SSpC_4t;

% Diffusion Factor
DFS_t1=1-((secd(theta3_1t)-(U_1t./w-(tand(beta2_1t)-tand(theta3_1t)))./(2.*Ssigma_1t))./(sqrt(1+(U_1t./w-tand(beta1_1t)).^2)));
DFS_t2=1-((secd(theta3_2t)-(U_2t./w-(tand(beta2_2t)-tand(theta3_2t)))./(2.*Ssigma_2t))./(sqrt(1+(U_2t./w-tand(beta1_2t)).^2)));
DFS_t3=1-((secd(theta3_3t)-(U_3t./w-(tand(beta2_3t)-tand(theta3_3t)))./(2.*Ssigma_3t))./(sqrt(1+(U_3t./w-tand(beta1_3t)).^2)));
DFS_t4=1-((secd(theta3_4t)-(U_4t./w-(tand(beta2_4t)-tand(theta3_4t)))./(2.*Ssigma_4t))./(sqrt(1+(U_4t./w-tand(beta1_4t)).^2)));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Spacing & Blade Numbers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Rotor & Stator Spacing (Not real)

RS_1h=RSpC_1h.*C_rotor; RS_2h=RSpC_2h.*C_rotor; RS_3h=RSpC_3h.*C_rotor; RS_4h=RSpC_4h.*C_rotor;
RS_1m=RSpC_1m.*C_rotor; RS_2m=RSpC_2m.*C_rotor; RS_3m=RSpC_3m.*C_rotor; RS_4m=RSpC_4m.*C_rotor;
RS_1t=RSpC_1t.*C_rotor; RS_2t=RSpC_2t.*C_rotor; RS_3t=RSpC_3t.*C_rotor; RS_4t=RSpC_4t.*C_rotor;

SS_1h=SSpC_1h.*C_stator; SS_2h=SSpC_2h.*C_stator; SS_3h=SSpC_3h.*C_stator; SS_4h=SSpC_4h.*C_stator;
SS_1m=SSpC_1m.*C_stator; SS_2m=SSpC_2m.*C_stator; SS_3m=SSpC_3m.*C_stator; SS_4m=SSpC_4m.*C_stator;
SS_1t=SSpC_1t.*C_stator; SS_2t=SSpC_2t.*C_stator; SS_3t=SSpC_3t.*C_stator; SS_4t=SSpC_4t.*C_stator;

% Rotor & Stator number of blades (Not real)

Rn_1h=(2.*pi.*rh_1)./RS_1h; Rn_2h=(2.*pi.*rh_2)./RS_2h; Rn_3h=(2.*pi.*rh_3)./RS_3h; Rn_4h=(2.*pi.*rh_4)./RS_4h;
Rn_1m=(2.*pi.*rm_1)./RS_1m; Rn_2m=(2.*pi.*rm_2)./RS_2m; Rn_3m=(2.*pi.*rm_3)./RS_3m; Rn_4m=(2.*pi.*rm_4)./RS_4m;
Rn_1t=(2.*pi.*rt_1)./RS_1t; Rn_2t=(2.*pi.*rt_2)./RS_2t; Rn_3t=(2.*pi.*rt_3)./RS_3t; Rn_4t=(2.*pi.*rt_4)./RS_4t;

Sn_1h=(2.*pi.*rh_1)./SS_1h; Sn_2h=(2.*pi.*rh_2)./SS_2h; Sn_3h=(2.*pi.*rh_3)./SS_3h; Sn_4h=(2.*pi.*rh_4)./SS_4h;
Sn_1m=(2.*pi.*rm_1)./SS_1m; Sn_2m=(2.*pi.*rm_2)./SS_2m; Sn_3m=(2.*pi.*rm_3)./SS_3m; Sn_4m=(2.*pi.*rm_4)./SS_4m;
Sn_1t=(2.*pi.*rt_1)./SS_1t; Sn_2t=(2.*pi.*rt_2)./SS_2t; Sn_3t=(2.*pi.*rt_3)./SS_3t; Sn_4t=(2.*pi.*rt_4)./SS_4t;

% Real Number of Blades

No_R1h=28; No_R2h=25; No_R3h=19; No_R4h=20;
No_R1m=18; No_R2m=19; No_R3m=19; No_R4m=20;
No_R1t=17; No_R2t=17; No_R3t=17; No_R4t=17;

No_S1h=25; No_S2h=24; No_S3h=22; No_S4h=21;
No_S1m=21; No_S2m=20; No_S3m=20; No_S4m=21;
No_S1t=20; No_S2t=20; No_S3t=20; No_S4t=20;

% Real Spacing(ft)

S_rotor_1h=(2.*pi.*rh_1)./No_R1h; S_rotor_2h=(2.*pi.*rh_2)./No_R2h; S_rotor_3h=(2.*pi.*rh_3)./No_R3h; S_rotor_4h=(2.*pi.*rh_4)./No_R4h;
S_rotor_1m=(2.*pi.*rm_1)./No_R1m; S_rotor_2m=(2.*pi.*rm_2)./No_R2m; S_rotor_3m=(2.*pi.*rm_3)./No_R3m; S_rotor_4m=(2.*pi.*rm_4)./No_R4m;
S_rotor_1t=(2.*pi.*rt_1)./No_R1t; S_rotor_2t=(2.*pi.*rt_2)./No_R2t; S_rotor_3t=(2.*pi.*rt_3)./No_R3t; S_rotor_4t=(2.*pi.*rt_4)./No_R4t;

S_stator_1h=(2.*pi.*rh_1)./No_S1h; S_stator_2h=(2.*pi.*rh_2)./No_S2h; S_stator_3h=(2.*pi.*rh_3)./No_S3h; S_stator_4h=(2.*pi.*rh_4)./No_S4h;
S_stator_1m=(2.*pi.*rm_1)./No_S1m; S_stator_2m=(2.*pi.*rm_2)./No_S2m; S_stator_3m=(2.*pi.*rm_3)./No_S3m; S_stator_4m=(2.*pi.*rm_4)./No_S4m;
S_stator_1t=(2.*pi.*rt_1)./No_S1t; S_stator_2t=(2.*pi.*rt_2)./No_S2t; S_stator_3t=(2.*pi.*rt_3)./No_S3t; S_stator_4t=(2.*pi.*rt_4)./No_S4t;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Blade Design
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Rotor

% Geometric Blade Angle

RconstA_1h=0.25./Rsigma_1h; RconstA_2h=0.25./Rsigma_2h; RconstA_3h=0.25./Rsigma_3h; RconstA_4h=0.25./Rsigma_4h;
RconstA_1m=0.25./Rsigma_1m; RconstA_2m=0.25./Rsigma_2m; RconstA_3m=0.25./Rsigma_3m; RconstA_4m=0.25./Rsigma_4m;
RconstA_1t=0.25./Rsigma_1t; RconstA_2t=0.25./Rsigma_2t; RconstA_3t=0.25./Rsigma_3t; RconstA_4t=0.25./Rsigma_4t;

Rgamma1_1h=beta1_1h-alpha; Rgamma2_1h=(RconstA_1h.*Rgamma1_1h-beta2_1h)./(RconstA_1h-1);
Rgamma1_2h=beta1_2h-alpha; Rgamma2_2h=(RconstA_2h.*Rgamma1_2h-beta2_2h)./(RconstA_2h-1);
Rgamma1_3h=beta1_3h-alpha; Rgamma2_3h=(RconstA_3h.*Rgamma1_3h-beta2_3h)./(RconstA_3h-1);
Rgamma1_4h=beta1_4h-alpha; Rgamma2_4h=(RconstA_4h.*Rgamma1_4h-beta2_4h)./(RconstA_4h-1);

Rgamma1_1m=beta1_1m-alpha; Rgamma2_1m=(RconstA_1m.*Rgamma1_1m-beta2_1m)./(RconstA_1m-1);
Rgamma1_2m=beta1_2m-alpha; Rgamma2_2m=(RconstA_2m.*Rgamma1_2m-beta2_2m)./(RconstA_2m-1);
Rgamma1_3m=beta1_3m-alpha; Rgamma2_3m=(RconstA_3m.*Rgamma1_3m-beta2_3m)./(RconstA_3m-1);
Rgamma1_4m=beta1_4m-alpha; Rgamma2_4m=(RconstA_4m.*Rgamma1_4m-beta2_4m)./(RconstA_4m-1);

Rgamma1_1t=beta1_1t-alpha; Rgamma2_1t=(RconstA_1t.*Rgamma1_1t-beta2_1t)./(RconstA_1t-1);
Rgamma1_2t=beta1_2t-alpha; Rgamma2_2t=(RconstA_2t.*Rgamma1_2t-beta2_2t)./(RconstA_2t-1);
Rgamma1_3t=beta1_3t-alpha; Rgamma2_3t=(RconstA_3t.*Rgamma1_3t-beta2_3t)./(RconstA_3t-1);
Rgamma1_4t=beta1_4t-alpha; Rgamma2_4t=(RconstA_4t.*Rgamma1_4t-beta2_4t)./(RconstA_4t-1);

% Stagger Angle

Rlambda_1h=atand((tand(Rgamma1_1h)+tand(Rgamma2_1h))./2); Rlambda_2h=atand((tand(Rgamma1_2h)+tand(Rgamma2_2h))./2);
Rlambda_3h=atand((tand(Rgamma1_3h)+tand(Rgamma2_3h))./2); Rlambda_4h=atand((tand(Rgamma1_4h)+tand(Rgamma2_4h))./2);

Rlambda_1m=atand((tand(Rgamma1_1m)+tand(Rgamma2_1m))./2); Rlambda_2m=atand((tand(Rgamma1_2m)+tand(Rgamma2_2m))./2);
Rlambda_3m=atand((tand(Rgamma1_3m)+tand(Rgamma2_3m))./2); Rlambda_4m=atand((tand(Rgamma1_4m)+tand(Rgamma2_4m))./2);

Rlambda_1t=atand((tand(Rgamma1_1t)+tand(Rgamma2_1t))./2); Rlambda_2t=atand((tand(Rgamma1_2t)+tand(Rgamma2_2t))./2);
Rlambda_3t=atand((tand(Rgamma1_3t)+tand(Rgamma2_3t))./2); Rlambda_4t=atand((tand(Rgamma1_4t)+tand(Rgamma2_4t))./2);

% Blade camberline

RD_1h=tand(Rgamma2_1h)-tand(Rgamma1_1h);
Ra_1h=RD_1h./(2.*C_rotor.*cosd(Rlambda_1h)); Rb_1h=tand(Rgamma1_1h); %  y=-7.7497x.^2+1.4335x
RD_2h=tand(Rgamma2_2h)-tand(Rgamma1_2h);
Ra_2h=RD_2h./(2.*C_rotor.*cosd(Rlambda_2h)); Rb_2h=tand(Rgamma1_2h); %  y=-8.0381x.^2+1.6426x
RD_3h=tand(Rgamma2_3h)-tand(Rgamma1_3h);
Ra_3h=RD_3h./(2.*C_rotor.*cosd(Rlambda_3h)); Rb_3h=tand(Rgamma1_3h); %  y=-8.9381x.^2+1.7772x
RD_4h=tand(Rgamma2_4h)-tand(Rgamma1_4h);
Ra_4h=RD_4h./(2.*C_rotor.*cosd(Rlambda_4h)); Rb_4h=tand(Rgamma1_4h); %  y=-9.0184x.^2+1.8659x

RD_1m=tand(Rgamma2_1m)-tand(Rgamma1_1m);
Ra_1m=RD_1m./(2.*C_rotor.*cosd(Rlambda_1m)); Rb_1m=tand(Rgamma1_1m); %  y=-8.9274x.^2+1.7649x
RD_2m=tand(Rgamma2_2m)-tand(Rgamma1_2m);
Ra_2m=RD_2m./(2.*C_rotor.*cosd(Rlambda_2m)); Rb_2m=tand(Rgamma1_2m); %  y=-9.0073x.^2+1.8538x
RD_3m=tand(Rgamma2_3m)-tand(Rgamma1_3m);
Ra_3m=RD_3m./(2.*C_rotor.*cosd(Rlambda_3m)); Rb_3m=tand(Rgamma1_3m); %  y=-9.0641x.^2+1.9148x
RD_4m=tand(Rgamma2_4m)-tand(Rgamma1_4m);
Ra_4m=RD_4m./(2.*C_rotor.*cosd(Rlambda_4m)); Rb_4m=tand(Rgamma1_4m); %  y=-9.1032x.^2+1.9566x

RD_1t=tand(Rgamma2_1t)-tand(Rgamma1_1t);
Ra_1t=RD_1t./(2.*C_rotor.*cosd(Rlambda_1t)); Rb_1t=tand(Rgamma1_1t); %  y=-10.1300x.^2+2.0432x
RD_2t=tand(Rgamma2_2t)-tand(Rgamma1_2t);
Ra_2t=RD_2t./(2.*C_rotor.*cosd(Rlambda_2t)); Rb_2t=tand(Rgamma1_2t); %  y=-10.1300x.^2+2.0432x
RD_3t=tand(Rgamma2_3t)-tand(Rgamma1_3t);
Ra_3t=RD_3t./(2.*C_rotor.*cosd(Rlambda_3t)); Rb_3t=tand(Rgamma1_3t); %  y=-10.1300x.^2+2.0432x
RD_4t=tand(Rgamma2_4t)-tand(Rgamma1_4t);
Ra_4t=RD_4t./(2.*C_rotor.*cosd(Rlambda_4t)); Rb_4t=tand(Rgamma1_4t); %  y=-10.1300x.^2+2.0432x

% Stator

% Geometric Blade Angle

SconstA_1h=0.25./Ssigma_1h; SconstA_2h=0.25./Ssigma_2h; SconstA_3h=0.25./Ssigma_3h; SconstA_4h=0.25./Ssigma_4h;
SconstA_1m=0.25./Ssigma_1m; SconstA_2m=0.25./Ssigma_2m; SconstA_3m=0.25./Ssigma_3m; SconstA_4m=0.25./Ssigma_4m;
SconstA_1t=0.25./Ssigma_1t; SconstA_2t=0.25./Ssigma_2t; SconstA_3t=0.25./Ssigma_3t; SconstA_4t=0.25./Ssigma_4t;

Sgamma1_1h=theta1_1h-alpha; Sgamma2_1h=(SconstA_1h.*Sgamma1_1h-theta2_1h)./(SconstA_1h-1);
Sgamma1_2h=theta1_2h-alpha; Sgamma2_2h=(SconstA_2h.*Sgamma1_2h-theta2_2h)./(SconstA_2h-1);
Sgamma1_3h=theta1_3h-alpha; Sgamma2_3h=(SconstA_3h.*Sgamma1_3h-theta2_3h)./(SconstA_3h-1);
Sgamma1_4h=theta1_4h-alpha; Sgamma2_4h=(SconstA_4h.*Sgamma1_4h-theta2_4h)./(SconstA_4h-1);

Sgamma1_1m=theta1_1m-alpha; Sgamma2_1m=(SconstA_1m.*Sgamma1_1m-theta2_1m)./(SconstA_1m-1);
Sgamma1_2m=theta1_2m-alpha; Sgamma2_2m=(SconstA_2m.*Sgamma1_2m-theta2_2m)./(SconstA_2m-1);
Sgamma1_3m=theta1_3m-alpha; Sgamma2_3m=(SconstA_3m.*Sgamma1_3m-theta2_3m)./(SconstA_3m-1);
Sgamma1_4m=theta1_4m-alpha; Sgamma2_4m=(SconstA_4m.*Sgamma1_4m-theta2_4m)./(SconstA_4m-1);

Sgamma1_1t=theta1_1t-alpha; Sgamma2_1t=(SconstA_1t.*Sgamma1_1t-theta2_1t)./(SconstA_1t-1);
Sgamma1_2t=theta1_2t-alpha; Sgamma2_2t=(SconstA_2t.*Sgamma1_2t-theta2_2t)./(SconstA_2t-1);
Sgamma1_3t=theta1_3t-alpha; Sgamma2_3t=(SconstA_3t.*Sgamma1_3t-theta2_3t)./(SconstA_3t-1);
Sgamma1_4t=theta1_4t-alpha; Sgamma2_4t=(SconstA_4t.*Sgamma1_4t-theta2_4t)./(SconstA_4t-1);

% Stagger Angle

Slambda_1h=atand((tand(Sgamma1_1h)+tand(Sgamma2_1h))./2); Slambda_2h=atand((tand(Sgamma1_2h)+tand(Sgamma2_2h))./2);
Slambda_3h=atand((tand(Sgamma1_3h)+tand(Sgamma2_3h))./2); Slambda_4h=atand((tand(Sgamma1_4h)+tand(Sgamma2_4h))./2);

Slambda_1m=atand((tand(Sgamma1_1m)+tand(Sgamma2_1m))./2); Slambda_2m=atand((tand(Sgamma1_2m)+tand(Sgamma2_2m))./2);
Slambda_3m=atand((tand(Sgamma1_3m)+tand(Sgamma2_3m))./2); Slambda_4m=atand((tand(Sgamma1_4m)+tand(Sgamma2_4m))./2);

Slambda_1t=atand((tand(Sgamma1_1t)+tand(Sgamma2_1t))./2); Slambda_2t=atand((tand(Sgamma1_2t)+tand(Sgamma2_2t))./2);
Slambda_3t=atand((tand(Sgamma1_3t)+tand(Sgamma2_3t))./2); Slambda_4t=atand((tand(Sgamma1_4t)+tand(Sgamma2_4t))./2);

% Blade camberline

SD_1h=tand(Sgamma2_1h)-tand(Sgamma1_1h);
Sa_1h=SD_1h./(2.*C_stator.*cosd(Slambda_1h)); Sb_1h=tand(Sgamma1_1h); %  y=6.1925x.^2
SD_2h=tand(Sgamma2_2h)-tand(Sgamma1_2h);
Sa_2h=SD_2h./(2.*C_stator.*cosd(Slambda_2h)); Sb_2h=tand(Sgamma1_2h); %  y=5.5646x.^2
SD_3h=tand(Sgamma2_3h)-tand(Sgamma1_3h);
Sa_3h=SD_3h./(2.*C_stator.*cosd(Slambda_3h)); Sb_3h=tand(Sgamma1_3h); %  y=5.4400x.^2
SD_4h=tand(Sgamma2_4h)-tand(Sgamma1_4h);
Sa_4h=SD_4h./(2.*C_stator.*cosd(Slambda_4h)); Sb_4h=tand(Sgamma1_4h); %  y=5.3935x.^2

SD_1m=tand(Sgamma2_1m)-tand(Sgamma1_1m);
Sa_1m=SD_1m./(2.*C_stator.*cosd(Slambda_1m)); Sb_1m=tand(Sgamma1_1m); %  y=5.6755x.^2
SD_2m=tand(Sgamma2_2m)-tand(Sgamma1_2m);
Sa_2m=SD_2m./(2.*C_stator.*cosd(Slambda_2m)); Sb_2m=tand(Sgamma1_2m); %  y=5.4460x.^2
SD_3m=tand(Sgamma2_3m)-tand(Sgamma1_3m);
Sa_3m=SD_3m./(2.*C_stator.*cosd(Slambda_3m)); Sb_3m=tand(Sgamma1_3m); %  y=5.3599x.^2
SD_4m=tand(Sgamma2_4m)-tand(Sgamma1_4m);
Sa_4m=SD_4m./(2.*C_stator.*cosd(Slambda_4m)); Sb_4m=tand(Sgamma1_4m); %  y=5.3630x.^2

SD_1t=tand(Sgamma2_1t)-tand(Sgamma1_1t);
Sa_1t=SD_1t./(2.*C_stator.*cosd(Slambda_1t)); Sb_1t=tand(Sgamma1_1t); %  y=5.1949x.^2
SD_2t=tand(Sgamma2_2t)-tand(Sgamma1_2t);
Sa_2t=SD_2t./(2.*C_stator.*cosd(Slambda_2t)); Sb_2t=tand(Sgamma1_2t); %  y=5.1949x.^2
SD_3t=tand(Sgamma2_3t)-tand(Sgamma1_3t);
Sa_3t=SD_3t./(2.*C_stator.*cosd(Slambda_3t)); Sb_3t=tand(Sgamma1_3t); %  y=5.1949x.^2
SD_4t=tand(Sgamma2_4t)-tand(Sgamma1_4t);
Sa_4t=SD_4t./(2.*C_stator.*cosd(Slambda_4t)); Sb_4t=tand(Sgamma1_4t); %  y=5.1949x.^2