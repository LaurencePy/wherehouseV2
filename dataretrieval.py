import mysql.connector
import json



with open("config.json", "r") as config:
    config = json.load(config)


user = config["user"]
password = config["password"]
host = config["host"]
database = config["database"]

# ^^ For security reasons, the connection details are kept seperate in a config.json file

connection = mysql.connector.connect(**config)
cursor = connection.cursor()


cursor.execute("SELECT * FROM tblItems")
result = cursor.fetchall()

for row in result:
    print(row)

cursor.close()
connection.close()