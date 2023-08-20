import android.util.Pair
import java.io.FileReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import com.google.gson.JsonParser
import java.sql.Connection
import com.google.gson.Gson
import java.io.Reader
import android.util.Log
object DatabaseConnection {
    fun getConnection(configFilePath: String): Connection {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val jdbcUrl = "jdbc:mysql://35.242.177.204:3306/wherehouse1"
            val (username, password) = readCredentialsFromConfig(configFilePath)
            DriverManager.getConnection(jdbcUrl, username, password)
        } catch (e: ClassNotFoundException) {
            throw SQLException("MySQL JDBC driver not found")
        }
    }

    private fun readCredentialsFromConfig(configFilePath: String): Pair<String, String> {
        val reader: Reader = FileReader(configFilePath)
        val gson = Gson()
        val json = gson.fromJson(reader, Config::class.java)

        val username = json.username
        val password = json.password

        return Pair(username, password)
    }

    fun getAllData(connection: Connection, tableName: String, columnName: String) {
        val query = "SELECT * FROM $tableName"
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            while (resultSet.next()) {
                val data = resultSet.getString(columnName)
                Log.d("DatabaseConnection", data)
            }

            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

fun main() {
    try {
        val configFilePath = "C:\\Users\\laeat\\Documents\\Coding\\config.json"
        val connection = DatabaseConnection.getConnection(configFilePath)
        DatabaseConnection.getAllData(connection, "tblitems", "ItemID")
    } catch (e: SQLException) {
        e.printStackTrace()
    }
}
