package ec.ismf.debersemana5y6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import androidx.annotation.Nullable;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Registro.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table historial(codigo TEXT, nombre TEXT, edad TEXT, evento TEXT, fechaevento TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists historial");
    }

    public void insertar(String codigo, String nombre, String edad, String evento) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("codigo", codigo);
        cv.put("nombre", nombre);
        cv.put("edad", edad);
        cv.put("evento", evento);
        DateFormat dateFormat = new DateFormat();
        cv.put("fechaevento", dateFormat.format("yyyy-MM-dd hh:mm:ss",  new Date()).toString());
        long res = DB.insert("historial", null, cv);
    }

    public void borrar( String evento ) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from historial where evento = ?", new String[]{evento});
        if (cursor.getCount() > 0) {
            long result = DB.delete("historial", "evento=?", new String[]{evento});
        }
    }

    public Cursor listar() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from historial", null);
        return cursor;
    }

}
