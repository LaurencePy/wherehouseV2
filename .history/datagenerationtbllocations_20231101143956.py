import csv
import random

class Location:
    def __init__(self, itemID, location):
        self.itemID = itemID
        self.location = location

class LocationGenerator:
    def __init__(self, csvFilename):
        self.csvFilename = csvFilename
        self.locations = []

    def generateLocations(self, numLocations):
        for i in range(1, numLocations + 1):
            itemID = "{:04}".format(i)
            randomLetter = str(random.choice('ABCDEFGHIJKLMNOPQRSTUVWXYZ'))
            randomNumber = str(random.randint(1, 9))
            location = randomLetter + randomNumber
            self.locations.append(Location(itemID, location))

    def exportToCsv(self):
        with open(self.csvFilename, "w", newline="") as csvfile:
            csvWriter = csv.writer(csvfile)
            csvWriter.writerow(["ItemID", "Location"])
            for location in self.locations:
                csvWriter.writerow([location.itemID, location.location])

if __name__ == "__main__":
    csvFilename = "tblLocations.csv"
    locationGenerator = LocationGenerator(csvFilename)
    locationGenerator.generateLocations(100)
    locationGenerator.exportToCsv()
    print("CSV file generated:", csvFilename)
