package com.huatec.edu.mobileshop.http.presenter;

import com.huatec.edu.mobileshop.entity.FavoriteGoodsEntity;
import com.huatec.edu.mobileshop.entity.GoodsDetailEntity;
import com.huatec.edu.mobileshop.entity.GoodsEntity;
import com.huatec.edu.mobileshop.entity.HttpResult;
import com.huatec.edu.mobileshop.http.HttpMethods;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/9.
 */

public class GoodsPresenter extends HttpMethods {

    /**
     * 根据二级分类id获取商品列表
     *
     * @param subscriber
     * @param catId
     */
    public static void list(Subscriber<List<GoodsEntity>> subscriber, int catId) {
        Observable<List<GoodsEntity>> observable = goodsService.list(catId)
                .map(new HttpResultFunc<List<GoodsEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 根据商品搜索关键词获取商品列表
     *
     * @param subscriber
     * @param keywords
     */
    public static void listByKeywords(Subscriber<List<GoodsEntity>> subscriber, String keywords) {
        Observable<List<GoodsEntity>> observable = goodsService.listByKeywords(keywords)
                .map(new HttpResultFunc<List<GoodsEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取商品详情
     *
     * @param subscriber
     * @param goodsId
     */
    public static void goodsDetail(Subscriber<GoodsDetailEntity> subscriber, int goodsId) {
        Observable<GoodsDetailEntity> observable = goodsService.goodsDetail(goodsId)
                .map(new HttpResultFunc<GoodsDetailEntity>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 添加到收藏列表
     *
     * @param subscriber
     * @param memberId
     * @param goodsId
     */
    public static void addToFavorite(Subscriber<HttpResult> subscriber, int memberId, int goodsId) {
        Observable<HttpResult> observable = goodsService.addToFavorite(memberId, goodsId);
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取商品收藏列表
     *
     * @param subscriber
     * @param memberId
     */
    public static void getFavoriteList(Subscriber<List<FavoriteGoodsEntity>> subscriber, int memberId) {
        Observable<List<FavoriteGoodsEntity>> observable = goodsService.favoriteList(memberId)
                .map(new HttpResultFunc<List<FavoriteGoodsEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除收藏的商品
     *
     * @param subscriber
     * @param likeId
     */
    public static void deleteFavoriteGoods(Subscriber<HttpResult> subscriber, int likeId) {
        Observable<HttpResult> observable = goodsService.deleteFavoriteGoods(likeId);
        toSubscribe(observable, subscriber);
    }

}
