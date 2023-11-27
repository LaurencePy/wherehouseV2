import mysql.connector
import json
from flask import Flask, jsonify

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
            database=self.configdata["database"],
            port=self.configdata.get("port", 3306)
        )
        self.cursor = self.connection.cursor()

    @app.route('/get_data', methods=['GET'])
    def get_data():
        config_file_path = r"C:\Users\laeat\Documents\Coding\Wherehousev2\wherehouseV2\app\build\intermediates\assets\debug\config.json"
        warehouse = DataRetrieval(config_file_path)

        try:
            warehouse.cursor.execute("SELECT itemid, salesweek, salesmonth, salesyear FROM tblSalesStatistics WHERE itemid >= 94 ORDER BY itemid ASC")
            results = warehouse.cursor.fetchall()

            # Convert SQL query result to the desired format
            sales_data = [
                {"itemid": row[0], "salesweek": row[1], "salesmonth": row[2], "salesyear": row[3]}
                for row in results
            ]

            return jsonify(sales_data)

        except Exception as e:
            return jsonify({"error": str(e)})

        finally:
            warehouse.close_connection()

    def close_connection(self):
        self.cursor.close()
        self.connection.close()

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
