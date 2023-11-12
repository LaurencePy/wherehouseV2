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

    @app.route('/get_data', methods=['GET'])
    def get_data():
        config_file_path = r"C:\Users\laeat\Documents\Coding\Wherehousev2\wherehouseV2\app\build\intermediates\assets\debug\config.json"
        warehouse = DataRetrieval(config_file_path)

        warehouse.load_config()
        warehouse.connect_to_database()
        warehouse.cursor.execute("SELECT * FROM tblSalesStatistics ORDER BY ItemID ASC")
        results = warehouse.cursor.fetchall()
        warehouse.close_connection()

        return jsonify(results)

    def close_connection(self):
        self.cursor.close()
        self.connection.close()

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
