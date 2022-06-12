package com.example.professionaltesting.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.professionaltesting.R;
import com.example.professionaltesting.model.Question;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<Question> questions = new ArrayList<>();
    private AdapterItemClickListener listener;

    public RecycleViewAdapter(AdapterItemClickListener listener){
        this.listener = listener;
    }

    public void setItems(Collection<Question> addingQuestions) {
        this.questions.addAll(addingQuestions);
        notifyDataSetChanged();
    }

    public void clearItems() {
        questions.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_for_check,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindToView(questions.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(questions.get(position) );
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder  {

        CardView cardView;
        TextView title, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            cardView = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.textViewQuestion);
            firstAnswer = itemView.findViewById(R.id.tvFirstAnswer);
            secondAnswer = itemView.findViewById(R.id.tvSeconsAnswer);
            thirdAnswer = itemView.findViewById(R.id.tvThirdAnswer);
            fourthAnswer = itemView.findViewById(R.id.tvFourthAnswer);
        }

        public void bindToView(Question question){
            title.setText(question.getTitle());
            firstAnswer.setText("A: " + question.getFirstAnswer());
            secondAnswer.setText("B: " + question.getSecondAnswer());
            thirdAnswer.setText("C: " + question.getThirdAnswer());
            fourthAnswer.setText("D: " + question.getFourthAnswer());
        }
    }
}
