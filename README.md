Original App Design Project - README Template
===

# LitTalk APP

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Group Members
1. Lilian Ofik
2. Alexander Urena
3. Micheal Giordano

## Overview
### Description
This is a social media app that allows for users to posts images, reviews and descriptions of books they have read and view the posts of others. This app allows the user to login and create an account. An API will be called on and used to display book information relevant to the app.
Google Books api endpoint - https://www.googleapis.com/books/v1
### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Photo & Video / Social
- **Mobile:** Website is view only, uses camera, mobile first experience.
- **Story:** Allows users to share their current reads or favorite books by posting pictures of the books. Allows users to keep in touch with the latest books, genres, etc. in the reading world.
- **Market:** Anyone that enjoys reading novels/books will enjoy this app. This app makes it easier for people to choose which books to purchase, this makes the users engage with the relevant content of the app.
- **Habit:** Users can post all day at any time of the day. Users can explore endless posts of books, reviews and ratings that they wish to see and for any good reader this will certainly be a habit forming app.
- **Scope:** This app is designed to show users the ratings of books as well as the most popular books in their area (within 1 mile). This will require an API that will provide information on books necessary for the success of the app, but asides from that, it is a relatively feasible app to build.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can register a new account
* User can login
* User can create a post 
* User can scroll through their feed and view others posts 
* User can view their own posts on their feed
* User can view posts on their feed in detail by tapping on them.
* User can view the popular books in their area
* User can view the books being read within 1 mile of their location

**Optional Nice-to-have Stories**
* When the user agrees to allow Google Maps access their location they will also see other people who have added books in their location. 
* Book suggestions by people you follow will be displayed to the users.
* Improve the user interface through styling and coloring

### 2. Screen Archetypes

* Login
   * User can login
* Register
   * User can register a new account
* Stream - User can scroll through important resources in a list
    * User can scroll through their feed and view others posts as well as theirs
* Creation
    * User can create posts
* Detail - User can view the specifics of a particular resource
    * User can view posts on their feed in detail by tapping on them.
* Profile 
    * User can view their own posts on their feed
* Search
    * User can search for other users

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Stream
* Profile
* Create
* Search

**Flow Navigation** (Screen to Screen)

* Login
   * Stream
* Register
   * Stream
* Stream - User can scroll through important resources in a list
    * Detail
* Creation
    * Stream
* Detail - User can view the specifics of a particular resource
    * Stream
* Profile 
    * Stream
    * Detail
* Search
    * Stream
    * Profile



## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://imgur.com/k8wJiiw.png" width=600>

### [BONUS] Digital Wireframes & Mockups
https://www.figma.com/file/MeFya5Xa4V1yQYh52lLreR/ALM-Book-Project?node-id=14%3A7

### [BONUS] Interactive Prototype
https://imgur.com/a/81lnKd5.gif
<img src="group project vid.gif">

## Schema 

### Models

User

| Property | Type | Description |
| -------- | -------- | -------- |
| Username | String     | Unique id for user login |
| password | String     | Secure password for user login |
| Email    | String     | Email address to receive correspondence or login |

Post

| Property | Type | Description |
| -------- | -------- | -------- |
| objectID | String   | Unique id for user post|
| image    | File     | 	image that user posts |
| caption  | String |  caption by author |
| likesCount | Number | number of likes for the post|
| createdAt  |DateTime|date when post is created|
| author     |Pointer to User	| 	image author |

Comments
| Property | Type | Description |
| -------- | -------- | -------- |
| objectID | String   | Unique id for user post|
| author | Pointer to User | Name of author that posted comment|
| likesCount | Number | number of likes for the post|
| createdAt  |DateTime|date when post is created|
| caption  | String |  comment made by author |

Likes
| Property | Type | Description |
| -------- | -------- | -------- |
| author | Pointer to User | Name of author that liked post|

Follows
| Property | Type | Description |
| -------- | -------- | -------- |
| author | Pointer to User     | name of user that followed |
| image    | File     | 	image of user that followed |
| followersCount | Number | number of followers for the user|




### Networking

* Login
   * (Read/GET) Query information from logged in user object
    ```
    ParseUser.logInInBackground("Username", "SecurePassword", new LogInCallback() {
      public void done(ParseUser user, ParseException e) {
        if (user != null) {
          // Hooray! The user is logged in.
        } else {
          // Signup failed. Look at the ParseException to see what happened.
        }
      }
    });
    ```
* Logout
    * (Update/PUT) Log user out of the app
    ```
    ParseUser.logOut();
    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    ```

* Register
   * (Create/POST) Create a new user object

    ```
    ParseUser user = new ParseUser();
    user.setUsername("my name");
    user.setPassword("my pass");
    user.setEmail("email@example.com");

    // other fields can be set just like with ParseObject

    user.signUpInBackground(new SignUpCallback() {
      public void done(ParseException e) {
        if (e == null) {
          // Hooray! Let them use the app now.
        } else {
          // Sign up didn't succeed. Look at the ParseException
          // to figure out what went wrong
        }
      }
    }); 
    ```
* Stream - User can scroll through important resources in a list
    * (Read/GET) Query all posts from accounts user follows
    ```    
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
    query.whereContainsAll(ParseUser.getCurrentUser();, isFollowing);
    query.findInBackground(new FindCallback<ParseObject>() {
        public void done(List<ParseObject> posts, ParseException e) {
            if (e == null) {
                Log.d("Posts", "Retrieved " + posts.size() + " posts");
            } else {
                Log.d("Posts", "Error: " + e.getMessage());
            }
        }
    });
    ```

* Creation
    * (Create/POST) Create a new post
    ```
    ParseObject myPost = new ParseObject("Post");
    
    myPost.put("username", userID); 
    myPost.put("description", content);
    myPost.put("timeStamp", currentTime);
    myPost.put("likes", 0);
    
    // Adds private write access to user and read access to all other users
    ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
    postACL.setPublicReadAccess(true);
    myPost.setACL(postACL);
    myPost.saveInBackground();

    ```
    * (Create/POST) Create a new comment on a post
    ```
    ParseObject myComment = new ParseObject("Comment");
    myComment.put("username", userID); 
    myComment.put("description", content);
    myComment.put("timeStamp", currentTime);
    myComment.put("likes", 0);
    //Create a relation between the post and the comment
    myComment.put("post", ParseObject.createWithoutData("Post", postObjectID));
    myComment.saveInBackground();
    
    ```
    * (Create/POST) Create a new like on a post
    ```
    Parse.Cloud.define("like", async request => {
  var post = new Parse.Object("Post");
  post.id = request.params.postId;
  post.increment("likes");
  await post.save(null, { useMasterKey: true })
    });

    ```
    * (Delete) Delete existing like
    ```
    Parse.Cloud.define("like", async request => {
  var post = new Parse.Object("Post");
  post.id = request.params.postId;
  post.decrement("likes");
  await post.save(null, { useMasterKey: true })
    });

    ```
    * (Delete) Delete existing post 
    ```
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
    query.getInBackground("PostId", new GetCallback<ParseObject>() {
    public void done(ParseObject object, ParseException e) {
    if (e == null) {
      // Is this the post you were looking for
    } else {
      // something went wrong
    }
    }
    myObject.remove("Post");
    myObject.saveInBackground();
    });
    ```
    * (Delete) Delete existing comment
    ```
    ParseQuery<ParseObject>
    ParseQuery.getInBackground("comment");
    relation.remove(comment);
    myObject.saveInBackground();
    ```

* Detail - User can view the specifics of a particular resource
    * (READ/GET) Post information including like comments, etc. 
    ```
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
    query.getInBackground(postObjectID, new GetCallback<ParseObject>() {
      public void done(ParseObject object, ParseException e) {
        if (e == null) {
        // Retrieve data using get methods of each data type
          String user = object.getString("username");
          String description = object.getString("description");
          String timestamp = object.getString("timestamp");
          int likes = object.getString("likes");
        } else {
          // something went wrong
        }
      }
    });
    ```
* Profile 
    * (Read/GET) Query logged in user object
    ```
    ParseQuery<ParseObject> query = ParseQuery.getQuery("ObjectId");
    query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
    public void done(ParseObject object, ParseException e) {
        if (e == null) {
          // This is what we found
        } else {
          // something went wrong
        }
    }
    });
    ```
    * (Update/PUT) Update user profile image
    ```
    ParseQuery<ParseObject> query = ParseQuery.getQuery("profileImage");

    // Retrieve the object by id
    query.getInBackground("ImageId", new GetCallback<ParseObject>() {
      public void done(ParseObject profileImage, ParseException e) {
        if (e == null) {
          // Image is not available to be used
          profileImage.saveInBackground();
        }
      }
    });
    ```

- [OPTIONAL: List endpoints if using existing API such as Yelp]

    Google Books API 
    * Base URL - https://www.googleapis.com/books/v1
    
    | HTTP Verb | Endpoint | Description |
    | -------- | -------- | -------- |
    | GET     | /volumes?q={search terms}| Performs a book search.     |
    | GET     | /volumes/volumeId | Retrieves a Volume resource based on ID.|
    
    New York Times API
    * Base URL - https://api.nytimes.com/svc/books/v3

    | HTTP Verb | Endpoint | Description |
    | -------- | -------- | -------- |
    | GET     | /lists/names.json | returns a list of all the NYT Best Sellers Lists|
    | GET     | /reviews.json?author | get NYT book review by author|
    | GET     | /reviews.json?title | get NYT book review by title|

    
    


