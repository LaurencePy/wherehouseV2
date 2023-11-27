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

        data = []

        for row in results:
            
            row_dict = {}
            
            
            for i in range(len(cursor.description)):
                column_name = cursor.description[i][0]
                row_dict[column_name] = row[i]
            
            data.append(row_dict)

        DataRetrieval.close_connection(cursor, connection)
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
