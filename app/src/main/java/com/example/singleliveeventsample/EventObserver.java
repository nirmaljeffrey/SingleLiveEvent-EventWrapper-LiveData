package com.example.singleliveeventsample;


import androidx.lifecycle.Observer;


public class EventObserver<T> implements Observer<Event<T>> {
    private EventHandler<T> onEventUnhandledContent;

    public EventObserver(EventHandler<T> onEventUnhandledContent) {
        this.onEventUnhandledContent = onEventUnhandledContent;
    }


    @Override
    public void onChanged(Event<T> event) {
        if(event != null){
                onEventUnhandledContent.onEventUnHandled(event.getContentIfNotHandled());
        }
    }



}

