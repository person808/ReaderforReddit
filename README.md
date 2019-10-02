### Reader for Reddit

Another Android Reddit Client (WIP)

[Screenshot](https://user-images.githubusercontent.com/6396431/63486948-4b408400-c45e-11e9-8c0c-9c08c3c7eefe.png)

## Building

To build the app, you need to sign up to use the Reddit API [here](https://www.reddit.com/prefs/apps).
Make sure to select "installed app" as the app type and enter a redirect uri. Next create a `gradle.properties`
file. While the project already has this file, it is tracked by git so I recommend creating one at
`~/.gradle/gradle.properties`. The format of the file should be like this:

```
# Replace the client id with the one reddit gives you
ReaderForReddit_ClientId="xxxxxxxxxxxxxx"
# This must match the redirect uri you provide reddit. https://localhost:8080 is a good value to use
ReaderForReddit_RedirectUri="https://localhost:8080"
# This must match the host for your redirect uri
ReaderForReddit_RedirectHost="localhost"
```

Once this file is created, you should be able to build and run the app provided the values are correct.

Additional Resources: [Where to put the gradle.properties file?](https://stackoverflow.com/questions/30333837/where-to-put-the-gradle-properties-file)
