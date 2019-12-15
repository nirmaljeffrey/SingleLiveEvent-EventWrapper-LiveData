package com.example.singleliveeventsample;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ObserverViewModel extends ViewModel {

    private MutableLiveData<Boolean> liveDataNavigation = new MutableLiveData<>();
    private MutableLiveData<Boolean> liveDataToast = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> singleLiveEventToast = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> singleLiveEventNavigation = new SingleLiveEvent<>();

    public MutableLiveData<Boolean> getLiveDataToast() {
        return liveDataToast;
    }

    public void setLiveDataToast(Boolean liveDataToast) {
        this.liveDataToast.setValue(liveDataToast);
    }

    public SingleLiveEvent<Boolean> getSingleLiveEventToast() {
        return singleLiveEventToast;
    }

    public void setSingleLiveEventToast(Boolean singleLiveEventToast) {
        this.singleLiveEventToast.setValue(singleLiveEventToast);
    }


    public SingleLiveEvent<Boolean> getSingleLiveEventNavigation() {
        return singleLiveEventNavigation;
    }

    public void setSingleLiveEventNavigation(Boolean singleLiveEventNavigation) {
        this.singleLiveEventNavigation.setValue(singleLiveEventNavigation);
    }

    public MutableLiveData<Boolean> getLiveDataNavigation() {
        return liveDataNavigation;
    }

    public void setLiveDataNavigation(Boolean liveDataNavigation) {
        this.liveDataNavigation.setValue(liveDataNavigation);
    }

}
