package com.library;

import com.library.services.BookServices;
import com.library.services.MemberServices;
import com.library.services.TransactionServices;
import com.library.services.WorkerServices;
import com.library.ui.LibraryMenu;


public class Main {
    public static void main(String[] args) {
        WorkerServices ws = new WorkerServices();
        BookServices bs = new BookServices();
        MemberServices ms = new MemberServices();
        TransactionServices ts = new TransactionServices(bs, ms);
    
        LibraryMenu menu = new LibraryMenu(bs, ms, ws, ts);
        menu.start();
    }
}