package com.mememorygame.snowgoat.mememorygame.GamePlay;

import android.app.Activity;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Supplier;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by WinNabuska on 18.11.2015.
 */
public class SingleRunScheduledTask extends TimerTask {

    private Supplier<Optional<Runnable>> taskSupplier;
    private Timer timer;
    //private Handler handler;
    private Activity activity;

    public SingleRunScheduledTask(Runnable runnable, long delay, Activity activity /*Handler handler*/){
        //this.handler = handler;
        this.activity = activity;
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        //ensimäisen kerran kun taskSupplierilta kysytään tehtävää se antaa Optional<Runnable>:n mutta seuraavilla kerrailla emptyn.
        this.taskSupplier = () -> atomicBoolean.getAndSet(false) ? Optional.of(runnable) : Optional.empty();
        timer = new Timer();
        timer.schedule(this, delay);
    }

    @Override
    public void run() {
        taskSupplier.get().ifPresent(r -> activity.runOnUiThread(r));
        timer.cancel();
    }

    //Huom tätä metodia täytyy kutsua UI Threadista
    public void flush(){
        taskSupplier.get().ifPresent(r -> r.run());
        timer.cancel();
        timer.purge();
    }
}
