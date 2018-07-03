$(document).ready(function(){
	
	
	
	$("input.btnUploadImg").on("click",function(e){
		var that = $(this);  //因为this对象放在ajax的success中是无效的
		//上传图片
		var formData = new FormData(that.parents(".uploadForm" )[0]);   
		
	     $.ajax({   
	          url:'http://localhost:8080/MobileShop/file/'+"upload",
	          type: 'POST',   
	          data: formData,
	          async: false,   
	          cache: false,   
	          contentType: false,
	          processData: false,   
	          success: function (returndata) {   
	             // alert(returndata.data);
	              var fileUrl=returndata.data;
	              //存入隐藏
	             console.log($(this));
	             that.siblings(".fileUrl").attr("value",fileUrl);
	              
	          },   
	          error: function (returndata) {   
	              alert(returndata);   
	          }   
	     });  
	     
        var fileUrl = $(this).siblings(".fileUrl").val().trim(); // 选中当前这个隐藏域
	     var goodsId =$(this).attr("data-goodsId");
	     var isdefault =  $(this).siblings(".isdefault").val().trim();
	     $.ajax({
				url:"http://localhost:8080/MobileShop/img/"+"add",
				type:"post",
				dataType:"json",
				data:{"goodsId":goodsId,"isdefault":isdefault,"img":fileUrl},
				success:function(result){
					//alert(result.msg);
					//将图片替换之前的图片
					var img = that.siblings(".upload-img").children(".imgShow");
					var fileUrl1=result.data.small;
					img.attr("src",fileUrl1);
					
					var imgId=result.data.img_id;
					that.siblings(".btnDeleteImg").attr("data-imgId",imgId);
				},
				error:function(){
					alert("新增商品图片异常");
				}
			});
		
	});
	
	//删除商品图片
	$("input.btnDeleteImg").on("click",function(e){
		var goodsId =$(this).attr("data-imgId");
		var that = $(this);
		$.ajax({
			url:"http://localhost:8080/MobileShop/img/"+goodsId,
			type:"DELETE",
			dataType:"json",
			data:{"goodsId":goodsId},
			success:function(result){
				alert(result.msg);
				//移除图片,替换成灰色的图片
				var img = that.siblings(".upload-img").children(".imgShow");
				img.attr("src","img/default_goods_image_tiny.gif");
			},
			error:function(){
				alert("新增商品图片异常");
			}
		});
	});
	
   $("a#stepThree").on('click',function(){
			//跳转到库存设置页面
		   $("div.storeNumber").siblings().hide();
		   $("div.storeNumber").show();
			
	   });
	
});