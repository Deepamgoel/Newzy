package com.example.deepamgoel.newsy.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkRepository {
    private BookmarkDao mBookmarkDao;

    public BookmarkRepository(Application application) {
        BookmarkDatabase database = BookmarkDatabase.getDatabase(application);
        mBookmarkDao = database.bookmarkDao();
    }

    public LiveData<List<Bookmark>> getAllBookmark() {
        return mBookmarkDao.getAllBookmarks();
    }

    public void insert(Bookmark bookmark) {
        new insertAsyncTask(mBookmarkDao).execute(bookmark);
    }

    private static class insertAsyncTask extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao mAsyncTaskDao;

        private insertAsyncTask(BookmarkDao Dao) {
            mAsyncTaskDao = Dao;
        }

        @Override
        protected Void doInBackground(final Bookmark... bookmarks) {
            mAsyncTaskDao.insert(bookmarks[0]);
            return null;
        }
    }
}
