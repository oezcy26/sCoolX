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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ch.oezcy.learningandroid.adapter.SubjectAdapter;
import ch.oezcy.learningandroid.db.SchoolXDatabase;
import ch.oezcy.learningandroid.db.entity.Subject;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEWSUBJECT = 1;
    public static final String RESULT_NEWSUBJECT = "ch.oezcy.learningApp.newsubjReply";

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
        registerForContextMenu(listView);
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
            subjectsAdapter.add(subj);
            //addNewListitem(subj);
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
