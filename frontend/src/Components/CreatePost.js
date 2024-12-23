import React, { useState } from 'react';
import './CreatePost.css';

function CreatePostComponent({ onPostCreate }) {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');
  const userId = localStorage.getItem('userId');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    if (!title || !content) {
      setError('Both title and content are required.');
      return;
    }
    if (!userId) {
      setError('User is not logged in.');
      return;
  }

    
    const newPost = {
      title,
      content,
      user: { id: Number(userId) },
      timestamp: new Date().toISOString(),
    };

    console.log("Submitting Post:", newPost);
    onPostCreate(newPost);

    
    setTitle('');
    setContent('');
  };

  return (
    <div className="create-post-container">
      <h3>Create Post</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Title</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter post title"
            required
          />
        </div>
        <div className="form-group">
          <label>Content</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="Enter post content"
            rows="4"
            required
          />
        </div>
        {error && <p className="error">{error}</p>}
        <button type="submit">Post</button>
      </form>
    </div>
  );
}

export default CreatePostComponent;
