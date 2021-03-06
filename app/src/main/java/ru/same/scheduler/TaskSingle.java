package ru.same.scheduler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileNotFoundException;

import io.realm.Realm;

import static io.realm.Realm.getApplicationContext;

public class TaskSingle extends Fragment {
    private static TextView[] notes;
    private static String[] paths;

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
        notes = new TextView[Constants.NOTE_NUMBER];
        notes[0] = view.findViewById(R.id.file1);
        notes[1] = view.findViewById(R.id.file2);
        notes[2] = view.findViewById(R.id.file3);
        notes[3] = view.findViewById(R.id.file4);
        notes[4] = view.findViewById(R.id.file5);
        Bundle bundle = this.getArguments();
        paths = bundle.getStringArray(Constants.NOTES_ARRAY_FIELD);
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

        for (int i = 0; i < Constants.NOTE_NUMBER; i++) {
            if (paths[i] != null) {
                try {
                    File file = new File(TaskRewrite.getPathFromUri(getApplicationContext(), Uri.parse(paths[i])));
                    notes[i].setVisibility(View.VISIBLE);
                    notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.note), null, null);
                    notes[i].setText(file.getName());
                    int finalI1 = i;
                    notes[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("---", paths[finalI1] + " " + finalI1);
                            Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paths[finalI1]));
                            openLinkIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity(openLinkIntent);

                        }
                    });
                }catch (NullPointerException e){
                    del(paths[i]);
                    Toast.makeText(getApplicationContext(), "Некоторые файлы не были найдены", Toast.LENGTH_LONG).show();
                }
            } else {
                notes[i].setVisibility(View.INVISIBLE);
                notes[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                notes[i].setText("");
            }
        }
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

    private void del(String path) {
        int k;
        for (k = 0; paths[k] != path; k++) ;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        TaskBean taskBean = realm.where(TaskBean.class).equalTo(Constants.TITLE_FIELD, getArguments().getString(Constants.TITLE_FIELD))
                .equalTo(Constants.TIME_FIELD, getArguments().getString(Constants.TIME_FIELD))
                .equalTo(Constants.BODY_FIELD, getArguments().getString(Constants.BODY_FIELD))
                .equalTo(Constants.NOTES_ARRAY_1, getArguments().getStringArray(Constants.NOTES_ARRAY_FIELD)[0])
                .equalTo(Constants.NOTES_ARRAY_2, getArguments().getStringArray(Constants.NOTES_ARRAY_FIELD)[1])
                .equalTo(Constants.NOTES_ARRAY_3, getArguments().getStringArray(Constants.NOTES_ARRAY_FIELD)[2])
                .equalTo(Constants.NOTES_ARRAY_4, getArguments().getStringArray(Constants.NOTES_ARRAY_FIELD)[3])
                .equalTo(Constants.NOTES_ARRAY_5, getArguments().getStringArray(Constants.NOTES_ARRAY_FIELD)[4])
                .findFirst();
        for (int j = k; j < Constants.NOTE_NUMBER - 1; j++) {
            paths[j] = paths[j + 1];
        }
        paths[Constants.NOTE_NUMBER - 1] = null;
        taskBean.setPath1(paths[0]);
        taskBean.setPath2(paths[1]);
        taskBean.setPath3(paths[2]);
        taskBean.setPath4(paths[3]);
        taskBean.setPath5(paths[4]);
        realm.commitTransaction();
        for (int i = 0; i < Constants.NOTE_NUMBER; i++) {
            if (paths[i] != null) {
                try {
                    File file = new File(TaskRewrite.getPathFromUri(getApplicationContext(), Uri.parse(paths[i])));
                    notes[i].setVisibility(View.VISIBLE);
                    notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.note), null, null);
                    notes[i].setText(file.getName());
                    int finalI1 = i;
                    notes[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("---", paths[finalI1] + " " + finalI1);
                            Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paths[finalI1]));
                            openLinkIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity(openLinkIntent);
                        }
                    });
                }catch (NullPointerException e){
                    del(paths[i]);
                }
            } else {
                notes[i].setVisibility(View.INVISIBLE);
                notes[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                notes[i].setText("");
            }
        }
    }

    private void goToRewrite(String title, String time, String body) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE_FIELD, title);
        bundle.putString(Constants.TIME_FIELD, time);
        bundle.putString(Constants.BODY_FIELD, body);
        bundle.putBoolean("isRewrite", true);
        bundle.putStringArray(Constants.NOTES_ARRAY_FIELD, paths);
        NavHostFragment.findNavController(TaskSingle.this)
                .navigate(R.id.action_SecondFragment_to_taskRewrite, bundle);
        MainActivity.showOk();
    }
}