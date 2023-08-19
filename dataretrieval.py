import mysql.connector
import json


with open("C:\\Users\\laeat\\Documents\\Coding\\config.json", "r") as config:
    configdata = json.load(config)


connection = mysql.connector.connect(
    user = configdata["user"],
    password = configdata["password"],
    host = configdata["host"],
    database = configdata["database"]
)
# ^^ For security reasons, the connection details are kept seperate in a config.json file


cursor = connection.cursor()


cursor.execute("SELECT * FROM tblItems")
result = cursor.fetchall()

for row in result:
    print(row)

cursor.close()
connection.close()