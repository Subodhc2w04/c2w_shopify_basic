package com.core2web.controller;

import java.io.File;
import java.util.Map;

import com.core2web.config.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


public class ImageUploadController {
    public static String imageUpload(File file) {

        Cloudinary cloudinary = CloudinaryConfig.getCloudinaryInstance();

        try {
            Map<String, Object> result = cloudinary
            .uploader()
            .upload(
                file, 
                ObjectUtils.asMap("resource_type", "image"));
            System.out.println(result);
            String url = String.valueOf(result.get("secure_url"));
            System.out.println(url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
