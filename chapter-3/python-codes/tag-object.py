import boto3

# Initialize the S3 client
s3 = boto3.client('s3')

# Specify the S3 bucket name and object key
bucket_name = 'my-first-but-awesome-bucket'
object_key = 'test-object.txt'

# Specify the tags you want to set
tags = [
    {
        'Key': 'Project',
        'Value': 'Blue'
    },
    {
        'Key': 'Classification',
        'Value': 'PHI'
    }
]

# Set the tags for the S3 object
s3.put_object_tagging(
    Bucket=bucket_name,
    Key=object_key,
    Tagging={'TagSet': tags}
)

print(f'Tags set for object {object_key} in bucket {bucket_name}')