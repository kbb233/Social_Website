import React from 'react';
import { Link ,useNavigate} from 'react-router-dom';
import './NavBar.css';

function NavigateBarComponent() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('userId'); // Clear user ID from local storage
        navigate('/'); // Redirect to login page
    };

    return (
        <nav className="navigate-bar">
            <ul>
                <li><Link to="/home">Home</Link></li>
                <li><Link to="/create-post">Create Post</Link></li>
                <li><Link to="/search">Search</Link></li>
                <li><Link to="/following-list">Following</Link></li>
                <li><Link to="/follower-list">Follower</Link></li>
                <li onClick={handleLogout} style={{ cursor: 'pointer' }}>Logout</li>
            </ul>
        </nav>
    );
}

export default NavigateBarComponent;
