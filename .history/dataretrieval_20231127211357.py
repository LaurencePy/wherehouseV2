import mysql.connector
import json
from flask import Flask, jsonify

app = Flask(__name__)

class DataRetrieval:
    config_path = r"C:\Users\laeat\Documents\Coding\Wherehousev2\wherehouseV2\app\build\intermediates\assets\debug\config.json"

    @staticmethod
    def connect_to_database():
        with open(DataRetrieval.config_path, "r") as config_file:
            configdata = json.load(config_file)

        connection = mysql.connector.connect(
            user=configdata["user"],
            password=configdata["password"],
            host=configdata["host"],
            database=configdata["database"]
        )

        cursor = connection.cursor()
        return connection, cursor

    @staticmethod
    def close_connection(cursor, connection):
        cursor.close()
        connection.close()

    @staticmethod
    def fetchdata(table):
        connection, cursor = DataRetrieval.connect_to_database()

        SQLquery = f"SELECT * FROM {table} ORDER BY ItemID ASC"
        cursor.execute(SQLquery)
        results = cursor.fetchall()

        # Initialize an empty list to store the final result
        data = []

        # Iterate through each row in the result
        for row in results:
            # Initialize an empty dictionary for each row
            row_dict = {}
            
            # Iterate through each column in the row
            for i in range(len(cursor.description)):
                # Get the column name
                col_name = cursor.description[i][0]
                # Add the column name and corresponding value to the dictionary
                row_dict[col_name] = row[i]
            
            # Add the dictionary representing the row to the final list
            data.append(row_dict)

        DataRetrieval.close_connection(cursor, connection)
        
        # Convert the list of dictionaries into a JSON array of objects
        return jsonify(data)

@app.route('/get_tblitems', methods=['GET'])
def get_tblitems():
    return DataRetrieval.fetchdata("tblitems")

@app.route('/get_tbllocations', methods=['GET'])
def get_tbllocations():
    return DataRetrieval.fetchdata("tbllocations")

@app.route('/get_tblsales', methods=['GET'])
def get_tblsales():
    return DataRetrieval.fetchdata("tblsales")

@app.route('/get_tblsalesstatistics', methods=['GET'])
def get_tblsalesstatistics():
    return DataRetrieval.fetchdata("tblsalesstatistics")

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
