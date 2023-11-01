import csv
import random
from datetime import date, timedelta

class Item:
    def __init__(self, itemId, itemName, expiryDate):
        self.itemId = itemId
        self.itemName = itemName
        self.expiryDate = expiryDate

def randomExpiryDate():
    startDate = date(2024, 1, 1)
    endDate = date(2024, 12, 31)
    randomDate = startDate + timedelta(days=random.randint(0, (endDate - startDate).days))
    return randomDate.strftime("%Y-%m-%d")

class Inventory:
    def __init__(self, csvFilename):
        self.csvFilename = csvFilename
        self.items = []

    def generateItems(self, numItems):
        items = ["Pet Food", "Pet Shampoo", "Pet Toy", "Pet Bed", "Pet Collar"]
        for i in range(1, numItems + 1):
            itemId = "{:04}".format(i)
            itemName = random.choice(items)
            expiryDate = randomExpiryDate()
            self.items.append(Item(itemId, itemName, expiryDate))

    def exportToCsv(self):
        with open(self.csvFilename, "w", newline="") as csvfile:
            csvWriter = csv.writer(csvfile)
            csvWriter.writerow(["ItemID", "ItemName", "ExpiryDate"])
            for item in self.items:
                csvWriter.writerow([item.itemId, item.itemName, item.expiryDate])

if __name__ == "__main__":
    csvFilename = "items3.csv"
    inventory = Inventory(csvFilename)
    inventory.generateItems(100)
    inventory.exportToCsv()
    print("CSV file generated:", csvFilename)
