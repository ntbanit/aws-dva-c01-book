aws polly put-lexicon --name awsLexicon --content file://aws-lexicon.xml --region us-west-2
# Synthesizing speech with custom lexicon in the same region
aws polly synthesize-speech --text 'Hello AWS World!' --voice-id Joanna --output-format mp3 hello.mp3 --lexicon-names="awsLexicon" --region us-west-2

# Trying again against a different Regional API endpoint
aws polly synthesize-speech --text 'Hello AWS World' --voice-id Joanna --output-format mp3 hello-custom.mp3 --lexicon-names="awsLexicon" --region us-east-1

aws polly delete-lexicon --name awsLexicon
aws polly delete-lexicon --name awsLexicon --region eu-west-2