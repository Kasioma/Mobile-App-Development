package com.example.hardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int background = R.drawable.dark;
        getWindow().setBackgroundDrawableResource(background);
        setContentView(R.layout.activity_main);
        Map<String, Map<String, String>> m = getNotesMap();
        displayNotes(m);
        //clearSharedPreferences(this);
    }

    public static void clearSharedPreferences(Context context) {
        String PREFERENCES_NAME = "MyNotes";
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public void launchActivity(View v){
        Intent i = new Intent(this, Note.class);
        i.putExtra("ID", "");
        startActivity(i);
    }

    protected void onResume() {
        super.onResume();

        try {
            Map<String, Map<String, String>> newMap = getNotesMap();
            if (!newMap.isEmpty()) {
                displayNotes(newMap);
            }

        } catch (JsonSyntaxException e) {
            Log.e("Gson", "Error parsing JSON", e);
        }

    }

    public void modifyActivity(View v, String id, String title, String content, String date, String time){
        Intent i = new Intent(this, Note.class);
        i.putExtra("ID", id);
        i.putExtra("title", title);
        i.putExtra("content",content);
        i.putExtra("date", date);
        i.putExtra("time", time);

        startActivity(i);
    }

    private void displayNotes(Map<String, Map<String, String>> m) {
        if (m == null || m.isEmpty()) {
            return;
        }
        LinearLayout linearLayout = findViewById(R.id.list);
        linearLayout.removeAllViews();

        Map<String, List<Reminder>> deviderMap = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> entry : m.entrySet()) {
            Map<String, String> dateSelectedElements = entry.getValue();
            List<Reminder> l = new ArrayList<>();
            for(Map.Entry<String,String> e : dateSelectedElements.entrySet()){
                String json = e.getValue();
                Reminder note = gson.fromJson(json, Reminder.class);
                l.add(note);
            }
            sortNoteList(l);
            deviderMap.put(entry.getKey(), l);
        }

        for (Map.Entry<String, List<Reminder>> entry : deviderMap.entrySet()){
            LinearLayout listContainer = new LinearLayout(this);
            listContainer.setOrientation(LinearLayout.VERTICAL);
            String listId = entry.getKey();
            int containerId = listId.hashCode();
            listContainer.setId(containerId);

            TextView keyTextView = new TextView(this);
            keyTextView.setText(entry.getKey());
            keyTextView.setTextSize(16);
            keyTextView.setTextColor(Color.WHITE);
            keyTextView.setPadding(16, 8, 0, 1);
            listContainer.addView(keyTextView);

            View lineView = new View(this);
            lineView.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
            );
            listContainer.addView(lineView, lineParams);

            for (Reminder r : entry.getValue()) {
                LinearLayout textView = createTextViewWithTime(r);
                listContainer.addView(textView);
            }
            linearLayout.addView(listContainer);
        }
    }

    private void sortNoteList(List<Reminder> noteList) {
        Collections.sort(noteList, new ReminderComparator());
    }

    private class ReminderComparator implements Comparator<Reminder> {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        @Override
        public int compare(Reminder r1, Reminder r2) {
            LocalDate date1 = LocalDate.parse(r1.getDate(), dateFormatter);
            LocalDate date2 = LocalDate.parse(r2.getDate(), dateFormatter);

            int dateComparison = date1.compareTo(date2);

            if (dateComparison == 0) {
                LocalTime time1 = LocalTime.parse(r1.getTime(), timeFormatter);
                LocalTime time2 = LocalTime.parse(r2.getTime(), timeFormatter);

                return time1.compareTo(time2);
            }

            return dateComparison;
        }
    }


    private LinearLayout createTextViewWithTime(Reminder r) {
        String title = r.getTitle();
        String content = r.getContent();
        String time = r.getTime();
        String date = r.getDate();
        String id = r.getId();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView titleTextView = new TextView(this);
        titleTextView.setText(title);
        titleTextView.setTextSize(30);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setGravity(Gravity.START);
        titleTextView.setPadding(35, 16, 16, 16);


        TextView timeTextView = new TextView(this);
        timeTextView.setText(time);
        timeTextView.setTextSize(30);
        timeTextView.setTextColor(Color.WHITE);
        timeTextView.setGravity(Gravity.END);
        timeTextView.setPadding(16, 16, 35, 16);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleTextView.setLayoutParams(titleParams);

        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        timeTextView.setLayoutParams(timeParams);

        linearLayout.addView(titleTextView);
        linearLayout.addView(timeTextView);

        linearLayout.setBackgroundResource(R.drawable.rounded);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 20;
        linearLayout.setLayoutParams(params);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyActivity(v, id, title, content, date, time);
            }
        });

        return linearLayout;
    }



    private Map<String, Map<String, String>> getNotesMap() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyNotes", MODE_PRIVATE);

        String notesJson = sharedPreferences.getString("notesMap", "{}");

        Log.d("Note", "Deserialized JSON: " + notesJson);

        Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
        return gson.fromJson(notesJson, type);
    }

}