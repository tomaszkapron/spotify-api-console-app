# spotify-api-console-app
Simple music advisor app, where I learnt how to work with APIs and OAuth2


## Table of Contents
1. [Description](#description)
2. [Installation](#installation)
3. [Sample output](#sample-output)
4. [What have I learnt](#what-have-i-learnt)

## Description
This is a project where I learnt how to work with WEB API's and to work with OAuth2 protocol. This application connects to Spotify account throught API, and let you interact with basic endpoints, providing you links and basic information about your account.

## Installation
Clone repository
```
  git clone https://github.com/tomaszkapron/spotify-api-console-app
```

Build and run (If working on Windows use gradlew.bat scripts instead)
```
  gradlew build
  gradlew run
```

Before first run of the program, clientID and clientSecret needs to be provided  in Config.java file.

**NOTE:** ClientSecret should not be keept as a plaintext in the code. It is done for simplicity and provided secret is not valid.

## Usage

 The following commands can be used to navigate in console:

- new - a list of new albums with artists and links on Spotify
- featured - a list of Spotify-featured playlists with their links fetched from API
- categories - a list of all available categories on Spotify (just their names)
- playlists <CATEGORY_NAME> - where CATEGORY_NAME is the name of category. The list contains playlists of this category and their links on Spotify
- auth - perform user authetication **FIRST COMMAND TO RUN AS IT MAKES ALL THE OTHER COMMANDS AVAILABLE**
- next - navigates to next page
- prev -navigates to previous page
- exit - shuts down the application

## Sample output
```
> auth
use this link to request the access code:
https://accounts.spotify.com/authorize?client_id=7217f2b1d3dc44cb8222bbb9c4525d83&redirect_uri=http://localhost:8080&response_type=code
waiting for code...
code received
making http request for access_token...
response:
{"access_token":"BQCWhSYNebQaGrwalQ9QEE7CxZSIjc2bP-FxYWyAwdbcB9G9FQG6Jk31Oevl9gOTgdvwZayGX0Qld4pALuG45GbUOSI9-cKekivxjmqeDek5w3XkFj-6BfqxfRhbLVdyO-hIesSWNcBvgGAXLp5QucsAPdiPOHPJy3506kNhQY-mzsnl5wP79wL4u3Fm6lst4Za3hA","token_type":"Bearer","expires_in":3600,"refresh_token":"AQBp1f1RkWHlAb2XIsHW8m0tmDRETXFdfiEsUuBnqG5MqcUK5vUT-6lIEGMVh9Av9sUZVy3kQqIRRWwp6d28b1tNpKp3zrhACrSrXxNLHJCvT70wzR5UrwI0fQpdqYB0TSs"}
> categories
lower: 0 upper: 5
Top Lists
Pop
Hip-Hop
Dance/Electronic
Rock
---PAGE 1 OF 4---
> next
lower: 5 upper: 10
Mood
Latin
French Pop
Indie
Alternative
---PAGE 2 OF 4---
> new
lower: 0 upper: 5
Special
[Lizzo]
https://open.spotify.com/album/1NgFBv1PxMG1zhFDW1OrRr

Gemini Rights
[Steve Lacy]
https://open.spotify.com/album/3Ks0eeH0GWpY4AU20D5HPD

Gangsta Art
[Yo Gotti, Moneybagg Yo, CMG The Label]
https://open.spotify.com/album/0mX7631qrFwwcnuRzuPpWU

Beatopia
[beabadoobee]
https://open.spotify.com/album/2rhNQbqRNxiNQkDXTffe1V

Summertime Blues
[Zach Bryan]
https://open.spotify.com/album/2qPki6xBkJ1Mbra43t7hnA

---PAGE 1 OF 4---
> playlists Mood
category: mood
lower: 0 upper: 5
Letni power
https://open.spotify.com/playlist/37i9dQZF1DXdmgtdl82XHM

Letni chill
https://open.spotify.com/playlist/37i9dQZF1DXakrXW5YU9SI

Bardzo dobry dzie�
https://open.spotify.com/playlist/37i9dQZF1DX4pq3ejIlJu2

Rap motywacja
https://open.spotify.com/playlist/37i9dQZF1DXa3d3ljLBxSG

... ale najpierw kawa
https://open.spotify.com/playlist/37i9dQZF1DX5T2mzbF9W6j

---PAGE 1 OF 4---

```
## What have I learnt
* How to work with WEB API
* OAuth2.0 protocol
* Basics of MVC architecture
