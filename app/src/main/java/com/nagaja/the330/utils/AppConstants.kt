package com.nagaja.the330.utils

object AppConstants {

    const val EXTRA_KEY1 = "EXTRA_KEY1"
    const val EXTRA_KEY2 = "EXTRA_KEY2"
    const val EXTRA_KEY3 = "EXTRA_KEY3"
    const val EXTRA_KEY4 = "EXTRA_KEY4"
    const val EXTRA_KEY5 = "EXTRA_KEY5"
    const val KEY_LIST_DATA = "KEY_LIST_DATA"
    const val KEY_STORIES_LIST_DATA = "KEY_LIST_DATA"
    const val NOTIFI_TYPE = "NOTIFI_TYPE"

    var YES = 0
    var NO = 1

    const val DELIVERY = "DELIVERY"
    const val RESERVATION = "RESERVATION"
    const val PICKUP_DROP = "PICKUP_DROP"

    const val SNS_TYPE_KAKAO = "SNS_TYPE_KAKAO"
    const val SNS_TYPE_NAVER = "SNS_TYPE_NAVER"
    const val SNS_TYPE_GOOGLE = "SNS_TYPE_GOOGLE"
    const val SNS_TYPE_FACEBOOK = "SNS_TYPE_FACEBOOK"
    var ADMIN = "ADMIN"
    var GENERAL = "GENERAL"
    var COMPANY = "COMPANY"

    object Notice {
        const val MY_REVIEW_HAS_LIKE = "MY_REVIEW_HAS_LIKE"
    }

    object Lang {
        const val EN = "en"
        const val PH = "ph"
        const val KR = "kr"
        const val CN = "cn"
        const val JP = "jp"
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


    object Announcement {
        const val ANNOUNCEMENT = "ANNOUNCEMENT"
        const val EVENT = "EVENT"
    }

    object SecondhandCategory {
        const val DIGITAL_DEVICE = "DIGITAL_DEVICE"
        const val HOME_APPLIANCES = "HOME_APPLIANCES"
        const val FURNITURE_INTERIOR = "FURNITURE_INTERIOR"
        const val BABY_PRODUCT = "BABY_PRODUCT"
        const val LIVING_PROCESSED_FOOD = "LIVING_PROCESSED_FOOD"
        const val SPORT_LEISURE = "SPORT_LEISURE"
        const val WOMEN_MISCELLANEOUS_GOOD = "WOMEN_MISCELLANEOUS_GOOD"
        const val WOMEN_CLOTHING = "WOMEN_CLOTHING"
        const val MEN_FASHION = "MEN_FASHION"
        const val MEN_ACCESSORIES = "MEN_ACCESSORIES"
        const val GAME_HOBBIES = "GAME_HOBBIES"
        const val BEAUTY = "BEAUTY"
        const val PET_PRODUCT = "PET_PRODUCT"
        const val BOOK_TICKET_ALBUM = "BOOK_TICKET_ALBUM"
        const val PLANT = "PLANT"
        const val OTHER = "OTHER"
        const val BUY = "BUY"
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