<a href="https://www.twilio.com">
  <img src="https://static0.twilio.com/marketing/bundles/marketing/img/logos/wordmark-red.svg" alt="Twilio" width="250" />
</a>

# Twilio Account Security Quickstart - Twilio Authy and Twilio Verify

A simple Java, Servlets and AngularJS implementation of a website that uses Twilio Authy Two-factor Authentication services to protect all assets within a folder. Additionally, it shows using Twilio Verify for a Phone Verification implementation.

It uses four channels for delivery, SMS, Voice, Soft Tokens, and Push Authentication. You should have the [Authy App](https://authy.com/download/) installed to try Soft Token and Push Notification support.

## Two-Factor Authentication Demo
- URL path "/protected" is protected with both user session and Twilio Two-Factor Authentication
- One Time Passwords (SMS and Voice)
- SoftTokens
- Push Notifications (via polling)

## Phone Verification
- Phone Verification
- SMS or Voice Call

## Setup

### Requirements
1. [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
   installed for your operative system.

2. A [Twilio Account](https://www.twilio.com/)

### Twilio Account Settings

This application should give you a ready-made starting point for writing your own application.
Before we begin, we need to collect all the config values we need to run the application:

| Config Value | Description |
| :----------  | :---------- |
| ACCOUNT_SECURITY_API_KEY  | Create a new Authy application in the [console](https://www.twilio.com/console/authy/). After you give it a name you can view the generated Account Security production API key. This is the string you will later need to set up in your environmental variables.|

### Local Development
1. Clone this repo
   ```bash
   git clone https://github.com/TwilioDevEd/account-security-quickstart-servlets.git
   cd account-security-quickstart-servlets
   ```

1. Set your environment variables.

   ```bash
    cp .env.example .env
   ```
   See [Twilio Account Settings](#twilio-account-settings) to locate the necessary environment variables.

1. Run `./gradlew build`

1. Run the application using Gradle Gretty plugin.

   ```bash
   ./gradlew appRun
   ```
   This will run the embedded Jetty application server that uses port 8080.

1. Navigate to [http://localhost:8080](http://localhost:8080)

### License
- MIT
