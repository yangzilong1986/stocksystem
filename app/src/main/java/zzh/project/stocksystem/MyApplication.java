package zzh.project.stocksystem;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MyApplication extends Application {
    public static final boolean DEBUG = true;
    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        initNetWorkStatusObservable();
        initImageLoader(this);
    }

    private NetWorkObservable mNetWorkObservable;

    /********************* 初始化网络监听 ***************/
    private void initNetWorkStatusObservable() {
        mNetWorkObservable = new NetWorkObservable(this);
    }

    public boolean isNetworkActive() {
        if (mNetWorkObservable != null) {
            return mNetWorkObservable.isNetworkActive();
        }
        return true;
    }

    public void registerNetWorkObserver(NetWorkObservable.NetWorkObserver observer) {
        if (mNetWorkObservable != null) {
            mNetWorkObservable.registerObserver(observer);
        }
    }

    public void unregisterNetWorkObserver(NetWorkObservable.NetWorkObserver observer) {
        if (mNetWorkObservable != null) {
            mNetWorkObservable.unregisterObserver(observer);
        }
    }

    /******************* 初始化图片加载 **********************/
    private void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), context.getPackageName()+"/img_cache");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (Runtime.getRuntime().maxMemory() / 5)) //设置内存缓存的大小
                .discCache(new TotalSizeLimitedDiscCache(cacheDir, 50 * 1024 * 1024)) // 最多缓存50M的图片
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(memoryCache).tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(4)
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
