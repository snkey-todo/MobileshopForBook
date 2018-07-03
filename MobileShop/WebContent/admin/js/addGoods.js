
$(document).ready(function(){

//商品分类
var CategoryItem =Backbone.Model.extend();

var CategoryList = Backbone.Collection.extend({
	model: CategoryItem,
	//url:url,  //可以在定义集合类时传入url
    parse : function(response){
        return response.data;
      }
});

//实例化Collection  -顶级分类
var  categoryList = new CategoryList();

//显示顶级商品分类的url
var path = "http://localhost:8080/MobileShop";
var url = path+"/cat/show";  
//调用GET方式从服务器提取数据
categoryList.fetch({
    reset:true,
    url:url,
    success: function(collection, response) {
    	 //console.log(collection.models[0]);
     }
});





var CategoryItemView = Backbone.View.extend({
	tagName : 'li',       //设置生成的DOM元素，这里为表格中的一行
    className : 'catLine',   //设置class属性
    itemTemplate : _.template($("#categoryTemplate").html()),//设置模板
    
    initialize:function(){
        this.render();
        this.listenTo(this.model,'all',this.render);
    },
	 render : function(){
		    /*console.log("进入单个视图");
		    console.log(this.model.toJSON());*/
      		this.$el.html(this.itemTemplate(this.model.toJSON()));
      		this.$el.attr("data-catId",this.model.get("cat_id"));
      		return this;  
      	}
});

//创建一个显示Collection对象的视图类  
var CategoryListView = Backbone.View.extend({ 
	    //事件绑定
	    events: {
		      "click li.catLine": "showByParentId", //当单击一级分类时，将显示它的二级分类
		    },
      	initialize: function() {
      		this.listenTo(this.collection,'all',this.render);//GoodsListView视图监听collection对象onLineGoodsList的all事件，render进行渲染
      	},
      	render :function(){
      		this.$el.empty();//清空
      		for(var i=0;i < this.collection.models.length;i++){
      			var view = new CategoryItemView ({model :this.collection.models[i] });  
      			this.$el.append(view.$el);
      		}
      	},
      	showByParentId:function(e){
      		//单击顶级分类导航时，出现对应的二级导航
      	   //实例化Collection  -顶级分类
           	
      		$(e.currentTarget).addClass("selected");
      		$(e.currentTarget).siblings().removeClass("selected");
      		
      		if($(e.currentTarget).parent().attr("id") == "cate-parent"){  // 判断是否单击的第一分类
      			 var parentId = $(e.currentTarget).attr("data-catId");//获取catid
      			 var  categoryList1 = new CategoryList();
         		 //console.log($(e.currentTarget).attr("data-catId"));  
         		//显示二级商品分类的url   /cat/show/{parentId}
           		
           		var path = "http://localhost:8080/MobileShop";
           		var url = path+"/cat/show/"+parentId;  
           		//调用GET方式从服务器提取数据
           		categoryList1.fetch({
           		    reset:true,
           		    url:url,
           		    success: function(collection, response) {
           		    	// console.log(collection.models);
           		     }
           		});
           		var categoryListView1 = new CategoryListView({
           			collection:categoryList1,
           			el : '#cate-child'
           			});
           		categoryListView1.render();
           		
            //当单击一级分类时，获取到它的分类name值
           	 var parentCatName= $(e.currentTarget).html();
           	// alert(parentCatName);
           	$("ol.category-path").empty();
         	$("ol.category-path").append("<li class='parent'   data-catId="+parentId+">"+parentCatName+"</li>");
      		
         	
      		}else{
      			 var childId = $(e.currentTarget).attr("data-catid");//获取catId
      			 var childCatName= $(e.currentTarget).html();//获取catName
      			//删除第3个li一直到后面
      			$("ol.category-path li").each(function(){
      				if(!$(this).hasClass("parent")){
      					$(this).remove();//删除
      				}
      			});
               $("ol.category-path").append("<li>&nbsp;>&nbsp;</li>");
               $("ol.category-path").append("<li class='child' data-catId="+childId+">"+childCatName+"</li>");
               
      		}
      		
      		
      		
      	}
      	
      });

//实例化View，页面显示出数据
var categoryListView = new CategoryListView({
	collection:categoryList,
	el : '#cate-parent'
	});
categoryListView.render();




$("#stepOne").on("click",function(){
	//跳转到基本信息页面
	$("div.baseInfo").siblings().hide();
	$("div.baseInfo").show();
	
});



//商品品牌
var BrandItem =Backbone.Model.extend();

var BrandList = Backbone.Collection.extend({
	model: BrandItem,
	//url:url,  //可以在定义集合类时传入url
    parse : function(response){
        return response.data;
      }
});

//实例化Collection  -顶级分类
var  brandList = new BrandList();

//url
var path = "http://localhost:8080/MobileShop";
var url = path+"/brand/disabled";  

//调用GET方式从服务器提取数据
brandList.fetch({
    reset:true,
    url:url,
    success: function(collection, response) {
     /* console.log(collection.models[0]);*/
     }
});



var BrandItemView  = Backbone.View.extend({
	tagName : 'option',       //设置生成的DOM元素，这里为表格中的一行
    className : 'brandItem',   //设置class属性
    itemTemplate : _.template($("#brandTemplate").html()),//设置模板
    
    initialize:function(){
        this.render();
        this.listenTo(this.model,'all',this.render);
    },
	 render : function(){
		    /*console.log("进入单个视图");
		    console.log(this.model.toJSON());*/
      		this.$el.html(this.itemTemplate(this.model.toJSON()));
      		this.$el.attr("data-brandName",this.model.get("name"));//brand_id
      		this.$el.attr("value",this.model.get("brand_id"));
      		return this;  
      	}
});



//通过view，显示在“select.brand”中去
var BrandListView = Backbone.View.extend({ 
    //事件绑定
    events: {
	      "click li.brandItem": "selectBrand", 
	    },
  	initialize: function() {
  		this.listenTo(this.collection,'all',this.render);//GoodsListView视图监听collection对象onLineGoodsList的all事件，render进行渲染
  	},
  	render :function(){
  		this.$el.empty();//清空
  		for(var i=0;i < this.collection.models.length;i++){
  			var view = new BrandItemView ({model :this.collection.models[i] });  
  			this.$el.append(view.$el);
  		}
  	},
  	selectBrand:function(e){}
});
//实例化View，页面显示出数据
var brandListView = new BrandListView({
	collection:brandList,
	el : 'select.brand'
	});
brandListView .render();


   //绑定事件到提交按钮上
   $("a#addGoodsBtn").on('click',function(){//a#editGoodsBtn这个按钮，不在这个 View的this.$el范围内，所以不能用上面的events来绑定
  	 //提取数据
    var name = $("#goodsName_add").val().trim();
	var sn = $("#sn_add").val().trim();
	var brief = $("#brief_add").val().trim();
	var description = $("#description_add").val().trim();
	var price = $("#price_add").val().trim();
	var cost= $("#cost_add").val().trim();
	var mktprice= $("#mktprice_add").val().trim();
	
    var weight= $("#weight_add").val().trim();
    var intro=$("#intro_add").val().trim();//获取富文本
  
   var catId =  $(".category-path .child").attr("data-catId") ; //catId的值从  $(".category-path .child")中获取
     // 放置给
   var brandId =$("select.brand option:selected").val(); //brandId 的值从$("select.brand option:selected")选中option的val中
  //alert(brandId);
   
   //这里又一次定义GoodsItem ，可以新建一个公用js，把所有的model 、collection放进去
   var  GoodsItem = Backbone.Model.extend({
		 idAttribute:"goodsId" , //将goodsId设置为键值属性
		// defaults:{briefBrand:"",briefGoodsCat:"",goods_id:"",mkt_enable:"",mktprice:"",name:"",price:"",small:""},
		 parse : function(response){//重构返回的值，因为通过Collection的fetch，自动实例化的Model，其parse也会被调用，所以要进行判断，如果返回值有data就返回data里的内容，没有data，就返回原来的样子
				if(response.data != null){
					return response.data;
					}else{
						return response;
					}
		        
		      }
	  });
   
        
        var goods = new GoodsItem();
  	 //构建url
		var path = "http://localhost:8080/MobileShop";
		var urlRoot = path+"/backbone/goods";   //这个API用于获取到goodsId的全信息
		goods.urlRoot = urlRoot ;
		
	 //设置参数
      goods.set({name:name,sn:sn,brief:brief,description:description,price:price,cost:cost,mktprice:mktprice,catId:catId,brandId:brandId,weight:weight,intro:intro});
  	  //与服务器同步
  	  goods.save(null,{
  		  success:function(model,response){//当添加成功之后，出现下一步 
  			  //获取到id
  		/*console.log(model);
  		console.log(model.get("goods_id"));*/
  		if(model.get("goods_id")!=null){//添加成功
  	  		$("a#addGoodsBtn").hide();
  	  		$("a#stepTwo").show();//出现下一步按钮
  	  		
  	     	 $("input.btnUploadImg").attr("data-goodsId",model.get("goods_id"));//将goodsId绑定在图片上传按钮上，以备后用
  	  	     $("input.btnDeleteImg").attr("data-goodsId",model.get("goods_id"));//将goodsId绑定在图片上传按钮上，以备后用
  	  	     $("a#stepThree").attr("data-goodsId",model.get("goods_id"));//将其绑定 图片添加页面 上的“下一步”按钮上
  	  	     $("a#stepFour").attr("data-goodsId",model.get("goods_id"));//将其绑定 图片添加页面 上的“下一步”按钮上
  	  	     $("a#stepFour").attr("data-catId",model.get("cat_id"));//将其绑定 图片添加页面 上的“下一步”按钮上
  	  	   
  	  	     $("a#saveStore").attr("data-goodsId",model.get("goods_id"));//将其绑定 图片添加页面 上的“下一步”按钮上
  	  	     
  		}else{
  			alert(model.get("msg"));
  		}

  	  }
  	  });
   });
   
   
   
   //到添加图片这一页
   $("a#stepTwo").on('click',function(){
		//跳转到添加图片页面
		$("div.addImg").siblings().hide();
		$("div.addImg").show();
		
   });
   
	
});