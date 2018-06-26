package ch.oezcy.learningandroid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.oezcy.learningandroid.db.entity.Subject;

@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subject")
    List<Subject> selectAllSubjects();

    @Query("SELECT * FROM subject where id = :id")
    Subject selectSubjectById(int id);

    @Insert
    long insert(Subject subj);

    @Delete
    void delete(Subject subj);



}
