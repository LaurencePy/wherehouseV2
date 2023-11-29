package com.example.wherehouse
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.wherehouse.R
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter(
    context: Context,
    resource: Int,
    objects: List<DataModel>       // Reference to my DataModel containing the different data items
) : ArrayAdapter<DataModel>(context, resource, objects) {

    // Called for every item on the HTTP server
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Creates views to display the data in the app interface (my xml file)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)

        // Get the data (Item ID, ItemName, and Expiry Date) for the current item
        val item = getItem(position)

        // Finds the part of the layout/xml file for where to display the item information
        val itemInfoTextView: TextView = view.findViewById(R.id.itemInfoTextView)

        val itemInfo = if (item != null) {
            "Item ID: ${item?.itemId}\nItem Name: ${item.itemName}\nExpiry Date: ${item?.expiryDate}"
        } else {
            // error handling
            "No data available"
        }

        // Displays the item data in the layout
        itemInfoTextView.text = itemInfo

        return view
    }
}


