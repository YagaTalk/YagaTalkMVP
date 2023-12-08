import './AssistantTable.css';
import React, { useContext, useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';
import { BACKEND_URL } from './Config';
import {AuthContext} from "./auth";
import Chat from "./Chat";

function NewAssistant() {
    const [name, setName] = useState('');
    const [content, setContent] = useState('');
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [showChatModal, setShowChatModal] = useState(false);
    const [sessionId, setSessionId] = useState(null)

    const handleOpenChatModal = () => {
        handleTestChat();
        setShowChatModal(true);
    };

    const handleCloseChatModal = () => {
        setShowChatModal(false);
    };


    const handleNameChange = (event) => {
        setName(event.target.value);
    };

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const {token} = useContext(AuthContext);

    const handleTestChat = async () => {

        const testUrl = `${BACKEND_URL}/api/assistants?isTestSession=true`;
        const headers = {'Authorization': `Bearer ${token}`};
        const data = {
            name,
            content,
        };

        try {
            const response = await axios.post(testUrl, data, {headers});
            const assistantId = response.data.id;
            try {
                const response = await fetch(`${BACKEND_URL}/api/sessions/current?assistantId=${assistantId}`);
                if (response.ok) {
                    const body = await response.json()
                    setSessionId(body.id);
                } else {
                    console.error('Failed to fetch current session');
                }
            } catch (error) {
                console.error('Error occurred while fetching current session:', error);
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.log(`Error calling endpoint ${testUrl}: ${error}`);
                if (error.response) {
                    console.error(`Error: ${error.response.status}`);
                } else {
                    console.error(error);
                }
            } else {
                throw error;
            }
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const url = `${BACKEND_URL}/api/assistants?isTestSession=false`;
        const headers = {'Authorization': `Bearer ${token}`};
        const data = {
            name,
            content,
        };

        try {
            const response = await axios.post(url, data, { headers });
            console.log('Response:', response.data);
            setSuccessMessage('Assistant created successfully!');
            setShowSuccessModal(true);
            setName('');
            setContent('');
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.log(`Error calling endpoint ${url}: ${error}`);
                if (error.response) {
                    console.error(`Error: ${error.response.status}`);
                } else {
                    console.error(error);
                }
            } else {
                throw error;
            }
        }
    };

    const handleCloseSuccessModal = () => {
        setShowSuccessModal(false);
    };

    return (
        <div className="new-page">
            <div className="chat-header">YagaTalk</div>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">
                        Name
                    </label>
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
                    <label htmlFor="content" className="form-label">
                        Content
                    </label>
                    <textarea
                        className="form-control"
                        id="content"
                        value={content}
                        onChange={handleContentChange}
                        style={{minWidth: '500px', width: 'auto', minHeight: '100px', resize: 'vertical'}}
                    />
                </div>
                <button type="submit" className="btn btn-primary">
                    Submit
                </button>
            </form>

            <Modal show={showSuccessModal} onHide={handleCloseSuccessModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Success</Modal.Title>
                </Modal.Header>
                <Modal.Body>{successMessage}</Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleCloseSuccessModal}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

            <button type="button" className="btn btn-secondary" onClick={handleOpenChatModal}>
                TestChat
            </button>
            <Modal show={showChatModal} onHide={handleCloseChatModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Test Chat</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Chat sessionId={sessionId}/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseChatModal}>
                        Close Chat
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default NewAssistant;
