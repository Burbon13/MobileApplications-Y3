package com.burbon13.app.screens

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.burbon13.app.R
import com.burbon13.app.data.model.Message
import com.burbon13.app.data.model.MessageDto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ExpandableListAdapter(
    private val context: Context,
    var messageDto: List<MessageDto>,
    private val mainViewModel: MainViewModel
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Message {
        return messageDto[groupPosition].messages[childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        val message = getChild(groupPosition, childPosition)
        val childText = message.text
        if (convertView == null) {
            val infalInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item, null)
        }
        val txtListChild = convertView?.findViewById(R.id.lblListItem) as TextView
        txtListChild.text = childText
        if(!message.read) {
            mainViewModel.setMessageRead(message)
            convertView.setBackgroundColor(Color.CYAN)
            GlobalScope.launch {
                delay(1000)
                convertView.setBackgroundColor(Color.WHITE)
            }
        }
        Log.d("HAAAAAAA", "groupPos=$groupPosition childPos=$childPosition")
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return messageDto[groupPosition].messages.size
    }

    override fun getGroup(groupPosition: Int): MessageDto {
        return messageDto[groupPosition]
    }

    override fun getGroupCount(): Int {
        return messageDto.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        val msgDto = getGroup(groupPosition)
        val headerTitle = msgDto.sender + " [" + msgDto.unread + "]"
        if (convertView == null) {
            val infalInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group, null)
        }
        val lblListHeader = convertView?.findViewById(R.id.lblListHeader) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}