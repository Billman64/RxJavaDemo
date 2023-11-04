package com.github.billman64.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Observable created based on a list of tasks
        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.createTaskList())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Throwable {
                        Log.d(TAG, "test: " + Thread.currentThread().getName());
                        try{
                            Thread.sleep(1000); // simulates loading time in a worker thread
                        } catch (InterruptedException e) {
                           e.printStackTrace();
                        }
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            TextView status = findViewById(R.id.status);
            ProgressBar progressBar = findViewById(R.id.progress_bar);

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe() called");
                status.setText(getString(R.string.subscribed));
            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext() " + Thread.currentThread().getName());
                Log.d(TAG, "onNext() " + task.getDescription());
                status.setText(task.getDescription());
                progressBar.setProgress(1);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError(): " + e);
                status.setText(getString(R.string.error));
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
                status.setText(getString(R.string.tasks_complete));
                progressBar.setVisibility(View.INVISIBLE);
                ImageView taskDoneImage = findViewById(R.id.task_complete);
                taskDoneImage.setVisibility(View.VISIBLE);

                TextView tv = findViewById(R.id.text);
                String text = tv.getText().toString();
                tv.setText(text + " \n\n:)");
            }
        });


    }
}