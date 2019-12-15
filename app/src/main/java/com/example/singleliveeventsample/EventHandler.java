package com.example.singleliveeventsample;

public interface EventHandler<V>{
    void onEventUnHandled(V obj);
}
