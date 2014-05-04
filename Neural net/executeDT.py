__author__ = 'karanshah'

import sys
import numpy as np
import matplotlib.pyplot as plt
import csv
import math
import random
import trainDT
import cPickle

# takes the csv data file and creates list for the attributes and
# the classification. We also store the entire sample in a list of list
class CreateData:
    def __init__ (self,file):
        #print "Input data"
        self.data = []
        self.sym=[]
        self.ecc=[]
        self.y=[]
        # Read the data from the file and append to the list
        for i in csv.reader(open(file, 'rb')):
            self.sym.append(float(i[0]))
            self.ecc.append(float(i[1]))
            self.y.append(float(i[2]))
            self.data.append([float(i[0]),float(i[1]),float(i[2])])
        #print "End of input data"

# we runt the decision tree on our test sample and classify according
# to the split value and feature
def Classify_test(sample,dtree):
    sym=sample[0]
    ecc=sample[1]
    #y=sample[2]
    curr = dtree
    while(curr!=None):
        if curr.sf=="a1":
            if sym<curr.sv:
                curr=curr.Dleft
                if(curr.Dleft==None):
                    pr=curr.prd
                    return pr
                continue
            elif sym>curr.sv:
                curr=curr.Dright
                if(curr.Dright==None):
                    pr=curr.prd
                    return pr
                continue
        elif curr.sf=="a2":
            if ecc<curr.sv:
                curr=curr.Dleft
                if(curr.Dleft==None):
                    pr=curr.prd
                    return pr
                continue
            elif ecc>curr.sv:
                curr=curr.Dright
                if(curr.Dright==None):
                    pr=curr.prd
                    return pr
                continue

# this prints the confusion matrix
def printme(confusionmatrix):
    #print len(confusionmatrix),"p"
    for i in confusionmatrix:
        print i

# creates the confusion matrix based on the estimated class and original class
def createConfusionMatrix(estclass,y):
    totaltestdata = len(y)
    #print estclass ,"Result CLass"
    #print y,"yOrignal Class"
    confusionmatrix=[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]
    #printme(confusionmatrix)
    count_error=0
    print "Confusion Matrix"
    # check for matching class labels and incorrect classification
    for i in range(len(estclass)):
        if y[i]== 1.0:
            if estclass[i] == 1.0:
                confusionmatrix[0][0]=confusionmatrix[0][0]+1
            if estclass[i] == 2.0:
                confusionmatrix[1][0]=confusionmatrix[1][0]+1
                count_error=count_error+1
            if estclass[i] == 3.0:
                count_error=count_error+1
                confusionmatrix[2][0]=confusionmatrix[2][0]+1
            if estclass[i] == 4.0:
                confusionmatrix[3][0]=confusionmatrix[3][0]+1
                count_error=count_error+1
        if y[i]== 2.0:
            if estclass[i] == 1.0:
                count_error=count_error+1
                confusionmatrix[0][1]=confusionmatrix[0][1]+1
            if estclass[i] == 2.0:
                confusionmatrix[1][1]=confusionmatrix[1][1]+1
            if estclass[i] == 3.0:
                count_error=count_error+1
                confusionmatrix[2][1]=confusionmatrix[2][1]+1
            if estclass[i] == 4.0:
                count_error=count_error+1
                confusionmatrix[3][1]=confusionmatrix[3][1]+1
        if y[i]== 3.0:
            if estclass[i] == 1.0:
                count_error=count_error+1
                confusionmatrix[0][2]=confusionmatrix[0][2]+1
            if estclass[i] == 2.0:
                count_error=count_error+1
                confusionmatrix[1][2]=confusionmatrix[1][2]+1
            if estclass[i] == 3.0:
                confusionmatrix[2][2]=confusionmatrix[2][2]+1
            if estclass[i] == 4.0:
                count_error=count_error+1
                confusionmatrix[3][2]=confusionmatrix[3][2]+1
        if y[i]== 4.0:
            if estclass[i] == 1.0:
                count_error=count_error+1
                confusionmatrix[0][3]=confusionmatrix[0][3]+1
            if estclass[i] == 2.0:
                count_error=count_error+1
                confusionmatrix[1][3]=confusionmatrix[1][3]+1
            if estclass[i] == 3.0:
                count_error=count_error+1
                confusionmatrix[2][3]=confusionmatrix[2][3]+1
            if estclass[i] == 4.0:
                confusionmatrix[3][3]=confusionmatrix[3][3]+1

    # WE calcualte the recognition rate by total correct classification divided by total samples * 100
    #print totaltestdata
    correctclassification= totaltestdata - count_error
    #print correctclassification
    recorate = float(correctclassification)/float(totaltestdata) * 100
    printme(confusionmatrix)
    print
    cal_profit(confusionmatrix)
    print "Classification Error", count_error
    print "Recognition Rate" , recorate, "%"

# calculate the profit from the confusion matrix
def cal_profit(confusionmatrix):
    add1=0
    add2=0
    add3=0
    add4=0
    for i in range(len(confusionmatrix)):
        if i==0:
            add1 = confusionmatrix[i][0]*0.2 - confusionmatrix[i][1]*0.07 - confusionmatrix[i][2]*0.07 - confusionmatrix[i][3]*0.07
        if i==1:
            add2 = confusionmatrix[i][0]* -0.07 + confusionmatrix[i][1]*0.15 - confusionmatrix[i][2]*0.07 - confusionmatrix[i][3]*0.07
        if i==2:
            add3 = confusionmatrix[i][0]*-0.07- confusionmatrix[i][1]*0.07 + confusionmatrix[i][2]*0.05 - confusionmatrix[i][3]*0.07
        if i==3:
            add4 = confusionmatrix[i][0]*-0.03 - confusionmatrix[i][1]*0.03 - confusionmatrix[i][2]*0.03 - confusionmatrix[i][3]*0.03

    final = add1+add2+add3+add4
    print "Profit", final

# plot the map for class regions from 00 to 11
def cmap(n,dtr):
    pointlist=[]
    for i in np.arange(0,1,0.01):
        for j in np.arange(0,1,0.01):
            point=[float(i),float(j)]
            pointlist.append(point)
    prd=[]
    for i in pointlist:
        prob_value=[0.0,0.0,0.0,0.0]
        for tv in dtr:
            pr_t=Classify_test(i,tv)
            if len(pr_t) !=0.0:
                for x in range(0,4):
                    prob_value[x]=prob_value[x]+pr_t[x]
            else:
                for x in range(0,4):
                    prob_value[x]=0.0
        for m in range(0,4):
            prob_value[m]=prob_value[m]/float(n)
        prd.append(prob_value)
    estclass=[]
    for i in prd:
        maxprd = max(i)
        classvalue  = i.index(maxprd)+1
        estclass.append(float(classvalue))

    count=0
    for i in estclass:
        if i==1:
            plt.plot(pointlist[count][0],pointlist[count][1], 'bo')
        if i==2:
            plt.plot(pointlist[count][0],pointlist[count][1], 'ro')
        if i==3:
            plt.plot(pointlist[count][0],pointlist[count][1], 'go')
        if i==4:
            plt.plot(pointlist[count][0],pointlist[count][1], 'yo')
        count=count+1
    plt.axis([0, 1, 0, 1])
    plt.show()

def main():
    if len(sys.argv) < 2 or len(sys.argv) >= 3 :
        print('Usage: python executeDT.py file.csv forest')
        return
    else:
        if len(sys.argv) == 2:
            file2=sys.argv[1]
            mydata=CreateData(file2)
            n=200
            file_name='RandomForest'+str(n)+'.txt'
            with open(file_name,'rb') as f:
                dtr=cPickle.load(f)
            f.close()
            sample=mydata.data
            prd=[]

            for i in sample:
                prob_value =[0.0,0.0,0.0,0.0]
                for tv in dtr:
                    pr_t=Classify_test(i,tv)
                    if len(pr_t) !=0.0:
                        for x in range(0,4):
                            prob_value[x]=prob_value[x]+pr_t[x]
                    else:
                        for x in range(0,4):
                            prob_value[x]=0.0
                for m in range(0,4):
                    prob_value[m]=prob_value[m]/float(n)
                prd.append(prob_value)
            estclass=[]
            for i in prd:
                maxprd = max(i)
                classvalue  = i.index(maxprd)+1
                estclass.append(float(classvalue))
            #print estclass
            cmap(n,dtr)
            createConfusionMatrix(estclass,mydata.y)

            #print "end"
main()