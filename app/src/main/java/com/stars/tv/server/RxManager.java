package com.stars.tv.server;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */
public class RxManager {

    private RxBus mRxBus = RxBus.getInstance();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 观察者管理
    private static CompositeDisposable composite = new CompositeDisposable(); //订阅者管理


    public void on(String eventName, Consumer<Object> consumer) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        composite.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, (e) -> e.printStackTrace()));

    }

    public static void add(Disposable m) {
        composite.add(m);
    }

    public void clear() {
        composite.clear();// 取消订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

}
