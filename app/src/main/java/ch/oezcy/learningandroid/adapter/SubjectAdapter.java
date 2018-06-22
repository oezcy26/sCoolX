package ch.oezcy.learningandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.oezcy.learningandroid.R;
import ch.oezcy.learningandroid.db.entity.Subject;

public class SubjectAdapter extends ArrayAdapter<Subject> {

    public SubjectAdapter(Context ctx, List<Subject> subjects){
        super(ctx, 0, subjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data for this position
        Subject subject = getItem(position);

        //Check if view there is view to reuse, otherwise inflate
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_subject, parent, false);
        }

        //data population
        TextView subjectName = convertView.findViewById(R.id.lv_subject_name);
        TextView subjectAvg = convertView.findViewById(R.id.lv_subject_average);

        subjectName.setText(subject.title);
        subjectAvg.setText(String.valueOf(subject.average));

        return convertView;
    }
}
