# Wherehouse
Wherehouse Android application

Incorporates the use of a FLask server with a MySQL Database to remotely store wherehouse data. Controlled via a mobile application.

**Functions**

View Stock - displays all items in order of identifier code and views information about the item including item description, quantity in stock, location and expiry date. Includes a search bar to easily locate particular items based on the description

Edit Stock - Allows the user to choose an item and edit the amount of stock, change the name, and information of the item or location

Item withdrawals - This will prompt the user to scan the barcodes of item orders and then return the location of the items. Then the user will scan the items and when all items are scanned they can package and add shipping labels

Item deposits - This will prompt the user to search and select the item from the stock list and the quantity as well as the expiry date of the item. Then barcodes will be downloaded and can be printed for each item for scanning later on. It will then give the location for the item to be placed and it can be placed on the shelf.

Add new item type to stock - Allows the user to create a new item to be added to the database, with a description, name and a new identifier will be generated as well as a shelf location added

Stock Prediction - shows the same page as “view stock” but allows the user to select an item to view statistics on previous sales and generates an expected sale amount depending on previous data with the use of an artificial intelligence machine learning model

Download Data - Allows the user to download a selected table of data to their device. 


<p align="center">
  <img src="wherehouse half size.png" />
</p>
