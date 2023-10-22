import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {format} from "date-fns";
import {Modal} from "react-bootstrap";
import {BACKEND_URL} from "./Config";


function ChatHistoryTable() {
    const { contextId } = useParams();
    const [chatSessions, setChatSessions] = useState([]);
    const [showModal, setShowModal] = useState(false);


    useEffect(() => {
        const fetchData = () => {
            fetch(`${BACKEND_URL}/api/chat/context/${contextId}/sessions`)
                .then(response => response.json())
                .then(data => setChatSessions(data))
                .catch(error => console.error('Error fetching contexts:', error));
        };

        fetchData();
    }, [contextId]);

    return (
        <div className="ChatHistory-page">
            <div className="chat-header">
                YagaTalk
            </div>

            <div className="context-list">
                {chatSessions.map(chatSession => (
                    <div className="context-item-container" key={chatSession.id}>
                        <li
                            onClick={() => setShowModal(true)}
                            className="context-item"
                        >
                            <div className="context-info">
                                <div className="context-date">{format(new Date(chatSession.createdTime), "yyyy-MM-dd HH:mm")}</div>
                            </div>
                        </li>
                    </div>
                ))}
            </div>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Body>
                    <div className="modal-content">
                        This is a test chat modal.
                    </div>
                </Modal.Body>
            </Modal>

        </div>
    );
}

export default ChatHistoryTable;