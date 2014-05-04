import pickle
import math
import csv
import sys


testData = []
costMatrix = {1:{1:0.2,2:-0.07,3:-0.07,4:-0.07}, 2:{1:-0.07,2:0.15,3:-0.07,4:-0.07}, 3:{1:-0.07,2:-0.07,3:0.05,4:-0.07}, 4:{1:-0.03,2:-0.03,3:-0.03,4:-0.03}}
Output = []
weightMatrix = []
confusionMatrix = {}
finalOutput = []

#reads input for testing from csv and plot them on graph
def initTestData():
    global testData
    import matplotlib.pyplot as pl
    with open('test_data.csv','rb') as file:
       line = csv.reader(file)
       k =0
       for row in line:
           #print row
           testData.append(map(float,row))
           if row[2] == '1':
               pl.plot(row[0],row[1],"bo")
           elif row[2] == '2':
               pl.plot(row[0],row[1],"ro")
           elif row[2] == '3':
               pl.plot(row[0],row[1],"go")
           elif row[2] == '4':
               pl.plot(row[0],row[1],"yo")
           k = k + 1
           if k == 20:
                break
    pl.show()

#claculation for hidden layer
def HiddenLayerOutput(hidden,row,bias1):
    global testData

    z = 0

    for i in range(0,2):
        z = z + (testData[row][i]*weightMatrix[i][hidden])
    z = z + weightMatrix[bias1][hidden]
    sigmoid = float(1/(1+math.exp((-1)*z)))
    return sigmoid

#calculations for output layer
def OutputLayerOutput(node,row,bias2,inputVal):
    global testData

    z = 0

    for i in range(3,8):
        z = z + (inputVal[i]*weightMatrix[i][node])
    z = z + weightMatrix[bias2][node]
    sigmoid = float(1/(1+math.exp((-1)*z)))
    return sigmoid

# calculations for calculating the total profit
def profit():
    current =0
    for i in range(1,5):
        for j in range(1,5):
            current = current + (costMatrix[i][j]*confusionMatrix[i][j])
    print 'Profit made :',current

#Prints the confusion matrix
def printconfusionMatrix():
    global confusionMatrix
    for i in range(1,5):
        for j in range(1,5):
            sys.stdout.write(str(confusionMatrix[i][j])+" ")
        print ''
    
#main method that gives the output of trained program
def updateConfMatrix(bias1,bias2):
    global trainingSet
    global weightMatrix
    global confusionMatrix
    global finalOutput
    correct = 0
    incorrect = 0
    
    #initialize confusion matrix
    for i in range(1,5):
        confusionMatrix[i] = {1:0,2:0,3:0,4:0}
    
    finalOutput = []

    for row in range(0,20):
        hiddenOut = {}
        #calculate hidden layer output
        for hidden in range(3,8):
            hiddenOut[hidden] = HiddenLayerOutput(hidden,row,bias1)
        OutputLayer = {}
        #calculate the outputlayer output
        for node in range(9,13):
            OutputLayer[node] = OutputLayerOutput(node,row,bias2,hiddenOut)
        atype = 0
        targetOutput = []
        actualOutput = testData[row][2]

        #decide which class it belongs to
        if actualOutput == 1:
            targetOut = [1,0,0,0]
            atype = 1
        elif actualOutput == 2:
            targetOut = [0,1,0,0]
            atype = 2
        elif actualOutput == 3:
            targetOut = [0,0,1,0]
            atype = 3
        elif actualOutput == 4 :
            targetOutput = [0,0,0,1]
            atype = 4

        maxi = -1
        index = -1
        for i in OutputLayer:
            if(OutputLayer[i] > maxi):
                maxi = OutputLayer[i]
                index = i

        type1 = -1
        counter = 1
        for node in range(9,13):
            if(index == node):
                type1 = counter
            counter = counter + 1
        #Calculate correct and incorrect
        if type1 == atype:
            correct = correct + 1
        else:
            incorrect = incorrect +1
        confusionMatrix[atype][type1] = confusionMatrix[atype][type1] +1
        tempOutput = []
        for i in range(9, 13):
                tempOutput.append(OutputLayer[i])
        tempOutput.append(type1)
        finalOutput.append(tempOutput)
    printconfusionMatrix()
    print 'percentage of correct classification : ', round(((float(correct)/20)*100),2)

continuousData = []
def plotContinuousData():
    import matplotlib.pyplot as plt
    import numpy as np
    bias1 = 2
    bias2 = 8
    x = np.arange(0,1,0.01)
    y = np.arange(0,1,0.01)
    for attr1 in range(0,100):
        for attr2 in range(0,100):
            #print x[attr1], "   ",y[attr2]
            
            hiddenOut = {}
            for hidden in range(3,8):
                z = 0
                z = z + (x[attr1]*weightMatrix[0][hidden])
                z = z + (y[attr2]*weightMatrix[1][hidden])
                z = z + weightMatrix[bias1][hidden]
                sigmoid = float(1/(1+math.exp((-1)*z)))
                hiddenOut[hidden] = sigmoid

            OutputLayer = {}
            for node in range(9,13):

                z = 0
                for i in range(3,8):
                    z = z + (hiddenOut[i]*weightMatrix[i][node])
                z = z + weightMatrix[bias2][node]
                sigmoid = float(1/(1+math.exp((-1)*z)))
                
                OutputLayer[node] = sigmoid

            #print OutputLayer
            maxi = -1
            index = -1
            for i in OutputLayer:
                if(OutputLayer[i] > maxi):
                    maxi = OutputLayer[i]
                    index = i
            #print index
            type1 = -1
            counter = 1

            for node in range(9,13):
                if(index == node):
                    type1 = counter
                counter = counter + 1

            color = ''
            if type1 == 1:
                color = "bs"
            elif type1 == 2:
                color = "rs"
            elif type1 == 3:
                color = "gs"
            elif type1 == 4:
                color = "ys"
            #print type1
            plt.plot(x[attr1],y[attr2],color)
    plt.axis([0,1,0,1])
    plt.show()
def main():
    global weightMatrix
    global finalOutput
    initTestData()
    
    f = open(sys.argv[1],'r')
    #print sys.argv[1]
    weightMatrix = pickle.load(f)
    updateConfMatrix(2,8)
    profit()
    plotContinuousData()

main()
