package ru.same.scheduler;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskTree extends Fragment {

    private static final int IDM_DELETE = 1;
    private LinearLayout layout;
    private Task clicked;

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
        layout = (LinearLayout) view.findViewById(R.id.layout1);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TaskBean> allTasks = realm.where(TaskBean.class).findAll();
        for (TaskBean taskBean : allTasks
        ) {
            Task task = new Task(view.getContext());
            task.setAllInfo(taskBean.getTitle(), taskBean.getBody(), taskBean.getTime());
            layout.addView(task);
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.TITLE_FIELD, task.getTitle().getText().toString());
                    bundle.putString(Constants.TIME_FIELD, task.getTime().getText().toString());
                    bundle.putString(Constants.BODY_FIELD, task.getBody().getText().toString());
                    setArguments(bundle);
                    NavHostFragment.findNavController(TaskTree.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                    ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
                }
            });
            registerForContextMenu(task);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Удалить");
        clicked = (Task) v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case IDM_DELETE:
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                TaskBean taskBean = realm.where(TaskBean.class)
                        .equalTo(Constants.BODY_FIELD, clicked.getBody().getText().toString())
                        .equalTo(Constants.TIME_FIELD, clicked.getTime().getText().toString())
                        .equalTo(Constants.TITLE_FIELD, clicked.getTitle().getText().toString())
                        .findFirst();
                taskBean.deleteFromRealm();
                realm.commitTransaction();
                layout.removeView(clicked);
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return true;
    }
}