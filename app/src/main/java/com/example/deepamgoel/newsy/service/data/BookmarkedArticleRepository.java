package com.example.deepamgoel.newsy.service.data;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.example.deepamgoel.newsy.models.Article;

import java.util.List;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;

public class BookmarkedArticleRepository {
    private static BookmarkedArticleRepository mRepository;
    private BookmarkedArticleDao mBookmarkedArticleDao;

    private BookmarkedArticleRepository() {
        AppDatabase database = AppDatabase.getDatabase(getAppContext());
        mBookmarkedArticleDao = database.mBookmarkedArticleDao();
    }

    public synchronized static BookmarkedArticleRepository getInstance() {
        if (mRepository == null) {
            mRepository = new BookmarkedArticleRepository();
        }
        return mRepository;
    }

    public LiveData<List<Article>> getAllBookmarks() {
        return mBookmarkedArticleDao.getAllBookmarks();
    }

    @WorkerThread
    public void addBookmark(Article article) {
        mBookmarkedArticleDao.addBookmark(article);
    }

}
