


 






## 框架总览 ##
[![](https://jitpack.io/v/MJLblabla/hapiReposity.svg)](https://jitpack.io/#MJLblabla/hapiReposity)

![此处输入图片的描述][1]

使用注解生成respotity层
手动控制缓存更加灵活，如先返回缓存数据再返回最新数据（回调两次），手动控制内存缓存等


## 网络接口 ##



    interface TestService {
        @AutoApi　　／／返回协程的接口
        suspend fun a(a: Int): List<List<UserInfo>>

        @SpCache　　  ／／返回协程的　使用sp缓存的接口 参数查看相应注解
         @GET("image/sogou/api.php")
        suspend fun b(a: Int): Int


        @SpCache(fetchStrategy = NetworkFetchStrategy.Both)　／／ ／／返回协程的　使用sp缓存的接口 参数查看相应注解
        @GET("image/sogou/api.php")
        suspend fun getImageWithFlow(@Query("type") type: String = "json"): ImageDataResponseBody

        @AutoApiWithCache(providerKey = "aaa")　／／／／返回协程的　使用自定义缓存的接口　　自定义缓存的场景　如使用数据库　／手动控制内存缓存
           @GET("image/sogou/api.php")
        suspend fun c(a: Int): List<UserInfo>

       @GET("image/sogou/api.php")　　　　／／／／返回rxjava 的接口　
        @AutoApiWithCache(providerKey = "aaa")
        fun d(a: Int): Observable<List<UserInfo>>


    }

　　kapt 自动生成　数据源层

    　　/**
     * This file is generated by kapt, please do not edit this file */
    open class TestReposity : BaseReposity<TestService>() {
        suspend fun a(a: Int): List<List<UserInfo>> = apiService.a(a)

        fun c(a: Int): Flow<List<UserInfo>> {
             return FlowDataFetcher(TestCahcheProvider(),NetworkFetchStrategy.OnlyRemote){
                     apiService.c(a)
                    }.startFetchData()
        }

        fun d(a: Int): Observable<List<UserInfo>> {
             return RxDataFetcher(TestCahcheProvider(),NetworkFetchStrategy.OnlyRemote){
                     apiService.d(a)
                    }.startFetchData()
        }

        fun b(a: Int): Flow<Int> {
             return FlowDataFetcher(MMKVCacheProvide("b${a}",10800000,Int::class.java),NetworkFetchStrategy.CacheFirst){
                     apiService.b(a)
                    }.startFetchData()
        }

        fun getImageWithFlow(type: String): Flow<ImageDataResponseBody> {
             return FlowDataFetcher(MMKVCacheProvide("getImageWithFlow${type}",10800000,ImageDataResponseBody::class.java),NetworkFetchStrategy.Both){
                     apiService.getImageWithFlow(type)
                    }.startFetchData()
        }
    }

## 网络请求策略 ##

    　　public enum NetworkFetchStrategy {
        /**
         * 只请求缓存
         */
        OnlyCache("OnlyCache"),
        /**
         * 只请求网络
         */
        OnlyRemote("OnlyRemote"),
        /**
         * 优先缓存，缓存拿到就展示缓存，缓存没拿到就请求网络
         */
        CacheFirst("CacheFirst"),
        /**
         * 缓存和网络同时请求，数据会返回2次（如果都成功的话）
         */
        Both("Both");

        public String strategy = "OnlyRemote";

        NetworkFetchStrategy(String i) {
            strategy = i;
        }

    }
## 自定义缓存 ##

    @Provider(providerKey = "aaa")
    class TestCahcheProvider (key:String) : LocalCacheProvide<List<User>>(key){
        /**
         * 获得缓存数据
         */
        override fun loadFromLocal(): List<UserInfo>? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        /**
         * 存储方案
         */
        override fun saveToLocal(data: List<UserInfo>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


## 接入 ##
　  AppCache.setContext(this) ／／hapi工具类初始化　hapi系列基础库
　  ReposityManager.get().setRetrofitClict(retrofit) ／／注入项目初始化好的retrofit
　


  [1]: https://github.com/MJLblabla/hapiVm/blob/latest_branch/img/ic.png