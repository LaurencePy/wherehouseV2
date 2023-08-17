import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

tblsalesstatistics_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database stuff\tblSalesStatistics.csv")
tblsales_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database stuff\tblSales.csv")
tblitems_data = pd.read_csv(r"C:\Users\laeat\Documents\Coding\wherehouse database stuff\tblitems.csv")

tblsales_data["SaleDate"] = pd.to_datetime(tblsales_data["SaleDate"])
tblitems_data["ExpiryDate"] = pd.to_datetime(tblitems_data["ExpiryDate"])

tblsalesstatistics_data_extract = tblsalesstatistics_data[["ItemID", "SalesWeek", "SalesMonth"]]
tblitems_data_extract = tblitems_data[["ItemID", "ExpiryDate"]]

tblitems_data_extract["days_to_expire"] = (tblitems_data["ExpiryDate"] - tblsales_data["SaleDate"]).dt.days

tblitems_days_to_expire_extract = tblitems_data_extract[["ItemID", "days_to_expire"]]

first_merge = pd.merge(tblsalesstatistics_data_extract, tblitems_days_to_expire_extract, on="ItemID", how="left")

merged_data = first_merge.drop_duplicates(subset="ItemID", keep="first")

normalise_data = merged_data[["SalesWeek", "SalesMonth", "days_to_expire"]]
avoid_data = merged_data["ItemID"]

normalised_data = (normalise_data - normalise_data.mean()) / normalise_data.std()

normalised2_data = pd.concat([avoid_data, normalised_data.reset_index(drop=True)], axis=1)

print(normalised2_data.head(10))
