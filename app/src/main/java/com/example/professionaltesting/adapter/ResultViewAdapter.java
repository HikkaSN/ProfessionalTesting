package com.example.professionaltesting.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.professionaltesting.R;
import com.example.professionaltesting.model.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewAdapter.ViewHolder> {

    private Map<Integer, Map<String, Object>> values = new HashMap<>();

    public ResultViewAdapter(){
    }

    public void setItems(Map<Integer, Map<String, Object>> values) {
        this.values = values;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ResultViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindToView(values.get(position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.textResultTitle);
            content = itemView.findViewById(R.id.textResultContent);
        }

        public void bindToView(Map<String, Object> hashValue){
            title.setText("Результат");
            String strContent = "";

            for (Map.Entry entry: hashValue.entrySet()) {
                strContent += entry.getKey() + ": " + entry.getValue();
                strContent += "\n";
            }
            content.setText(strContent);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

        }
    }
}
