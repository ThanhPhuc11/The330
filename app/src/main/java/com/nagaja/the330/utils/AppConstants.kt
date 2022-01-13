package com.nagaja.the330.utils

object AppConstants {
    const val PREF_NAME = "fitor_pref"
    const val COMMENT_ACTION_REPLY_TYPE = "COMMENT_ACTION_REPLY_TYPE"
    const val COMMENT_ACTION_EDIT_TYPE = "COMMENT_ACTION_EDIT_TYPE"
    const val EXTRA_KEY1 = "EXTRA_KEY1"
    const val EXTRA_KEY2 = "EXTRA_KEY2"
    const val EXTRA_KEY3 = "EXTRA_KEY3"
    const val EXTRA_KEY4 = "EXTRA_KEY4"
    const val EXTRA_KEY5 = "EXTRA_KEY5"
    const val KEY_LIST_DATA = "KEY_LIST_DATA"
    const val KEY_STORIES_LIST_DATA = "KEY_LIST_DATA"
    const val NOTIFI_TYPE = "NOTIFI_TYPE"
    const val LIVE = "LIVE"
    const val VOD = "VOD"
    const val TEST = "TEST"
    const val VOD_WATCHED = "VOD_WATCHED"
    const val REQUESTING = "REQUESTING"
    const val REJECTED = "REJECTED"
    const val APPROVED = "APPROVED"
    const val CANCELLED = "CANCELLED"
    const val CHANGE_REQUEST = "CHANGE_REQUEST"
    const val CHANGE_APPROVE = "CHANGE_APPROVE"
    const val CHANGE_REJECT = "CHANGE_REJECT"
    const val CHANGE_SUCCESS = "CHANGE_SUCCESS"
    const val CANCELLED_NON_REFUND = "CANCELLED_NON_REFUND"
    const val CANCELLED_REFUNDED = "CANCELLED_REFUNDED"
    const val CERT_APPROVE = 1000
    const val CERT_REJECT = 1001
    const val BANK_APPROVE = 1002
    const val BANK_REJECT = 1003
    const val SUSPENSION_END = 1004
    const val PENDING = "PENDING"
    const val COMPLETED = "COMPLETED"
    const val UN_SETTLEMENT = "UN_SETTLEMENT"
    const val NOT_SELECT = "-1"
    const val SELECT_ALL = "-2"
    const val PAGE_SIZE = 30
    var YES = 0
    var NO = 1
    var CURRENT_ACC = 11
    var OTHER_ACC = 12
    var OPTION_TOP = 111
    var OPTION_MIDDLE = 333
    var OPTION_BOTTOM = 222
    var BOTTOM_SHEET_DELAY = 310
    const val SNS_TYPE_KAKAO = "SNS_TYPE_KAKAO"
    const val SNS_TYPE_NAVER = "SNS_TYPE_NAVER"
    const val SNS_TYPE_GOOGLE = "SNS_TYPE_GOOGLE"
    const val SNS_TYPE_FACEBOOK = "SNS_TYPE_FACEBOOK"
    var ADMIN = "ADMIN"
    var GENERAL = "GENERAL"
    var COMPANY = "COMPANY"
    var RESERVATION_CREATE = 0
    var RESERVATION_CHANGE = 1
    const val SHOP_ITEM_SIMPLE = 0
    const val SHOP_ITEM_TWO_ACTIONS = 1
    const val SHOP_ITEM_ONE_ACTION = 2
    const val SHOP_ITEM_NO_ACTION = 3

    object Notice {
        const val NOTIFICATION_NEW_ANNOUNCEMENT = "NOTIFICATION_NEW_ANNOUNCEMENT"
        const val NOTIFICATION_ADMIN_MESSAGE = "NOTIFICATION_ADMIN_MESSAGE"
        const val ISSUE_WELCOME_COUPON = "ISSUE_WELCOME_COUPON"
        const val MY_REVIEW_HAS_LIKE = "MY_REVIEW_HAS_LIKE"
        const val MY_REVIEW_HAS_COMMENT = "MY_REVIEW_HAS_COMMENT"
        const val BOMCHEF_FOLLOWER_START_LIVE = "BOMCHEF_FOLLOWER_START_LIVE"
        const val BOMCHEF_FOLLOWER_SUBMIT_VOD = "BOMCHEF_FOLLOWER_SUBMIT_VOD"
        const val LIVE_RESERVATION_1HOURS_BEFORE = "LIVE_RESERVATION_1HOURS_BEFORE"
        const val USER_MAKE_PAYMENT_PRODUCT = "USER_MAKE_PAYMENT_PRODUCT"
        const val USER_MAKE_CANCEL_ORDER = "USER_MAKE_CANCEL_ORDER"
        const val ORDER_TO_SHIPMENT_ON_IT_WAY = "ORDER_TO_SHIPMENT_ON_IT_WAY"
        const val ORDER_TO_SHIPMENT_COMPLETED = "ORDER_TO_SHIPMENT_COMPLETED"
        const val ORDER_TO_RETURN_EXCHANGE = "ORDER_TO_RETURN_EXCHANGE"
        const val BOMCHEF_REGISTER_NEW_PRODUCT = "BOMCHEF_REGISTER_NEW_PRODUCT"
        const val BOMCHEF_NEW_FOLLOWER = "BOMCHEF_NEW_FOLLOWER"
        const val INQUIRY_REQUEST = "INQUIRY_REQUEST"
        const val INQUIRY_ANSWER = "INQUIRY_ANSWER"
        const val MEMBER_NOT_LOGIN_11_MONTHS = "MEMBER_NOT_LOGIN_11_MONTHS"
        const val MEMBER_NOT_LOGIN_1_YEARS = "MEMBER_NOT_LOGIN_1_YEARS "
    }

    object Order {
        const val INIT = "INIT"
        const val PAYMENT_SUCCESS = "PAYMENT_SUCCESS"
        const val PREPARING = "PREPARING"
        const val SHIPPING = "SHIPPING"
        const val SHIP_COMPLETED = "SHIP_COMPLETED"
        const val CANCELLED = "CANCELLED"
        const val RETURN_REQUEST = "RETURN_REQUEST"
        const val EXCHANGE_REQUEST = "EXCHANGE_REQUEST"
        const val DELETED = "DELETED "
    }

    object Online {
        const val ONLINE = "ONLINE"
        const val IN_CLASS = "IN_CLASS"
    }

    object Announcement {
        const val ANNOUNCEMENT = "ANNOUNCEMENT"
        const val EVENT = "EVENT"
    }

    object ExerciseClass {
        const val INSTANT_MATCHING = "INSTANT_MATCHING"
        const val CLASS_NOW = "CLASS_NOW"
        const val RESERVATION = "RESERVATION"
        const val INIT = "INIT"
        const val MIN_30 = "MIN_30"
        const val MIN_5 = "MIN_5"
        const val AVAILABLE = "AVAILABLE"
        const val NONE = "NONE"
        const val LIVE = "LIVE"
        const val COMPLETE = "COMPLETE"
        const val FORCE_COMPLETE = "FORCE_COMPLETE"
        const val INTERRUPT = "INTERRUPT"
        const val CANCELED_TIME_EXCEEDED = "CANCELED_TIME_EXCEEDED"
        const val CANCELED_PAYMENT_TIMEOUT = "CANCELED_PAYMENT_TIMEOUT"
        const val DELETED = "DELETED"
    }

    object Accrual {
        const val CASH = "CASH"
        const val CASH_CHARGE = "CHARGE"
        const val CASH_USE = "USE"
        const val CASH_USE_CLASS_EXTEND = "USE_CLASS_EXTEND"
        const val CASH_PAID = "PAID"
        const val CASH_PAID_EXTEND = "PAID_EXTEND"
        const val CASH_RESERVATION_REFUND = "RESERVATION_REFUND"
        const val CASH_CLASS_EXTEND_REFUND = "CLASS_EXTEND_REFUND"
        const val CASH_CLASS_REFUND = "CLASS_REFUND"
        const val CASH_WITHDRAW_REQUEST = "WITHDRAW_REQUEST"
        const val CASH_WITHDRAW_REJECT = "WITHDRAW_REJECT"
        const val CASH_FORCE_CLOSE = "FORCE_CLOSE"
        const val POINT = "POINT"
        const val POINT_ADMIN_PROVIDE = "ADMIN_PROVIDE"
        const val POINT_USE = "USE"
        const val POINT_USE_CLASS_EXTEND = "USE_CLASS_EXTEND"
        const val POINT_RESERVATION_REFUND = "RESERVATION_REFUND"
        const val POINT_CLASS_EXTEND_REFUND = "CLASS_EXTEND_REFUND"
        const val POINT_CLASS_REFUND = "CLASS_REFUND"
        const val POINT_SHARE_SNS = "SHARE_SNS"
        const val POINT_CHARGE = "CHARGE"
        const val PENALTY = "PENALTY"
    }

    object TabType {
        const val ITEM_COOKING_GENERAL = "ITEM_COOKING_GENERAL"
        const val ITEM_COOKING_CHEF = "ITEM_COOKING_CHEF"
        const val ITEM_COOKING_CHEF_OTHER = "ITEM_COOKING_CHEF_OTHER"
        const val ITEM_STORE = "ITEM_STORE"
        const val ITEM_STORE_OTHER = "ITEM_STORE_OTHER"
    }

    object DateTime {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
    }

    object UserGender {
        const val UNKNOWN = "UNKNOWN"
        const val MALE = "MALE"
        const val FEMALE = "FEMALE"
    }

    object Configuration {
        const val LANDSCAPE = "LANDSCAPE"
        const val PORTRAIT = "PORTRAIT"
    }

    object CardMethod {
        const val VISA = "카드" // Thẻ
        const val PHONE = "가상계좌" // Ví điện tử
    }

    enum class FilterType {
        SALE_VOLUME, NEWEST, HIGH_LOW_PRICE, LOW_HIGH_PRICE, MOST_POPULAR, BOMCHEF_NAME_ASC, BOMCHEF_NAME_DESC
    }
}