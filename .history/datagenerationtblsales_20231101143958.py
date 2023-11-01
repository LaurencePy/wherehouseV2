import csv
import random
from datetime import date, timedelta

class Sale:
    def __init__(self, orderID, itemID, saleDate, salePrice, quantitySold):
        self.orderID = orderID
        self.itemID = itemID
        self.saleDate = saleDate
        self.salePrice = salePrice
        self.quantitySold = quantitySold

class SalesGenerator:
    def __init__(self, csvFilename):
        self.csvFilename = csvFilename
        self.sales = []

    def randomSaleDate(self):
        startdate = date(2022, 1, 1)
        enddate = date(2022, 12, 31)
        randomdate = random.randint(0, (enddate - startdate).days)
        return startdate + timedelta(days=randomdate)

    def generateSales(self, numSales):
        for i in range(1, numSales + 1):
            randomItemID = random.randint(1, 100)
            itemID = "{:04}".format(randomItemID)
            orderID = "{:04}".format(i)
            saleDate = self.randomSaleDate().strftime("%Y-%m-%d")
            randomNum = random.uniform(5, 100)
            salePrice = round(randomNum, 2)
            quantitySold = random.randint(1, 1000)
            self.sales.append(Sale(orderID, itemID, saleDate, salePrice, quantitySold))

    def exportToCsv(self):
        with open(self.csvFilename, "w", newline="") as csvfile:
            csvWriter = csv.writer(csvfile)
            csvWriter.writerow(["OrderID", "ItemID", "SaleDate", "SalePrice", "QuantitySold"])
            for sale in self.sales:
                csvWriter.writerow([sale.orderID, sale.itemID, sale.saleDate, sale.salePrice, sale.quantitySold])

if __name__ == "__main__":
    csvFilename = "tblSales.csv"
    salesGenerator = SalesGenerator(csvFilename)
    salesGenerator.generateSales(100)
    salesGenerator.exportToCsv()
    print("CSV file generated:", csvFilename)
