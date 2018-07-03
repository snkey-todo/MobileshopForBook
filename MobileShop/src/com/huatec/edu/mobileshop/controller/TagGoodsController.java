package com.huatec.edu.mobileshop.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huatec.edu.mobileshop.service.TagGoodsService;
import com.huatec.edu.mobileshop.util.Result;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/tag_goods")
public class TagGoodsController {
	@Resource
	private TagGoodsService tagGoodsService;
	
	//新增标签商品关联信息
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="新增标签商品关联信息")
	public Result add(@ApiParam(value="标签编号")@RequestParam("tagId") int tagId,
			@ApiParam(value="商品编号")@RequestParam("goodsId") int goodsId){
		Result result=tagGoodsService.add(tagId, goodsId);
		return result;
	}
	//根据标签id加载商品
	@RequestMapping(value="/tag/{tagId}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据标签编号加载商品")
	public Result loadByTagId(@PathVariable("tagId") int tagId){
		Result result=tagGoodsService.loadByTagId(tagId);
		return result;
	}
	//根据id更新排序（sort）
	@RequestMapping(value="/sort/{id}",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value="更新排序")
	public Result updateSortById(@ApiParam(value="编号")@PathVariable("id") int id,
			@ApiParam(value="排序")@RequestParam("sort") int sort){
		Result result=tagGoodsService.updateSortById(id, sort);
		return result;
	}
	//根据id删除
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value="删除标签商品关联信息")
	public Result deleteById(@PathVariable("id") int id){
		Result result=tagGoodsService.deleteById(id);
		return result;
	}
}
