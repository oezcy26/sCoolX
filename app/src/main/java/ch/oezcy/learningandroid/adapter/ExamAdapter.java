package ch.oezcy.learningandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.oezcy.learningandroid.R;
import ch.oezcy.learningandroid.db.entity.Exam;

public class ExamAdapter extends ArrayAdapter<Exam> {

    public ExamAdapter(Context ctx, List<Exam> exams){
        super(ctx, 0, exams);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data for this position
        Exam exam = getItem(position);

        //Check if view there is view to reuse, otherwise inflate
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_general, parent, false);
        }

        //data population
        TextView subjectName = convertView.findViewById(R.id.listitem_name);
        TextView subjectAvg = convertView.findViewById(R.id.listitem_note);

        subjectName.setText(exam.title);
        subjectAvg.setText(String.valueOf(exam.note));


        return convertView;
    }

}
