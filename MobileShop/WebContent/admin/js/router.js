 var AppRouter = Backbone.Router.extend({
      initialize: function(){
    	  $('#main').show();
    	  $('#main').siblings().hide();
      },
      routes:{
    	   'main':'showMain',
    	   'publishGoods':'showPublishGoods',
           'online':'showOnline',
           'stockGoods':'showStockGoods'
      } ,
      showPublishGoods: function (){
    	  $('#publishGoods').show();
    	  $('#publishGoods').siblings().hide();
    	  //显示选择分类页面，其他页面隐藏
 	  $('#publishGoods .selectCate').show();
  	  $('#publishGoods .selectCate').siblings().hide();
    	  //显示商品基本信息页面，其他页面隐藏
    	 // $('#publishGoods .baseInfo').show();
    	//  $('#publishGoods .baseInfo').siblings().hide();
    	  //暂时显示添加图片页面
    	  /* $('#publishGoods .addImg').show();
     	  $('#publishGoods .addImg').siblings().hide();*/
      },
      showOnline: function (){
    	  $('#online').show();
    	  $('#online').siblings().hide();
      },
      showStockGoods: function (){
    	  $('#stockGoods').show();
    	  $('#stockGoods').siblings().hide();
      },

      
 })
 
   var app = new AppRouter();//实例化一个AppRouter
   Backbone.history.start(); //通过history对象的start方法启动对URL地址变化的监听