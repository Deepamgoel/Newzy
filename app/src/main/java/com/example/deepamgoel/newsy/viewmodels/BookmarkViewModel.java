package com.example.deepamgoel.newsy.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.deepamgoel.newsy.data.Bookmark;
import com.example.deepamgoel.newsy.data.BookmarkRepository;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {
    private BookmarkRepository mRepository;

    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookmarkRepository(application);
    }

    public LiveData<List<Bookmark>> getAllBookmarks() {
        return mRepository.getAllBookmark();
    }

    public void insert(Bookmark bookmark) {
        mRepository.insert(bookmark);
    }
}
