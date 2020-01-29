package com.burbon13.app.data

import android.util.Log
import com.burbon13.app.data.model.Message
import com.burbon13.app.data.model.MessageDatabase
import com.burbon13.app.utils.MyApp
import com.burbon13.app.utils.MyResult
import com.burbon13.app.utils.TAG
import java.lang.Exception


object Repository {
    private val messageDao = MessageDatabase.getDatabase(
        MyApp.context ?: throw Exception("Unable to retrieve Application context")
    ).messaggeDao()

    suspend fun getMessages(): MyResult<List<Message>> {
        Log.d(TAG, "Retrieving messages repository")
        val result = MessageDataSource.getMessages()
        persistMessages(MessageDataSource.getMessages())
        return result
    }

    suspend fun markMessageRead(message: Message) {
        message.read = true
        message.read = true
        Log.d(TAG,"Server update request")
        MessageDataSource.update(message)
        Log.d(TAG, "Local update request")
        messageDao.update(message)
    }

    private suspend fun persistMessages(messagesResult: MyResult<List<Message>>) {
        Log.d(TAG, "Persisting locally messages")
        if(messagesResult.succeeded) {
            val sucResult = messagesResult as MyResult.Success
            sucResult.data.forEach { msg -> messageDao.insert(msg) }
        }
    }
}