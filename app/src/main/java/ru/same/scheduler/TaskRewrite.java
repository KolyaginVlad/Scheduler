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
        Bundle bundle = this.getArguments();
        title.setText(bundle.getString("title"));
        time.setText(bundle.getString("time"));
        body.setText(bundle.getString("body"));
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", title.getText().toString());
                bundle.putString("time",time.getText().toString());
                bundle.putString("body",body.getText().toString());
                NavHostFragment.findNavController(TaskRewrite.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", getArguments().getString("title"));
                bundle.putString("time",getArguments().getString("time"));
                bundle.putString("body",getArguments().getString("body"));
                NavHostFragment.findNavController(TaskRewrite.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);
            }
        });
    }
}