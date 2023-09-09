package com.example;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class S3Bucket {

    private AmazonS3 s3client;
    private String bucketName;

    public S3Bucket(String clientRegion, String bucketName) {
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(clientRegion)
                .build();
        this.bucketName = bucketName;
    }

    public void createBucket(String bucketName) {
        if (!(s3client.doesBucketExistV2(bucketName))) {
            // Note that CreateBucketRequest does not specify region. So bucket is
            // created in the region specified in the client.
            s3client.createBucket(new CreateBucketRequest(bucketName));
        }
        // Get location.
        String bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName));
        System.out.println("bucket location = " + bucketLocation);
    }

    public void deleteObject(String keyName, String versionId) {
        try {
            if (versionId == null) {
                s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            } else {
                s3client.deleteVersion(new DeleteVersionRequest(bucketName, keyName, versionId));
            }
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    public void readObject(String key) throws IOException {
        S3Object object = s3client.getObject(new GetObjectRequest(bucketName, key));
        InputStream objectData = object.getObjectContent();
        // Process the objectData stream.
        System.out.println("objectData = " + objectData);
        objectData.close();
    }

    public void listAllObject() {
        System.out.println("Listing objects");
        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
        ListObjectsV2Result result;
        do {
            result = s3client.listObjectsV2(req);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());
    }

    public void generatePresignUrl(String objectKey) {
        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 60; // Add 1 hour.
        expiration.setTime(msec);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL s = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        // Use the pre-signed URL to upload an object.
    }
    public void testKMSkeyUploadObject(String objectKey, String kmsCmkId) throws Exception {
        KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kmsCmkId);
        AmazonS3EncryptionClient encryptionClient = new AmazonS3EncryptionClient(new ProfileCredentialsProvider(), materialProvider,
                new CryptoConfiguration().withKmsRegion(Regions.US_EAST_1))
                .withRegion(Region.getRegion(Regions.US_EAST_1));
        // Upload object using the encryption client.
        byte[] plaintext = "Hello World, S3 Client-side Encryption Using Asymmetric Master Key!" .getBytes();
        System.out.println("plaintext's length: " + plaintext.length);
        encryptionClient.putObject(new PutObjectRequest(bucketName, objectKey,
                new ByteArrayInputStream(plaintext), new ObjectMetadata()));
        // Download the object.
        S3Object downloadedObject = encryptionClient.getObject(bucketName, objectKey);
        byte[] decrypted = IOUtils.toByteArray(downloadedObject.getObjectContent());
        // Verify same data.
        System.out.println(Arrays.equals(plaintext, decrypted));
    }

}
