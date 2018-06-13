package ch.oezcy.learningandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_NEWSUBJECT = 1;
    public static final String RESULT_NEWSUBJECT = "ch.oezcy.learningApp.newsubjReply";

    private TableLayout table_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_NEWSUBJECT && resultCode==RESULT_OK){
            String newsubject = data.getStringExtra(RESULT_NEWSUBJECT);
            addNewTablerow(newsubject);

        }
    }

    private void startNewSubjectActivity(){
        Intent intent = new Intent(this, NewSubjectActivity.class);
        startActivityForResult(intent, REQUEST_NEWSUBJECT);

    }

    private void addNewTablerow(String newsubject) {
        TableRow tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.tablerow_subject, null);
        ((TextView)tableRow.findViewById(R.id.tablerow_subject_name)).setText(newsubject);
        table_subject.addView(tableRow);

    }

}
