package com.example.controller;

import com.example.entity.*;
import com.example.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SocialWebsiteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private LikeRepository likeRepository;


    // User Registration
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
    Map<String, String> response = new HashMap<>();

    try {
        // Log the username being checked
        System.out.println("Checking username: " + user.getUsername());

        // Validate input
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty()) {
            response.put("message", "Username and password must not be empty.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the username already exists
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            System.out.println("Username already exists: " + existingUser.getUsername());
            response.put("message", "Username already exists. Please choose another username.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); 
        }

        // Save the user to the database
        User savedUser = userRepository.save(user);
        response.put("message", "Registration successful!");
        response.put("userId", String.valueOf(savedUser.getId()));
        return ResponseEntity.ok(response);

    } catch (Exception e) {
        // Log the exception for debugging purposes
        System.err.println("Error during registration: " + e.getMessage());
        e.printStackTrace();

        // Return a generic error message to the client
        response.put("message", "An unexpected error occurred during registration. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

    // User Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");

    User user = userRepository.findByUsername(username);
    if (user != null && user.getPassword().equals(password)) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("userId", user.getId());
        return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Invalid username or password"));
}


    // Create Post
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        System.out.println("Received Post Payload: " + post);
    
        if (post.getUser() == null) {
            return ResponseEntity.badRequest().body("User object is missing in the payload.");
        }
    
        if (post.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("User ID is missing in the payload.");
        }
    
        Optional<User> user = userRepository.findById(post.getUser().getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }
    
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    // Get All Posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        List<Post> posts = postRepository.findAll(PageRequest.of(page, size)).getContent();
        List<PostDTO> postDTOs = posts.stream().map(PostDTO::new).toList();
        return ResponseEntity.ok(postDTOs);
}
    
    // Search Posts by Content
    @GetMapping("/posts/search")
    public ResponseEntity<List<Post>> searchPosts(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(postRepository.findByContentContaining(keyword, pageable));
}

    // Add Comment to Post
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
    try {

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findById(commentDTO.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setPost(postOptional.get());
        comment.setUser(userOptional.get());

        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(new CommentDTO(savedComment));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    // Follow User
    @PostMapping("/users/{followerId}/follow/{followedId}")
    public ResponseEntity<Follower> followUser(@PathVariable Long followerId, @PathVariable Long followedId) {
        Optional<User> follower = userRepository.findById(followerId);
        Optional<User> followed = userRepository.findById(followedId);
        if (follower.isPresent() && followed.isPresent()) {
            Follower newFollower = new Follower();
            newFollower.setFollower(follower.get());
            newFollower.setFollowed(followed.get());
            Follower savedFollower = followerRepository.save(newFollower);
            return ResponseEntity.ok(savedFollower);
        }
        return ResponseEntity.badRequest().build();
    }

    // Like Post
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {
    try {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Post post = postOptional.get();
            User user = userOptional.get();

            // Check if the user already liked the post
            boolean alreadyLiked = likeRepository.existsByPostAndUser(post, user);
            if (alreadyLiked) {
                return ResponseEntity.badRequest().body("User already liked this post");
            }

            // Save the like
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like); 

            // Increment likedNumber
            post.setLikedNumber(post.getLikedNumber() + 1);
            postRepository.save(post); 

            return ResponseEntity.ok("Post liked");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post or User not found");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error liking post");
    }
}

    // Search Users by Username
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        return ResponseEntity.ok(users);
    }


    // Check if user is following another user
    @GetMapping("/users/{followerId}/is-following/{followedId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long followerId, @PathVariable Long followedId) {
    boolean isFollowing = followerRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    return ResponseEntity.ok(isFollowing);
}

    // Unfollow a user
    @DeleteMapping("/users/{followerId}/unfollow/{followedId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followerId, @PathVariable Long followedId) {
    Optional<Follower> followerRelation = followerRepository.findByFollowerIdAndFollowedId(followerId, followedId);
    if (followerRelation.isPresent()) {
        followerRepository.delete(followerRelation.get());
        return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
}
    @GetMapping("/users/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
    List<User> following = followerRepository.findAllByFollowerId(userId)
        .stream()
        .map(Follower::getFollowed)
        .toList();
    return ResponseEntity.ok(following);
}

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
    List<User> followers = followerRepository.findAllByFollowedId(userId)
        .stream()
        .map(Follower::getFollower)
        .toList();
    return ResponseEntity.ok(followers);
}

}
