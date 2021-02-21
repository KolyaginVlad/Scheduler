package ru.same.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;


public class TaskRewrite extends Fragment {


    private static EditText title;
    private static TextView time;
    private static EditText body;
    private static Bundle bundle1;
    private static Fragment fragment;

    public static void okClicked() {
        Bundle bundle = new Bundle();
        Realm realm = Realm.getDefaultInstance();
        if (!bundle1.getBoolean("isRewrite")) {
            Calendar calendar = new GregorianCalendar();
            bundle.putString(Constants.TIME_FIELD, calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
            realm.beginTransaction();
            TaskBean taskBean = realm.createObject(TaskBean.class);
            taskBean.setBody(body.getText().toString());
            taskBean.setTime(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
            taskBean.setTitle(title.getText().toString());
            realm.commitTransaction();
        } else {
            bundle.putString(Constants.TIME_FIELD, time.getText().toString());
            realm.beginTransaction();
            TaskBean taskBean = realm.where(TaskBean.class).equalTo(Constants.TITLE_FIELD, bundle1.getString(Constants.TITLE_FIELD))
                    .equalTo(Constants.TIME_FIELD, bundle1.getString(Constants.TIME_FIELD))
                    .equalTo(Constants.BODY_FIELD, bundle1.getString(Constants.BODY_FIELD))
                    .findFirst();
            taskBean.setTitle(title.getText().toString());
            taskBean.setBody(body.getText().toString());
            realm.commitTransaction();
        }
        bundle.putString(Constants.TITLE_FIELD, title.getText().toString());
        bundle.putString(Constants.BODY_FIELD, body.getText().toString());
        NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title3);
        time = view.findViewById(R.id.time3);
        body = view.findViewById(R.id.body3);
        bundle1 = this.getArguments();
        fragment = TaskRewrite.this;
        title.setText(bundle1.getString(Constants.TITLE_FIELD));
        time.setText(bundle1.getString(Constants.TIME_FIELD));
        body.setText(bundle1.getString(Constants.BODY_FIELD));
//        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                Realm realm = Realm.getDefaultInstance();
//                if (!bundle1.getBoolean("isRewrite")) {
//                    Calendar calendar = new GregorianCalendar();
//                    bundle.putString(Constants.TIME_FIELD, calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
//                    realm.beginTransaction();
//                    TaskBean taskBean = realm.createObject(TaskBean.class);
//                    taskBean.setBody(body.getText().toString());
//                    taskBean.setTime(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
//                    taskBean.setTitle(title.getText().toString());
//                    realm.commitTransaction();
//                } else {
//                    bundle.putString(Constants.TIME_FIELD, time.getText().toString());
//                    realm.beginTransaction();
//                    TaskBean taskBean = realm.where(TaskBean.class).equalTo(Constants.TITLE_FIELD, bundle1.getString(Constants.TITLE_FIELD))
//                            .equalTo(Constants.TIME_FIELD, bundle1.getString(Constants.TIME_FIELD))
//                            .equalTo(Constants.BODY_FIELD, bundle1.getString(Constants.BODY_FIELD))
//                            .findFirst();
//                    taskBean.setTitle(title.getText().toString());
//                    taskBean.setBody(body.getText().toString());
//                    realm.commitTransaction();
//                }
//                bundle.putString(Constants.TITLE_FIELD, title.getText().toString());
//                bundle.putString(Constants.BODY_FIELD, body.getText().toString());
//                NavHostFragment.findNavController(TaskRewrite.this)
//                        .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
//            }
//        });
//        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!bundle1.getBoolean("isRewrite")) {
//                    NavHostFragment.findNavController(TaskRewrite.this).navigate(R.id.action_taskRewrite_to_FirstFragment);
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constants.TITLE_FIELD, getArguments().getString(Constants.TITLE_FIELD));
//                    bundle.putString(Constants.TIME_FIELD, getArguments().getString(Constants.TIME_FIELD));
//                    bundle.putString(Constants.BODY_FIELD, getArguments().getString(Constants.BODY_FIELD));
//                    NavHostFragment.findNavController(TaskRewrite.this)
//                            .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
//                }
//            }
//        });
    }
}