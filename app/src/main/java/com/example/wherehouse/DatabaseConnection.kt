import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.google.gson.Gson
import java.io.InputStreamReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

data class Config(val username: String, val password: String)

object DatabaseConnection {
    fun getConnection(assetManager: AssetManager, configFileName: String): Connection {
        return try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val jdbcUrl = "jdbc:mysql://35.242.177.204:3306/wherehouse1"
            val (username, password) = readCredentialsFromConfig(assetManager, configFileName)
            DriverManager.getConnection(jdbcUrl, username, password)
        } catch (e: ClassNotFoundException) {
            throw SQLException("MySQL JDBC driver not found")
        }
    }

    private fun readCredentialsFromConfig(assetManager: AssetManager, configFileName: String): Pair<String, String> {
        val gson = Gson()
        val json = assetManager.open(configFileName).bufferedReader().use { it.readText() }
        val config = gson.fromJson(json, Config::class.java)

        val username = config.username
        val password = config.password

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

fun main(context: Context) {
    try {
        val configFileName = "config.json"
        val assetManager: AssetManager = context.assets

        val connection = DatabaseConnection.getConnection(assetManager, configFileName)
        DatabaseConnection.getAllData(connection, "tblitems", "ItemID")
    } catch (e: SQLException) {
        e.printStackTrace()
    }
}
