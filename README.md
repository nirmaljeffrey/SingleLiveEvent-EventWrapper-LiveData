## SingleLiveEvent Vs LiveData Vs EventWrapper

### LiveData:
LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
Read about about advatages of liveData from the [documentation](https://developer.android.com/topic/libraries/architecture/livedata "documentation").   I list them here by the way:
### Advantages of Livedata:

>  #### Ensures your UI matches your data state 
> LiveData follows the observer pattern. LiveData notifies Observer objects when the lifecycle state changes. You can consolidate your code to update the UI in these Observer objects. Instead of updating the UI every time the app data changes, your observer can update the UI every time there's a change.
> #### No memory leaks 
> Observers are bound to Lifecycle objects and clean up after themselves when their associated lifecycle is destroyed.
> #### No crashes due to stopped activities
> If the observer's lifecycle is inactive, such as in the case of an activity in the back stack,then it doesn’t receive any LiveData events.
> #### No more manual lifecycle handling 
> UI components just observe relevant data and don’t stop or resume observation. LiveData automatically manages all of this since it’s aware of the relevant lifecycle status changes while observing.
> #### Always up to date data 
> If a lifecycle becomes inactive, it receives the latest data upon becoming active again. For example, an activity that was in the background receives the latest data right after it returns to the foreground.
> #### Proper configuration changes 
> If an activity or fragment is recreated due to a configuration change, like device rotation, it immediately receives the latest available data.

### Problem:
The liveData is good for maintaining the state of the data. But for events like displaying a snackbar, toast and navigation, the observers should observe only once and after configuration changes, the observer should not observe. To achieve this, some of the advantages of liveData has to be disabled, like
 - Proper configuration changes
 -  Always up to date data

### Solution:
Use **SingleLiveEvent** or **Event wrapper** for handling events.

### SingleLiveEvent:

 SingleLiveEvent is a lifecycle-aware observable that sends only new updates after subscription and it will call the observable only if there's an explicit call to setValue() or call(). Note that only one observer is going to be notified of changes.
```java

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    public void observe(@NotNull LifecycleOwner owner, @NotNull final Observer<? super T> observer) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}
```

### Event Wrapper:
Event Class is used as a wrapper for data that is exposed via a LiveData that represents an event. Using this Event class, multiple observers can observe an event.

```java
public class Event<T> {

private T mContent;
private boolean hasBeenHandled = false;

public Event(T content) {
	if (content == null) {
	   throw new IllegalArgumentException("null values in Event are not allowed.");
	}
       mContent = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return mContent;
        }
    }
    public T peekContent(){
        return mContent;
    }
    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }
}
```
Use this EventObserver to remove some boilerplate code if you end up having lots of events.
```java
public class EventObserver<T> implements Observer<Event<T>> {
    private EventHandler<T> onEventUnhandledContent;

    public EventObserver(@NotNull EventHandler<T> onEventUnhandledContent) {
        this.onEventUnhandledContent = onEventUnhandledContent;
    }

    @Override
    public void onChanged(Event<T> event) {
        if(event != null){
                onEventUnhandledContent.onEventUnHandled(event.getContentIfNotHandled());
        }
    }
}
```
EventHandler interface used in EventObserver class.
```java
public interface EventHandler<V>{
    void onEventUnHandled(V object);
}
```
##### Usage:
Sample code for observing events in fragment or activity.
```java
viewModel.getEventToast().observe(this, new EventObserver<>(new EventHandler<Object>() {

            @Override
            public void onEventUnHandled(Object object) {
                Toast.makeText(MainActivity.this, "event wrapper toast", Toast.LENGTH_SHORT).show();
                Log.d("tag", "event wrapper: observer one ");
            }
        }));
```
Sample code for event observable in viewmodel.
```java
public class FragmentViewModel extends ViewModel {

    private MutableLiveData<Event<Object>> eventToast = new MutableLiveData<>();

    public MutableLiveData<Event<Object>> getEventToast() {
        return eventToast;
    }

    public void displayToastUsingEvent(){
        eventToast.setValue(new Event<>(new Object()));
    }
}
```
Sample code in XML for calling displayToastUsingEvent() method from viewModel
```xml
...
 <Button
            android:id="@+id/button2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Toast using event"
            android:onClick="@{() -> viewModel.displayToastUsingEvent()}"
       />
	   ...
```

### Summary:
|   | Observer only once   | multpile observers   |
| ------------ | ------------ | ------------ |
|  LiveData |  No |  Yes   |
| SIngleLiveEvent  |   Yes |   No |
| EventWrapper | Yes | Yes |

### References:
- [Proof Of Concepts](https://github.com/nirmaljeffrey/SingleLiveEvent-EventWrapper-LiveData "POC")
- [https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
- [Architecture samples from google](https://github.com/android/architecture-samples/tree/todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp)
