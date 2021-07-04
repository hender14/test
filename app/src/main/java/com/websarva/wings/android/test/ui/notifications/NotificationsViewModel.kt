package com.websarva.wings.android.test.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "新着のお知らせはありません"
    }
    val text: LiveData<String> = _text
}