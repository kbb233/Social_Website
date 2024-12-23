import React, { useState } from 'react';
import PostListComponent from './PostList';
import UserListComponent from './UserList';
import './SearchComponent.css';

function SearchComponent() {
    const [searchType, setSearchType] = useState('user');
    const [searchTerm, setSearchTerm] = useState('');
    const [results, setResults] = useState([]);
    const [error, setError] = useState('');

    const handleSearch = async () => {
        setError('');
        setResults([]);

        if (!searchTerm.trim()) {
            setError('Please enter a search term.');
            return;
        }

        try {
            const endpoint =
                searchType === 'user'
                    ? `/api/users/search?username=${encodeURIComponent(searchTerm)}`
                    : `/api/posts/search?keyword=${encodeURIComponent(searchTerm)}&page=0&size=10`;

            const response = await fetch(`http://localhost:8080${endpoint}`);
            if (!response.ok) throw new Error('Failed to fetch results');

            const data = await response.json();
            setResults(data);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="search-container">
            <h3>Search</h3>
            <div className="search-controls">
                <select
                    value={searchType}
                    onChange={(e) => setSearchType(e.target.value)}
                    className="search-select"
                >
                    <option value="user">Search Users</option>
                    <option value="post">Search Posts</option>
                </select>
                <input
                    type="text"
                    placeholder="Enter search term"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input"
                />
                <button onClick={handleSearch} className="search-button">
                    Search
                </button>
            </div>
            {error && <p className="error">{error}</p>}
            {results.length > 0 ? (
                searchType === 'user' ? (
                    <UserListComponent searchTerm={searchTerm} />
                ) : (
                    <PostListComponent posts={results} />
                )
            ) : (
                !error && <p>No results found.</p>
            )}
        </div>
    );
}

export default SearchComponent;
