package com.example.sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite.MainActivity;
import com.example.sqlite.R;
import com.example.sqlite.entity.Task;

import java.util.List;


public class TaskAdapter extends BaseAdapter {

    private MainActivity context;
    private int layout;
    private List<Task> taskList;

    public TaskAdapter(MainActivity context, int layout, List<Task> taskList) {
        this.context = context;
        this.layout = layout;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            holder.task_name = convertView.findViewById(R.id.task_name);
            holder.image_delete = convertView.findViewById(R.id.image_delete);
            holder.image_edit = convertView.findViewById(R.id.image_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = taskList.get(position);
        holder.task_name.setText(task.getName());
        holder.image_delete.setImageResource(R.drawable.delete);
        holder.image_edit.setImageResource(R.drawable.edit);

        holder.image_edit.setOnClickListener(v -> {
            context.dialogEditTask(task.getName(), task.getId());
//            Toast.makeText(context, "update " + task.getName(), Toast.LENGTH_SHORT).show();
        });

        holder.image_delete.setOnClickListener(v -> {
            context.dialogDeleteTask(task.getName(), task.getId());
//            Toast.makeText(context, "delete " + task.getName(), Toast.LENGTH_SHORT).show();
        });

        return convertView;

    }

    private class ViewHolder{
        TextView task_name;
        ImageView image_delete, image_edit;
    }
}
