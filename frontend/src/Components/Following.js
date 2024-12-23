import React, { useState, useEffect } from 'react';
//import './Following.css';

function FollowingComponent() {
    const [followers, setFollowers] = useState([]);
    const [error, setError] = useState('');
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchFollowers = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/users/${userId}/followers`);
                if (!response.ok) throw new Error('Failed to fetch followers list');
                const data = await response.json();
                setFollowers(data);
            } catch (err) {
                setError(err.message);
            }
        };
        fetchFollowers();
    }, [userId]);

    return (
        <div className="following-container">
            <h3>Who are following you</h3>
            {error && <p className="error">{error}</p>}
            {followers.length === 0 ? (
                <p>No one is following you.</p>
            ) : (
                <ul className="following-list">
                    {followers.map((user) => (
                        <li key={user.id} className="following-item">
                            <p><strong>{user.username}</strong></p>
                            <p>{user.description}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default FollowingComponent;
