package com.example;

/**
 * test class
 */
public class App {
    public static void main(String[] args) {
        String clientRegion = "ap-southeast-1";
        String bucketName = "my-first-but-awesome-bucket";
        S3Bucket bucket = new S3Bucket(clientRegion, bucketName);
        String keyName = "test-object.txt";
        bucket.deleteObject(keyName, null);
    }
}
