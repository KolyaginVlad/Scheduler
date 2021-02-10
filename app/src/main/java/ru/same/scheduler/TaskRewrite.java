package ru.same.scheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class TaskRewrite extends Fragment {



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
        EditText title = view.findViewById(R.id.title3);
        TextView time = view.findViewById(R.id.time3);
        EditText body = view.findViewById(R.id.body3);
        Bundle bundle1 = this.getArguments();
        title.setText(bundle1.getString("title"));
        time.setText(bundle1.getString("time"));
        body.setText(bundle1.getString("body"));
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Realm realm = Realm.getDefaultInstance();
                if (!bundle1.getBoolean("isRewrite")) {
                    Calendar calendar = new GregorianCalendar();
                    bundle.putString("time", calendar.get(Calendar.DAY_OF_MONTH)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR));
                    realm.beginTransaction();
                    TaskBean taskBean = realm.createObject(TaskBean.class);
                    taskBean.setBody(body.getText().toString());
                    taskBean.setTime(calendar.get(Calendar.DAY_OF_MONTH)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR));
                    taskBean.setTitle(title.getText().toString());
                    realm.commitTransaction();
                }
                else {
                    bundle.putString("time", time.getText().toString());
                    realm.beginTransaction();
                        TaskBean taskBean = realm.where(TaskBean.class).equalTo("title", bundle1.getString("title"))
                                .equalTo("time", bundle1.getString("time"))
                                .equalTo("body", bundle1.getString("body"))
                                .findFirst();
                        taskBean.setTitle(title.getText().toString());
                        taskBean.setBody(body.getText().toString());
                    realm.commitTransaction();
                }
                    bundle.putString("title", title.getText().toString());
                    bundle.putString("body",body.getText().toString());
                    NavHostFragment.findNavController(TaskRewrite.this)
                            .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bundle1.getBoolean("isRewrite")) {
                    NavHostFragment.findNavController(TaskRewrite.this).navigate(R.id.action_taskRewrite_to_FirstFragment);
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getArguments().getString("title"));
                    bundle.putString("time", getArguments().getString("time"));
                    bundle.putString("body", getArguments().getString("body"));
                    NavHostFragment.findNavController(TaskRewrite.this)
                            .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
                }
            }
        });
    }
}