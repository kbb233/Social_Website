import React, { useState } from 'react';
import './PostList.css';

function PostListComponent({ posts=[], setPosts }) { 
    const [comments, setComments] = useState({}); 
    const [error, setError] = useState('');

    const handleAddComment = async (postId) => {
        setError('');
        try {
            const userId = localStorage.getItem('userId');
            if (!userId) throw new Error('User not logged in');
            const response = await fetch(`http://localhost:8080/api/posts/${postId}/comments`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json' 
                },
                body: JSON.stringify({
                    content: comments[postId] || '', 
                    userId: parseInt(userId),
                }),
            });
            if (!response.ok) {
                throw new Error(`Failed to add comment. Status: ${response.status}`);
            }
            setComments((prev) => ({ ...prev, [postId]: '' })); 
        } catch (err) {
            setError(err.message);
        }
    };

    const handleCommentChange = (postId, value) => {
        setComments((prev) => ({
            ...prev,
            [postId]: value, 
        }));
    };

    const handleLikePost = async (postId) => {
        try {
            const userId = localStorage.getItem('userId');
            if (!userId) {
                throw new Error("User not logged in");
            }
    
            const response = await fetch(`http://localhost:8080/api/posts/${postId}/like?userId=${userId}`, {
                method: 'POST',
            });
    
            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(`Failed to like post: ${errorMessage}`);
            }
    
            // Update the likedNumber in the local state
            const updatedPosts = posts.map((post) =>
                post.id === postId ? { ...post, likedNumber: (post.likedNumber || 0) + 1 } : post
            );
            setPosts(updatedPosts); 
        } catch (err) {
            console.error(err.message);
        }
    };
    
    return (
        <div className="post-list-container">
            {posts.length > 0 ? (
            <ul className="post-list">
                {posts.map((post) => (
                    <li key={post.id} className="post-item">
                        <h4>{post.title}</h4>
                        <p>{post.content}</p>
                        <small>By {post.username|| "Unknown User"}</small>
                        <p>Likes: {post.likedNumber|| 0}</p>
                        <button onClick={() => handleLikePost(post.id)}>Like</button>
                        <div className="comments">
                            <h5>Comments</h5>
                            {post.comments && post.comments.length > 0 ? (
                                post.comments.map((comment) => (
                                <p key={comment.id}>{comment.content}</p>
                            ))
                        ) : (
                            <p>No comments yet.</p>
                        )}
                        </div>
                        <input
                            type="text"
                            value={comments[post.id] || ''} 
                            onChange={(e) => handleCommentChange(post.id, e.target.value)}
                            placeholder="Add a comment"
                        />
                        <button onClick={() => handleAddComment(post.id)}>Comment</button>
                        {error && <p className="error">{error}</p>}
                    </li>
                ))}
            </ul>
            ) : (
                <p>No posts found.</p>
            )}
        </div>
    );
}

export default PostListComponent;
