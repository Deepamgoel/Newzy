package com.example.deepamgoel.newsy.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.model.Resource;
import com.example.deepamgoel.newsy.util.AppExecutor;
import com.example.deepamgoel.newsy.util.QueryUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.deepamgoel.newsy.NewsyApplication.getAppContext;

public abstract class NetworkBoundResources<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private AppExecutor appExecutors;

    @MainThread
    NetworkBoundResources(AppExecutor appExecutor) {

        this.appExecutors = appExecutor;

        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                if (QueryUtils.isConnected(getAppContext()))
                    fetchFromNetwork(dbSource);
                else {
                    onFetchFailed();
                    result.addSource(dbSource, newData ->
                            result.setValue(Resource.error(getAppContext().getString(R.string.msg_no_internet), newData)));
                }
            } else {
                result.addSource(dbSource, newData -> setValue(Resource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createCall().enqueue(new Callback<RequestType>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                result.removeSource(dbSource);
                if (response.isSuccessful()) {
                    RequestType body = response.body();
                    if (body == null || response.code() == 204) {
                        appExecutors.getMainThread().execute(() ->
                                result.addSource(loadFromDb(), newData ->
                                        result.setValue(Resource.empty(newData))));
                    } else {
                        appExecutors.getDiskIO().execute(() -> {
                            saveCallResults(body);
                            appExecutors.getMainThread().execute(() ->
                                    result.addSource(loadFromDb(), newData ->
                                            result.setValue(Resource.success(newData))));
                        });
                    }
                } else {
                    onFetchFailed();
                    result.addSource(dbSource, newData ->
                            result.setValue(Resource.error(String.valueOf(response.errorBody()), newData)));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RequestType> call, Throwable t) {
                // TODO: 02-05-2019 error
                onFetchFailed();
                result.addSource(dbSource, newData ->
                        result.setValue(Resource.error(t.toString(), newData)));
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue)
            result.setValue(newValue);
    }

    protected void onFetchFailed() {
    }

    final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }

    @WorkerThread
    protected abstract void saveCallResults(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Call<RequestType> createCall();
}
