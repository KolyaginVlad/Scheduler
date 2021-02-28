package ru.same.scheduler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private static MenuItem item;

    public static void showOk() {
        item.setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.SecondFragment)
                    Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.FirstFragment);
                else {
                    Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigateUp();
                }
                if (Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.FirstFragment) {
                    toolbar.setNavigationIcon(null);
                }
                toolbar.getMenu().findItem(R.id.ok).setVisible(false);
            }
        });
        if (Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.SecondFragment || Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.taskRewrite)
            toolbar.setNavigationIcon(R.drawable.back);
        else
            toolbar.setNavigationIcon(null);
        if (Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.taskRewrite)
            toolbar.getMenu().findItem(R.id.ok).setVisible(true);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.taskRewrite);
                toolbar.setNavigationIcon(R.drawable.back);
                if (Navigation.findNavController(findViewById(R.id.nav_host_fragment)).getCurrentDestination().getId() == R.id.taskRewrite) {
                    toolbar.getMenu().findItem(R.id.ok).setVisible(true);
                }
            }
        });
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.ok);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ok) {
            TaskRewrite.okClicked();
            item.setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();

//        Intent i = new Intent(Intent.ACTION_MAIN);
//        i.addCategory(Intent.CATEGORY_HOME);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            finishAndRemoveTask();
//        }
//        else
//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//            {
//                finishAffinity();
//            } else
//            {
//                finish();
//            }
//        }

    }
}