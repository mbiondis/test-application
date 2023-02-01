# Test Application

Use the following ADB commands to showcase deep linking. Remember to force close the application after each step with this command ```adb shell am force-stop com.example.testapplication```

```bash
// Open the main screen
adb shell am start -a android.intent.action.VIEW -d "ppm-tech//open" com.example.testapplication

// Open a random link
adb shell am start -a android.intent.action.VIEW -d "https://ppm-tech.app.link/?hello=there" com.example.testapplication

// Open an explicit link
adb shell am start -a android.intent.action.VIEW -d "https://ppm-tech.app.link/?deep_link_test=other" com.example.testapplication

// Open a deep link
adb shell am start -a android.intent.action.VIEW -d "https://ppm-tech.app.link/xtZqd2an4wb" com.example.testapplication
```
