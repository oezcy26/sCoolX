package ch.oezcy.learningandroid;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.oezcy.learningandroid.adapter.SubjectAdapter;
import ch.oezcy.learningandroid.db.SchoolXDatabase;
import ch.oezcy.learningandroid.db.entity.Subject;

public class MainActivity extends AppCompatActivity {

    public static final int FORRESULT_NEWSUBJECT = 1;
    public static final int FORRESULT_NEWEXAM = 2;

    public static final String EXTRA_NEWSUBJECT_TITLE = "ch.oezcy.learningApp.newsubjReply";
    public static final String EXTRA_SUBJECT_ID = "ch.oezcy.learningApp.extra_Subjectid";
    public static final String EXTRA_NEWEXAM_TITLE = "ch.oezcy.learningapp.newaxamTitle";
    public static final String EXTRA_NEWEXAM_NOTE = "ch.oezcy.learningapp.newexamnote";

    private ListView listView;
    public static SchoolXDatabase db;
    private SubjectAdapter subjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                SchoolXDatabase.class, "schoolx").build();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listview_subjects);
        FloatingActionButton fab = findViewById(R.id.fab);
        registerForContextMenu(fab);

        List<Subject> subjects = new ArrayList<>();
        try {
            subjects = new SubjectAllSelector().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        subjectsAdapter = new SubjectAdapter(this,subjects);
        listView.setAdapter(subjectsAdapter);

        //context menu for listview
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject clickedSubject = subjectsAdapter.getItem(position);
                startExamsActivity(clickedSubject.id);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Subject> subjects = new ArrayList<>();
        try {
            subjects = new SubjectAllSelector().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        subjectsAdapter.clear();
        subjectsAdapter.addAll(subjects);

    }

    /**
        When returning from another Activity with result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FORRESULT_NEWSUBJECT && resultCode==RESULT_OK){
            String newSubjectName = data.getStringExtra(EXTRA_NEWSUBJECT_TITLE);
            Subject subj = new Subject(newSubjectName);
            try {
                int newId = new SubjectInserter().execute(new Subject(newSubjectName)).get();
                // id must set to be able search for exams, when ExamsActivity is started.
                subj.id = newId;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            subjectsAdapter.add(subj);
        }
    }

    /**
     * Methods implementing contextmenu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v instanceof ListView){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.subject_contextmenu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Subject subj = subjectsAdapter.getItem(info.position);

        switch (item.getItemId()){
            case R.id.ctx_subject_delete:
                subjectsAdapter.remove(subj);
                new SubjectDeleter().execute(subj);
                return true;
        }
        return false;
    }

    public void onFloatingABClicked(View v){
        startNewSubjectActivity();
    }


    private void startNewSubjectActivity(){
        Intent intent = new Intent(this, NewSubjectActivity.class);
        startActivityForResult(intent, FORRESULT_NEWSUBJECT);
    }

    private void startExamsActivity(int subjectId){
        Intent intent = new Intent(this, ExamsActivity.class);
        intent.putExtra(EXTRA_SUBJECT_ID, subjectId);
        startActivity(intent);
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

    private class SubjectInserter extends AsyncTask<Subject, Void, Integer>{
        @Override
        protected Integer doInBackground(Subject... subjects) {
            Subject subj = subjects[0];
            long newId = db.subjectDao().insert(subj);
            return (int)newId;
        }

    }

    private class SubjectAllSelector extends AsyncTask<Void, Void, List<Subject>>{
        @Override
        protected List<Subject> doInBackground(Void... voids) {
            List<Subject> subjects = db.subjectDao().selectAllSubjects();
            return subjects;
        }
    }

    private class SubjectDeleter extends AsyncTask<Subject, Void, Void> {
        @Override
        protected Void doInBackground(Subject... subjects) {
            db.subjectDao().delete(subjects[0]);
            return null;
        }
    }

}
