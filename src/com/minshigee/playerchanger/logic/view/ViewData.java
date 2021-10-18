package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.logic.view.domain.ViewTask;

import java.util.PriorityQueue;
import java.util.Queue;

public class ViewData {
    private Queue<ViewTask> viewTaskQueue = new PriorityQueue<>();

    public ViewTask getAndPopTask(){return viewTaskQueue.poll();}
    public ViewTask getViewTask(){return viewTaskQueue.peek();}
    public void addViewTask(ViewTask task){viewTaskQueue.add(task);}

}
