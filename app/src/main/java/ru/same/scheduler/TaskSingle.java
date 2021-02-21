package ru.same.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class TaskSingle extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = view.findViewById(R.id.title2);
        TextView time = view.findViewById(R.id.time2);
        TextView body = view.findViewById(R.id.body2);
        Bundle bundle = this.getArguments();
        title.setText(bundle.getString(Constants.TITLE_FIELD));
        time.setText(bundle.getString(Constants.TIME_FIELD));
        body.setText(bundle.getString(Constants.BODY_FIELD));
//        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(TaskSingle.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRewrite(title.getText().toString(), time.getText().toString(), body.getText().toString());
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRewrite(title.getText().toString(), time.getText().toString(), body.getText().toString());
            }
        });
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRewrite(title.getText().toString(), time.getText().toString(), body.getText().toString());
            }
        });
    }

    private void goToRewrite(String title, String time, String body) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE_FIELD, title);
        bundle.putString(Constants.TIME_FIELD, time);
        bundle.putString(Constants.BODY_FIELD, body);
        bundle.putBoolean("isRewrite", true);
        NavHostFragment.findNavController(TaskSingle.this)
                .navigate(R.id.action_SecondFragment_to_taskRewrite, bundle);
        MainActivity.showOk();
    }
}