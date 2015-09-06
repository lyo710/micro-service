<#include "macro.ftl">
<!DOCTYPE html>
<html lang="en">
<@header>
<script>
    $(function () {
        ${tab}();
    })

    function topology() {
        $("#container").load("${request.contextPath}/service-governance/console/topology");
    }

    function provider() {
        $("#container").load("${request.contextPath}/service-governance/console/provider");
    }
</script>
</@header>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="javascript:topology()">[Service Topology]</a>
            <a class="navbar-brand" href="javascript:">[Dependency Topology]</a>
            <a class="navbar-brand" href="javascript:provider()">[Service Provider]</a>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<div id="container"></div>
</body>
</html>