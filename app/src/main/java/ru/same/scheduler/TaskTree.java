package ru.same.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;
import io.realm.RealmResults;

public class TaskTree extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout1);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TaskBean> allTasks = realm.where(TaskBean.class).findAll();
        for (TaskBean taskBean: allTasks
             ) {
            Task task = new Task(view.getContext());
            task.setAllInfo(taskBean.getTitle(), taskBean.getBody(), taskBean.getTime());
            layout.addView(task);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", task.getTitle().getText().toString());
                    bundle.putString("time", task.getTime().getText().toString());
                    bundle.putString("body", task.getBody().getText().toString());
                    setArguments(bundle);
                    NavHostFragment.findNavController(TaskTree.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);

                }
            });
        }

    }
}