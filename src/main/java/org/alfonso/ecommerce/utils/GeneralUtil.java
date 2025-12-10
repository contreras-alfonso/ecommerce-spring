package org.alfonso.ecommerce.utils;

import com.github.slugify.Slugify;
import org.alfonso.ecommerce.repositories.SlugExistenceRepository;

import java.util.UUID;

public class GeneralUtil {

    private static final Slugify slugify = new Slugify();

    public static String generateSlug(String content) {
        return slugify.slugify(content);
    }

    public static <T extends SlugExistenceRepository> String createUniqueSlug(String baseName, T repository) {
        String baseSlug = generateSlug(baseName);

        String finalSlug = baseSlug;
        int count = 1;

        while (repository.existsBySlugIgnoreCase(finalSlug)) {
            finalSlug = baseSlug + "-" + count;
            count++;
        }
        return finalSlug;
    }

    public static boolean isUuid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
