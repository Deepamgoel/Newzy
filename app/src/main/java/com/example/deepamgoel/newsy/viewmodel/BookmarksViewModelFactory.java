package com.example.deepamgoel.newsy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.deepamgoel.newsy.data.db.AppDatabase;
import com.example.deepamgoel.newsy.data.db.BookmarksDao;
import com.example.deepamgoel.newsy.repository.BookmarksRepository;
import com.example.deepamgoel.newsy.util.AppExecutor;

public class BookmarksViewModelFactory implements ViewModelProvider.Factory {
    private BookmarksRepository mRepository;

    public BookmarksViewModelFactory(Application application) {
        BookmarksDao bookmarksDao = AppDatabase.getDatabase(application).bookmarksDao();
        AppExecutor executor = new AppExecutor();

        mRepository = BookmarksRepository.getInstance(bookmarksDao, executor);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BookmarksViewModel(mRepository);
    }
}
