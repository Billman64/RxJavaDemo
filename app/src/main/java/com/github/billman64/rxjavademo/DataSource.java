package com.github.billman64.rxjavademo;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static List<Task> createTaskList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Brush teeth", true, 4));
        tasks.add(new Task("Workout routine", false, 2));
        tasks.add(new Task("Take a shower", false, 4));
        tasks.add(new Task("Brush hair", true, 3));
        tasks.add(new Task("Make coffee", false, 5));
        tasks.add(new Task("Make breakfast", false, 1));
        return tasks;
    }
}
