package com.example.almullaexchange_demo.toDo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.almullaexchange_demo.R;

import java.util.List;


public class list_Data extends RecyclerView.Adapter<list_Data.viewHolder>
{
    private Context context;
    private List<model_List> dataModelArrayList;

    public list_Data(Context context, List<model_List> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        String title = dataModelArrayList.get(position).getTitle();
        String time = dataModelArrayList.get(position).getTime();

        holder.todoName.setText(title);
        holder.todoDate.setText(time);
    }


    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder{
        protected TextView todoName;
        protected TextView todoDate;

        public viewHolder( View holder)
        {
            super(holder);
            todoName =  holder.findViewById(R.id.todoName);
            todoDate =  holder.findViewById(R.id.todoDate);
        }
    }
}