package com.example.taskmaster2;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Todo;

import java.util.ArrayList;
import java.util.List;

//public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
//    List<Todo> allTasksData = new ArrayList<>();
//    public TaskAdapter(ArrayList<Todo> tasksList) {
//        this.allTasksData = tasksList;
//    }
//
//
//
//
//    public static  class TaskViewHolder extends RecyclerView.ViewHolder{
//        public Todo task;
//        View itemView;
//
//        public TaskViewHolder (@NonNull View itemView){
//            super(itemView);
//            this.itemView = itemView;
//            itemView.setOnClickListener(v -> {
//                Intent goToDetailsPagePutExtra=new Intent(v.getContext(),TaskDetail.class);
//                goToDetailsPagePutExtra.putExtra("taskName", task.getTitle());
////                goToDetailsPagePutExtra.putExtra("taskBodyClickListener",task.body);
////                goToDetailsPagePutExtra.putExtra("taskStateClickListener",task.state);
//                v.getContext().startActivity(goToDetailsPagePutExtra);
////                Log.d("STRING", Integer.toString(getAdapterPosition()));
//            });
//        }
//    }
//
//
//
//    @NonNull
//    @Override
//    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
////        return taskViewHolder;
//        return  new TaskViewHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
//        holder.task = allTasksData.get(position);
//        TextView title = holder.itemView.findViewById(R.id.TitleText);
//        TextView body = holder.itemView.findViewById(R.id.BodyText);
//        TextView state = holder.itemView.findViewById(R.id.StateText);
//
//        title.setText(holder.task.getTitle());
//        body.setText(holder.task.getBody());
//        state.setText(holder.task.getState());
//    }
//
//    @Override
//    public int getItemCount() {
//        return allTasksData.size();
//    }
//}

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final List<TaskOld> task;
    private OnTaskItemClickListener listener;


    public TaskAdapter(List<TaskOld> taskMasterItem, OnTaskItemClickListener listener) {
        this.task = taskMasterItem;
        this.listener = listener;
    }


    public interface OnTaskItemClickListener {
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent,false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskOld item = task.get(position);
        holder.title.setText(item.title);
        holder.body.setText(item.body);
        holder.state.setText(item.state);

    }

    @Override
    public int getItemCount() {
        return task.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView body;
        private TextView state ;
        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener){
            super(itemView);

            title = itemView.findViewById(R.id.TitleText);
            body = itemView.findViewById(R.id.BodyText);
            state = itemView.findViewById(R.id.StateText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(getAdapterPosition());

                }
            });
        }

    }
}