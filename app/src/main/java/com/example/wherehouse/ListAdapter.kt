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
    objects: List<DataModel>
) : ArrayAdapter<DataModel>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)
        val item = getItem(position)

        val itemInfoTextView: TextView = view.findViewById(R.id.itemInfoTextView)

        val itemInfo = if (item != null) {
            "Item ID: ${item?.itemId}\nItem Name: ${item?.itemName}\nExpiry Date: ${item?.expiryDate}"
        } else {
            "No data available"
        }

        itemInfoTextView.text = itemInfo

        return view
    }
}
