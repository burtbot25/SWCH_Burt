package com.bcit.swch.swch;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.burt.swch_planner.R;

import java.util.ArrayList;

/**
 * Created by Burt on 2017-11-26.
 */

public class TodoFragment extends Fragment {
    private ListView listView;
    private TodoDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.todo_layout, container, false);
        return myView;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedDate = adapterView.getItemAtPosition(i).toString();
                Cursor cursor = db.getTask(selectedDate);
                String task = "";
                while (cursor.moveToNext()) {
                    task += cursor.getString(0);
                }
                Intent intent = new Intent(getApplicationContext(), ViewTaskActivity.class);
                intent.putExtra("task", task);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });
    }

    public void populateTaskList() {
        Cursor data = db.getData();
        ArrayList<String> dates = new ArrayList<>();

        while (data.moveToNext()) {
            dates.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, dates);
        listView.setAdapter(adapter);
    }

    //The method changes the activity depending on the action bar icon that is pressed.
    //Additional cases are needed for each action bar icon that is added.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iconId = item.getItemId();

        switch (iconId) {
            case R.id.calendarIcon:
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
