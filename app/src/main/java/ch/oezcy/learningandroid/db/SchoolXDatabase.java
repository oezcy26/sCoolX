package ch.oezcy.learningandroid.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ch.oezcy.learningandroid.db.dao.SubjectDao;
import ch.oezcy.learningandroid.db.entity.Subject;

@Database(entities = {Subject.class}, version = 1)
public abstract class SchoolXDatabase extends RoomDatabase {
    public abstract SubjectDao subjectDao();

}
