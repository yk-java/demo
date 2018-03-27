<%@ page contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>文件上传下载</title>
    <script type="text/javascript" src="<%=basePath%>scripts/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        var basePath="<%=basePath%>";
        $(function(){
            $("#excel").click(function(){
                exportBatch();
                return false;
            });
        });
        function exportBatch() {
            //get请求，可以传递参数，比方说我这里就传了一堆卷号，我只生成传过去的这堆卷号的检验记录
            //参数rollNumbers的细节就不展示了，业务相关
            window.location.href = basePath+'excel/downUser';
        }
    </script>
</head>
<body>
<h2>上传多个文件 实例</h2>
<form action="${pageContext.request.contextPath }/file/filesUpload" method="post" enctype="multipart/form-data">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    选择文件:<input type="file" name="files" width="120px">
    <p>
    <input type="submit" value="提交">
</form>
<hr>
<form action="${pageContext.request.contextPath }/file/down/" method="get">
    <input type="submit" value="下载">
    <input id="excel" type="button" value="导出">
</form>
<form action="${pageContext.request.contextPath }/zip/downloadFiles" method="get">
    <input type="submit" value="zip下载">
</form>
</body>
</html>
