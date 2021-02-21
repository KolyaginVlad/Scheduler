package ru.same.scheduler;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private static OnItemClickListener mItemClickListener;
    private final LayoutInflater inflater;
    private final List<Task> tasks;
    private int position;

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
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        TaskAdapter.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        final ConstraintLayout layout;
        final TextView title, body, time;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.taskTitle);
            time = view.findViewById(R.id.taskTime);
            body = view.findViewById(R.id.taskBody);
            layout = view.findViewById(R.id.layoutTask);
            layout.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.del, Menu.NONE, "Удалить");
        }
    }

}
