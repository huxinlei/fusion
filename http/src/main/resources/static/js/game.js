/**
 * 简单封装bootsrap-table中ajax设置
 */
var gameBootstrapTable = function(url, params) {
	gameAjax({
		url : url,
		data : params.data,
		callback : function(obj) {
			params.success({
				total : obj.totalElements,
				rows : obj.content
			});
		}
	});
	params.complete();
};

/**
 * option:{ 
 *   url:请求地址 
 *   type:请求方法，默认post方法 
 *   data:请求参数 
 *   async:是否同步请求，默认异步
 *   callback:回调函数 
 * }
 */
var gameAjax = function(option) {
	if (!option.url) {
		swal("", "Request url is required", "error");
		return;
	}
	if (option.type === undefined || option.type === null) {
		option.type = "post";
	}
	if (option.async === undefined || option.async === null) {
		option.async = true;
	}
	$.ajax({
		url : option.url,
		dataType : "json",
		type : option.type,
		data : option.data,
		async : option.async,
		success : function(data) {
			if (data.code == 0) {
				option.callback(data.body, data.message);
			} else {
				swal("", data.message, "warning");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			swal("Server is fucked！", errorThrown, "error");
		}
	});
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var swalConfirm = function (callback) {
	swal({
		title : "您确定要删除吗？",
		text : "删除后将无法恢复，请谨慎操作！",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
	}, function(isConfirm) {
		if (isConfirm) {
			callback();
		}
	})
}