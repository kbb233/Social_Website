import './App.css';
import React from 'react';
import LoginComponent from './Components/Login.js';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegisterComponent from './Components/Register.js';
import CreatePostComponent from './Components/CreatePost.js';
import NavigateBarComponent from './Components/NavBar.js';
import SearchComponent from './Components/SearchComponent.js';
import PostListComponent from './Components/PostList.js';
import UserListComponent from './Components/UserList.js';
import HomeComponent from './Components/home.js';
import FollowerComponent from './Components/Follower.js';
import FollowingComponent from './Components/Following.js';

function App() {
  return (
    
    <Router>
      <div>
        <NavigateBarComponent />
        <Routes>
          <Route path="/" element={<LoginComponent />} />
          <Route path="/register" element={<RegisterComponent />} />
          <Route path="/home" element={<HomeComponent />} />

          <Route path="/create-post" element={<CreatePostComponent  onPostCreate={handlePostCreate}/>} />
          <Route path="/posts" element={<PostListComponent />} />
          <Route path="/search" element={<SearchComponent />} />
          <Route path="/following-list" element={<FollowerComponent />} />
          <Route path="/follower-list" element={<FollowingComponent />} />
      </Routes>
      </div>
      
    </Router>
  );

  async function handlePostCreate(post) {
    try {
        const response = await fetch('http://localhost:8080/api/posts', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(post),
        });

        if (!response.ok) {
            throw new Error('Failed to save post');
        }

        const data = await response.json();
        console.log('Post saved successfully:', data);
    } catch (error) {
        console.error('Error saving post:', error.message);
    }
}
}

export default App;
