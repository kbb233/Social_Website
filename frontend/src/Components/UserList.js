import React, { useState, useEffect } from 'react';
import './UserList.css';

function UserListComponent({ searchTerm }) {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [followingStatus, setFollowingStatus] = useState({});
    const currentUserId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchUsers = async () => {
            if (!searchTerm) {
                setUsers([]);
                return;
            }
            try {
                const response = await fetch(
                    `http://localhost:8080/api/users/search?username=${encodeURIComponent(searchTerm)}`
                );
                if (!response.ok) {
                    throw new Error('Failed to fetch users');
                }
                const data = await response.json();
                setUsers(data);

                //check follow status
                const status = {};
                for (const user of data) {
                    const followingResponse = await fetch(`http://localhost:8080/api/users/${currentUserId}/is-following/${user.id}`);
                    if (followingResponse.ok) {
                        const isFollowing = await followingResponse.json();
                        status[user.id] = isFollowing;
                    }
                }
                setFollowingStatus(status);
            } catch (err) {
                setError(err.message);
            }
        };
        fetchUsers();
    }, [searchTerm]);

    const handleFollow = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/users/${currentUserId}/follow/${userId}`, { method: 'POST' });
            if (response.ok) {
                setFollowingStatus((prev) => ({ ...prev, [userId]: true }));
            }
        } catch (err) {
            console.error(err.message);
        }
    };

    const handleUnfollow = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/users/${currentUserId}/unfollow/${userId}`, { method: 'DELETE' });
            if (response.ok) {
                setFollowingStatus((prev) => ({ ...prev, [userId]: false }));
            }
        } catch (err) {
            console.error(err.message);
        }
    };

    return (
        <div className="user-list-container">
            <h3>User List</h3>
            {error && <p className="error">{error}</p>}
            {users.length === 0 ? (
                <p>No users found</p>
            ) : (
                <ul className="user-list">
                    {users.map((user) => (
                        <li key={user.id} className="user-item">
                            <p><strong>{user.username}</strong></p>
                            <p>{user.description}</p>
                            {currentUserId !== String(user.id) && (
                                <button
                                    onClick={() => followingStatus[user.id] ? handleUnfollow(user.id) : handleFollow(user.id)}
                                >
                                    {followingStatus[user.id] ? 'Followed' : 'Follow'}
                                </button>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default UserListComponent;
