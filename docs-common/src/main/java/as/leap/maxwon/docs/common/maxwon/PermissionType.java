package as.leap.maxwon.docs.common.maxwon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Sets;

import java.util.Set;

public enum PermissionType {


    SUPER_SYS_ADMIN("super_sys_admin", null),
    SUPER_SYS_AGENCY("super_sys_agency",SUPER_SYS_ADMIN),
    AGENCY_USER("agency_user",SUPER_SYS_AGENCY),
    ADMIN_SYS_USER("admin_sys_user",SUPER_SYS_ADMIN),
    ADMIN_SYS_PERMISSION("admin_sys_permission",SUPER_SYS_ADMIN),
    ADMIN_SYS_TRACE("admin_sys_trace",SUPER_SYS_ADMIN),
    AMA("ama", SUPER_SYS_ADMIN),

    AMA_APP("ama_app", AMA),
    MASTER_KEY("master_key", AMA),
    SYS_USER("sys_user", AMA_APP),
    APP_USER("app_user", SYS_USER),
    API_KEY("api_key", APP_USER),

    AMA_APP_CREATE("ama_app_create", AMA_APP),
    AMA_APP_CREATE_NAME("ama_app_create_name",AMA_APP_CREATE),
    AMA_APP_CREATE_CONPONENT("ama_app_create_component",AMA_APP_CREATE),
    AMA_APP_CREATE_GRENERATION("ama_app_create_generation",AMA_APP_CREATE),

    AMA_APP_MANAGE("ama_app_manage",AMA_APP),
    AMA_APP_MANAGE_MEMBER("ama_app_manage_member",AMA_APP_MANAGE),
    AMA_APP_MANAGE_MEMBER_INFO("ama_app_manage_member_info",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_POINT("ama_app_manage_member_point",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_LEVEL("ama_app_manage_member_level",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_WITHDRAW("ama_app_manage_member_withdraw",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_RECHARGE("ama_app_manage_member_recharge",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_AUDIT("ama_app_manage_member_audit",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_POINTBUY("ama_app_manage_member_pointbuy",AMA_APP_MANAGE_MEMBER),
    AMA_APP_MANAGE_MEMBER_PREPAYCARD("ama_app_manage_member_prepaycard",AMA_APP_MANAGE_MEMBER),

    /** 配送 */
    AMA_APP_MANAGE_DIST("ama_app_manage_dist",AMA_APP_MANAGE),

    AMA_APP_MANAGE_PRODUCT("ama_app_manage_product",AMA_APP_MANAGE),
    AMA_APP_MANAGE_COMMENT("ama_app_manage_comment",AMA_APP_MANAGE),
    AMA_APP_MANAGE_PAY("ama_app_manage_pay",AMA_APP_MANAGE),
    AMA_APP_MANAGE_PAY_CHANNEL("ama_app_manage_pay_channel",AMA_APP_MANAGE_PAY),
    AMA_APP_MANAGE_PAY_TRADE("ama_app_manage_pay_trade",AMA_APP_MANAGE_PAY),
    AMA_APP_MANAGE_PAY_WEBHOOK("ama_app_manage_pay_webhook",AMA_APP_MANAGE_PAY),
    AMA_APP_MANAGE_PAY_ORDERNO("ama_app_manage_pay_orderno",AMA_APP_MANAGE_PAY),

    AMA_APP_MANAGE_ORDER("ama_app_manage_order",AMA_APP_MANAGE),

    AMA_APP_MANAGE_MALLSHOP("ama_app_manage_mallshop",AMA_APP_MANAGE),
    AMA_APP_MANAGE_MALLSHOP_CATEGORY("ama_app_manage_mallshop_category",AMA_APP_MANAGE_MALLSHOP),
    AMA_APP_MANAGE_MALLSHOP_MALL("ama_app_manage_mallshop_mall",AMA_APP_MANAGE_MALLSHOP),

    AMA_APP_MANAGE_SYS("ama_app_manage_sys",AMA_APP_MANAGE),
    AMA_APP_MANAGE_SYS_USER("ama_app_manage_sys_user",AMA_APP_MANAGE_SYS),
    AMA_APP_MANAGE_SYS_PERMISSION("ama_app_manage_sys_permission",AMA_APP_MANAGE_SYS),
    AMA_APP_MANAGE_SYS_TRACE("ama_app_manage_sys_trace",AMA_APP_MANAGE_SYS),
    AMA_APP_MANAGE_SYS_SET("ama_app_manage_sys_set",AMA_APP_MANAGE_SYS),
    AMA_APP_MANAGE_CMS("ama_app_manage_cms",AMA_APP_MANAGE),
    AMA_APP_MANAGE_STORE("ama_app_manage_store",AMA_APP_MANAGE),
    AMA_APP_MANAGE_COUPONS("ama_app_manage_coupons",AMA_APP_MANAGE),
    AMA_APP_MANAGE_RESERVE("ama_app_manage_reserve",AMA_APP_MANAGE),
    AMA_APP_MANAGE_RESERVEORDER("ama_app_manage_reserveorder",AMA_APP_MANAGE),
    AMA_APP_MANAGE_DISTRIBUTION("ama_app_manage_distribution",AMA_APP_MANAGE),
    AMA_APP_MANAGE_SECBUY("ama_app_manage_secbuy",AMA_APP_MANAGE),
    AMA_APP_MANAGE_IM("ama_app_manage_im",AMA_APP_MANAGE),
    AMA_APP_MANAGE_BBS("ama_app_manage_bbs",AMA_APP_MANAGE),
    AMA_APP_MANAGE_CIRCLE("ama_app_manage_circle",AMA_APP_MANAGE),
    AMA_APP_MANAGE_FORMSET("ama_app_manage_formset",AMA_APP_MANAGE),
    AMA_APP_MANAGE_LIVE("ama_app_manage_live", AMA_APP_MANAGE),
    AMA_APP_MANAGE_LIVE_HOST("ama_app_manage_live_host", AMA_APP_MANAGE_LIVE),
    AMA_APP_MANAGE_LIVE_CHANNEL("ama_app_manage_live_channel", AMA_APP_MANAGE_LIVE),
    AMA_APP_MANAGE_ZONE("ama_app_manage_zone", AMA_APP_MANAGE),
    AMA_APP_MANAGE_QUICK_MENU("ama_app_manage_quickmenu", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_BANNER("ama_app_manage_banner", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_CASHIER("ama_app_manage_cashier", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_MALL("ama_app_manage_mall", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_REPORTING("ama_app_manage_reporting", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_FREIGHT("ama_app_manage_freight", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_CATEGORY("ama_app_manage_category", AMA_APP_MANAGE),
    AMA_APP_MANAGE_MANAGE_BILL("ama_app_manage_bill", AMA_APP_MANAGE),


    AMA_APP_MANAGE_PUBLIC("ama_app_manage_public",AMA_APP_MANAGE),

    AMA_APP_MANAGE_RESERVE_MALL("ama_app_manage_reserve_mall", AMA_APP_MANAGE),

    AMA_APP_MANAGE_PRECEPIT("ama_app_manage_precepit", AMA_APP_MANAGE),
    AMA_APP_MANAGE_PDISTRIBUTION("ama_app_manage_pdistribution", AMA_APP_MANAGE),

    AMA_APP_MARKETING("ama_app_marketing", AMA_APP),
    AMA_APP_MARKETING_CS("ama_app_marketing_cs",AMA_APP_MARKETING),
    AMA_APP_MARKETING_SMS("ama_app_marketing_sms",AMA_APP_MARKETING),
    AMA_APP_MARKETING_PUSH("ama_app_marketing_push",AMA_APP_MARKETING),
    AMA_APP_MARKETING_WECHAT("ama_app_marketing_wechat",AMA_APP_MARKETING),
    AMA_APP_MARKETING_ASSERVICE("ama_app_marketing_asservice",AMA_APP_MARKETING),
    AMA_APP_MARKETING_VOUCHER("ama_app_marketing_voucher",AMA_APP_MARKETING),
    AMA_APP_MARKETING_RECHARGE("ama_app_marketing_recharge",AMA_APP_MARKETING),
    AMA_APP_MARKETING_SALE("ama_app_marketing_sale",AMA_APP_MARKETING),


    AMA_APP_ANA("ama_app_ana", AMA_APP),
    AMA_APP_ANA_ANALYTICS("ama_app_ana_analytics", AMA_APP_ANA),
    AMA_APP_ANA_USER("ama_app_ana_user", AMA_APP_ANA),
    AMA_APP_ANA_EBANA("ama_app_ana_ebana", AMA_APP_ANA),
    AMA_APP_ANA_LIVEREPORT("ama_app_ana_livereport", AMA_APP_ANA),
    AMA_APP_ANA_DISTREPORT("ama_app_ana_disreport", AMA_APP_ANA),

    AMA_APP_OTHER("ama_app_other", AMA_APP),
    AMA_APP_OTHER_USERCUSTOMATTR("ama_app_other_usercustomattr", AMA_APP_OTHER),
    AMA_APP_OTHER_PRODUCTIMPORT("ama_app_other_productimport", AMA_APP_OTHER),
    AMA_APP_OTHER_ORDERADD("ama_app_other_orderadd", AMA_APP_OTHER),
    AMA_APP_OTHER_ORDERDELETE("ama_app_other_orderdelete", AMA_APP_OTHER),
    AMA_APP_OTHER_WONMANAGER("ama_app_other_wonmanager", AMA_APP_OTHER),
    AMA_APP_OTHER_WONDISPATCHER("ama_app_other_wondispatcher", AMA_APP_OTHER),

    AMA_APP_SERVICEMARKET("ama_app_servicemarket", AMA_APP),
    AMA_APP_SERVICEMARKET_APPMALL("ama_app_servicemarket_appmall", AMA_APP_SERVICEMARKET),

    AMA_APP_APP("ama_app_app", AMA_APP),
    AMA_APP_APP_SITUATION("ama_app_app_situation", AMA_APP_APP),
    AMA_APP_APP_SHARE("ama_app_app_share", AMA_APP_APP),


    AMA_APP_CONFIG("ama_app_config", AMA_APP),
    AMA_APP_CONFIG_ADVANCED("ama_app_config_advanced", AMA_APP_CONFIG),



    MALL("mall",null),

    MALL_SYS_USER("mall_sys_user", MALL),

    MALL_APP_MANAGER_MALL("mall_app_manage_mall",MALL),
    MALL_APP_MANAGER_MALL_INFO("mall_app_manage_mall_info",MALL_APP_MANAGER_MALL),

    MALL_APP_MANAGE_PRODUCT("mall_app_manage_product",MALL),//商品
    MALL_APP_MANAGE_CATEGORY("mall_app_manage_category",MALL),//分类
    MALL_APP_MANAGE_FREIGHT("mall_app_manage_freight",MALL),//运费

    MALL_APP_MANAGE_ORDER("mall_app_manage_order",MALL),  //订单
    MALL_APP_MANAGE_ORDER_INFO("mall_app_manage_order_info",MALL_APP_MANAGE_ORDER),

    MALL_APP_MANAGE_CASHIER("mall_app_manage_cashier",MALL), //收银台

    MALL_APP_MANAGE_REPORTING("mall_app_manage_reporting",MALL),  //报表
    MALL_APP_MANAGE_REPORTING_INFO("mall_app_manage_reporting_info",MALL_APP_MANAGE_REPORTING),  //店铺报表管理
    MALL_APP_MANAGE_REPORTING_RECEIPT("mall_app_manage_reporting_receipt",MALL_APP_MANAGE_REPORTING),  //店铺应收账款管理

    MALL_APP_MANAGE_MEMBER("mall_app_manage_member",MALL),  //会员管理
    MALL_APP_MANAGE_MEMBER_INFO("mall_app_manage_member_info",MALL_APP_MANAGE_MEMBER),


    MALL_APP_MANAGE_PAY("mall_app_manage_pay",MALL),  //支付管理
    MALL_APP_MANAGE_PAY_RECEIPT("mall_app_manage_pay_receipt",MALL_APP_MANAGE_PAY),  //支付管理

    MALL_APP_MANAGE_SYS("mall_app_manage_sys",MALL),
    MALL_APP_MANAGE_SYS_USER("mall_app_manage_sys_user",MALL_APP_MANAGE_SYS),
    MALL_APP_MANAGE_SYS_PERMISSION("mall_app_manage_sys_permission",MALL_APP_MANAGE_SYS),
    MALL_APP_MANAGE_SYS_TRACE("mall_app_manage_sys_trace",MALL_APP_MANAGE_SYS),

    MALL_APP_MANAGE_COMMENT("mall_app_manage_comment",MALL),
    MALL_APP_MARKETING("mall_app_marketing", MALL),
    MALL_APP_MARKETING_CS("mall_app_marketing_cs",MALL_APP_MARKETING),
    MALL_APP_MARKETING_ASSERVICE("mall_app_marketing_asservice",MALL_APP_MARKETING),

    MALL_APP_MANAGE_RESERVE("mall_app_manage_reserve", MALL),
    MALL_APP_MANAGE_RESERVEORDER("mall_app_manage_reserveorder", MALL),


    MALL_APP_MANAGE_DIST("mall_app_manage_dist",MALL),
    MALL_APP_MANAGE_DIST_USER("mall_app_manage_dist_user",MALL),
    MALL_APP_MANAGE_DIST_SCOPE("mall_app_manage_dist_scope",MALL),
    MALL_APP_MANAGE_DIST_REPORT("mall_app_manage_dist_report",MALL),

    MALL_APP_MANAGE_VOUCHER("mall_app_manage_voucher",MALL),

    MALL_APP_ANA_DISTREPORT("mall_app_ana_distreport",MALL),

    MALL_APP_MANAGE_SALE("mall_app_manage_sale",MALL),
    MALL_APP_MANAGE_SALE_GROUP("mall_app_manage_sale_group",MALL),
    MALL_APP_MANAGE_SALE_BUYING("mall_app_manage_sale_buying",MALL),
    MALL_APP_MANAGE_SALE_VOUCHER("mall_app_manage_sale_voucher",MALL)
    ;

    private String identity;
    private PermissionType parentType;

    private PermissionType(String identity, PermissionType parentType) {
        this.identity = identity;
        this.parentType = parentType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public PermissionType getParentType() {
        return parentType;
    }

    public void setParentType(PermissionType parentType) {
        this.parentType = parentType;
    }


    public static Set<PermissionType> getAllPermission(PermissionType permissionType){

        Set<PermissionType> types  = Sets.newHashSet();
        for(PermissionType type :PermissionType.values()){
            if(type.getParentType() == permissionType) {
                types.add(type);
                types.addAll(getAllPermission(type));
            }
        }
        return types;
    }


    @JsonCreator
    public static PermissionType fromString(String identity) {
        if (identity == null) return null;

        for (PermissionType permissionType: PermissionType.values()){
            if (identity.equals(permissionType.getIdentity())){
                return permissionType;
            }
        }

        return null;
    }

    @JsonValue
    public String toString() {
        return this.identity;
    }
}