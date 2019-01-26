app.controller("baseController", function($scope){
	$scope.paginationConf = {
			currentPage : 1,//当前页
			totalItems : 10,//总记录数
			itemsPerPage : 10,//每页的记录数
			perPageOptions : [ 10, 20, 30, 40, 50 ],//分页选项
			onChange : function() {//更改页面时触发事件
				$scope.reloadList();
			}
		};

		//刷新列表
		$scope.reloadList = function() {
			$scope.search($scope.paginationConf.currentPage,
					$scope.paginationConf.itemsPerPage);
		}
		
		$scope.selectIds = [];//用户勾选的id集合

		//用户勾选复选框
		$scope.updateSelection = function($event, id) {
			if ($event.target.checked) {
				$scope.selectIds.push(id);
			} else {
				var index = $scope.selectIds.indexOf(id);
				$scope.selectIds.splice(index, 1);
			}
		}
		
		$scope.jsonToString = function(jsonString, key){
			var json = JSON.parse(jsonString);
			var value = "";

			for(var i = 0; i < json.length; i++){
				if(i > 0){
					value += ", ";
				}
				value += json[i][key];
			}
			return value;
		}
		
		//在list集合中根据某key的值查询对象
		$scope.searchObjectByKey = function(list, key, keyValue){
			for(var i=0; i < list.length; i ++){
				if(list[i][key] == keyValue){
					return list[i];
				}
			}
			return null;
		}
});