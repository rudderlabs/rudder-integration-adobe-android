package com.rudderstack.android.integrations.adobe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class AdobeIntegrationFactoryTest {

    @Test
    public void convertTwoFullProductsInStringForm_set1() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoFullProducts_set1();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertTwoFullProductsInStringForm_set2() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoFullProducts_set2();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    // Single Products

    @Test
    public void convertSingleFullProductsInStringForm_set1() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getSingleFullProducts_set1();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertSingleFullProductsInStringForm_set2() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getSingleFullProducts_set2();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    // Partial Products
    @Test
    public void convertTwoPartialProductsInStringForm_set1() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoPartialProducts_set1();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertTwoPartialProductsInStringForm_set2() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoPartialProducts_set2();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertTwoPartialProductsInStringForm_set3() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoPartialProducts_set3();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertSinglePartialProductsInStringForm_set4() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getSinglePartialProducts_set4();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertTwoPartialProductsInStringForm_set5() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoPartialProducts_set5();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertSinglePartialProductsInStringForm_set6() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getSinglePartialProducts_set6();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }

    @Test
    public void convertTwoPartialProductsInStringForm_set7() throws JSONException {
        AdobeIntegrationFactory adobeIntegrationFactory = mock(AdobeIntegrationFactory.class);
        TestConstants.Products products = TestConstants.getTwoPartialProducts_set7();
        when(adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList)).thenCallRealMethod();
        when(adobeIntegrationFactory.getProductId(any())).thenReturn(products.productId);

        String productInStringForm = adobeIntegrationFactory.getProductsArrayInStringFormat(products.productsList);

        Assert.assertEquals(products.expectedProducts, productInStringForm);
    }
}
