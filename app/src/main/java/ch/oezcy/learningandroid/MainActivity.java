package ch.oezcy.learningandroid;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.oezcy.learningandroid.db.SchoolXDatabase;
import ch.oezcy.learningandroid.db.entity.Subject;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEWSUBJECT = 1;
    public static final String RESULT_NEWSUBJECT = "ch.oezcy.learningApp.newsubjReply";

    private TableLayout table_subject;
    public static SchoolXDatabase db;
    private Subject contextSubject; // object of longclick

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                SchoolXDatabase.class, "schoolx").build();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        table_subject = findViewById(R.id.table_subject);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startNewSubjectActivity();
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });

        try {
            List<Subject> subjects = new SubjectAllSelector().execute().get();
            addSubjectsToView(subjects);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void addSubjectsToView(List<Subject> subjects) {
        table_subject.removeAllViews();
        for(Subject s : subjects){
            addNewTablerow(s);
        }
    }


    private void addNewTablerow(Subject subject) {
        TableRow tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.tablerow_subject, null);
        ((TextView)tableRow.findViewById(R.id.tablerow_subject_name)).setText(subject.title);
        tableRow.setTag(subject);
        registerForContextMenu(tableRow);
        table_subject.addView(tableRow);

    }

    /**
        When returning from another Activity with result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_NEWSUBJECT && resultCode==RESULT_OK){
            String newSubjectName = data.getStringExtra(RESULT_NEWSUBJECT);
            Subject subj = new Subject(newSubjectName);
            addNewTablerow(subj);
            new SubjectInserter().execute(new Subject(newSubjectName));

        }
    }

    /**
     * Methods implementing contextmenu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject_contextmenu, menu);
        contextSubject = (Subject)v.getTag();

        //TODO getTag from v , then store as field-variable to get in onContextItemSelected (unten). dort dann löschen
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ctx_subject_delete:
                deleteSubject(contextSubject);
                return true;
        }
        return false;

    }

    private void deleteSubject(Subject contextSubject) {
        try {
            List<Subject> subjects = new SubjectDeleter().execute(contextSubject).get();
            addSubjectsToView(subjects);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void startNewSubjectActivity(){
        Intent intent = new Intent(this, NewSubjectActivity.class);
        startActivityForResult(intent, REQUEST_NEWSUBJECT);

    }

    public void logSubjects(View v){
        try {
            List<Subject> subjects = new SubjectAllSelector().execute().get();
            for(Subject s : subjects){
                System.out.println();
                System.out.println(s);
                System.out.println();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class SubjectInserter extends AsyncTask<Subject, Void, Void>{

        @Override
        protected Void doInBackground(Subject... subjects) {
            db.subjectDao().insert(subjects[0]);
            return null;
        }
    }

    private class SubjectAllSelector extends AsyncTask<Void, Void, List<Subject>>{

        @Override
        protected List<Subject> doInBackground(Void... voids) {
            List<Subject> subjects = db.subjectDao().selectAllSubjects();
            return subjects;
        }
    }

    private class SubjectDeleter extends AsyncTask<Subject, Void, List<Subject>> {

        @Override
        protected List<Subject> doInBackground(Subject... subjects) {
            db.subjectDao().delete(subjects[0]);
            List<Subject> newSubj = db.subjectDao().selectAllSubjects();
            return newSubj;
        }
    }

}
