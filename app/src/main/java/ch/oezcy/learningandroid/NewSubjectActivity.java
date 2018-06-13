package ch.oezcy.learningandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewSubjectActivity extends AppCompatActivity {


    private EditText editNewSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);

        editNewSubject = findViewById(R.id.edit_newsubject);

        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();

                String newSubjText = editNewSubject.getText().toString();
                replyIntent.putExtra(MainActivity.RESULT_NEWSUBJECT, newSubjText);
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });

    }
}
