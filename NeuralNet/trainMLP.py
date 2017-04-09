import random
import csv
import pickle
import math
import copy

weightMatrix = {}
trainingData = []
squaredError = []
#This method fetches data from the csv file and populates into the trainingData[] and also plots the input points.
def getData():
    global trainingData
    import matplotlib.pyplot as pl
    with open('train_data.csv','rb') as file:
       line = csv.reader(file)
       for row in line:
           trainingData.append(map(float,row))
           if row[2] == '1':
               pl.plot(row[0],row[1],"bo")
           elif row[2] == '2':
               pl.plot(row[0],row[1],"ro")
           elif row[2] == '3':
               pl.plot(row[0],row[1],"go")
           elif row[2] == '4':
               pl.plot(row[0],row[1],"yo")
    pl.show()

#this method sets up the weight matrix it assigns random weights to the matrix between (-1,1)
def init():
    global weightMatrix

    matrix = {}
    row1 = {}
    row2 = {}
    for inputNode in range(0,3):
        random.seed()
        for hidden in range(3,8):
            row1[hidden] = random.uniform(-1.0,1.0)
        weightMatrix[inputNode] = row1
        row1 = {}

    for hidden in range(3,9):
        random.seed()
        for outputNode in range(9,13):
            row2[outputNode] = random.uniform(-1.0,1.0)
        weightMatrix[hidden] = row2
        row2 = {}

# Calculates the squared eroor. Calculates the learning rate
def squaredErrorSum(actualOutput, calculatedOutput):
    Sum = 0.0
    Count = 0
    output = sorted(calculatedOutput)
    for node in output:
            Sum = Sum + (calculatedOutput[node]-actualOutput[Count])**2
            Count = Count + 1
	
    return float(Sum)

#Output to hidden layer is calculated in this method
def calculateHiddenOutput(i,count,bias):
    global trainingData
    global weightMatrix
    z = 0.0
    for k in range(0,2):
        z = z+(trainingData[count][k]*weightMatrix[k][i])
    z = z + weightMatrix[bias][i]
    sigmoid = float(1/(1+math.exp((-1)*z)))
    return sigmoid

# output to output layer is calculated here.
def calculateOutputLayer(i,bias,hiddenOutput):
    global weightMatrix

    z = 0.0
    for hidden in range(3,8):
        z = z + (hiddenOutput[hidden]*weightMatrix[hidden][i])
    z = z + weightMatrix[bias][i]

    sigmoid = float(1/(1+math.exp((-1)*z)))
    return sigmoid

#The main method that runs and updates the weightmatrix for each dataset    
def MLP(epoch,bias1,bias2,weightFile):
    global weightMatrix
    global squaredError
    alpha = 0.1
    sumOfSquaredError = 0.0
    
    for count in range(0,epoch):
        sumOfSquaredError = 0.0
        weightsBefore = copy.deepcopy(weightMatrix)
        cor = 0
        incor = 0
        for row in range(0,74):
            delta = {}
            for i in range(3,13):
                if (i == bias2):
                    pass
                else:
                    delta[i] = 0.0

                    
            #hidden layer calculations
            hiddenOutput = {}

            for i in range(3,8):
                hiddenOutput[i] = calculateHiddenOutput(i,row,bias1)
                
            #output layer calculations
            outputLayer = {}
        
            for i in range(9,13):
                outputLayer[i] = calculateOutputLayer(i,bias2,hiddenOutput)
            
            index = 0
            temp = 0
            for ii in range(9,13):
                if(outputLayer[ii]>temp):
                    index = ii-8
                    temp = outputLayer[ii]
                    
            #identifies the classification from the files
            actualClassification = trainingData[row][2]
            targetOut = []
            if(actualClassification  == 1):
                targetOut = [1,0,0,0]
            elif(actualClassification  == 2):
                targetOut = [0,1,0,0]
            elif(actualClassification  == 3):
                targetOut = [0,0,1,0]
            elif(actualClassification  == 4):
                targetOut = [0,0,0,1]
            targetOutCount = 0
            for out in range(9,13):
                delta[out] = (outputLayer[out] - targetOut[targetOutCount])*outputLayer[out]*(1-outputLayer[out])
                targetOutCount = targetOutCount + 1

            for hidden in range(3,8):
                delout = 0.0
                for out in range(9,13):
                    delout = delout + delta[out]* weightMatrix[hidden][out]
                    delta[hidden] = delout*hiddenOutput[hidden]*(1-hiddenOutput[hidden])
                
            hiddenOutput[bias2] = 1.0
            #updates the weights at hidden to output edge
            for hidden in range(3,9):
                for out in range(9,13):
                    weightMatrix[hidden][out] = weightMatrix[hidden][out] - (delta[out]*hiddenOutput[hidden]*alpha)

            index1 =0
            #updates the weights at input to hidden edge
            for inputNode in range(0,2):
                for hidden in range(3,8):
                    weightMatrix[inputNode][hidden] = weightMatrix[inputNode][hidden] - (delta[hidden]*trainingData[row][index1]*alpha)
                index1 = index1 +1

            #updates for bias
            for out in range(3, 8):
		weightMatrix[2][out] = weightMatrix[2][out] - (delta[out]*1*alpha)#since bias is 1

            #calculate squared error
            error = squaredErrorSum(targetOut, outputLayer)
            sumOfSquaredError = sumOfSquaredError + error
            
        #copy weight matrix to appropriate files
        squaredError.append(sumOfSquaredError)
        if count == 0 :            
            f = open('WeightMatrix1.txt','w')
            pickle.dump(weightMatrix,f)
            f.close()
        elif count == 10 :
            f = open('WeightMatrix2.txt','w')
            pickle.dump(weightMatrix,f)
            f.close()
        elif count == 100:
            f = open('WeightMatrix3.txt','w')
            pickle.dump(weightMatrix,f)
            f.close()
        elif count == 1000:
            f = open('WeightMatrix4.txt','w')
            pickle.dump(weightMatrix,f)
            f.close()
        elif count == 9999:
            f = open('WeightMatrix5.txt','w')
            pickle.dump(weightMatrix,f)
            f.close()
def plot():
    import matplotlib.pyplot as plt
    
    plt.plot(squaredError,"blue")
   
    plt.show()    

def main():
    global squaredError
    init()
    getData()
    bias1 = 2
    bias2 = 8
    squaredArray = []
    MLP(10000,bias1,bias2,'weightMatrix4.txt')
    plot()

main()
