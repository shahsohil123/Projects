__author__ = 'karanshah'

import csv
import math
import sys
import numpy as np
import random
import matplotlib.pylab as plt
import cPickle

# takes the csv data file and creates list for the attributes and
# the classification. We also store the entire sample in a list of list
class CreateData():
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

# Leaf Node will be used to test if we reach the end nodes of
# our decision tree. Its classification will be the final classification
class LeafNode():
    def __init__ (self,sample):
        self.sample=sample
        self.prd=[]

# Creates a decision tree that will classify the sample. It calculates the entropy and selects
# the best attribute  depending on the information gain.
class DecisionTree():
    def __init__(self,_sample,depth):

        #print "Create D Tree "
        self.sample=_sample
        self.len_of_sample = len(_sample)
        self.depth= depth
        self.sv =0
        self.sf =0
        self.prd=[]
        self.Dright = None
        self.Dleft = None
        self.y=[]
        self.takeSample(_sample)
        self.createTree(self.sample)

    # calcualte the probability for the sample
    def cal_prob(self,sample):
        cl1=0
        cl2=0
        cl3=0
        cl4=0

        len_sample=len(sample)
        prd=[]
        for cv in sample:
            if float(cv[2]) ==1:
                cl1= cl1+1
            if float(cv[2]) ==2:
                cl2= cl2+1
            if float(cv[2]) ==3:
                cl3= cl3+1
            if float(cv[2]) ==4:
                cl4= cl4+1
        if (len_sample !=0.0):
            if (cl1!=0.0):
                prd.append(float(cl1)/float(len_sample))
            else:
                prd.append(0.0)
            if (cl2!=0.0):
                prd.append(float(cl2)/float(len_sample))
            else:
                prd.append(0.0)
            if (cl3!=0.0):
                prd.append(float(cl3)/float(len_sample))
            else:
                prd.append(0.0)
            if (cl4!=0.0):
                prd.append(float(cl4)/float(len_sample))
            else:
                prd.append(0.0)
        return prd

    # calcualtes the entropy for the node
    def cal_entropy(self,y):

        class1=0
        class2=0
        class3=0
        class4=0
        totalfinal = 0
        #leny=len(y)
        count_total = len(y) # total samples

        # counting the number of class labels in the data
        for cv in y:
            if cv == 1.0:
                class1 = class1 + 1
            if cv == 2.0:
                class2 = class2 + 1
            if cv == 3.0:
                class3 = class3 + 1
            if cv == 4.0:
                class4 = class4 + 1
        #print class1, class2, class3, class4

        TotalClassValue = [float(class1),float(class2),float(class3),float(class4)]
        #print countofclass, "karan"
        # Calculating the entropy based class labels and total class samples
        for i in TotalClassValue:
            #print i
            #print count_total, "size"
            if (count_total!=0.0 and i!=0.0):
                totalfinal=totalfinal+((i/count_total)*math.log((i/count_total),2))
        #print totalfinal
        totalfinal=-1.0*totalfinal
        return totalfinal

    # Calculate the entropy for the left and right nodes which will be summed
    # subtracted from global entropy to get the information gain
    def split_entropy(self,ll,lg):
        #print
        lenof_less = len(ll)
        lenof_grt = len(lg)
        totalsample = lenof_grt + lenof_less

        # count of class labeld for both the split data
        c1_1=0
        c1_2=0
        c1_3=0
        c1_4=0
        cg_1=0
        cg_2=0
        cg_3=0
        cg_4=0

        # count the class labels
        for val in ll:
            if float(val[2]) == 1:
                c1_1 = c1_1 + 1
            if float(val[2]) == 2:
                c1_2 = c1_2 + 1
            if float(val[2]) == 3:
                c1_3 = c1_3 + 1
            if float(val[2]) == 4:
                c1_4 = c1_4 + 1

        for val in lg:
            if float(val[2]) == 1:
                cg_1 = cg_1 + 1
            if float(val[2]) == 2:
                cg_2 = cg_2 + 1
            if float(val[2]) == 3:
                cg_3 = cg_3 + 1
            if float(val[2]) == 4:
                cg_4 = cg_4 + 1

        cll = [float(c1_1),float(c1_2),float(c1_3),float(c1_4)]
        clg= [float(cg_1),float(cg_2),float(cg_3),float(cg_4)]

        # calculate the entropy for left node
        lessentropy = 0
        for i in cll:
            if(lenof_less !=0.0 and i!=0.0):
                lessentropy=lessentropy+((i/lenof_less)*math.log((i/lenof_less),2))
        lessentropy = -1.0 * lessentropy
        prob1 = (float(lenof_less)/float(totalsample))
        entropyforless = prob1 * lessentropy

        # calcualte the entropy for right node
        grtentropy = 0
        for i in clg:
            if(lenof_grt !=0.0 and i!=0.0):
                grtentropy=grtentropy+((i/lenof_grt)*math.log((i/lenof_grt),2))
        grtentropy = -1.0 * grtentropy
        prob2 = (float(lenof_grt)/float(totalsample))
        entropyforgrt = prob2 * grtentropy

        r = entropyforgrt +entropyforless
        return r

    # We partition data according to the random value into left and right
    # corresponding to split value and feature
    def parititon_data(self,sv,sf,sample):
        #print xcount
        lesslist= []
        greaterlist=[]
        for v in self.sample:
            if(sf == "a1"):
                if v[0] < sv:
                    lesslist.append(v)
                else:
                    greaterlist.append(v)
            else:
                if v[1] < sv:
                    lesslist.append(v)
                else:
                    greaterlist.append(v)
        #print "less", self.list_less
        #print "karan"
        #print "great",self.list_grt
        return lesslist,greaterlist

    # We calculate the inforamtion gain to select the best attribute
    # based on max
    def cal_infogain(self,y,sv,sf,sample):
        total_entropy = self.cal_entropy(y) # get global entropy
        #print total_entropy
        ll,lg = self.parititon_data(sv,sf,sample) # partition data
        r=self.split_entropy(ll,lg) # get summation of entropy of partitioned data
        #print r, "remain"
        info =0
        info = total_entropy - r
        return info

    # Get the class labels
    def takeSample(self,sample):
        for cv in sample:
            self.y.append(cv[2])

    # creates decision tree of dept n
    def TerminatingCond(self,depth):
        finaldepth= 2
        if(self.depth == finaldepth):
            return True
        else:
            return False

    # create tree runs a recursive loop for given depth
    def createTree(self,sample):
        k=3 # random value to be generated
        self.prd=self.cal_prob(sample) # calculate the probability dist
        Flag = self.TerminatingCond(self.depth)
        if Flag == True:
            return self
        yDist=len(set(self.y))
        if len(sample)==0:
            return LeafNode(self.sample)
        elif (yDist == 1):
            return LeafNode(self.y[0])
        else:
            IGList = [] # list of information gain
            count = 1
            randomlist = [] # randomly generated float number between  0 and 1
            for i in range(k*2):
                randomlist.append(random.uniform(0.0,1.0))
            #print randomlist
            # find best information gain
            for randval in randomlist:
                if count < 4:
                    IGList.append(self.cal_infogain(self.y,randval,"a1",sample))
                else:
                    IGList.append(self.cal_infogain(self.y,randval,"a2",sample))
                count = count + 1
            #print IGList,"IG"
            maxig = max(IGList)
            #print maxig
            # find the best split value and split feature
            self.sv=randomlist[IGList.index(maxig)]
            if(IGList.index(maxig) > k-1):
                self.sf="a2"
            else:
                self.sf="a1"
            # we partition data according to the found best split feature and value
            ll,lg=self.parititon_data(self.sv,self.sf,sample)
            self.Dleft=DecisionTree(ll,self.depth+1) # create left node
            self.Dright=DecisionTree(lg,self.depth+1) # create right node

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


def SSE(sample,epoch):
    dtr=[]
    file_namess='RandomForest'+str(epoch)+'.txt'
    with open(file_namess,'rb') as f:
        dtr=cPickle.load(f)
    f.close()
    orgdist=[]
    for s in sample:
        if s[2]==1:
            orgclass=[1,0,0,0]
            orgdist.append(orgclass)
        if s[2]==2:
            orgclass=[0,1,0,0]
            orgdist.append(orgclass)
        if s[2]==3:
            orgclass=[0,0,1,0]
            orgdist.append(orgclass)
        if s[2]==4:
            orgclass=[0,0,0,1]
            orgdist.append(orgclass)

    prd=[]
    SSE = []
    tsse=[]
    for e in range(1,epoch+1):
        sse=0
        ssecount = 0
        for s in sample:
            prob_value =[0.0,0.0,0.0,0.0]
            for q in range(0,e,1):
                pr_t=Classify_test(s,dtr[q])
                if len(pr_t) !=0.0:
                    for x in range(0,4):
                        prob_value[x]=prob_value[x]+pr_t[x]
                else:
                    for x in range(0,4):
                        prob_value[x]=0.0
            for m in range(0,4):
                prob_value[m]=prob_value[m]/float(e)
            tempsse=0
            for m in range(0,4):
                tempsse=tempsse+math.pow((prob_value[m]-orgdist[ssecount][m]),2)
            sse=sse+tempsse
            ssecount=ssecount+1
        SSE.append(sse)
    v=[]
    estclass=[]
    for i in range(1,epoch+1):
        v.append(i)
    if(epoch==1):
        plt.plot(1,SSE[0],'bo')
    plt.plot(v,SSE)
    plt.show()


# takes input the training data and create decision tree and
# stores in a a file using pickle
def main():
    if len(sys.argv) < 2 or len(sys.argv) >=3:
        print('Usage: python trainDT.py file.csv')
        return
    else:
        if len(sys.argv) == 2:
            file1=sys.argv[1]
            mydata=CreateData(file1)
            #dt=DecisionTree(mydata.data,0)
            file_name1 = 'RandomForest1.txt'
            randomforest=[]
            for i in range(1):
                temp = DecisionTree(mydata.data,0)
                randomforest.append(temp)
            f= open(file_name1,"wb")
            cPickle.dump(randomforest,f,protocol=cPickle.HIGHEST_PROTOCOL)
            f.close()
            file_name2 = 'RandomForest10.txt'
            randomforest=[]
            for i in range(10):
                temp = DecisionTree(mydata.data,0)
                randomforest.append(temp)
            f= open(file_name2,"wb")
            cPickle.dump(randomforest,f,protocol=cPickle.HIGHEST_PROTOCOL)
            f.close()
            file_name3 = 'RandomForest100.txt'
            randomforest=[]
            for i in range(100):
                temp = DecisionTree(mydata.data,0)
                randomforest.append(temp)
            f= open(file_name3,"wb")
            cPickle.dump(randomforest,f,protocol=cPickle.HIGHEST_PROTOCOL)
            f.close()
            file_name4 = 'RandomForest200.txt'
            randomforest=[]
            for i in range(200):
                temp = DecisionTree(mydata.data,0)
                randomforest.append(temp)
            f= open(file_name4,"wb")
            cPickle.dump(randomforest,f,protocol=cPickle.HIGHEST_PROTOCOL)
            f.close()
            #SSE(mydata.data,1)
            #SSE(mydata.data,10)
            #SSE(mydata.data,100)
            SSE(mydata.data,200)
            #print "end"

main()