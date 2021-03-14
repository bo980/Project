package com.school.project.database;

import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.school.project.base.BaseDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Access point for managing data.
 */
public abstract class DataSourceModel<T, DAO extends BaseDao<T>> extends AndroidViewModel {

    protected final DAO dao;
    protected final LiveData<PagedList<T>> allDataObserve;
    private final DataSource<Integer, T> dataSource;

    public DataSourceModel(@NonNull Application application) {
        super(application);
        this.dao = setDao();
        PagedList.Config.Builder config = new PagedList.Config.Builder()
                .setPageSize(setPageSize())
                .setPrefetchDistance(setPrefetchDistance())
                .setInitialLoadSizeHint(setInitialLoadSizeHint())
                .setEnablePlaceholders(setEnablePlaceholders());
        dataSource = bindAllData().create();
        this.allDataObserve = new LivePagedListBuilder<>(
                bindAllData(), config.build())
                .setBoundaryCallback(dataBoundaryCallback)
                .build();
    }

    protected abstract DAO setDao();

    public final DataSource.Factory<Integer, T> bindAllData() {
        return dao.queryAll();
    }

    /**
     * 每页加载多少数据，必须大于0，这里默认20
     *
     * @return 每页加载数据数量
     */
    protected int setPageSize() {
        return 20;
    }

    /**
     * 距底部还有几条数据时，加载下一页数据，默认为PageSize
     *
     * @return 距底部数据的数量
     */
    protected int setPrefetchDistance() {
        return -1;
    }

    /**
     * 第一次加载多少数据，必须是PageSize的倍数，默认为PageSize*3
     *
     * @return 第一次加载数据的数量 eg：PageSize*n
     */
    protected int setInitialLoadSizeHint() {
        return -1;
    }

    /**
     * 是否启用占位符，若为true，则视为固定数量的item
     *
     * @return 默认为true
     */
    protected boolean setEnablePlaceholders() {
        return true;
    }

    /**
     * 数据库没有查询到数据，可以通过触发网络等其他渠道加载数据到数据库
     */
    protected void onZeroItemsLoaded() {
    }

    /**
     * 忽略，因为我们只附加到数据库中的内容
     */
    protected void onItemAtFrontLoaded(T itemAtFront) {
    }

    /**
     * 数据已到达列表末尾，可以通过触发网络加载更多数据到数据库
     */
    protected void onItemAtEndLoaded(T itemAtEnd) {
    }

    public final DAO getDao() {
        return dao;
    }

    public LiveData<PagedList<T>> getDataObserve() {
        return allDataObserve;
    }

    public final Disposable insert(@NonNull final T data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().insert(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public final Disposable insert(@NonNull final List<T> data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().insert(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public final Disposable update(@NonNull final T data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().update(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public final Disposable update(@NonNull final List<T> data) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().update(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public final void delete(@NonNull final T data) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().delete(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void delete(final List<T> data) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                getDao().delete(data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public static <T extends DataSourceModel> T getViewModel(@NonNull FragmentActivity activity, Class<T> clazz) {
        return ViewModelProviders.of(activity).get(clazz);
    }

    public static <T extends DataSourceModel> T getViewModel(@NonNull Fragment fragment, Class<T> clazz) {
        return ViewModelProviders.of(fragment).get(clazz);
    }

    final protected PagedList.BoundaryCallback<T> dataBoundaryCallback = new PagedList.BoundaryCallback<T>() {

        @Override
        @MainThread
        public void onZeroItemsLoaded() {
            DataSourceModel.this.onZeroItemsLoaded();
        }

        @Override
        @MainThread
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            DataSourceModel.this.onItemAtFrontLoaded(itemAtFront);
        }

        @Override
        @MainThread
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            DataSourceModel.this.onItemAtEndLoaded(itemAtEnd);
        }
    };

    public void invalidate() {
        dataSource.invalidate();
    }

}
