#Hello World but in a fancy way 
import boto3
#Implicit Client Configuration
polly = boto3.client('polly')
result = polly.synthesize_speech(Text='Hello World!',
								OutputFormat='mp3',
								VoiceId='Aditi')
# Save the Audio from the response
audio = result['AudioStream'].read()
with open("helloworld.mp3","wb") as file:
	file.write(audio)