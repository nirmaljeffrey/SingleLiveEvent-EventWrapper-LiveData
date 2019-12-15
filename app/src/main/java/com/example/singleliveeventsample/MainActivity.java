package com.example.singleliveeventsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.singleliveeventsample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ObserverViewModel viewModel = ViewModelProviders.of(this).get(ObserverViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getEventNavigation().observe(this, new EventObserver<>(new EventHandler<Object>() {

            @Override
            public void onEventUnHandled(Object object) {
                Intent intent = new Intent(MainActivity.this, EventWrapperActivity.class);
                startActivity(intent);
                Log.d("tag", "event wrapper: intent trigger ");
            }
        }));

        viewModel.getEventToast().observe(this, new EventObserver<>(new EventHandler<Object>() {

            @Override
            public void onEventUnHandled(Object object) {
                Toast.makeText(MainActivity.this, "event wrapper toast", Toast.LENGTH_SHORT).show();
                Log.d("tag", "event wrapper: observer one ");
            }
        }));


        viewModel.getEventToast().observe(this, new Observer<Event<Object>>() {
                    @Override
                    public void onChanged(Event<Object> objectEvent) {
                        if(objectEvent != null){
                            if(objectEvent.peekContent() != null){
                                Log.d("tag", "event wrapper: observer two ");
                            }
                        }

                    }});


        viewModel.getLiveDataNavigation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCLicked) {
                if (isCLicked) {
                    Intent intent = new Intent(MainActivity.this, LiveDataActivity.class);
                    startActivity(intent);

                    Log.d("tag", "livedata: intent trigger ");
                }
            }
        });

        viewModel.getSingleLiveEventNavigation().observeSingleEvent(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCLicked) {
                if (isCLicked) {
                    Log.d("tag", "single live event: intent trigger ");
                    Intent intent = new Intent(MainActivity.this, SingleEventActivity.class);
                    startActivity(intent);

                }
            }
        });
        viewModel.getSingleLiveEventToast().observeSingleEvent(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCLicked) {
                if (isCLicked) {
                    Toast.makeText(MainActivity.this, "single event toast", Toast.LENGTH_SHORT).show();
                    Log.d("tag", "single live event: observer one ");
                }
            }
        });
        viewModel.getSingleLiveEventToast().observeSingleEvent(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCLicked) {
                if (isCLicked) {
                    Log.d("tag", "single live event: observer two ");
                }
            }
        });

        viewModel.getLiveDataToast().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isClicked) {
                Toast.makeText(MainActivity.this, "live data toast", Toast.LENGTH_SHORT).show();
                Log.d("tag", "livedata: observer one ");

            }
        });
        viewModel.getLiveDataToast().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isClicked) {
                Log.d("tag", "livedata: observer two ");

            }
        });
    }
}
