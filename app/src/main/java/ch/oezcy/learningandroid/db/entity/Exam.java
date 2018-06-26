package ch.oezcy.learningandroid.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity (foreignKeys = @ForeignKey(entity = Subject.class,
        parentColumns = "id",
        childColumns = "subject_id"))
public class Exam {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "subject_id")
    public int subjectId;

    public String title;

    public double note;

    public Exam(int subjectId, String title, double note) {
        this.subjectId = subjectId;
        this.title = title;
        this.note = note;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(subjectId);
        sb.append(" ");
        sb.append(title);
        sb.append(" ");
        sb.append(note);
        return sb.toString();
    }
}
