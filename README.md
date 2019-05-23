# NΞWZY

>NΞWZY is an android app that brings the latest headlines on different categories from all over the world to user's mobile through [News API](https://newsapi.org/).

## Project Setup

- Register on newsapi.org to generate an api key.
- Locate the `.gradle` folder in your home directory. Usually it can be found at:
  - Windows: C:\Users\<username>\.gradle
  - Mac: /Users/\<username>/.gradle
  - Linux: /home/\<username>/.gradle
- Open a file called `gradle.properties` (just create it if there isn’t any).
- Add following line to the file

```properties
Newsy_NewsApiKey="<your-api-key>"
```


## Sreenshots

|     |     |     |
|:---:|:---:|:---:|
| Launch Screen |
| ![img](Screenshots\screener_1557235179588.png) |
| Home Screen |
| ![img](Screenshots\screener_1557235428943.png) Card | ![img](Screenshots\screener_1557235461931.png) List |
| Search Screen |
| ![img](Screenshots\screener_1557235954696.png) | ![img](Screenshots\screener_1557236233734.png) | ![img](Screenshots\screener_1557235982255.png) |
| Bookmarks Screen |
| ![img](Screenshots\screener_1557236337663.png) | ![img](Screenshots\screener_1557236384379.png) | ![img](Screenshots\screener_1557236415373.png) |
| Settings Screen |
| ![img](Screenshots\screener_1557236457653.png) | ![img](Screenshots\screener_1557236487520.png) |
| Chrome Custom Tabs |
| ![img](Screenshots\screener_1557236630410.png) |
| Internet State Handling |
| ![img](Screenshots\screener_1557236849551.png) | ![img](Screenshots\screener_1557236803961.png) |

## Features

- Bookmark headlines to read later
- Offline caching of data for lesser network consumption
- Search through millions of articles from over 30,000 large and small news sources and blogs.
- Supports news from over 52 countries and regions throughout the world.
- Built on latest development tools and design patterns like Android Architecture Components (Viewmodels, Livedata, Room).
- Interface customisation available as per user preference.
- Chrome powered Custom Tabs
- Notifications (Soon)
- Dark Theme (Soon)

## Acknowledgments

- [ButterKnife](https://github.com/JakeWharton/butterknife)
- [Custom Tabs](https://github.com/GoogleChrome/custom-tabs-client)
- [GSON](https://github.com/google/gson)
- [Leak Canary](https://github.com/square/leakcanary)
- [Material Componenta](https://github.com/material-components/material-components-android)
- [Picasso](https://github.com/square/picasso)
- [Retrofit](https://github.com/square/retrofit)

## Previous Version

https://github.com/Deepamgoel/Newsy/tree/legacy

## License

   Copyright 2019 Deepam Goel

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
