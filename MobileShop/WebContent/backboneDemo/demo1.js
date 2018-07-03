$(document).ready(function(){
	//创建模型类
	var  GoodsItem = Backbone.Model.extend({
		defaults: {
			sn:"",
			name:"",
			price:""
		}
	});
	
	//创建集合类
	var  GoodsList = Backbone.Collection.extend({
		model: GoodsItem
	});
	
    var data =[
                {"sn":"sp001","name":"汇源果汁","price":"4.5"},
				{"sn":"sp002","name":"圣诞袜子","price":"8"},
				{"sn":"sp003","name":"圣诞苹果","price":"20"}
				];
    
    //实例化集合，并将数据传递给它
    var goodsList = new GoodsList(data);
    
   //创建视图类
   var GoodsListView = Backbone.View.extend({
	   el: $("body"),
       initialize: function () {
        this.render();
       },
       render: function(){
    	   for(var i=0;i < goodsList.models.length;i++){
         	  
          	 $("#goodsInfo").append("<tr class='item'> <td >"+goodsList.models[i].get("sn")+"</td> <td>"+goodsList.models[i].get("name")+"</td><td>"+goodsList.models[i].get("price")+"</td></tr>");
            }
       }
   });
   //实例化视图
   var goodListView = new GoodsListView;
	
	
	
});