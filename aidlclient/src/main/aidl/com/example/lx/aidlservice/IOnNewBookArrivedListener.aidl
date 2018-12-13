// IOnNewBookArrivedListener.aidl
package com.example.lx.aidlservice;

// Declare any non-default types here with import statements
import com.example.lx.aidlservice.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
   }
