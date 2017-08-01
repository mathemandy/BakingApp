package com.ng.tselebro.bakingapp.util;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;


public class VideoCacheProxyFactory
{

    private static HttpProxyCacheServer sharedProxy;

    private VideoCacheProxyFactory() {
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        return sharedProxy == null ? (sharedProxy = newProxy(context)) : sharedProxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer(context);
    }
}
