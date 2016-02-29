# Project 3 - *MySimpleTweets*

**MySimpleTweets** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **16** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] The app includes **all required user stories** from Week 3 Twitter Client
* [x] User can **switch between Timeline and Mention views using tabs**
  * [x] User can view their home timeline tweets.
  * [x] User can view the recent mentions of their username.
* [x] User can navigate to **view their own profile**
  * [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [x] User can **click on the profile image** in any tweet to see **another user's** profile.
 * [x] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * [x] Profile view includes that user's timeline
* [x] User can [infinitely paginate](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView) any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** features are implemented:

* [x] User can view following / followers list through the profile
* [ ] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [x] When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* [x] User can **"reply" to any tweet on their home timeline**
  * [x] The user that wrote the original tweet is automatically "@" replied in compose
* [x] User can click on a tweet to be **taken to a "detail view"** of that tweet
 * [x] User can take favorite (and unfavorite) or retweet actions on a tweet
* [x] Improve the user interface and theme the app to feel twitter branded
* [x] User can **search for tweets matching a particular query** and see results

The following **bonus** features are implemented:

* [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [x] Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce view boilerplate.
* [ ] User can view their direct messages (or send new ones)

The following **additional** features are implemented:

* [x] Added red color to the counter when the length exceeds the maximum
* [x] Added close button in the compose dialog
* [x] Disable "Tweet" button if characters exceeded
* [x] Added creation date instead of relative date in detail screen
* [x] Added RTs, Favs and Media image in Detail Activity
* [x] Added no network detection
* [x] User can see others followers/following recursively
* [x] Backwards navigation implemented in Toolbar

* [ ] User can **select "reply" from detail view to respond to a tweet**
* [ ] User can watch embedded video within the tweet

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://s3-eu-west-1.amazonaws.com/chezlui.freelancer/codepath/Simpletweets.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

The use of ActiveAndroid with other libraries forces to tune the classes used. For instance for GSON the @Expose annotation must be used, and for Parceler the class that we want to be serialized must be explicitly indicated: @Parcel(analyze={Tweet.class}).

I had a funny error sending to the REST api a "id" field with a value that Twitter was continuosly rejecting me. After a while I detected the "id" field was not from my Tweet class but from the ActiveAndroid generated class.



## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Butterknife](https://github.com/JakeWharton/butterknife) - View "injection" library for Android
- [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling
- [GSON]
- [RecyclerView - Animators](https://github.com/wasabeef/recyclerview-animators) - An Android Animation library which easily add itemanimator to RecyclerView items
- [Active Android] (https://github.com/pardom/ActiveAndroid) - Active record style SQLite persistence for Android http://www.activeandroid.com
- [Parceler] (https://github.com/johncarl81/parceler) - Android Parcelables made easy through code generation. http://parceler.org

## License

    Copyright [2016] [Jose Luis Martin Romera]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
