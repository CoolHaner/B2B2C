//控制层 
app.controller('seckillGoodsController', function($scope, seckillGoodsService, $location, $interval) {
	//读取列表数据绑定到表单中  
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			}			
		);
	} 

	
	//查询商品
	$scope.findOne = function(){
		//接受参数ID
		var id = $location.search()["id"];
		seckillGoodsService.findOne(id).success(function(response){
			$scope.entity = response;
			
			//倒计时开始
			//获取从结束时间到当前日期的秒数
			allsecond = Math.floor( (new Date($scope.entity.endTime).getTime() - new Date().getTime())/1000 );
			
			time = $interval(function(){
				allsecond = allsecond -1;
				$scope.timeString = convertTimeString(allsecond);
				if (allsecond <= 0){
					$interval.cancel(time);
				}
			},1000 );
		})
	}
	
	//日期转换
	convertTimeString = function(allsecond){
		var days = Math.floor( allsecond/(3600*24));
		var hours = Math.floor((allsecond - days*3600*24)/(3600));
		var minuts = Math.floor((allsecond - days*3600*24 - hours*3600)/60);
		var seconds = allsecond - days*3600*24 - hours*3600 - minuts*60;
		var timeString = "";
		if (days > 0){
			timeString = days + "天";
		}
		return timeString + hours + ":" + minuts + ":" + seconds;
	}
		
	//提交订单
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder($scope.entity.id).success(
			function(response){
				if(response.success){
					alert("下单成功，请在1分钟内完成支付");
					location.href="pay.html";
				}else{
					alert(response.message);
				}
			}
		);		
	}

});
