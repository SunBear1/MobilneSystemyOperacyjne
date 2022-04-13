import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATABASENAME = "MY DATABASE"
val TABLENAME = "LAB3"
val COL_TEXT = "name"
val COL_DATE = "age"
val COL_ID = "id"

class Entry(
    val text: String,
    val date: String,
)
{}

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TEXT + " VARCHAR(256)," + COL_DATE + " DATETIME)"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }
    fun insertData(text: String, date: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TEXT, text)
        contentValues.put(COL_DATE, date)

        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Błąd", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Dodano rekord", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("Range")
    fun readData(): MutableList<Entry> {
        val list: MutableList<Entry> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                //var currentId = result.getString(result.getColumnIndex(COL_ID)).toInt()
                var currentText = result.getString(result.getColumnIndex(COL_TEXT))
                var currentDate = result.getString(result.getColumnIndex(COL_DATE))
                val entry = Entry(currentText,currentDate)
                list.add(entry)
            }
            while (result.moveToNext())
        }
        return list
    }
}

