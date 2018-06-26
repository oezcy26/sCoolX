package ch.oezcy.learningandroid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.oezcy.learningandroid.db.entity.Exam;

@Dao
public interface ExamDao {

    @Insert
    void insert(Exam exam);

    @Query("SELECT * FROM exam WHERE subject_id=:subjectId")
    List<Exam> selectExamsBySubjectId(int subjectId);
}
