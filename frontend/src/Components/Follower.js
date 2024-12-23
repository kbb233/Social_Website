import React, { useState, useEffect } from 'react';
//import './Follower.css';

function FollowerComponent() {
    const [following, setFollowing] = useState([]);
    const [error, setError] = useState('');
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchFollowing = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/users/${userId}/following`);
                if (!response.ok) throw new Error('Failed to fetch following list');
                const data = await response.json();
                setFollowing(data);
            } catch (err) {
                setError(err.message);
            }
        };
        fetchFollowing();
    }, [userId]);

    return (
        <div className="follower-container">
            <h3>You are following</h3>
            {error && <p className="error">{error}</p>}
            {following.length === 0 ? (
                <p>You are not following anyone.</p>
            ) : (
                <ul className="follower-list">
                    {following.map((user) => (
                        <li key={user.id} className="follower-item">
                            <p><strong>{user.username}</strong></p>
                            <p>{user.description}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default FollowerComponent;
