// IBookManager.aidl
package com.example.lx.aidlservice;

// Declare any non-default types here with import statements
import  com.example.lx.aidlservice.Book;
import com.example.lx.aidlservice.IOnNewBookArrivedListener;


interface IBookManager {
     List<Book> getListBook();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}
