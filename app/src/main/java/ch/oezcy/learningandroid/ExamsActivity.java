package ch.oezcy.learningandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.oezcy.learningandroid.adapter.ExamAdapter;
import ch.oezcy.learningandroid.db.entity.Exam;
import ch.oezcy.learningandroid.db.entity.Subject;

import static ch.oezcy.learningandroid.MainActivity.EXTRA_NEWEXAM_NOTE;
import static ch.oezcy.learningandroid.MainActivity.EXTRA_NEWEXAM_TITLE;
import static ch.oezcy.learningandroid.MainActivity.FORRESULT_NEWEXAM;
import static ch.oezcy.learningandroid.MainActivity.db;

public class ExamsActivity extends AppCompatActivity {

    private Subject subject;
    private ExamAdapter examAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView examList = findViewById(R.id.listview_exams);


        //get Subject with the id in Extra
        int subjectId = getIntent().getIntExtra(MainActivity.EXTRA_SUBJECT_ID, -1);
        try {
            subject = new SubjectSelectorById().execute(subjectId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //get all exams and set Adapter
        try {
            List<Exam> exams = new ExamSelectorBySubjectId().execute(subjectId).get();
            examAdapter = new ExamAdapter(this, exams);
            examList.setAdapter(examAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void startNewExamActivity(View v){
        Intent intent = new Intent(this, NewExamActivity.class);
        startActivityForResult(intent, FORRESULT_NEWEXAM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FORRESULT_NEWEXAM && resultCode == RESULT_OK){
            String examTitle = data.getStringExtra(EXTRA_NEWEXAM_TITLE);
            double examNote = data.getDoubleExtra(EXTRA_NEWEXAM_NOTE, 0.0);

            Exam newExam = new Exam(subject.id, examTitle, examNote);
            examAdapter.add(newExam);
            new ExamInserter().execute(newExam);
        }
    }



    private class SubjectSelectorById extends AsyncTask<Integer, Void, Subject> {
        @Override
        protected Subject doInBackground(Integer... integers) {
            Integer subjectId = integers[0];
            Subject subject = db.subjectDao().selectSubjectById(subjectId);
            setTitle(subject.title);
            return subject;
        }
    }

    private class ExamSelectorBySubjectId extends AsyncTask<Integer, Void, List<Exam>> {
        @Override
        protected List<Exam> doInBackground(Integer... ints) {
            List<Exam> exams = db.examDao().selectExamsBySubjectId(ints[0]);
            return exams;
        }
    }

    private class ExamInserter extends AsyncTask<Exam, Void, Void> {
        @Override
        protected Void doInBackground(Exam... exams) {
            db.examDao().insert(exams[0]);
            return null;
        }
    }



}
