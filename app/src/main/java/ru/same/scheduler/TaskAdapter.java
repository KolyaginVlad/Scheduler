package ru.same.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.inflater = LayoutInflater.from(context);
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.body.setText(task.getBody());
        holder.time.setText(task.getTime());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title,body, time;
        ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.taskTitle);
            time = (TextView) view.findViewById(R.id.taskTime);
            body = (TextView) view.findViewById(R.id.taskBody);
        }
    }
}
