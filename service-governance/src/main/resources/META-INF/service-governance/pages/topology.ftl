<link rel="stylesheet" href="${request.contextPath}/service-governance/static/css/base.css"/>
<link rel="stylesheet" href="${request.contextPath}/service-governance/static/css/Spacetree.css.cgi"/>
<script type="text/javascript" src="${request.contextPath}/service-governance/static/js/jit.js.cgi"></script>
<script type="text/javascript" src="${request.contextPath}/service-governance/static/js/topology.js.cgi"></script>
<script>
    function serviceTopology() {
        $.getJSON("${request.contextPath}/service-governance/console/topology/providers", function (json) {
            init(json);
        })
    }

    $(function () {
        serviceTopology();
    })
</script>
<style>
    #center-container {
        width: 100%;
        left: -200px;
    }

    #infovis {
        width: 100%;
        /*left: -200px;*/
    }

    #infovis-canvaswidget{
        width: 100%;
        /*left: -200px;*/
    }

    /*#center-container .node {*/
    /*line-height: 30px;*/
    /*}*/

</style>


<div id="center-container">
    <div id="infovis"></div>
</div>

<div id="right-container">

    <h4>Tree Orientation</h4>
    <table>
        <tr>
            <td>
                <label for="r-left">Left </label>
            </td>
            <td>
                <input type="radio" id="r-left" name="orientation" checked="checked" value="left"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="r-top">Top </label>
            </td>
            <td>
                <input type="radio" id="r-top" name="orientation" value="top"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="r-bottom">Bottom </label>
            </td>
            <td>
                <input type="radio" id="r-bottom" name="orientation" value="bottom"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="r-right">Right </label>
            </td>
            <td>
                <input type="radio" id="r-right" name="orientation" value="right"/>
            </td>
        </tr>
    </table>

    <h4>Selection Mode</h4>
    <table>
        <tr>
            <td>
                <label for="s-normal">Normal </label>
            </td>
            <td>
                <input type="radio" id="s-normal" name="selection" checked="checked" value="normal"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="s-root">Set as Root </label>
            </td>
            <td>
                <input type="radio" id="s-root" name="selection" value="root"/>
            </td>
        </tr>
    </table>

</div>

<div id="log"></div>