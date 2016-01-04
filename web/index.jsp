<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>API Monitor</title>
    <link rel="stylesheet" href="./skins/css/common.css">
    <script src="./skins/js/libs/jquery-1.9.1.min.js"></script>
    <script>
        var start = ${status};
    </script>
</head>
<body>
<header>
    <h2>
        <span>API Monitor</span>
        <a href="javascript:;" id="conf_btn" class="btn btn-link">配置参数</a>
    </h2>
</header>
<section class="content">
    <div class="btn-contain">
        <a href="javascript:;" id="start" class="btn btn-circle btn-start<c:if test="${status}"> disabled hidden</c:if>">拉取实时数据</a>
        <a href="javascript:;" id="stop" class="btn btn-circle btn-stop<c:if test="${!status}"> disabled hidden</c:if>">停&emsp;止</a>
    </div>
    <div class="chart" id="minuteChart"></div>
</section>
<script src="./skins/js/libs/echarts/echarts.js"></script>
<script src="./skins/js/common/common.js"></script>
</body>
</html>
