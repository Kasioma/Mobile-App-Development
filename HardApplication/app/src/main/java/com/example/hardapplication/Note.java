package com.example.hardapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Note extends AppCompatActivity{

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public AndroidAlarmScheduler scheduler;
    public String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int background = R.drawable.dark;
        getWindow().setBackgroundDrawableResource(background);
        setContentView(R.layout.note);
        scheduler = new AndroidAlarmScheduler(this);
        id = getIntent().getStringExtra("ID");
        if(!Objects.equals(id, "")){
            EditText title = findViewById(R.id.editTextText);
            TextInputLayout content = findViewById(R.id.content);
            EditText date = findViewById(R.id.date);
            EditText time = findViewById(R.id.time);
            title.setText(getIntent().getStringExtra("title"));
            EditText e = content.getEditText();
            if(!Objects.equals(getIntent().getStringExtra("content"), "")){
                e.setText(getIntent().getStringExtra("content"));
            }
            date.setText(getIntent().getStringExtra("date"));
            time.setText(getIntent().getStringExtra("time"));
            Map<String, Map<String, String>> m = getNotesMap();
            deleteNote(m, getIntent().getStringExtra("date"), getIntent().getStringExtra("ID"));
            saveNotesMap(m);
            try {
                LocalDateTime dateAndTime = LocalDateTime.parse(getIntent().getStringExtra("date") + "-" + getIntent().getStringExtra("time"), DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm"));
                AlarmItem item = new AlarmItem(dateAndTime, id.hashCode(), getIntent().getStringExtra("title"));
                scheduler.cancel(item);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    }

    public void back(View v){
        EditText title = findViewById(R.id.editTextText);
        TextInputLayout content = findViewById(R.id.content);
        EditText date = findViewById(R.id.date);
        EditText time = findViewById(R.id.time);
        String text = "";
        boolean dateValid = false;
        boolean timeValid = false;
        boolean check = false;
        String parsedDate = "";
        String parsedTime = "";
        String parsedContent = "";

        if(date != null && time!= null) {

            parsedDate = date.getText().toString();
            parsedTime = time.getText().toString();

            dateValid = checkDate(parsedDate.trim());
            timeValid = isTimeValid(parsedTime);
            check = true;
        }else{
            Toast.makeText(getApplicationContext(), "date or time arent set", Toast.LENGTH_SHORT).show();
        }
        if(check) {
            if (title != null && dateValid && timeValid) {
                if(title.length() > 15){
                    Toast.makeText(getApplicationContext(), "title too long bozo", Toast.LENGTH_SHORT).show();
                }else {
                    text = title.getText().toString();

                    Reminder r = new Reminder();
                    if(Objects.equals(id, "")){
                        r.generateId();
                    }else{
                        r.setId(id);
                    }
                    try {
                        LocalDateTime dateAndTime = LocalDateTime.parse(parsedDate + "-" + parsedTime, DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm"));
                        AlarmItem item = new AlarmItem(dateAndTime, r.getId().hashCode(), text);
                        System.out.println("date and time: " + item);
                        scheduler.schedule(item);
                        r.setAlarm(item);
                    } catch (Exception e) {
                        System.out.println();
                        e.printStackTrace();
                    }
                    r.setTitle(text);
                    r.setDate(parsedDate);
                    r.setTime(parsedTime);
                    System.out.println("parsed: "+ parsedContent);
                    if(content.getEditText() != null){
                        parsedContent = content.getEditText().getText().toString();
                        r.setContent(parsedContent);
                    }


                    System.out.println("parsed: "+ parsedContent);

                    saveNote(r, parsedDate);

                    finish();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "sum is wrong you bozo", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNote(Reminder r, String date){
       Map<String, Map<String, String>> notesMap = getNotesMap();
       Map<String, String> note = notesMap.computeIfAbsent(date, k -> new HashMap<>());
       String noteJson = gson.toJson(r);

       note.put(r.getId(), noteJson);
       notesMap.put(date, note);

        System.out.println("this map is the result:    " + notesMap);

       saveNotesMap(notesMap);
    }

    public Map<String, Map<String,String>> getNotesMap(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyNotes", MODE_PRIVATE);
        String noteJson = sharedPreferences.getString("notesMap", "{}");

        Type type = new TypeToken<Map<String, Map<String,String>>>() {}.getType();
        return gson.fromJson(noteJson, type);
    }

    public void deleteNote(Map<String, Map<String, String>> m, String date, String id){
        Map<String, String> map = m.get(date);
        if (m.get(date) != null) {
            m.get(date).remove(id);
            System.out.println("oter bs: " + m);
            if (m.get(date).isEmpty()) {
                m.remove(date);
            }
        }
    }

    private void saveNotesMap(Map<String, Map<String,String>> map){
        SharedPreferences sharedPreferences = getSharedPreferences("MyNotes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String noteJson;
        noteJson = gson.toJson(map);

        editor.putString("notesMap", noteJson);
        editor.apply();
    }

    private static boolean isTimeValid(String timeInput) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeFormat.setLenient(false);

        try {
            Date parsedTime = timeFormat.parse(timeInput);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }



    private boolean checkDate(String date){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        f = f.withResolverStyle(ResolverStyle.LENIENT);
        f = f.withLocale(Locale.US);
        System.out.println(date);
        try {
            LocalDate ld = LocalDate.parse(date,f);
            return true;
        } catch ( DateTimeParseException e ) {
            System.out.println("error: " +e);
            return false;
        }
    }

    public void deletion(View v){
        Map<String, Map<String, String>> m = getNotesMap();
        if(!Objects.equals(id, "")){
            deleteNote(m, getIntent().getStringExtra("date"), getIntent().getStringExtra("ID"));
            saveNotesMap(m);
            try {
                LocalDateTime dateAndTime = LocalDateTime.parse(getIntent().getStringExtra("date") + "-" + getIntent().getStringExtra("time"), DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm"));
                AlarmItem item = new AlarmItem(dateAndTime, id.hashCode(), getIntent().getStringExtra("title"));
                scheduler.cancel(item);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
        finish();
    }

}
