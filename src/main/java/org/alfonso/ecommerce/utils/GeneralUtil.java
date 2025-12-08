package org.alfonso.ecommerce.utils;

import com.github.slugify.Slugify;

public class GeneralUtil {
    public static String generateSlug(String content) {
        Slugify slugify = new Slugify();
        return slugify.slugify(content);
    }
}
