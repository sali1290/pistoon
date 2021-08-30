package ir.rahnama.pistoon.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.util.Log
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import ir.rahnama.pistoon.model.*


class MyDatabase(var context: Context) : SQLiteAssetHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {


    private var query = ""


    companion object{
        private const val DATABASE_NAME = "database.db"
        private const val DATABASE_VERSION = 1

        // inja esm table haro be sorat final minevisim baraye dastresi dar kol barname
        private const val GOLDENTEST_TABLE = "golden_quiz"
        private const val GOLDENTEST_PART = "part"

        //PAGE COUNTERS
        private const val PAGE1 = "page1"
        private const val PAGE2 = "page2"
        private const val PAGE3 = "page3"
        private const val PAGE4 = "page4"
        private const val PAGE5 = "page5"
        private const val PAGE6 = "page6"
        private const val IMAGE = "image"


        private const val THEORY_COURSE_TABLE = "theory_course_table"
        private const val THEORY_COURSE_ID = "id"
        private const val THEORY_COURSE_QUIZ_IS_DONE = "quiz_is_done"
        private const val THEORY_COURSE_COURSE_IS_DONE = "course_is_done"
        private const val THEORY_COURSE_TYPE = "type"
        private const val THEORY_COURSE_TITLE = "title"

        private const val PRACTICAL_COURSE_TABLE = "practical_course_table"
        private const val PRACTICAL_COURSE_ID = "id"
        private const val PRACTICAL_COURSE_QUIZ_IS_DONE = "quiz_is_done"
        private const val PRACTICAL_COURSE_COURSE_IS_DONE = "course_is_done"
        private const val PRACTICAL_COURSE_TYPE =  "type"
        private const val PRACTICAL_COURSE_TITLE = "title"


        private const val BOARD_TABLE = "board_table"
        private const val BOARD_TYPE = "type"
        private const val BOARD_TITLE = "title"

        private const val QUIZ_TABLE = "quiz_table"
        private const val QUIZ_ID = "id"


        private const val TRICKY_TABLE = "wrong_question_table"
        private const val TRICKY_QUIZ_ID = "quiz_id"


        private const val GUIDELINE_TABLE = "guideline_table"


        //saved tanle
        private const val SAVED_COURSE_TABLE = "saved_course_page"
        private const val SAVED_COURSE_TYPE = "course_type"
        private const val SAVED_COURSE_ID = "course_id"
        private const val SAVED_COURSE_PAGE = "course_page"



        //percent list
        private const val PERCENT_QUIZ_TABLE = "quiz_percent"
        private const val PERCENT_QUIZ_ID = "id"

        //percent list
        private const val PERCENT_EXAM_TABLE = "exam_percent"
        private const val PERCENT_EXAM_ID = "id"
        private const val PERCENT = "percent"


        //quiz numbers
        private const val QUIZ_NUMBERS = "quiz_number"
        private const val EXAM_TABLE   = "exam_tabel"
        private const val EXAM_NUMBER = "exam_number"


        //save quiz
        private const val SAVED_QUIZ = "saved_quiz"
        private const val SAVED_QUIZ_ID = "quiz_id"
        private const val SAVED_QUIZ_ID_PR = "id"


    }







    fun getGoldenTest(partName: String) :List<GoldenTestModel>{
        val data :MutableList<GoldenTestModel> = ArrayList()
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $GOLDENTEST_TABLE WHERE $GOLDENTEST_PART = ? "
        val cursor = db.rawQuery(query, arrayOf(partName))
        if (cursor.moveToFirst())
            data.clear()
        do {
            val id =cursor.getString(0)
            val part = cursor.getString(1)
            val title = cursor.getString(2)
            val text =  cursor.getString(3)
            data.add(GoldenTestModel(id.toLong(), part.toLong(), title, text))
        }while (cursor.moveToNext())
        cursor.close()
        db.close()

        return data
    }


    fun getTheoryCourse() :List<CourseListModel>{
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $THEORY_COURSE_TABLE"
        val cursor = CourseListCursor(db.rawQuery(query, null))
        db.close()
        return cursor
    }


    fun getTheoryCourseById(id: String) :MutableList<CourseListModel>{
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $THEORY_COURSE_TABLE WHERE $THEORY_COURSE_ID = ?  "
        val cursor = CourseListCursor(db.rawQuery(query, arrayOf(id)))
        db.close()
        return cursor
    }


    fun getPracticalCourse() :List<CourseListModel>{
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PRACTICAL_COURSE_TABLE"
        val cursor = CourseListCursor(db.rawQuery(query, null))
        db.close()
        return cursor
    }

    fun getPracticalCourseById(id: String) :MutableList<CourseListModel>{
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PRACTICAL_COURSE_TABLE WHERE $PRACTICAL_COURSE_ID = ?  "
        val cursor = CourseListCursor(db.rawQuery(query, arrayOf(id)))
        db.close()
        return cursor
    }

    fun getTheoryQuizIsDone() :Long{
        var data :Long = 0
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT $THEORY_COURSE_QUIZ_IS_DONE FROM $THEORY_COURSE_TABLE"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
        do {
            val id =cursor.getString(0)
            if (id=="1") data++
        }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data

    }


    fun setTheoryIsDone(theory_id: String, quiz: String, course: String){
        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(THEORY_COURSE_QUIZ_IS_DONE, quiz)
        contentValues.put(THEORY_COURSE_COURSE_IS_DONE, course)
        db.update(THEORY_COURSE_TABLE, contentValues, "$THEORY_COURSE_ID = ? ", arrayOf(theory_id))
        db.close()
    }

    fun setPracticalIsDone(theory_id: String, quiz: String, course: String){
        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(PRACTICAL_COURSE_QUIZ_IS_DONE, quiz)
        contentValues.put(PRACTICAL_COURSE_COURSE_IS_DONE, course)
        db.update(
            PRACTICAL_COURSE_TABLE, contentValues, "$PRACTICAL_COURSE_ID = ? ", arrayOf(
                theory_id
            )
        )
        db.close()
    }

    fun getPracticalQuizIsDone() :Long{
        var data :Long = 0
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT $PRACTICAL_COURSE_QUIZ_IS_DONE FROM $PRACTICAL_COURSE_TABLE"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {
                val id =cursor.getString(0)
                if (id=="1") data++
            }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data

    }

    fun getBoardByType(part: String) :MutableList<BoardModel>{
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $BOARD_TABLE WHERE $BOARD_TYPE = ?  "
        val cursor =BoardListCursor(db.rawQuery(query, arrayOf(part)))
        db.close()
        return cursor
    }

    fun getAllBoard() :MutableList<BoardModel>{
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $BOARD_TABLE "
        val cursor = BoardListCursor(db.rawQuery(query, null))
        db.close()
        return cursor



    }

    fun getAllGuideLine() :List<GuideLineModel>{
        val data :MutableList<GuideLineModel> = ArrayList()
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $GUIDELINE_TABLE "
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {


                val id = cursor.getString(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val text = cursor.getString(cursor.getColumnIndex("text"))
                val image = cursor.getBlob(cursor.getColumnIndex("image"))
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)

                data.add(GuideLineModel(id.toLong(), title, text, imageBitmap))


            }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data



    }

     fun SearchInBoard(value: String) : MutableList<BoardModel>{
         val db :SQLiteDatabase = readableDatabase
         query=" SELECT * FROM $BOARD_TABLE WHERE $BOARD_TITLE LIKE '%$value%' "
         val cursor = BoardListCursor(db.rawQuery(query, null))
         db.close()
         return cursor
     }

    fun deleteAllTricky(){
        val db :SQLiteDatabase = writableDatabase
        db.delete("wrong_question_table", null, null)
        db.close()
    }

    fun deleteTrickyById(id: String){
        val db :SQLiteDatabase = writableDatabase
        db.delete("wrong_question_table", "quiz_id=?", arrayOf(id))
        db.close()
    }

   fun getTrickyQuiz():MutableList<AllTestModel>{
       val db :SQLiteDatabase = readableDatabase
       query=" SELECT * FROM $QUIZ_TABLE INNER JOIN $TRICKY_TABLE ON $QUIZ_TABLE.$QUIZ_ID=$TRICKY_TABLE.$TRICKY_QUIZ_ID "
       val cursor = QuizListCursor(db.rawQuery(query, null))
       db.close()
       return cursor
   }

    fun saveTrickyTest(quizId: Int){
        val db : SQLiteDatabase=writableDatabase
        query=" SELECT * FROM $TRICKY_TABLE WHERE $TRICKY_QUIZ_ID = ?  "
        val cursor=db.rawQuery(query, arrayOf(quizId.toString()))
        if (cursor.count==0){
            val contentValues=ContentValues()
            contentValues.put(TRICKY_QUIZ_ID, quizId)
            db.insert(TRICKY_TABLE, null, contentValues)
        }
        cursor.close()
        db.close()
    }

    fun getQuizForCourse(firstId: String , secondId:String):MutableList<AllTestModel>{
        val data :MutableList<AllTestModel> = ArrayList()
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $QUIZ_TABLE WHERE $QUIZ_ID = ?  "
        var cursor = db.rawQuery(query, arrayOf(firstId))
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val answer1 = cursor.getString(cursor.getColumnIndex("answer1"))
                val answer2 = cursor.getString(cursor.getColumnIndex("answer2"))
                val answer3 = cursor.getString(cursor.getColumnIndex("answer3"))
                val answer4 = cursor.getString(cursor.getColumnIndex("answer4"))
                val true_answer = cursor.getString(cursor.getColumnIndex("true_answer"))
                val image  = cursor.getBlob(cursor.getColumnIndex("image"))
                if (image==null){
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong()))
                }else{ val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong(), imageBitmap))
                }
            }while (cursor.moveToNext())
            cursor.close()
        }

        query=" SELECT * FROM $QUIZ_TABLE WHERE $QUIZ_ID = ?  "
         cursor = db.rawQuery(query, arrayOf(secondId))
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val answer1 = cursor.getString(cursor.getColumnIndex("answer1"))
                val answer2 = cursor.getString(cursor.getColumnIndex("answer2"))
                val answer3 = cursor.getString(cursor.getColumnIndex("answer3"))
                val answer4 = cursor.getString(cursor.getColumnIndex("answer4"))
                val true_answer = cursor.getString(cursor.getColumnIndex("true_answer"))
                val image  = cursor.getBlob(cursor.getColumnIndex("image"))
                if (image==null){
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong()))
                }else{ val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong(), imageBitmap))
                }
            }while (cursor.moveToNext())}
        cursor.close()
        db.close()
        return data
    }

    //cursor k amal khandan etelaat database ro baraye hame model haye course anjam mide
    private fun CourseListCursor(cursor: Cursor):MutableList<CourseListModel>{
        val data :MutableList<CourseListModel> = ArrayList()
        data.clear()
        if (cursor.moveToFirst()){
            do {
                val id =cursor.getString(cursor.getColumnIndex("id"))
                val type =cursor.getString(cursor.getColumnIndex("type"))
                val image =cursor.getBlob(cursor.getColumnIndex("image"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val page1 =  cursor.getString(cursor.getColumnIndex("page1"))
                val page2 =  cursor.getString(cursor.getColumnIndex("page2"))
                val page3 =  cursor.getString(cursor.getColumnIndex("page3"))
                val page4 =  cursor.getString(cursor.getColumnIndex("page4"))
                val page5 =  cursor.getString(cursor.getColumnIndex("page5"))
                val page6 =  cursor.getString(cursor.getColumnIndex("page6"))
                val course_is_done =   cursor.getString(cursor.getColumnIndex("course_is_done"))
                val quiz_is_done =   cursor.getString(cursor.getColumnIndex("quiz_is_done"))
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)


                data.add(
                    CourseListModel(
                        id.toLong(),
                        type.toLong(),
                        imageBitmap,
                        title,
                        page1,
                        page2,
                        page3,
                        page4,
                        page5,
                        page6,
                        course_is_done.toLong(),
                        quiz_is_done.toLong()
                    )
                )
            }while (cursor.moveToNext())}
        cursor.close()

        return data
    }

    //cursor k amal khandan etelaat database ro baraye hame model haye board anjam mide
    private fun BoardListCursor(cursor: Cursor):MutableList<BoardModel>{
        val data :MutableList<BoardModel> = ArrayList()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(0)
                val type = cursor.getString(1)
                val title = cursor.getString(2)
                val image = cursor.getBlob(cursor.getColumnIndex("image"))
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                data.add(BoardModel(id.toLong(), type, title, imageBitmap))
            }while (cursor.moveToNext())}
        cursor.close()
        return data
    }

    //cursor k amal khandan etelaat database ro baraye hame model haye quiz anjam mide
    private fun QuizListCursor(cursor: Cursor):MutableList<AllTestModel>{
        val data :MutableList<AllTestModel> = ArrayList()


        if (cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val answer1 = cursor.getString(cursor.getColumnIndex("answer1"))
                val answer2 = cursor.getString(cursor.getColumnIndex("answer2"))
                val answer3 = cursor.getString(cursor.getColumnIndex("answer3"))
                val answer4 = cursor.getString(cursor.getColumnIndex("answer4"))
                val true_answer = cursor.getString(cursor.getColumnIndex("true_answer"))
                val image  = cursor.getBlob(cursor.getColumnIndex("image"))
                if (image==null){
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong()))
                }else{ val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    data.add(AllTestModel(id.toLong(), title, answer1, answer2, answer3, answer4, true_answer.toLong(), imageBitmap))
                }
            }while (cursor.moveToNext())}
        cursor.close()
        return data
    }



    fun SavedCoursePages(course_type: String, courseId: String, coursePage: String){
        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(SAVED_COURSE_TYPE, course_type)
        contentValues.put(SAVED_COURSE_ID, courseId)
        contentValues.put(SAVED_COURSE_PAGE, coursePage)
        db.insert(SAVED_COURSE_TABLE,null,contentValues)
        db.close()
    }

    fun DeleteSavedCoursePages(course_type: String, courseId: String, coursePage: String){
        val db : SQLiteDatabase=writableDatabase
        query=" DELETE FROM $SAVED_COURSE_TABLE WHERE ( $SAVED_COURSE_TYPE = $course_type AND $SAVED_COURSE_ID = $courseId AND $SAVED_COURSE_PAGE = $coursePage ) "
        db.execSQL(query)
        db.close()
    }


    fun getSavedCoursePageNumber(courseType:String , courseId:String) : MutableList<Int>{

        val data :MutableList<Int> = arrayListOf()
        val db:SQLiteDatabase=readableDatabase
        query="SELECT * FROM $SAVED_COURSE_TABLE WHERE ( $SAVED_COURSE_TYPE = $courseType AND $SAVED_COURSE_ID = $courseId ) "
        val cursor = db.rawQuery(query,null)
        if (cursor.moveToFirst()){
            do {
                val pageNum = cursor.getInt(cursor.getColumnIndex(SAVED_COURSE_PAGE))
                data.add(pageNum)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
return data
    }



    fun loadSavedCources () : MutableList<SavedCourseModel> {

        val data : MutableList<SavedCourseModel> = arrayListOf()
        val db : SQLiteDatabase = readableDatabase
        query = "SELECT * FROM $THEORY_COURSE_TABLE LEFT JOIN $SAVED_COURSE_TABLE ON $THEORY_COURSE_TABLE.$THEORY_COURSE_TYPE=$SAVED_COURSE_TABLE.$SAVED_COURSE_TYPE" +
                " WHERE $THEORY_COURSE_TABLE.$THEORY_COURSE_ID = $SAVED_COURSE_TABLE.$SAVED_COURSE_ID "
        var cursor = db.rawQuery(query , null)
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(THEORY_COURSE_ID))
                val course_type  = cursor.getString(cursor.getColumnIndex(SAVED_COURSE_TYPE))
                val title = cursor.getString(cursor.getColumnIndex(THEORY_COURSE_TITLE))
                val pageNum = cursor.getInt(cursor.getColumnIndex(SAVED_COURSE_PAGE))
                val image = cursor.getBlob(cursor.getColumnIndex(IMAGE))
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                var body= ""
                when(pageNum){
                    0 -> body = cursor.getString(cursor.getColumnIndex(PAGE1))
                    1 -> body = cursor.getString(cursor.getColumnIndex(PAGE2))
                    2 -> body = cursor.getString(cursor.getColumnIndex(PAGE3))
                    4 -> body = cursor.getString(cursor.getColumnIndex(PAGE4))
                    5 -> body = cursor.getString(cursor.getColumnIndex(PAGE5))
                    7 -> body = cursor.getString(cursor.getColumnIndex(PAGE6))
                }
                Log.i("test",pageNum.toString())
                data.add(SavedCourseModel(id.toLong() , course_type , title , body , pageNum.toLong(), imageBitmap))
            }while (cursor.moveToNext())
            cursor.close()
        }

        query =  "SELECT * FROM $PRACTICAL_COURSE_TABLE INNER JOIN $SAVED_COURSE_TABLE ON practical_course_table.type = $SAVED_COURSE_TABLE.$SAVED_COURSE_TYPE " +
                "WHERE $PRACTICAL_COURSE_TABLE.$PRACTICAL_COURSE_ID = $SAVED_COURSE_TABLE.$SAVED_COURSE_ID"
        cursor = db.rawQuery(query , null)
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(PRACTICAL_COURSE_ID))
                val course_type  = cursor.getString(cursor.getColumnIndex(SAVED_COURSE_TYPE))
                val title = cursor.getString(cursor.getColumnIndex(PRACTICAL_COURSE_TITLE))
                val pageNum = cursor.getInt(cursor.getColumnIndex(SAVED_COURSE_PAGE))
                val image = cursor.getBlob(cursor.getColumnIndex(IMAGE))
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                var body : String = ""
                when(pageNum){
                    0 -> body = cursor.getString(cursor.getColumnIndex(PAGE1))
                    1 -> body = cursor.getString(cursor.getColumnIndex(PAGE2))
                    2 -> body = cursor.getString(cursor.getColumnIndex(PAGE3))
                    4 -> body = cursor.getString(cursor.getColumnIndex(PAGE4))
                    5 -> body = cursor.getString(cursor.getColumnIndex(PAGE5))
                    7 -> body = cursor.getString(cursor.getColumnIndex(PAGE6))
                }
                data.add(SavedCourseModel(id.toLong() , course_type , title , body , pageNum.toLong(), imageBitmap))
            }while (cursor.moveToNext())
            cursor.close()
            db.close()
        }
        return data
    }




    fun getquizList():MutableList<TestListModel>{
        val data :MutableList<TestListModel> = ArrayList()
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PERCENT_QUIZ_TABLE "
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {
                val id =cursor.getString(cursor.getColumnIndex("id"))
                val name =cursor.getString(cursor.getColumnIndex("name"))
                val percent =cursor.getString(cursor.getColumnIndex("percent"))
                data.add(TestListModel(id.toLong(), name, percent.toLong()))
            }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }
    fun getExamList():MutableList<TestListModel>{
        val data :MutableList<TestListModel> = ArrayList()
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PERCENT_EXAM_TABLE "
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {
                val id =cursor.getString(cursor.getColumnIndex("id"))
                val name =cursor.getString(cursor.getColumnIndex("name"))
                val percent =cursor.getString(cursor.getColumnIndex("percent"))
                data.add(TestListModel(id.toLong(), name, percent.toLong()))
            }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }

    fun getQuizTruePercent():Int {
        var data :Int=0
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PERCENT_QUIZ_TABLE"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {
                val percent1 =cursor.getString(cursor.getColumnIndex("percent")).toInt()
                data += percent1
            } while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }


    fun setQuizPercent(quizId: String, percent: Int){

        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(PERCENT, percent)
        db.update(PERCENT_QUIZ_TABLE, contentValues, "$PERCENT_QUIZ_ID = ? ", arrayOf(quizId))
        db.close()
    }

    fun setExamPercent(examId: String, percent: Int){

        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(PERCENT, percent)
        db.update(PERCENT_EXAM_TABLE, contentValues, "$PERCENT_EXAM_ID = ? ", arrayOf(examId))
        db.close()
    }


    fun getExamTruePercent():Int {
        var data :Int=0
        val db : SQLiteDatabase = readableDatabase
        query= " SELECT * FROM $PERCENT_EXAM_TABLE"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst())
            do {
                val percent1 =cursor.getString(cursor.getColumnIndex("percent")).toInt()
                data += percent1
            } while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }


    fun getExamTests(type: Int) : MutableList<AllTestModel> {
        val db : SQLiteDatabase = readableDatabase
        query = "SELECT * FROM $EXAM_TABLE   WHERE ( $EXAM_NUMBER = $type ) "
        val cursor = QuizListCursor(db.rawQuery(query, null))
        db.close()
        return cursor
    }

    fun getQuizTests(type: Int) : MutableList<AllTestModel> {
        val db : SQLiteDatabase = readableDatabase
        query= "SELECT * FROM $QUIZ_TABLE WHERE ( $QUIZ_NUMBERS = $type ) "
        val cursor = QuizListCursor(db.rawQuery(query, null))
        db.close()
        return cursor
    }


    fun setSaveQuizId(quizid: Int){
        val db : SQLiteDatabase=writableDatabase
        val contentValues =ContentValues()
        contentValues.put(SAVED_QUIZ_ID, quizid)
        db.insert(SAVED_QUIZ, null, contentValues)
        db.close()
    }

    fun getSavedQuiz() : MutableList<AllTestModel>{
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $QUIZ_TABLE INNER JOIN $SAVED_QUIZ ON $QUIZ_TABLE.$QUIZ_ID=$SAVED_QUIZ.$SAVED_QUIZ_ID "
        val cursor = QuizListCursor(db.rawQuery(query, null))
        db.close()
        return cursor

    }

    fun getSavedQuizOrderedId() : MutableList<Int>{
        val data= arrayListOf<Int>()
        val db :SQLiteDatabase = readableDatabase
        query=" SELECT * FROM $SAVED_QUIZ "
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()){
            do {
                val id =cursor.getString(cursor.getColumnIndex("quiz_id"))
                data.add(id.toInt())
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return data

    }

    fun deleteSavedQuiz(quizid: String){
        val db :SQLiteDatabase = writableDatabase
        val deleteQuery = "DELETE FROM $SAVED_QUIZ WHERE $SAVED_QUIZ_ID_PR='$quizid'"
        db.execSQL(deleteQuery)
        db.close()
    }




}