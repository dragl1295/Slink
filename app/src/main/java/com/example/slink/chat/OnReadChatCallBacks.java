package com.example.slink.chat;


import com.example.slink.model.Chats;

import java.util.List;

public interface OnReadChatCallBacks {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
