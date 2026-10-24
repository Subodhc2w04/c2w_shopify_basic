package com.core2web.config;

import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
    static {
        initializeCloudinary();
    }
    public static Cloudinary cloudinary;
    public static void initializeCloudinary() {
         cloudinary = new Cloudinary(Map.of(
                "cloud_name", "dsl3wjwp",
                "api_key", "674535288376598",
                "api_secret", "LVn-mq0vNJsxQx1XskMbh6nCtsI"));

    }
    public static Cloudinary getCloudinaryInstance() {
        return cloudinary;
    }
}
