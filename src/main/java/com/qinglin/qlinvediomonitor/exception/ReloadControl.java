package com.qinglin.qlinvediomonitor.exception;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname ReloadControl
 * @Description
 * @date 2023/4/11 16:27
 */
public class ReloadControl {

    /** Always reload resources */
    static class Always extends ResourceBundle.Control {
        private boolean needsReload = true;

        @Override
        public long getTimeToLive(String baseName, Locale locale) {
            if (needsReload) {
                return 0;
            }
            return ResourceBundle.Control.TTL_NO_EXPIRATION_CONTROL;
        }

        @Override
        public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
            // Do not cache, always reload
            return true;
        }

    }
}
