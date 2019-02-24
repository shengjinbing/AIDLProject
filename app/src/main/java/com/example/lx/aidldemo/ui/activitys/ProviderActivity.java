package com.example.lx.aidldemo.ui.activitys;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lx.aidldemo.R;
import com.example.lx.aidldemo.bean.Book;
import com.example.lx.aidldemo.bean.User;
import com.example.lx.aidldemo.ui.provider.BookProvider;

public class ProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        initData();
    }

    /**
     * query,update,insert,delete都运行在binder线程中
     */
    private void initData() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

           /*
           Uri uri = Uri.parse("content://com.example.lx.aidldemo.ui.provider");
           ContentResolver resolver = getContentResolver();
            resolver.query(uri,null,null,null,null,null);
            resolver.query(uri,null,null,null,null,null);
            resolver.query(uri,null,null,null,null,null);*/
            Uri bookUri = Uri.parse("content://com.example.lx.aidldemo.ui.provider/book");
            ContentValues values = new ContentValues();
            values.put("_id", 6);
            values.put("name", "程序设计的艺术");
            getContentResolver().insert(bookUri, values);
            Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
            while (bookCursor.moveToNext()) {
                Book book = new Book();
                book.bookId = bookCursor.getInt(0);
                book.bookName = bookCursor.getString(1);
                Log.d(BookProvider.TAG, "query book:" + book.toString());
            }
            bookCursor.close();

            Uri userUri = Uri.parse("content://com.example.lx.aidldemo.ui.provider/user");
            Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
            while (userCursor.moveToNext()) {
                User user = new User();
                user.userId = userCursor.getInt(0);
                user.userName = userCursor.getString(1);
                user.isMale = userCursor.getInt(2) == 1;
                Log.d(BookProvider.TAG, "query user:" + user.toString());
            }
            userCursor.close();

        }
    }
}
