app.controller('itemController',function($scope){
    
	$scope.num=1;
	
	$scope.changNum=function(x){
		
		$scope.num=$scope.num+x;
		if($scope.num <= 0){
			$scope.num=1;
		}
		
	}
	
	//规格
	$scope.specificationItems = {};
	
	$scope.updateSpec=function(specName, opName){
		
		$scope.specificationItems[specName] = opName;
		
	}
	
	//默认选中
	$scope.isSelected=function(specName, opName){
		
		if($scope.specificationItems[specName] != opName){
			return false;
		}else{
			return true;
		}
		
	}
	
	//加载默认的Sku
	$scope.loadDefaultSku=function(){
		
		$scope.sku=itemList[0];
		
		$scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
		
		
	}
	
	changeSku=function(){
		
		for(var i=0; i<itemList.length;i++){
			if(equalObj(itemList[i].spec,$scope.specificationItems)){
				$scope.sku = itemList[i];
				return;
			}
		}
	}
	
	
	equalObj=function(obj1, obj2){
		
		for(var k in obj1){
			if(obj1[k] != obj2[k]){
				return false;
			}
		}
		
		for(var k in obj2){
			if(obj2[k] != obj1[k]){
				return false;
			}
		}
		
		return true;
	}

});







