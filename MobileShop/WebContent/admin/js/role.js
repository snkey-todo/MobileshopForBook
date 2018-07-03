$(document).ready(function(){


   $("#roleLink").on('click',function(e){
        e.preventDefault();
        //ajax加载role.html页面
        var url = "http://localhost:8080/MobileShop/admin"+$(this).attr("href");
        $(".mainCon").empty();
        $(".mainCon").load(url);
           
        //重构角色列表页面上的内容
        var getRoleUrl = "http://localhost:8080/MobileShop/role"
        $.get(getRoleUrl).
        done(function(data){
            if(data.status==0){
                  for(var i=0;i<data.data.length;i++){
                      var    role = data.data[i].name;
                      var    description = data.data[i].description;
                      var roleId = data.data[i].role_id;//提取角色id，并绑定到编辑和删除键
                    var codeStr =  "<tr class='item'> <td class='roleName'>"+role+"</td> <td class='roleDescription'>"+description+"</td><td><a href='#' class='edit' data-roleId='"+roleId+" 'data-name='"+role+"' data-description='"+description+"'>修改</a> <a href='#' class='delete' data-roleId='"+roleId+"'>删除</a></td></tr>"
                //     var codeStr = "<tr class='item'> <td>"+role+"</td> <td>"+description+"</td><td><a href='#' class='edit' data-roleId='"+roleId+"'>修改</a> <a href='#' class='delete' data-roleId='"+roleId+"'>删除</a></td></tr>"
                    $(".roleList tbody").append(codeStr);     
                  }    
                 
                  //要放置在这里，因为事件绑定，必须页面上有这个元素才能绑定
                  //添加角色
                  $("#addRoleBtn").on("click",function(e){
                           e.preventDefault();
                           e.stopPropagation();
                             $(".blackCover").show();
                              //显示编辑div
                             $(".addRole").show();
                             
               });
                 
                 //进入添加角色面板之后，点击添加角色按钮
                  $("#btn2").on("click",function(e){
              e.preventDefault(); 
              e.stopPropagation();
                         var name = $("#name").val().trim();
                         var description =  $("#description").val().trim();
                        
                         var url = "http://localhost:8080/MobileShop/role";
                       $.ajax({
                           type:"post",
                           data:{name:name,description:description},
                           url:url,
                           timeout:2000,
                           success:function(data){
                                if(data.status==0){
                                     alert(data.msg);
                         //添加到面板中去
                                     var codeStr =  "<tr class='item'> <td class='roleName'>"+data.data.name+"</td> <td>"+data.data.description+"</td><td><a href='#' class='edit' data-roleId='"+data.data.role_id+"' data-name='"+data.data.name+"' data-description='"+data.data.description+"'>修改</a> <a href='#' class='delete' data-roleId='"+data.data.role_id+"'>删除</a></td></tr>"
                                     $(".roleList tbody").append(codeStr);
                                      //编辑角色,再写一次，
                                           $(".roleList tbody .edit").on("click",editRole);
                                          
                                      //删除角色,再写一次，因为这里刚新增加的删除按钮
                              $(".roleList tbody .delete").on("click",deleteRole);
                                    
                               }else{
                                    alert(data.msg);
                               }
                           },
                           fail:function(){
                              alert("服务器忙，请稍后再试");
                           }
                         });               
               });
          //关闭角色面板
          $(".closeRoleBtn").on("click",function(e){
                $(".blackCover").fadeOut(1000);
                $(".addRole").fadeOut(1000 );
          });
                 
                 
                  //删除角色
          $(".roleList tbody .delete").on("click",deleteRole);
          function deleteRole(e){
           var roleId = $(this).attr("data-roleId");
                         var url =  "http://localhost:8080/MobileShop"+"/role/"+roleId;//构建url
                         $.ajax({
                              type: 'DELETE',
                     url:url,
                     success:function(data){
                      alert(data.msg);
                      //移除
                      $(".roleList  tbody .item").each(function(){
                            if( $(this).find(".delete").attr("data-roleId") == roleId )           
                                $(this).remove();
                             });
                              /* $(this).closest("tr").remove();*////从页面中移除
                     }
                 });
          }
         
                 
                  //编辑角色
                 $(".roleList tbody .edit").on("click",editRole);
                 function editRole(){
                            //显示黑色遮罩层
                             $(".blackCover").fadeIn(1000);
                              //显示编辑div
                             $(".editRole").fadeIn(1000);
                             //细节处理2，将现有的角色name和description填入面板中的输入框。
                             //方法1：在最初时就将两个数据绑定在这个按钮上
                          var name = $(this).attr("data-name");
                          var description = $(this).attr("data-description");
                          //加到input上去
                         $("#newName").val(name);
                         $("#newDescription").val(description);
                          var roleId =  $(this).attr("data-roleId");
                        
                           $("#editRoleBtn").on("click",function(e){
                              
                               var name = $("#newName").val().trim();
                               var description = $("#newDescription").val().trim();
                              
                               var url = "http://localhost:8080/MobileShop"+"/role/"+roleId;
                               $.ajax({
                                      type:"PUT",
                                      data:{roleId:roleId,name:name,description:description},
                                      url:url,
                                      timeout:2000,
                                      success:function(data){
                                           if(data.stuts == 0){
                                                 alert(data.msg);
                                                 //细节处理1，编辑角色之后要实时更新表格中的角色信息
                                                 
                                           }else{
                                                 alert(data.msg);
                                            }
                                        },
                                        fail:function(){
                                                 alert("服务器忙，请稍后再试");
                                              }
                               });
                              
                           });
                             
                   }
        
                 //关闭编辑角色面板
         $(".closeEditRoleBtn").on("click",function(e){
               $(".blackCover").fadeOut(1000);
               $(".editRole").fadeOut(1000 );
               $("#roleLink").trigger("click");//刷新一次
         });
                
           
                }else{
                       alert(data.msg);
                }
        })
        .fail(function(){
             alert("服务器忙，请稍后再试");
        });
 
      
      });
})
