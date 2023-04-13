package com.rudderstack.android.integrations.adobe;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void getMappedContextValue() {
        TestConstants.RSMessage rsMessage = TestConstants.getRudderMessage();

        Assert.assertEquals(rsMessage.expectedUserId, Utils.getMappedContextValue(".userId", rsMessage.rudderMessage));
        Assert.assertEquals(rsMessage.expectedNameProperty, Utils.getMappedContextValue(".properties.name", rsMessage.rudderMessage));
        Assert.assertEquals(rsMessage.expectedCategoryProperty, Utils.getMappedContextValue(".properties.singleItem.category", rsMessage.rudderMessage));
    }
}
