import csv
import random
from datetime import date, timedelta

class Item:
    def __init__(self, item_id, item_name, expiry_date):
        self.item_id = item_id
        self.item_name = item_name
        self.expiry_date = expiry_date

    @classmethod
    def random_expiry_date(cls):
        start_date = date(2024, 1, 1)
        end_date = date(2024, 12, 31)
        random_date = start_date + timedelta(days=random.randint(0, (end_date - start_date).days))
        return random_date

class Inventory:
    def __init__(self, csv_filename):
        self.csv_filename = csv_filename
        self.items = []

    def generate_items(self, num_items):
        items = ["Pet Food", "Pet Shampoo", "Pet Toy", "Pet Bed", "Pet Collar"]
        for i in range(1, num_items + 1):
            item_id = "{:04}".format(i)
            item_name = random.choice(items)
            expiry_date = Item.random_expiry_date().strftime("%Y-%m-%d")
            self.items.append(Item(item_id, item_name, expiry_date))

    def export_to_csv(self):
        with open(self.csv_filename, "w", newline="") as csvfile:
            csvwriter = csv.writer(csvfile)
            csvwriter.writerow(["ItemID", "ItemName", "ExpiryDate"])
            for item in self.items:
                csvwriter.writerow([item.item_id, item.item_name, item.expiry_date])

if __name__ == "__main__":
    csv_filename = "items3.csv"
    inventory = Inventory(csv_filename)
    inventory.generate_items(100)
    inventory.export_to_csv()
    print("CSV file generated:", csv_filename)
