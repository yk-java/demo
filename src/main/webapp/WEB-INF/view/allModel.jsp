<%@ page contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全部员工</title>
<script type="text/javascript" src="../../scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$(".down").click(function(){
			var href = $(this).attr("href");
			$("form").attr("action", href).submit();			
			return false;
		});
        $(".delete").click(function(){
            var href = $(this).attr("href");
            $("#deleteModel").attr("action", href).submit();
            return false;
        });
	});
</script>
</head>
<body>

	<form action="" method="get">
	</form>

	<form id="deleteModel" action="" method="post">
		<input type="hidden" name="_method" value="DELETE" />
	</form>

	<c:choose>
		<c:when test="${empty requestScope.ALLMODEL}">
			没有模型信息
		</c:when>
		<c:otherwise>
			<table border="1">
				<tr>
					<th>编号</th>
					<th>名称</th>
					<th>流程部署key值</th>
					<th>版本号</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${requestScope.ALLMODEL }" var="Model">
					<tr>
						<td>&nbsp;${Model.id }&nbsp;</td>
						<td>&nbsp;${Model.name }&nbsp;</td>
						<td>${Model.key }</td>
						<td>${Model.version }</td>
						<td>${Model.metaInfo }</td>
						<td><a class="delete"
							href="${pageContext.request.contextPath }/models/delete/${Model.id}">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
							href="${pageContext.request.contextPath }/models/deploy/${Model.id}">部署</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
                                href="${pageContext.request.contextPath }/modeler.html?modelId=${Model.id}">编辑流程</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>

</body>
</html>