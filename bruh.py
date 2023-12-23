import os
from flask import Flask, request, jsonify, send_from_directory

app = Flask(__name__)

# Your upload route remains the same

@app.route('/download/graph_1.png', methods=['GET'])
def download_graph(filename):
    target_directory = 'graphs'  # Replace with the actual path on your server
    
    if not os.path.exists(target_directory):
        return jsonify({"message": "Directory not found"}), 404
    
    file_path = os.path.join(target_directory, filename)
    
    if os.path.exists(file_path):
        return send_from_directory(target_directory, filename)
    else:
        return jsonify({"message": "File not found"}), 404

if __name__ == '__main__':
    app.run(host='192.168.1.2', port=5000)
