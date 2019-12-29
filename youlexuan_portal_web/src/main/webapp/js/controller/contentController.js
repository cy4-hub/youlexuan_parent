//广告控制层（运营商后台）
app.controller("contentController", function($scope, contentService) {
    $scope.findByCategoryId = function(categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function(response) {
                $scope.contentList = response;
            });
      }

    $scope.search=function(){
        location.href="http://localhost:9007/search.html#?keywords="+$scope.keywords;
    }
});
