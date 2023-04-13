package com.rudderstack.android.sample.kotlin

import com.rudderstack.android.sdk.core.RudderProperty
import org.json.JSONObject

class ECommerceEvents {
    fun productViewed() {
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "product viewed",                       //prodView
            getRudderPropertyWithProductsArray()
        )

        /*
        data: {
        "action": "prodView",
        "contextdata": {
            "anon": "82bb43ecdb842c72",
            "custom_1": "Custom Track call 5",
            "custom_3": "1237774444478899",
            "custom_2": "{\"name\":\"RStest_name2\",\"age\":27}",
            "purchaseid": "3001",
            "custom_4": "2.02022266656E9",
            "&&events": "prodView",
            "rating": "76",
            "channel": "mobile",
            "currency": "USD",
            "url": "https:example5.com",
            "&&products": "RSCat5;RSmonopoly5;200;1000040.0,RSCat25;games5;700;5600140.0"
            }
        }
         */
    }

    fun productAdded() {
//        MainApplication.rudderClient.track("Product Added")     //scAdd

        MainApplication.rudderClient.track("Product Added", getRudderPropertyWithoutProduct())
    }

    fun productRemoved() {
        MainApplication.rudderClient.track("Product Removed")

        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "Product Removed",       //scRemove
            getRudderPropertyWithProductAtRoot()
        )
    }

    fun orderCompleted() {
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "order completed",                      //purchase
            getRudderPropertyWithProductsArray()
        )
    }

    fun cartViewed() {
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "Cart Viewed",                          //scView
            getRudderPropertyWithProductsArray()
        )
    }

    fun checkoutStarted() {
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "checkout started",                     //scCheckout
            getRudderPropertyWithProductsArray()
        )
    }

    fun cartOpened() {
        //E-Commerce track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with products.
        MainApplication.rudderClient.track(
            "cart opened",                     //scOpe
            getRudderPropertyWithProductsArray()
        )
    }

    fun identify() {
        MainApplication.rudderClient.identify("Adobe Android userId")
    }

    fun customTrackWithProperties() {
        MainApplication.rudderClient.track(
            "Custom Track",    // 'Custom Track' is mapped at RS dashboard to 'custom_track_call'
            RudderProperty()
                //Prefix + extraProperties
                .putValue("1", "Custom Track call 1")    //Mapping to adobe as: custom_1
                .putValue("2", true)                     // Prefix is configured at the dashboard: 2 -> custom_2
        )

        //Custom track with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        MainApplication.rudderClient.track(
            "Custom Track",                      // 'Custom Track' is mapped at RS dashboard to 'custom_track_call'
            getCustomProperty()
        )
    }

    fun customTrackWithoutProperties() {
        MainApplication.rudderClient.track("Custom Track")      // 'Custom Track' is mapped at RS dashboard to 'custom_track_call'
    }

    fun screen() {
        MainApplication.rudderClient.screen(
            "screen_call",
            RudderProperty()
                //Prefix + extraProperties
                .putValue("1", "Custom Track call 1")       // Prefix is configured at the dashboard: 1 -> custom_1
                .putValue("2", false)                       // Prefix is configured at the dashboard: 2 -> custom_2  - value is: false
        )
    }

    fun reset() {
        MainApplication.rudderClient.reset()
    }

    fun flush() {
        MainApplication.rudderClient.flush()
    }

    private fun getRudderPropertyWithProductsArray(): RudderProperty {
        return RudderProperty()
            //Prefix + extraProperties
            .putValue("1", "Custom Track call 5")        // Prefix is configured at the dashboard: 1 -> custom_1
            .putValue(
                "2",
                getAnswer1()
            )                                            // Prefix is configured at the dashboard: 2 -> custom_2  - value is: answer1
            .putValue("3", 1237774444478899)             // Prefix is configured at the dashboard: 3 -> custom_3
            .putValue("4", 2020222666.56)                // Prefix is configured at the dashboard: 4 -> custom_4
            .putValue("order_id", "3001")                // Mapped to: order_id -> purchaseid
            //Custom mapped properties
            .putValue("curr", "USD")                     // Mapped at dashboard to: curr -> currency
            .putValue("rat", 76)                         // Mapped at dashboard to: rat -> rating
            .putValue("URL", "https:example5.com")       // Mapped at dashboard to: URL -> url
            //Products
            .putValue("products", getProducts())

        /*
        "contextdata": {
            "anon": "<anonymousId>",
            "custom_1": "Custom Track call 5",
            "custom_3": "1237774444478899",
            "custom_2": "{\"name\":\"RStest_name2\",\"age\":27}",
            "purchaseid": "3001",
            "custom_4": "2.02022266656E9",
            "&&events": "<event_name>",
            "rating": "76",
            "channel": "mobile",
            "currency": "USD",
            "url": "https:example5.com",
            "&&products": "RSCat5;RSmonopoly5;200;1000040.0,RSCat25;games5;700;5600140.0"
        }
         */
    }

    private fun getRudderPropertyWithProductAtRoot(): RudderProperty {
        return RudderProperty()
            //Prefix + extraProperties
            .putValue("1", "Custom Track call 2")       // Prefix is configured at the dashboard: 1 -> custom_1
            .putValue(
                "2",
                getAnswer1()
            )                                           // Prefix is configured at the dashboard: 2 -> custom_2  - value is: answer1
            .putValue("3", 12345622)                    // Prefix is configured at the dashboard: 3 -> custom_3
            .putValue("4", 20222.56)                    // Prefix is configured at the dashboard: 4 -> custom_4
            //Custom mapped properties
            .putValue("curr", "INR")                    // Mapped at dashboard to: curr -> currency
            .putValue("rat", 68)                        // Mapped at dashboard to: rat -> rating
            .putValue("URL", "https:example2.com")      // Mapped at dashboard to: URL -> url
            //Product at root
            .putValue("id", 12.45)                      // ProductIdentifier is set to: id - anyone of the id/sku/name
            .putValue("name", "apple")                  // ProductIdentifier is set to: name - anyone of the id/sku/name
            .putValue("sku", "etc")                     // ProductIdentifier is set to: sku - anyone of the id/sku/name
            .putValue("category", "RSBigBasket")
            .putValue("quantity", 50)
            .putValue("price", 500)
            .putValue("order_id", "3001")               // Mapped to: order_id -> purchaseid

        /*
        {
            "action": "<event_name>",
            "contextdata": {
                "anon": "<anonymousId>",
                "custom_1": "Custom Track call 2",
                "custom_3": "12345622",
                "custom_2": "{\"name\":\"RStest_name2\",\"age\":27}",
                "purchaseid": "3001",
                "custom_4": "20222.56",
                "custom_id": "12.45",
                "rating": "68",
                "channel": "mobile",
                "url": "https:example2.com",
                "custom_sku": "etc",
                "&&events": "scRemove",
                "currency": "INR",
                "&&products": "RSBigBasket;apple;50;25000.0"
            }
        }
         */
    }

    private fun getRudderPropertyWithoutProduct(): RudderProperty {
        //E-Commerce event with 'custom mapped properties' & 'prefix + extraProperties' (prefix is set to: "custom_")
        //with only product Identifier set as: "ID/SKU/Name"
        return RudderProperty()
            //Prefix + extraProperties
            .putValue("1", "Custom Track call 2")        // Prefix is configured at the dashboard: 1 -> custom_1
            .putValue("2", false)                        // Prefix is configured at the dashboard: 2 -> custom_2  - value is: false
            .putValue("3", 12345622)                     // Prefix is configured at the dashboard: 3 -> custom_3
            .putValue("4", 20222.56)                     // Prefix is configured at the dashboard: 4 -> custom_4
            .putValue("order_id", "3001")                // Mapped to: order_id -> purchaseid
            //Custom mapped properties
            .putValue("curr", "INR")                     // Mapped at dashboard to: curr -> currency
            .putValue("rat", 68)                         // Mapped at dashboard to: rat -> rating
            .putValue("URL", "https:example2.com")       // Mapped at dashboard to: URL -> url
//            .putValue("id", 12.45)                       // ProductIdentifier is set to: id - anyone of the id/sku/name
//            .putValue("name", "apple")                   // ProductIdentifier is set to: name - anyone of the id/sku/name
//            .putValue("sku", "etc")                      // ProductIdentifier is set to: sku - anyone of the id/sku/name
    }

    private fun getCustomProperty(): RudderProperty {
        return RudderProperty()
            //Prefix + extraProperties
            .putValue("1", "Custom Track call 99")          // Prefix is configured at the dashboard: 1 -> custom_1
            .putValue(
                "2",
                "5678"
            )                                               // Prefix is configured at the dashboard: 2 -> custom_2
            .putValue("3", 1555566667777)                   // Prefix is configured at the dashboard: 3 -> custom_3
            .putValue("4", 200077.56)                       // Prefix is configured at the dashboard: 4 -> custom_4
            //Custom mapped properties
            .putValue("curr", "INR")                        // Mapped at dashboard to: curr -> currency
            .putValue("rat", 80)                            // Mapped at dashboard to: rat -> rating
            .putValue("URL", "https:example_custom.com")    // Mapped at dashboard to: URL -> url
    }

    private fun getAnswer1(): JSONObject {
        val answer1 = JSONObject("""{"name":"RStest_name2", "age":27}""")
        return answer1
    }

    private fun getProducts(): List<Map<String, Any>> {
        val map = mapOf(
            "product_id" to "RSpro5",
            "name" to "RSmonopoly5",
            "price" to 5000.2,
            "quantity" to "200",
            "category" to "RSCat5"
        )
        val map1 = mapOf(
            "product_id" to "pro5",
            "name" to "games5",
            "price" to "8000.20",
            "quantity" to "700",
            "category" to "RSCat25"
        )
        //Special case where productIdentifier is missing:
//        val map1= mapOf("price" to "8000.20","quantity" to "700", "category" to "RSCat25")
        val list = listOf(map, map1)
        return list
    }

}