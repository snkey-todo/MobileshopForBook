$(document).ready(function(){
	//model构建
	var pageId =1;

	
	var Goods= Backbone.Model.extend({
		 url:basePath+"/goods/enable/page/pageId",
         parse : function(response){
          return response.data;
        }
	});
	
	var GoodsList = Backbone.Collection.extend({
		model :Goods
	});
	
	//实例化
	var goodsList = new GoodsList();
	
	//构建View，与模板挂钩
	var GoodsView = Backbone.View.extend({
		
		
	});
	
	//实例化View
	
	
	
	
	
	
	
	
});