package com.example.major_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.major_project.Helper.QuestionHelper;
import com.example.major_project.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionQD>  ///here QuestionQD is view holder class name hai
{
    public QuestionAdapter(List<QuestionHelper> questionList) {
        this.questionList = questionList;
    }

    List<QuestionHelper>questionList;

    @NonNull
    @Override
    public QuestionQD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new QuestionQD(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionQD holder, int position) {
        QuestionHelper ques =  questionList.get(position);
        holder.questiontxt.setText(ques.getQuestion());
        holder.descriptiontxt.setText(ques.getDescription());

        boolean isExpandable  = questionList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE :View.GONE);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionQD extends RecyclerView.ViewHolder {


        TextView questiontxt , descriptiontxt;

        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        public QuestionQD(@NonNull View itemView) {
            super(itemView);

            questiontxt = itemView.findViewById(R.id.question);
            descriptiontxt = itemView.findViewById(R.id.description1);

            linearLayout = itemView.findViewById(R.id.linear);
            expandableLayout = itemView.findViewById(R.id.expanded_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QuestionHelper ques = questionList.get(getAdapterPosition());
                    ques.setExpandable(!ques.isExpandable());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}

