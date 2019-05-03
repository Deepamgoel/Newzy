package com.example.deepamgoel.newsy.repository;

import androidx.lifecycle.LiveData;

import com.example.deepamgoel.newsy.data.db.BookmarksDao;
import com.example.deepamgoel.newsy.model.Article;
import com.example.deepamgoel.newsy.util.AppExecutor;

import java.util.List;

public class BookmarksRepository {
    private static BookmarksRepository repository;

    private final BookmarksDao bookmarksDao;
    private final AppExecutor executor;


    private BookmarksRepository(BookmarksDao bookmarksDao, AppExecutor executor) {
        this.bookmarksDao = bookmarksDao;
        this.executor = executor;
    }

    public synchronized static BookmarksRepository getInstance(BookmarksDao bookmarksDao,
                                                               AppExecutor executor) {
        if (repository == null) {
            repository = new BookmarksRepository(bookmarksDao, executor);
        }
        return repository;
    }

    public LiveData<List<Article>> getAllBookmarks() {
        return bookmarksDao.getAllBookmarks();
    }

    public void addBookmark(Article article) {
        executor.getDiskIO().execute(() -> bookmarksDao.addBookmark(article));
    }

    public void deleteBookmark(Article article) {
        executor.getDiskIO().execute(() -> bookmarksDao.deleteBookmark(article));
    }

}
