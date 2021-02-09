package ru.same.scheduler;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Task extends LinearLayout {
    private TextView title;
    private TextView body;
    private TextView time;

    public Task(Context context) {
        super(context);
        init(context);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getBody() {
        return body;
    }

    public TextView getTime() {
        return time;
    }

    private void init(Context context){
        View.inflate(context,R.layout.task,this);
        title = findViewById(R.id.taskTitle);
        body = findViewById(R.id.taskBody);
        time = findViewById(R.id.taskTime);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setBody(String body) {
        this.body.setText(body);
    }

    public void setTime(String time) {
        this.time.setText(time);
    }
    public void setAllInfo(String tit, String bod, String tim){
        title.setText(tit);
        body.setText(bod);
        time.setText(tim);
    }
}
