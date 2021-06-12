# the number of 1 in the input is odd -> output is 1
# else -> output is -1
# there are 128 inputs: consist of 1 or not

# 2-layer perceptron with backpropagation
# 1 hidden layer and 1 output layer
# 12 hidden layer nodes
# 1 output node

# every input's absolute error <= 0.1, then stop
# initial weights and biases randomly -1< w,b <1

import numpy as np
import itertools

# make all parity input patterns randomly
cand=[[1,-1],[1,-1],[1,-1],[1,-1],[1,-1],[1,-1],[1,-1]]
candpro=list(itertools.product(*cand))

input=[]
for i in candpro:
    input.append(list(i))

print(len(input))

# Make a true target of parity problem
target=[]
ip_cp=input.copy()
for i in ip_cp:
    count1=i.count(1)
    if count1%2 == 0:
        target.append(-1)
    else:
        target.append(1)
#print(target)

# make derivative of tanh
def diftanh(x):
    return (1/(np.cosh(x)**2))

# another non-linear function
def sigmoid(x):
    return 1/(1+np.exp(-x))

# derivative of sigmoid
def difsig(x):
    return sigmoid(x)*(1-sigmoid(x))


np.random.seed(4)

lr=0.005
alpha=1  # alpha is momentum

xx=[] # xx is learning rates 0.05 to 0.5 with 0.05++
yy=[] # yy is epoch

for i in range(1,11):  # for 0.005 - 0.05 learning rate
    learning_rate=lr*i
    xx.append(learning_rate)
    print("Learning rate:", learning_rate)

    # set initializations for each learning rate
    # create initial random weights and biases between -1 with 1 for 1st layer
    weights11 = np.random.uniform(-1, 1, size=(7, 12)).reshape(7, 12)  # [7,12]
    bias11 = np.random.uniform(-1, 1, size=(1, 12)).reshape(1, 12)  # [1,12]

    ## create initial random weights and biases between -1 with 1 for 2nd layer
    weights22 = np.random.uniform(-1, 1, size=(12, 1)).reshape(12, 1)  # [12,1]
    bias22 = np.random.uniform(-1, 1, size=(1, 1)).reshape(1, 1)  # [1,1]

    # make initial weights and biases for each inputs
    weights1 = []
    weights2 = []
    bias1 = []
    bias2 = []
    for i in range(len(input)):
        weights1.append(weights11)
        weights2.append(weights22)
        bias1.append(bias11)
        bias2.append(bias22)


    # Now start to train

    for epoch in range(100000):  # for all data: all done of 128 inputs

        input_E=[]
        for j in range(len(input)):  # for each data: 1 input

            # print(input[j])    #[1,7]
            # forward
            wx1=np.dot(input[j],weights1[j])+bias1[j]   # w1 * input + b  # should be [1,12]

            # Activation of wx+b : output of 1st layer   [1,12]
            yj=np.tanh(wx1)
            #yj=sigmoid(wx1)

            # output of 1st layer is input of 2nd layer
            wx2=np.dot(yj,weights2[j])+bias2[j]   # w2 * yj + b   [1,1]

            # Activation of wx+b : output of 2nd layer
            yk=np.tanh(wx2)     # [1,1]
            ### To use sigmoid, remove # of below line.
            #yk=2*sigmoid(wx2)-1 # to make range between -1< a <1


            # back propagation

            E=0.5*((target[j]-yk)**2)  # Cost = Error between true target amd predicted target  #[1,1]
            error=np.abs(target[j]-yk)  # absolute error

            dE_dyk= yk-target[j]  # (dE/de)*(de/dy) : e = target - y,  de/dy=-1   [1,1]
            dyk_dwx2= diftanh(wx2)  # dy/dwx1 = derivative or activation function, wx2 = weights2*yj+bias  [1,1]
            ### To use derivative of sigmoid, remove # of below line.
            #dyk_dwx2 = 2*difsig(wx2)


            delta_k=dE_dyk*dyk_dwx2   # this is delta_k  [1,1]
            dwx2_dw2=yj     # yj is output of 1st layer  [1, 12]

            grad_Ek=np.dot(dwx2_dw2.T, delta_k)  # get gradient = dE/dw2   [12,1]
            grad_Eb2= delta_k     # gradient bias dE/db2 = dE/dyk * dyk/dwx2 * dwx2/db2   dwx2/db2 = 1 [12,1]

            # dE/dw1= dE/dyj * dyj/dwx1 * dwx1/dw1
            # dE/dyj = dE/dwx2 * dwx2/yj    here, yj = x2
            dE_dwx2= dE_dyk*dyk_dwx2      #   [1,1]
            dwx2_dyj= weights2[j]    # wx2= weights2*yj + b   [1,12]

            dE_dyj=np.dot(dE_dwx2, dwx2_dyj.T)    # [1,12]
            dyj_dwx1=diftanh(wx1)    # [12,1]
            #dyj_dwx1=difsig(wx1)

            delta_j=dE_dyj* dyj_dwx1   # this is delta j  [1,12]

            dwx1_dw1=np.array(input[j]).reshape(1,7)   # d(wx+b)/dw = x = input  [7,1]

            grad_Ej=np.dot(dwx1_dw1.T, delta_j)  # get gradient dE_dw1
            grad_Eb1= delta_j  # dE/db2 = dE/dyj * dyj/dwx1 * dwx1/db1   dwx1/db1 = 1

            # update weights and biases with Gradient descent
            weights1[j]=alpha*weights1[j] - learning_rate*grad_Ej
            weights2[j]=alpha*weights2[j] - learning_rate*grad_Ek

            bias1[j]=bias1[j] - learning_rate*grad_Eb1
            bias2[j]=bias2[j] - learning_rate*grad_Eb2

        input_E.append(error.tolist())

        if np.mean(input_E) < 0.1:
            print("Epoch: ", epoch, "   Mean Error of 128 inputs: ", np.mean(input_E))
            yy.append(epoch)
            break


print("Learning rates:  ", xx)
print("Epochs:  ", yy)

dic={}
for i in range(len(xx)):
  dic[xx[i]]=yy[i]
print(dic)

import matplotlib.pyplot as plt

plt.xlabel('learning rate')
plt.ylabel('epoch')
plt.plot(xx,yy, '*-')
