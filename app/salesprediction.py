import mysql.connector

config = {
    'user': 'root',
    'password': 'Mali0208172001',
    'host': '35.242.177.204',
    'database': 'schema1'
}

connection = mysql.connector.connect(**config)
cursor = connection.cursor()

query = "SELECT * FROM tblItems"
cursor.execute(query)
result = cursor.fetchall()

for row in result:
    print(row)

cursor.close()
connection.close()