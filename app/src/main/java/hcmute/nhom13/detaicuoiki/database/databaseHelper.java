package hcmute.nhom13.detaicuoiki.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Class đọc ghi database
public class databaseHelper extends SQLiteOpenHelper {
    //Đường dẫn (vị trí) của cơ sở dữ liệu trên thiết bị
    private String DB_PATH = " ";
    private static String DB_NAME = "busmap.sqlite"; //Tên database
    private final Context mContext;
    private SQLiteDatabase mDataBase;

    //Do đường dẫn ở phiên bản API > 17 thay đổi nên cần kiểm tra
    public databaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1); // 1? Phiên bản cơ sở dữ liệu
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() {
        // Nếu database không tồn tại, sao chép từ các phần tử.
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
            // Sao chép database từ folder assets
                copyDataBase();
                Log.e("Thanh cong", "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Kiểm tra database có tồn tại: /data/data/your package/databases/DB_NAME
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    // Sao chép database từ folder assets
    private void copyDataBase() throws IOException {
        // Lấy database đưa vào myInput
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        // Lấy đường dẫn file đưa vào myInput
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        // Chép dữ liệu từ myInput vào myOutput
        // Đọc từng buffer
        while ((mLength = mInput.read(mBuffer)) > 0) {
            // Ghi vào myOutput
            mOutput.write(mBuffer, 0, mLength);
        }
        // Làm rỗng myOutput
        mOutput.flush();
        // Đóng các file lại
        mOutput.close();
        mInput.close();
    }

    //Mở database, tiến hành truy vấn
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    // Đóng database
    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    //Truy vấn không trả kết quả: CREATE, INSERT, UPDATE,..
    public void QueryData(String sql)
    {
        mDataBase =getWritableDatabase();
        mDataBase.execSQL(sql);
    }

    // Truy vấn có trả kết quả: SELECT
    public Cursor GetData(String sql)
    {
        mDataBase = getReadableDatabase();
        return  mDataBase.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
