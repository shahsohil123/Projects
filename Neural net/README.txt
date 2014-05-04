For MLP:

To run trainMLP.py the input should be in format of

python trainMLP.py

It takes train_data.csv inside the program no need to provide it in command line.
it plots all the data 1st then you have to close it and it starts the classification process and you
 will have to wait for a few seconds before a learning curve appears on the screen.

to run executeMLP.py the input should be in format of

python executeMLP.py (weightMatrix.txt)

weightmatrix.txt can be the weight matrix that file made by trainMLP.py. Files eg. weightMatrix1.txt



To run trainDT the input should be in format of 

python trainDT.py file.csv

where file.csv is the data that you want to train.(In our project it is train_data.csv). We create random forest text files for 1, 10 ,100, 200 that will contain the  decision tree which are then used for testing.
We generate and plot the learning curve for 200.

To run the executeDT the input should be in the format of

python executeDT.py file.csv

where file.csv is the file you want to test.(In this project it is test_data.csv). We plot the learning curve  followed by class regions for our testing data on  n=200. The output will be a confusion matrix where the diagonal represents the correct classification. Classification error, recognition rate and profit are printed using the confusion matrix.