package com.huatec.edu.mobileshop.db;

import android.content.Context;

import com.huatec.edu.mobileshop.entity.Category;
import com.huatec.edu.mobileshop.gen.CategoryDao;

/**
 * Created by zhusheng on 2017/7/4.
 */

public class DbUtils {
    private Context context;
    private CategoryDao categoryDao;

    public DbUtils(Context context){
        this.context = context;
        categoryDao =
                GreenDaoManager.getInstance().getSession().getCategoryDao();

    }

    public void add(){
        Category category;
        for(int i = 0 ; i <10 ; i ++){
            category = new Category();
            category.setName("增加商品" + i);
            category.setGoods_count(i + 1);
            categoryDao.insert(category);
        }
    }

}
