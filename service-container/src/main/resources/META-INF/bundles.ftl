<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>出错啦</title>
    <link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.2.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.2.0/css/bootstrap-theme.min.css"/>
    <script type="text/javascript" src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://libs.baidu.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <h1>已加载模块列表</h1>


<#list components as component>
    <h2>${component.name}对外发布的服务:</h2>

    <span>
        <ul>
            <#list services[component.name] as service>
                <li>${service}</li>
            </#list>
        </ul>
    </span>


</#list>
</div>
</body>
</html>