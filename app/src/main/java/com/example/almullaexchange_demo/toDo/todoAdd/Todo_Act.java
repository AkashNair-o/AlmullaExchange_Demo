package com.example.almullaexchange_demo.toDo.todoAdd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.almullaexchange_demo.R;
import com.example.almullaexchange_demo.toDo.DatabaseHelper;
import com.example.almullaexchange_demo.toDo.ToDo;
import com.example.almullaexchange_demo.toDo.model_List;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Todo_Act extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    Button btn_dateTime;
    EditText edit_task;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;

    DatabaseHelper addtask;
    List<model_List> dataModelArrayList = new ArrayList<>();

    String finalDate = "Date and Time";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_);

        btn_dateTime = findViewById(R.id.btn_dateTime);
        edit_task = findViewById(R.id.edit_task);
        addtask = new DatabaseHelper(this);

        Bundle b;
        if (getIntent().getExtras() != null)
        {
            b = getIntent().getExtras();
            id = b.getInt("id");

            // //  Log.e("ASA ", template_type);
        }


        btn_dateTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Todo_Act.this, Todo_Act.this,year, month,day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(Todo_Act.this, Todo_Act.this, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        String AM_PM ;
        if(hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }

        myHour = hourOfDay;
        myMinute = minute;

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

        String date_time = myday +"/" + myMonth +"/" + myYear +"  "+ strHrsToShow+":"+ myMinute+" "+AM_PM;
        finalDate = date_time;

        btn_dateTime.setText(date_time);
    }

    public void addData(View view)
    {
        if (finalDate.equals("Date and Time"))
        {
            Toast.makeText(this, "Select Date and Time!", Toast.LENGTH_SHORT).show();
        }
        else if (edit_task.getText().length()<1)
        {
            Toast.makeText(this, "Add TODO Task!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            {
                model_List playerModel = new model_List();
                playerModel.setTitle(edit_task.getText().toString().trim());
                playerModel.setTime(finalDate);
                playerModel.setId(String.valueOf(id));
                model_List dataModel = new model_List(String.valueOf(id), edit_task.getText().toString().trim(), finalDate);
                addtask.addData(dataModel);
                dataModelArrayList.add(playerModel);
                Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ToDo.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void clearData(View view)
    {
        edit_task.setText("");
        btn_dateTime.setText("Date and Time");
    }

    public void backAdd(View view)
    {
        finish();
    }
}