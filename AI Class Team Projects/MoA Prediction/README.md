# final - Bo Peng, Kyeong Joo Jung, Jinhee Lee, Zhenhao Lu

# File organization
-----------------------------------------------------
input directory: includes datasets

tabnet directory: includes codes and files needed for tabnet method

PytorchNN.py

Datasets.py

README

keras_multilabel.py

krr.py

util.py 

# File explanation
-----------------------------------------------------
Please check that the four input data files are in ./input/lish-moa

Final method and methods to improve the performance
-----------------------------------
util.py : the code for the preprocess 

keras_multilabel.py is the code for our method 

&ensp;&ensp;Packages needed: sys, iterstrat.ml_stratifiers, numpy, pandas, tensorflow, tensorflow_addons, sklearn.model_selection, sklearn, tqdm, typing, utils 

&ensp;&ensp;Run Command: python3 keras_multilabel.py
  
-----

Compared methods
-----------------------------------

krr.py is the code for compared method: Kernel Logistic Regression

&ensp;&ensp;Package used in this method: pandas, numpy, sklearn, iterstrat.ml_stratifiers, tqdm

&ensp;&ensp;Run Command: python3 krr.py

---------

PytorchNN.py and Datasets.py are the code for compared method: Multi-layer Perceptrons with Pytorch. 

&ensp;&ensp;Notes before running: Datasets.py are in the same folder as PytorchNN.py

&ensp;&ensp;Package used in this method: pandas, numpy, torch, sklearn

&ensp;&ensp;Run Command: python3 PytorchNN.py

---------

Inside tabnet directory: 

run.py is the code for compared method: Tabnet 

&ensp;&ensp;Run Command: python run.py
  
---------