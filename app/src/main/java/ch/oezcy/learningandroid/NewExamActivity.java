package ch.oezcy.learningandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewExamActivity extends AppCompatActivity {

    private EditText editNewExam;
    private EditText editExamNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);

        editNewExam = findViewById(R.id.edit_newexam);
        editExamNote = findViewById(R.id.edit_examnote);

        final Button button = findViewById(R.id.button_save_exam);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();

                String newExamText = editNewExam.getText().toString();
                double newExamNote = Double.parseDouble(editExamNote.getText().toString());

                replyIntent.putExtra(MainActivity.EXTRA_NEWEXAM_TITLE, newExamText);
                replyIntent.putExtra(MainActivity.EXTRA_NEWEXAM_NOTE, newExamNote);
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });

    }
}
