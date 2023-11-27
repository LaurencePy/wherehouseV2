import mysql.connector
import json
from flask import Flask, jsonify, request

app = Flask(__name__)

class DataRetrieval:
    def __init__(self, config_path):
        self.config_path = config_path
        self.load_config()
        self.connect_to_database()

    def load_config(self):
        with open(self.config_path, "r") as config_file:
            self.configdata = json.load(config_file)

    def connect_to_database(self):
        self.connection = mysql.connector.connect(
            user=self.configdata["user"],
            password=self.configdata["password"],
            host=self.configdata["host"],
            database=self.configdata["database"]
        )
        self.cursor = self.connection.cursor()

    @app.route('/get_tblitems', methods=['GET'])
    def get_tblitems():
        return DataRetrieval().fetchdata("tblitems")

    @app.route('/get_tbllocations', methods=['GET'])
    def get_tbllocations():
        return DataRetrieval().fetchdata("tbllocations")

    @app.route('/get_tblsales', methods=['GET'])
    def get_tblsales():
        return DataRetrieval().fetchdata("tblsales")

    @app.route('/get_tblsalesstatistics', methods=['GET'])
    def get_tblsalesstatistics():
        return DataRetrieval().fetchdata("tblsalesstatistics")

    def fetchdata(self, table):
        SQLquery = f"SELECT * FROM {table} ORDER BY ItemID ASC"
        self.cursor.execute(SQLquery)
        results = self.cursor.fetchall()
        return jsonify(results)

    def close_connection(self):
        self.cursor.close()
        self.connection.close()

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
