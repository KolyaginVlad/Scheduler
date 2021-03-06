package ru.same.scheduler;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

import static io.realm.Realm.getApplicationContext;


public class TaskRewrite extends Fragment {


    private static EditText title;
    private static TextView time;
    private static EditText body;
    private static Bundle bundle1;
    private static Fragment fragment;
    private static TextView[] notes;
    private static int add;
    private static String[] paths;
    TextView clicked;

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
            taskBean.setPath1(paths[0]);
            taskBean.setPath2(paths[1]);
            taskBean.setPath3(paths[2]);
            taskBean.setPath4(paths[3]);
            taskBean.setPath5(paths[4]);
            realm.commitTransaction();
        } else {
            bundle.putString(Constants.TIME_FIELD, time.getText().toString());
            realm.beginTransaction();
            TaskBean taskBean = realm.where(TaskBean.class).equalTo(Constants.TITLE_FIELD, bundle1.getString(Constants.TITLE_FIELD))
                    .equalTo(Constants.TIME_FIELD, bundle1.getString(Constants.TIME_FIELD))
                    .equalTo(Constants.BODY_FIELD, bundle1.getString(Constants.BODY_FIELD))
                    .equalTo(Constants.NOTES_ARRAY_1, bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[0])
                    .equalTo(Constants.NOTES_ARRAY_2, bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[1])
                    .equalTo(Constants.NOTES_ARRAY_3, bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[2])
                    .equalTo(Constants.NOTES_ARRAY_4, bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[3])
                    .equalTo(Constants.NOTES_ARRAY_5, bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[4])
                    .findFirst();
            taskBean.setTitle(title.getText().toString());
            taskBean.setBody(body.getText().toString());
            taskBean.setPath1(paths[0]);
            taskBean.setPath2(paths[1]);
            taskBean.setPath3(paths[2]);
            taskBean.setPath4(paths[3]);
            taskBean.setPath5(paths[4]);
            realm.commitTransaction();
        }
        bundle.putString(Constants.TITLE_FIELD, title.getText().toString());
        bundle.putString(Constants.BODY_FIELD, body.getText().toString());
        bundle.putStringArray(Constants.NOTES_ARRAY_FIELD, paths);
        NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_ThirdFragment_to_SecondFragment, bundle);

    }

    //    public String getRealPathFromURI(Uri uri) {
//        String path = "";
//        if (getApplicationContext().getContentResolver() != null) {
//            Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
//                path = cursor.getString(idx);
//                cursor.close();
//            }
//        }
//        return path;
//    }
    public static String getPathFromUri(final Context context, final Uri uri) {


        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // TODO: 27.02.2021 add 10 MB test
            if (data != null) {
                File file = new File(getPathFromUri(getApplicationContext(), data.getData()));
                boolean flag = false;
                for (int i = 0; i < Constants.NOTE_NUMBER; i++) {
                    if (paths[i] == null) break;
                    if (paths[i].equals(data.getDataString())) {
                        flag = true;
                        break;
                    }
                }
                if ((double) (file.length()) / (1024 * 1024) > 10) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Файл слишком велик", Toast.LENGTH_LONG);
                    toast.show();
                } else if (flag) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Такой файл уже добавлен", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    paths[add] = data.getDataString();
                    notes[add].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.note), null, null);
                    if (add < Constants.NOTE_NUMBER - 1) {
                        notes[add + 1].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.plus), null, null);
                        notes[add + 1].setText("Добавить файл");
                        notes[add + 1].setVisibility(View.VISIBLE);
                        notes[add + 1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.setType("*/*");
                                intent.addCategory(Intent.CATEGORY_OPENABLE);


                                // special intent for Samsung file manager
                                Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                                sIntent.putExtra("CONTENT_TYPE", "*/*");
                                sIntent.addCategory(Intent.CATEGORY_DEFAULT);


                                Intent chooserIntent;
                                if (getApplicationContext().getPackageManager().resolveActivity(sIntent, 0) != null) {
                                    // it is device with samsung file manager
                                    chooserIntent = Intent.createChooser(sIntent, "Choose");
                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                                } else {
                                    chooserIntent = Intent.createChooser(intent, "Choose");
                                }

                                try {
                                    int i;
                                    for (i = 0; notes[i] != (TextView) view; i++) ;
                                    add = i;
                                    startActivityForResult(chooserIntent, 1);

                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    notes[add].setText(file.getName());
                    registerForContextMenu(notes[add]);
                    int a = add;
                    notes[add].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paths[a]));
                            openLinkIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            //openLinkIntent.setDataAndType(Uri.parse(paths[a]), "image/*");

                            if (openLinkIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                startActivityForResult(openLinkIntent, 10);
                            } else {
                                Log.d("Intent", "Не получается обработать намерение!");
                            }
                        }
                    });
                }
            }
        } else {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title3);
        time = view.findViewById(R.id.time3);
        body = view.findViewById(R.id.body3);
        notes = new TextView[Constants.NOTE_NUMBER];
        notes[0] = view.findViewById(R.id.note1);
        notes[1] = view.findViewById(R.id.note2);
        notes[2] = view.findViewById(R.id.note3);
        notes[3] = view.findViewById(R.id.note4);
        notes[4] = view.findViewById(R.id.note5);
        bundle1 = this.getArguments();
        fragment = TaskRewrite.this;


        paths = new String[Constants.NOTE_NUMBER];
        if (bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD) != null)
            for (int i = 0; i < Constants.NOTE_NUMBER; i++) {
                if (bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[i] != null)
                    paths[i] = (bundle1.getStringArray(Constants.NOTES_ARRAY_FIELD)[i]) + "";
            }


        boolean first = true;
        for (int i = 0; i < Constants.NOTE_NUMBER; i++) {
            if (paths[i] != null) {
                File file = new File(getPathFromUri(getApplicationContext(), Uri.parse(paths[i])));
                notes[i].setVisibility(View.VISIBLE);
                notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.note), null, null);
                notes[i].setText(file.getName());
                registerForContextMenu(notes[i]);
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
            } else if (first) {
                first = false;
                notes[i].setVisibility(View.VISIBLE);
                notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.plus), null, null);
                notes[i].setText("Добавить файл");
                int finalI = i;
                notes[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);


                        // special intent for Samsung file manager
                        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                        sIntent.putExtra("CONTENT_TYPE", "*/*");
                        sIntent.addCategory(Intent.CATEGORY_DEFAULT);


                        Intent chooserIntent;
                        if (getApplicationContext().getPackageManager().resolveActivity(sIntent, 0) != null) {
                            // it is device with samsung file manager
                            chooserIntent = Intent.createChooser(sIntent, "Choose");
                            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                        } else {
                            chooserIntent = Intent.createChooser(intent, "Choose");
                        }

                        try {
                            startActivityForResult(chooserIntent, 1);
                            add = finalI;
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                notes[i].setVisibility(View.INVISIBLE);
                notes[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                notes[i].setText("");
            }
        }
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

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.del, Menu.NONE, "Удалить");
        clicked = (TextView) v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.del:
                int i;
                for (i = 0; notes[i] != clicked; i++) ;
                for (int j = i; j < Constants.NOTE_NUMBER - 1; j++) {
                    paths[j] = paths[j + 1];
                }
                paths[Constants.NOTE_NUMBER - 1] = null;
                for (int j = 0; j < Constants.NOTE_NUMBER; j++) {
                    unregisterForContextMenu(notes[j]);
                }
                boolean first = true;
                for (i = 0; i < Constants.NOTE_NUMBER; i++) {
                    if (paths[i] != null) {
                        File file = new File(TaskRewrite.getPathFromUri(getApplicationContext(), Uri.parse(paths[i])));
                        notes[i].setVisibility(View.VISIBLE);
                        notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.note), null, null);
                        notes[i].setText(file.getName());
                        registerForContextMenu(notes[i]);
                        int finalI1 = i;
                        notes[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri address = Uri.parse(paths[finalI1]);
                                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);

                                if (openLinkIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                    startActivity(openLinkIntent);
                                } else {
                                    Log.d("Intent", "Не получается обработать намерение!");
                                }
                            }
                        });
                    } else if (first) {
                        first = false;
                        notes[i].setVisibility(View.VISIBLE);
                        notes[i].setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.plus), null, null);
                        notes[i].setText("Добавить файл");
                        int finalI = i;
                        notes[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("*/*");
                                intent.addCategory(Intent.CATEGORY_OPENABLE);

                                // special intent for Samsung file manager
                                Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                                sIntent.putExtra("CONTENT_TYPE", "*/*");
                                sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                                Intent chooserIntent;
                                if (getApplicationContext().getPackageManager().resolveActivity(sIntent, 0) != null) {
                                    // it is device with samsung file manager
                                    chooserIntent = sIntent;
                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                                } else {
                                    chooserIntent = intent;
                                }

                                try {
                                    startActivityForResult(chooserIntent, 1);
                                    add = finalI;
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        notes[i].setVisibility(View.INVISIBLE);
                        notes[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        notes[i].setText("");
                    }
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return true;
    }
}