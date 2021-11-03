package com.example.taskmaster2;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    ArrayList<Task> tasksList = new ArrayList<Task>();

    public TaskAdapter(ArrayList<Task> tasksList) {
        this.tasksList = tasksList;
    }




    public  class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;
        View itemView;

        public TaskViewHolder (@NonNull View itemView){
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(v -> {
                Intent goToDetailsPagePutExtra=new Intent(v.getContext(),TaskDetail.class);
                goToDetailsPagePutExtra.putExtra("taskNameClickListener",tasksList.get(getAdapterPosition()).title);
//                goToDetailsPagePutExtra.putExtra("taskBodyClickListener",task.body);
//                goToDetailsPagePutExtra.putExtra("taskStateClickListener",task.state);
                v.getContext().startActivity(goToDetailsPagePutExtra);
                Log.d("STRING", Integer.toString(getAdapterPosition()));
            });
        }
    }



    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
//        return taskViewHolder;
        return  new TaskViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        holder.task = tasksList.get(position);
        TextView title = holder.itemView.findViewById(R.id.TitleText);
        TextView body = holder.itemView.findViewById(R.id.BodyText);
        TextView state = holder.itemView.findViewById(R.id.StateText);

        title.setText(holder.task.title);
        body.setText(holder.task.body);
        state.setText(holder.task.state);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}