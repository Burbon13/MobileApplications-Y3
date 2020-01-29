package com.burbon13.app.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.app.data.Repository
import com.burbon13.app.data.model.Message
import com.burbon13.app.data.model.MessageDto
import com.burbon13.app.utils.MyResult
import com.burbon13.app.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private val messagesMap = HashMap<String, MessageDto>()
    val messages = MutableLiveData<List<MessageDto>>()

    init {
        Log.d(TAG, "MainViewModel init initializing loading messages")
        viewModelScope.launch(Dispatchers.IO) {
            val result = Repository.getMessages()
            if (result.failed) {
                Log.e(TAG, "LOADING MESSAGES FAILED: ${(result as MyResult.Error).message}")
            } else {
                Log.d(TAG, "Messages received, arranging them")
                val messagesResult = result as MyResult.Success
                val messagesList = messagesResult.data
                messagesList.forEach { message -> insertMessage(message) }
                messages.postValue(arrangeMessagesList())
            }
        }
    }

    fun newMessage(message: Message) {
        Log.d(TAG, "New message received: $message")
        insertMessage(message)
        messages.postValue(arrangeMessagesList())
    }

    private fun insertMessage(message: Message) {
        if (!messagesMap.containsKey(message.sender)) {
            messagesMap[message.sender] = MessageDto(message.sender, 0, -1, ArrayList())
        }
        val messageDto = messagesMap[message.sender] as MessageDto
        messageDto.messages.add(message)
        if (!message.read) {
            messageDto.unread += 1
            messageDto.lastUnread = messageDto.lastUnread.coerceAtLeast(message.created)
        }
    }

    private fun arrangeMessagesList(): List<MessageDto> {
        val messagesList = messagesMap.values.toList()
        val sortedList = messagesList.sortedWith(compareBy({ -it.unread }, { -it.lastUnread }))
        sortedList.forEach { msgDto -> msgDto.messages.sortWith(compareBy { -it.created }) }
        return sortedList
    }

    fun setMessageRead(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.markMessageRead(message)
        }
        val msgDto = messagesMap[message.sender] as MessageDto
        val savedMsg = msgDto.messages.find { msg -> msg.id == message.id }
        savedMsg?.read = true
        msgDto.unread -= 1
    }
}
