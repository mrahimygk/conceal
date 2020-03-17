# conceal
Concealing WAVE audio files inside images

## Description
This application is useful for hiding wave audio data inside the least significant bits of an image. 
The resulting image can be shared and received images can be parsed by this app.

## Sharing Note
Some social media applications like Telegram and Instagram change the content of images and re-compress them before sending. Sharing the resulting image to those applications would probably remove audio data that is concealed inside the image. We suggest sending the resulting image on Telegram as un-compressed file instead of photo.
When no solution is available, you can upload your image to an image hosting website for sharing. Sending them as email attachment is known to keep the original data. Removing any meta-data from the image does not break the concealing process.
