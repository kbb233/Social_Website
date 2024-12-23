import React, { useState, useEffect } from 'react';
import PostListComponent from './PostList';
import './Home.css';

function HomeComponent() {
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);

    const fetchPosts = async () => {
        if (loading) return;

        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8080/api/posts?page=${page}&size=10`);
            if (!response.ok) throw new Error('Failed to load posts');

            const data = await response.json();
            console.log("Fetched Posts:", data);
            console.log("Post Data:", data);

            if (Array.isArray(data)) { // Handles array response
                if (data.length < 10) setHasMore(false);
                setPosts((prevPosts) => {
                    const uniquePosts = [...prevPosts, ...data].filter(
                        (post, index, self) => self.findIndex(p => p.id === post.id) === index
                    );
                    return uniquePosts;
                });
            } else if (data.content) { // Handles object response with `content`
                if (data.content.length < 10) setHasMore(false);
                setPosts((prevPosts) => {
                    const uniquePosts = [...prevPosts, ...data.content].filter(
                        (post, index, self) => self.findIndex(p => p.id === post.id) === index
                    );
                    return uniquePosts;
                });
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const loadMorePosts = () => {
        if (hasMore && !loading) {
            setPage((prevPage) => prevPage + 1);
        }
    };

    useEffect(() => {
        fetchPosts();
    }, [page]);

    return (
        <div className="home-container">
            <h3>All Posts</h3>
            <PostListComponent posts={posts} setPosts={setPosts}/>
            {hasMore && (
                <button onClick={loadMorePosts} className="load-more-btn">
                    Load More
                </button>
            )}
             {loading && <p className="loading">Loading...</p>}
        </div>
    );
}

export default HomeComponent;
