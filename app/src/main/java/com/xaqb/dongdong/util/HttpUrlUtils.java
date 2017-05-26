package com.xaqb.dongdong.util;

/**
 * 接口地址
 */
public class HttpUrlUtils {
    private static HttpUrlUtils httpUrl = new HttpUrlUtils();

    public static HttpUrlUtils getHttpUrl() {
        return httpUrl;
    }

    private String getBaseUrl() {
        return "http://api.qbdongdong.com/v1/user/";
    }

    /**
     * 登录
     *  "username":"user11","password":"123456","device":
     * @return
     */
    public String userLogin(){
        return getBaseUrl() + "login.json?";
    }


    /**
     * 实名认证
     * @return
     */
    public String realApply(){
        return getBaseUrl() + "real_apply.json?";
    }


    /**
     * 新用户注册
     * @return
     */
    public String userRegister(){
        return  getBaseUrl()+"reg.json?";
    }

    /**
     * 手机验证码
     * @return
     */
    public String phoneCode(){
        return getBaseUrl()+"smscode.json?";
    }

    /**
     * 找回密码
     * @return
     */
    public String findPsw(){
        return getBaseUrl()+"backpwd.json?";
    }

    /**
     * 地址查询
     * @return
     */

    //http://api.qbdongdong.com/v1/user/address/用户ID参数.json?access_token=token值
    public String getAddress(){
        return getBaseUrl()+"address/";
    }
    /**
     * 地址编辑
     * @return
     */

    // http://api.qbdongdong.com/v1/user/address/地址ID.json?access_token=token值
    public String editAddress(){
        return getBaseUrl()+"address/";
    }

    /**
     * 地址删除
     * @return
     */

    //  http://api.qbdongdong.com/v1/user/address/ID参数.json?access_token=token值
    public String deleteAddress(){
        return getBaseUrl()+"address/";
    }

    /**
     * 新建地址
     * @return
     */

    //http://api.qbdongdong.com/v1/user/address.json?access_token=token值
    public String addAddress(){
        return getBaseUrl()+"address.json?";
    }

    /**
     * 获取积分
     * @return
     */

    //http://api.qbdongdong.com/v1/user/integral.json?:uid&:p
    public String getGrade(){
        return getBaseUrl()+"integral.json?";
    }

    /**
     * 修改密码
     * @return
     */
    //http://api.tqbdongdong.com/v1/user/password/id/5.json
    public String modifyPSW(){
        return getBaseUrl()+"/password/id/";
    }

    /**
     * 修改用户信息
     * @return
     */
    // http://api.qbdongdong.com/v1/user/profile/用户ID.json?access_token=token值
    public String modifyMessage(){
        return getBaseUrl()+"/password/id/";
    }

    /**
     *  http://api.qbdongdong.com/v1/user/order.json?
     *  下单
     * @return
     */
    public String doOrder(){
        return getBaseUrl()+"order.json?";
    }


    /**
     *     http://api.qbdongdong.com/v1/user/order.json?uid=1&status=0&p=1
     *     获取订单
     * @return
     */
    public String getOrder(){

        return getBaseUrl()+"order.json?";
    }


    /**
     *      http://api.qbdongdong.com/v1/user/order/:id.json
     *     获取订单详情
     * @return
     */
    public String getOrderDetail(){

        return getBaseUrl()+"order/";
    }


    /**
     *       http://api.qbdongdong.comv1/user/cancel_order/订单id.json
     *     取消订单
     * @return
     */
    public String cancalOrder(){

        return getBaseUrl()+"cancel_order/";
    }


    /**
     *        http://api.qbdongdong.com/v1/user/profile/用户ID.json?access_token=token值
     *     设置个人信息
     * @return
     */
    public String modifyMess(){

        return getBaseUrl()+"profile/";
    }

}