package org.alfonso.ecommerce.utils;

import com.github.slugify.Slugify;
import org.alfonso.ecommerce.repositories.SlugExistenceRepository;

public class GeneralUtil {
    public static String generateSlug(String content) {
        Slugify slugify = new Slugify();
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
}
