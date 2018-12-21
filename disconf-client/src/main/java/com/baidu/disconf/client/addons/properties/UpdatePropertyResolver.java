package com.baidu.disconf.client.addons.properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

public class UpdatePropertyResolver extends PropertyPlaceholderConfigurer implements PropertyPlaceholderHelper.PlaceholderResolver {

    private Properties props = null;

    @Override
    public String resolvePlaceholder(String placeholderName) {
        return this.resolvePlaceholder(placeholderName,SYSTEM_PROPERTIES_MODE_FALLBACK);
    }

    String parseString(String strVal) {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
                placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
        return helper.replacePlaceholders(strVal, getInstance());
    }

    private String resolvePlaceholder(String s ,int mode){
        return super.resolvePlaceholder(s,props,mode);
    }

    void setProps(Properties props){
        this.props = props;
    }

    public static UpdatePropertyResolver getInstance() {
        return UpdatePropertyResolverInner.updatePropertyResolver;
    }

    private static class UpdatePropertyResolverInner{

        private static UpdatePropertyResolver updatePropertyResolver = new UpdatePropertyResolver();
    }

}
