import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from datetime import date, timedelta
import os

class StockPrediction:
    def __init__(self):
        self.coefficients = None
        self.cost_history = None

    def read_data(self):

        # importing necessary csv data files
        self.tblsalesstatistics_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database\tblSalesStatistics.csv")
        self.tblsales_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database\tblSales.csv")
        self.tblitems_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database\tblitems.csv")
        self.sales_predict_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database\sales_predict_data.csv")

        # handling date and time format using the pandas library

        self.tblsales_data["SaleDate"] = pd.to_datetime(self.tblsales_data["SaleDate"])
        self.tblitems_data["ExpiryDate"] = pd.to_datetime(self.tblitems_data["ExpiryDate"])

        # extracting and merging data for use later on
        self.tblsalesstatistics_data_extract = self.tblsalesstatistics_data[["ItemID", "SalesWeek", "SalesMonth"]]
        self.tblitems_data_extract = self.tblitems_data[["ItemID", "ExpiryDate"]]
        expiry = self.tblitems_data["ExpiryDate"]
        
        self.tblitems_data_extract["days_to_expire"] = (self.tblitems_data["ExpiryDate"] - self.tblsales_data["SaleDate"]).dt.days
        self.tblitems_days_to_expire_extract = self.tblitems_data_extract[["ItemID", "days_to_expire"]]

        # merging on ItemID values, to keep the correct corresponding values across tables
        # as ItemID is a primary key

        self.first_merge = pd.merge(self.tblsalesstatistics_data_extract, self.tblitems_days_to_expire_extract, on="ItemID", how="left")
        self.merged_data = self.first_merge.drop_duplicates(subset="ItemID", keep="first")
        
        self.concatenation_with_predict_data = pd.concat([self.merged_data, self.sales_predict_data], axis=1)
        self.normalise_data = self.concatenation_with_predict_data[["SalesWeek", "SalesMonth", "days_to_expire", "predict_sales"]]

        # assigning ItemID to avoid_data to mark it so that it's not used in calculations
        self.avoid_data = self.merged_data["ItemID"]


        self.normalised_data = (self.normalise_data - self.normalise_data.mean()) / self.normalise_data.std()
        self.normalised2_data = pd.concat([self.avoid_data, self.normalised_data.reset_index(drop=True)], axis=1)

    def prepare_inputs_outputs(self):

        # iloc used to select data by row and columns with the pandas library
        # slice of data with :, 1: to select all rows, but avoid the ItemID column with 1:
        self.inputs = self.normalised2_data.iloc[:, 1:]

        # ones is to make an array of ones with numpy
        ones = np.ones([self.inputs.shape[0], 1])

        # then I concatenate that to the inputs for use as a bias term
        # this is the error created by approximating a real world issue/calculation with a model that is not complex enough
        self.inputs = np.concatenate((ones, self.inputs), axis=1)

        self.outputs = self.normalised2_data["predict_sales"].values
        expiry = self.tblitems_data["ExpiryDate"]

        #storing coefficients for the model, by first setting the array to 0's to then be adjusted.
        self.coefficients = np.zeros([1, self.inputs.shape[1]])
        return expiry

    def compute_cost(self):

        # using numpy for multiplication of matrices using the inputs and parameters/coefficients.
        sum1 = np.power(((self.inputs @ self.coefficients.T) - self.outputs), 2)
        # Mean squared error (MSE) calculated 
        return np.sum(sum1) / (2 * len(self.inputs))

    def gradient_descent(self, learning_rate, iterations):
        # creating an array using numpy of 0's with the number of iterations so the computer cost can be stored
        cost_history = np.zeros(iterations)
        for i in range(iterations):

            # .T transposes the coefficients from a row to a column vector
            # @ used for multipliction of matrices inputs and coefficients in column vector form
            prediction_errors = self.inputs @ self.coefficients.T - self.outputs
            self.coefficients = self.coefficients - (learning_rate / len(self.inputs)) * (prediction_errors.T @ self.inputs)
            cost_history[i] = self.compute_cost()
        self.cost_history = cost_history

    def predict_sales_for_item(self, item_id_input):
        # validation
        if item_id_input > 0:
            # filter for the inputted item ID to only use that data
            item_data = self.normalised2_data[self.normalised2_data['ItemID'] == item_id_input]

            sales_month = item_data["SalesMonth"].values
            sales_amount = item_data["predict_sales"].values

            sales_amount = sales_amount * self.normalise_data["predict_sales"].std() + self.normalise_data["predict_sales"].mean()
            
            sales_year = sales_month * 30

            # constructing and plotting the graph to be viewed in the mobile application
            plt.figure(figsize=(10, 6))
            plt.plot(sales_year, sales_amount, marker="o", label="Predicted Sales (SalesMonth)", color="blue")
            plt.xlabel("Time Period (days)")
            plt.ylabel("Predicted Sales")
            plt.title(f"Predicted Sales for ItemID {item_id_input}")
            plt.grid(True)
            plt.tight_layout()
            plt.legend()
            #plt.show()
            plt.xlim(0, 30)
            # downloading the graph as an image for transfer
            self.saving_graph(item_id_input, graph_location)
        else:
            print("Invalid Item ID input")

    def saving_graph(self, item_id, graph_location):
        plt.savefig(os.path.join(graph_location, f'graph_{item_id}.png'))
        plt.close()



if __name__ == "__main__":
    graph_location = r'C:\Users\laeat\Documents\Coding\Wherehousev2\wherehouseV2\graphs'
    stock_prediction = StockPrediction()
    stock_prediction.read_data()
    expiry = stock_prediction.prepare_inputs_outputs()
    learning_rate = 0.1
    iterations = 1000
    stock_prediction.gradient_descent(learning_rate, iterations)
    final_cost = stock_prediction.compute_cost()

    #print("Optimised coefficients:", stock_prediction.coefficients)
    
    #print("Final cost:", final_cost)

    #item_ID_input = int(input("Enter the ItemID to predict SalesWeek: "))
    #stock_prediction.predict_sales_for_item(item_ID_input)
    
    #for item_id in range(1, 101):
    #    stock_prediction.predict_sales_for_item(item_id)
