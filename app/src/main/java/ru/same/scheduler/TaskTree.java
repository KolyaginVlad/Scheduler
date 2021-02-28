package ru.same.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskTree extends Fragment {


    private static RecyclerView recyclerView;
    private List<Task> tasks;
    private View w;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list);
        tasks = new ArrayList<>();
        w = view;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TaskBean> allTasks = realm.where(TaskBean.class).findAll();
        for (TaskBean taskBean : allTasks
        ) {
            Task task = new Task(view.getContext());
            task.setAllInfo(taskBean.getTitle(), taskBean.getBody(), taskBean.getTime(), taskBean.getPath1(), taskBean.getPath2(), taskBean.getPath3(), taskBean.getPath4(), taskBean.getPath5());
            tasks.add(task);
//            task.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.TITLE_FIELD, task.getTitle());
//                    bundle.putString(Constants.TIME_FIELD, task.getTime());
//                    bundle.putString(Constants.BODY_FIELD, task.getBody());
//                    setArguments(bundle);
//                    NavHostFragment.findNavController(TaskTree.this)
//                            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
//                    ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
//                }
//            });
        }
        TaskAdapter adapter = new TaskAdapter(view.getContext(), tasks);
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TITLE_FIELD, tasks.get(position).getTitle());
                bundle.putString(Constants.TIME_FIELD, tasks.get(position).getTime());
                bundle.putString(Constants.BODY_FIELD, tasks.get(position).getBody());
                bundle.putStringArray(Constants.NOTES_ARRAY_FIELD, tasks.get(position).getNotes());
                setArguments(bundle);
                NavHostFragment.findNavController(TaskTree.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.del:
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                TaskBean taskBean = realm.where(TaskBean.class)
                        .equalTo(Constants.BODY_FIELD, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getBody())
                        .equalTo(Constants.TIME_FIELD, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getTime())
                        .equalTo(Constants.TITLE_FIELD, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getTitle())
                        .equalTo(Constants.NOTES_ARRAY_1, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getNotes()[0])
                        .equalTo(Constants.NOTES_ARRAY_2, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getNotes()[1])
                        .equalTo(Constants.NOTES_ARRAY_3, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getNotes()[2])
                        .equalTo(Constants.NOTES_ARRAY_4, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getNotes()[3])
                        .equalTo(Constants.NOTES_ARRAY_5, tasks.get(((TaskAdapter) recyclerView.getAdapter()).getPosition()).getNotes()[4])
                        .findFirst();
                taskBean.deleteFromRealm();
                realm.commitTransaction();
//                recyclerView.removeViewAt(((TaskAdapter)recyclerView.getAdapter()).getPosition());
                //не обновлялся
                tasks.remove(((TaskAdapter) recyclerView.getAdapter()).getPosition());
                TaskAdapter adapter = new TaskAdapter(w.getContext(), tasks);
                adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.TITLE_FIELD, tasks.get(position).getTitle());
                        bundle.putString(Constants.TIME_FIELD, tasks.get(position).getTime());
                        bundle.putString(Constants.BODY_FIELD, tasks.get(position).getBody());
                        bundle.putStringArray(Constants.NOTES_ARRAY_FIELD, tasks.get(position).getNotes());
                        setArguments(bundle);
                        NavHostFragment.findNavController(TaskTree.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
                    }
                });
                recyclerView.setAdapter(adapter);
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return true;
    }
}