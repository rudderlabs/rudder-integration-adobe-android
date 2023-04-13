package com.rudderstack.android.integrations.adobe;

import com.rudderstack.android.sdk.core.RudderMessage;
import com.rudderstack.android.sdk.core.RudderMessageBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConstants {
    static class Products {
        List<Map<String, Object>> productsList;
        String expectedProducts;
        String productId;

        Products(List<Map<String, Object>> productsList, String expectedProducts, String productId) {
            this.productsList = productsList;
            this.expectedProducts = expectedProducts;
            this.productId = productId;
        }
    }

    public static Products getTwoFullProducts_set1() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "RSCat1");
        product1.put("product_id", "RSPro1");
        product1.put("quantity", "100");
        product1.put("price", 1000.2);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("category", "RSCat2");
        product2.put("product_id", "Pro2");
        product2.put("quantity", 200);
        product2.put("price", "2000.20");

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, "RSCat1;RSPro1;100;100020.0,RSCat2;RSPro1;200;400040.0", "RSPro1");
    }

    public static Products getTwoFullProducts_set2() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "Example category 1");
        product1.put("product_id", "Example product"); // This should be kept same for easy testing purpose
        product1.put("quantity", "1");
        product1.put("price", 3.5);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("category", "Example category 2");
        product2.put("product_id", "Example product"); // This should be kept same for easy testing purpose
        product2.put("quantity", "1");
        product2.put("price", 5.99);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, "Example category 1;Example product;1;3.5,Example category 2;Example product;1;5.99", "Example product");
    }


    // Single product
    public static Products getSingleFullProducts_set1() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "RSCat1");
        product1.put("product_id", "RSPro1");
        product1.put("quantity", "100");
        product1.put("price", 1000.2);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);

        return new Products(products, "RSCat1;RSPro1;100;100020.0", "RSPro1");
    }

    public static Products getSingleFullProducts_set2() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "Example category");
        product1.put("product_id", "Example product");
        product1.put("quantity", "1");
        product1.put("price", 3.5);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);

        return new Products(products, "Example category;Example product;1;3.5", "Example product");
    }

    // Partial products
    public static Products getTwoPartialProducts_set1() {
        Map<String, Object> product1 = new HashMap<>();
//        product1.put("category", "RSCat1");
        product1.put("product_id", "RSPro1");
        product1.put("quantity", "100");
        product1.put("price", 1000.2);

        Map<String, Object> product2 = new HashMap<>();
//        product2.put("category", "RSCat2");
        product2.put("product_id", "Pro2");
        product2.put("quantity", 200);
        product2.put("price", "2000.20");

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, ";RSPro1;100;100020.0,;RSPro1;200;400040.0", "RSPro1");
    }

    public static Products getTwoPartialProducts_set2() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "RSCat1");
        product1.put("product_id", "RSPro1");
//        product1.put("quantity", "100");
//        product1.put("price", 1000.2);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("category", "RSCat2");
        product2.put("product_id", "Pro2");
//        product2.put("quantity", 200);
//        product2.put("price", "2000.20");

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, "RSCat1;RSPro1,RSCat2;RSPro1", "RSPro1");
    }

    public static Products getTwoPartialProducts_set3() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "Example category");
        product1.put("product_id", "Example product");

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);

        return new Products(products, "Example category;Example product", "Example product");
    }

    public static Products getSinglePartialProducts_set4() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("product_id", "Example product");

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);

        return new Products(products, ";Example product", "Example product");
    }

    public static Products getTwoPartialProducts_set5() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "Example category");
        product1.put("product_id", "Example product"); // This should be kept same for easy testing purpose
//        product1.put("quantity", "1");
//        product1.put("price", 3.5);

        Map<String, Object> product2 = new HashMap<>();
//        product2.put("category", "Example category 2");
        product2.put("product_id", "Example product"); // This should be kept same for easy testing purpose
//        product2.put("quantity", "1");
//        product2.put("price", 5.99);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, "Example category;Example product,;Example product", "Example product");
    }

    public static Products getSinglePartialProducts_set6() {
        Map<String, Object> product1 = new HashMap<>();
//        product1.put("category", "Example category");
        product1.put("product_id", "Example product"); // This should be kept same for easy testing purpose
        product1.put("quantity", "1");
        product1.put("price", 6.99);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);

        return new Products(products, ";Example product;1;6.99", "Example product");
    }

    public static Products getTwoPartialProducts_set7() {
        Map<String, Object> product1 = new HashMap<>();
//        product1.put("category", "Example category");
        product1.put("product_id", "Example product"); // This should be kept same for easy testing purpose
        product1.put("quantity", "4");
        product1.put("price", 2.15);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("category", "Example category");
        product2.put("product_id", "Example product"); // This should be kept same for easy testing purpose
        product2.put("quantity", "4");
        product2.put("price", 2.49);

        // Creating a List of Maps
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        return new Products(products, ";Example product;4;8.6,Example category;Example product;4;9.96", "Example product");
    }

    // RudderMessage
    static class RSMessage {
        RudderMessage rudderMessage;
        String expectedUserId;
        String expectedNameProperty;
        String expectedCategoryProperty;

        RSMessage(RudderMessage rudderMessage, String expectedUserId, String expectedNameProperty, String expectedCategoryProperty) {
            this.rudderMessage = rudderMessage;
            this.expectedUserId = expectedUserId;
            this.expectedNameProperty = expectedNameProperty;
            this.expectedCategoryProperty = expectedCategoryProperty;
        }
    }

    public static RSMessage getRudderMessage() {
        return new RSMessage(
                new RudderMessageBuilder()
                        .setProperty(getProperties())
                        .setUserId("Random UserId")
                        .build(),
                "Random UserId",
                "Random Name",
                "RSCat1"
        );
    }

    private static Map<String, Object> getProperties() {
        Map<String, Object> eventProperties = new HashMap<>();
        eventProperties.put("name", "Random Name");
        eventProperties.put("singleItem", getMapItem());

        return eventProperties;
    }

    private static Map<String, Object> getMapItem() {
        Map<String, Object> product1 = new HashMap<>();
        product1.put("category", "RSCat1");
        product1.put("product_id", "RSPro1");
        product1.put("quantity", "100");
        product1.put("price", 1000.2);

        return product1;
    }
}
