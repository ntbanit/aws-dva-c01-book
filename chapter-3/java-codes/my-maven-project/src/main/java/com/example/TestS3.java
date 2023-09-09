package com.example;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

public class TestS3 {

    public static void createBucket(String bucketName){
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.US_WEST_1));
        if (!(s3client.doesBucketExist(bucketName))) {
            // Note that CreateBucketRequest does not specify region. So bucket is
            // created in the region specified in the client.
            s3client.createBucket(new CreateBucketRequest(bucketName));
        }
        // Get location.
        String bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName));
        System.out.println("bucket location = " + bucketLocation);
    }

}
