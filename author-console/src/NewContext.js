import './AssistantTable.css';
import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';
import {BACKEND_URL} from "./Config";

function NewContext() {
    const [name, setName] = useState('');
    const [content, setContent] = useState('');
    const [showModal, setShowModal] = useState(false);

    const handleNameChange = (event) => {
        setName(event.target.value);
    };

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await axios.post(`${BACKEND_URL}/api/chat/context`, {
                name,
                content
            });
            console.log('Response:', response.data);
            setShowModal(true);
            setName('');
            setContent('');
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    return (
        <div className="new-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="name"
                        value={name}
                        onChange={handleNameChange}
                        style={{ minWidth: '500px', width: 'auto' }}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="content" className="form-label">Content</label>
                    <textarea
                        className="form-control"
                        id="content"
                        value={content}
                        onChange={handleContentChange}
                        style={{ minHeight: '100px', resize: 'vertical' }}
                    />
                </div>
                <button type="submit" className="btn btn-primary">Submit</button>
            </form>

            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Success!</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Context created successfully!
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleCloseModal}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

        </div>
    );
}

export default NewContext;