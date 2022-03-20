<%--
  Created by IntelliJ IDEA.
  User: 文博
  Date: 2021/12/9
  Time: 13:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<style>
    .layui-form-select{
        width: 500px;
    }
</style>
<div class="layuimini-container layuimini-page-anim">
    <div class="layuimini-main">

        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
            <legend>修改订单</legend>
        </fieldset>

        <form method="post" class="layui-form" action="/UpdateOrder">
            <div class="layui-form-item">
                <label class="layui-form-label">订单号</label>
                <div class="layui-input-block">
                    <input type="text" name="custom_id" lay-verify="required" autocomplete="off" placeholder="请输入订单号"
                           class="layui-input" value="${sessionScope.product.data[0].custom_id}">
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-form-item">
                    <label class="layui-form-label">客户名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="customer" lay-verify="required" autocomplete="off"
                               placeholder="请输入客户名称"
                               class="layui-input" value="${sessionScope.product.data[0].customer}">
                    </div>
                </div>

                <div class="layui-form-item">

                    <div class="layui-inline">
                        <label class="layui-form-label">交付日期</label>
                        <div class="layui-input-inline">
                            <input type="text" name="required_time" id="required_time" lay-verify="date"
                                   placeholder="请选择日期"
                                   autocomplete="off" class="layui-input" value="${sessionScope.product.data[0].required_time}">
                        </div>
                    </div>

                </div>

                <div class="layui-inline">
                    <label class="layui-form-label" >请选择产品</label>
                    <div class="layui-input-inline" >
                        <select name="product_id" id="product_id" lay-verify="required" lay-search=""  class="layui-input" value="${sessionScope.product.data[0].name}">

                        </select>
                    </div>

                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">产品单价</label>
                    <div class="layui-input-block">
                        <input type="number" name="price" lay-verify="required|number" lay-reqtext="必填项"
                               placeholder="请输入" autocomplete="off" class="layui-input" value="${sessionScope.product.data[0].price}">
                    </div>
                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">毛板尺寸</label>
                    <div class="layui-input-block">
                        <input type="text" name="maoban_size"
                               autocomplete="off" class="layui-input"  value="${sessionScope.product.data[0].maoban_size}">
                    </div>
                </div>
                <div class="layui-form-item">

                    <label class="layui-form-label">毛板数量</label>
                    <div class="layui-input-block">
                        <input type="number" name="mbsl"
                               autocomplete="off" class="layui-input"  value="${sessionScope.product.data[0].mbsl}">
                    </div>
                </div>

                <div class="layui-form-item">

                    <label class="layui-form-label">投料</label>
                    <div class="layui-input-block">
                        <input type="text" name="touliao"
                               placeholder="请输入" autocomplete="off" class="layui-input"  value="${sessionScope.product.data[0].touliao}">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">使用库存</label>
                    <div class="layui-input-block">
                        <input type="number" name="reduce" lay-verify="required|number" lay-reqtext="必填项"
                               placeholder="请输入" autocomplete="off" class="layui-input" value="${sessionScope.product.data[0].using_stock}">
                    </div>
                </div>


                <div class="layui-form-item">

                    <label class="layui-form-label">需求数量</label>
                    <div class="layui-input-block">
                        <input type="number" name="amount" lay-verify="required|number" lay-reqtext="必填项"
                               placeholder="请输入" autocomplete="off" class="layui-input"  value="${sessionScope.product.data[0].amount}">
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-form-item">
                        <label class="layui-form-label">备注</label>
                        <div class="layui-input-block">
                            <input type="text" name="beizhu" autocomplete="off" placeholder="请输入备注"
                                   class="layui-input"  value="${sessionScope.product.data[0].beizhu}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button type='submit' class="layui-btn" lay-submit="" lay-filter="demo1">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>

    </div>
</div>
<script charset="UTF-8">
    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate;

        /**
         * 初始化表单，要加上，不然刷新部分组件可能会不加载
         */
        form.render();

        //日期
        laydate.render({
            elem: '#required_time'
        });
        var $ = layui.jquery

        $.ajax({
            url: 'GetProducts?flag=search',
            dataType: 'json',
            type: 'get',
            success: function (data) {
                $("#product_id").empty();
                // $("#product_id").append(new Option("请选择或搜索产品", ""));
                <%--console.log(${sessionScope.product.data[0]})--%>
                let display_kucun = parseInt(${sessionScope.product.data[0].kucun}) + parseInt(${sessionScope.product.data[0].using_stock});
                $("#product_id").append(new Option("原产品：${sessionScope.product.data[0].name}${sessionScope.product.data[0].standard}${sessionScope.product.data[0].size}库存："+display_kucun,""));
                console.log(data);//下面会提到这个data是什么值
                //使用循环遍历，给下拉列表赋值
                $.each(data.data, function (index, value) {
                    var kcb='无库存板';
                    // console.log(value.department_id);
                    // if(value.maoban.length===0){
                    //     kcb='库存板';
                    // }
                    var hcl=' 后处理:';
                    // console.log(value.department_id);
                    if(value.houchuli.length===0){
                        hcl='';
                    }
                    // if(value.kucun>0){
                    if(parseInt(value.id)==parseInt(${sessionScope.product.data[0].product_id})){
                        $('#product_id').append(new Option(value.name + " " + value.standard + " " + value.size + " 库存:" + display_kucun +hcl + value.houchuli + " ", value.id+','+value.kucun));// 下拉菜单里添加元素
                    }else {
                        $('#product_id').append(new Option(value.name + " " + value.standard + " " + value.size + " 库存:" + value.kucun+ hcl + value.houchuli + " ", value.id+','+value.kucun));// 下拉菜单里添加元素
                    }
                    // }
                });
                layui.form.render("select");//重新渲染 固定写法
            },
            error: function () {
                layer.open({
                    content: '加载失败！',
                });
            }
        })
        //监听提交
        form.on('submit(demo1)', function (data) {
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // })
            console.log('addorders',data)
            var xmlHttpReg = null;
            if (window.ActiveXObject) {//如果是IE

                xmlHttpReg = new ActiveXObject("Microsoft.XMLHTTP");

            } else if (window.XMLHttpRequest) {

                xmlHttpReg = new XMLHttpRequest(); //实例化一个xmlHttpReg
            }
            var product=data.field.product_id.split(',');//product[0]为product_id,product[1]为product的库存
            console.log('product',product)
            if(product && product.length>=2 && parseInt(product[1])<parseInt(data.field.reduce)){
                layer.msg('库存不足,请修改使用库存',{icon:2});
                return false;
            }
            //如果实例化成功,就调用open()方法,就开始准备向服务器发送请求
            if (xmlHttpReg != null) {
                xmlHttpReg.open("POST", "UpdateOrder?id="+${sessionScope.product.data[0].id}, true);
                xmlHttpReg.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xmlHttpReg.onreadystatechange = doResult; //设置回调函数

                var realdata={...data.field,'product_id':product[0]}
                xmlHttpReg.send(JSON.stringify(realdata));
            }

            //回调函数
            //一旦readyState的值改变,将会调用这个函数,readyState=4表示完成相应

            //设定函数doResult()
            function doResult() {

                if (xmlHttpReg.readyState === 4) {//4代表执行完成


                    if (xmlHttpReg.status === 200) {//200代表执行成功
                        //将xmlHttpReg.responseText的值赋给ID为resText的元素
                        if (xmlHttpReg.responseText.includes("success")) {
                            alert("操作成功");
                            parent.reloadData();
                            layer.closeAll();
                        } else alert("操作失败！" + xmlHttpReg.responseText);

                    }
                }

            }

            return false;
        });

    });
</script>

