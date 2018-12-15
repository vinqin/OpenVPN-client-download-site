<%@ page contentType="text/html;UTF-8" %>

<!-- 登陆连接 -->
<h3><a href="javascript:void(0);" onclick="show()">Download Config File</a></h3>
<!-- 弹出登录表单开始 -->
<div id="hsDiv" style="display:none;">
    <!-- 右上角关闭按钮 -->
    <div id="closediv">
        <a href="javascript:void(0);" onclick="closeDiv()">
            <!--<img src="images/logingb.png">-->
        </a>
    </div>
    <!-- 登陆标题 -->
    <!--<div id="dlTitle">登录</div>-->
    <!-- 登陆div -->
    <form action="download.do?type=config" method="post" onsubmit="return beforeSubmit()">
        <div id="tbdiv">
            <table id="logintb">
                <tr>
                    <td class="logintd1"><br></td>
                </tr>
                <tr>
                    <td class="logintd1">Username:</td>
                </tr>
                <tr>
                    <td class="logintd2">
                        <input type="text" name="username"/>
                    </td>
                </tr>
                <tr>
                    <td class="logintd1">Password:</td>
                </tr>
                <tr>
                    <td class="logintd2">
                        <input type="password" id="_password" name="_password"/>
                        <input type="hidden" id="password" name="password">
                    </td>
                </tr>
                <tr>
                    <td class="logintd4">
                        <input type="submit" value="Login in"/>
                    </td>
                </tr>
            </table>

        </div>
    </form>
</div>
<div id="overDiv" style="display:none;"></div>

<script type="text/ecmascript" src="js/sha1.js"></script>
<script type="text/javascript">
    function beforeSubmit() {
        var _psw = document.getElementById("_password");
        var sha = hex_sha1(_psw.value);
        _psw.value = ""; // for security
        var psw = document.getElementById("password");
        psw.value = sha;
        return true;
    }

</script>
</head>
