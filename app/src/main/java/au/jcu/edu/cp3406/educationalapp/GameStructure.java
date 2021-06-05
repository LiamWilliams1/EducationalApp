package au.jcu.edu.cp3406.educationalapp;

import android.provider.BaseColumns;
// creates structure for questions in the form of a table
// for extension of the app at a later date
//
public final class GameStructure {
    private GameStructure() {
    }
    //Basecolumns provides a string ID which will automatically increment with each new entry
    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_ANSWER_NR = "answer_nr";
    }
}
